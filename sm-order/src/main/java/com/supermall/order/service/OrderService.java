package com.supermall.order.service;

import com.supermall.auth.pojo.UserInfo;
import com.supermall.common.dto.CartDTO;
import com.supermall.common.enums.ExceptionEnum;
import com.supermall.common.exception.SmException;
import com.supermall.common.utils.IdWorker;
import com.supermall.item.api.GoodsApi;
import com.supermall.item.pojo.Sku;
import com.supermall.order.client.AddressClient;
import com.supermall.order.client.GoodClient;
import com.supermall.order.dto.AddressDTO;
import com.supermall.order.dto.OrderDTO;
import com.supermall.order.enums.OrderStatusEnum;
import com.supermall.order.enums.PayState;
import com.supermall.order.intercepter.UserInterceptor;
import com.supermall.order.mapper.OrderDetailMapper;
import com.supermall.order.mapper.OrderMapper;
import com.supermall.order.mapper.OrderStatusMapper;
import com.supermall.order.pojo.Order;
import com.supermall.order.pojo.OrderDetail;
import com.supermall.order.pojo.OrderStatus;
import com.supermall.order.utils.PayHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import java.util.*;
import java.util.stream.Collectors;
@Slf4j
@Service
public class OrderService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Autowired
    private OrderStatusMapper orderStatusMapper;
    @Autowired
    IdWorker idWorker;
    @Autowired
    GoodClient goodClient;
    @Autowired
    PayHelper payHelper;
    @Transactional
    public Long createOrder(OrderDTO orderDTO){
        //新增订单
        Order  order = new Order();
        //订单编号，基本信息
        long orderId = idWorker.nextId();
        order.setOrderId(orderId);
        order.setCreateTime(new Date());
        order.setPaymentType(orderDTO.getPaymentType());
        //用户信息
        UserInfo user = UserInterceptor.getUser();
        order.setUserId(user.getId());
        order.setBuyerNick(user.getUsername());
        order.setBuyerRate(false);
        //收货地址
        AddressDTO addr = AddressClient.findById(orderDTO.getAddressId());
        order.setReceiver(addr.getName());
        order.setReceiverAddress(addr.getAddress());
        order.setReceiverCity(addr.getCity());
        order.setReceiverDistrict(addr.getDistrict());
        order.setReceiverMobile(addr.getPhone());
        order.setReceiverState(addr.getState());
        order.setReceiverZip(addr.getZipCode());
       //订单金额
        Map<Long, Integer> numMap = orderDTO.getCarts().stream().collect(Collectors.toMap(CartDTO::getSkuId, CartDTO::getNum));
        Set<Long> ids = numMap.keySet();
        List<Sku> skus = goodClient.querySkuByIds(new ArrayList<>(ids));
        long totalPay = 0L;
        List<OrderDetail> details = new ArrayList<>();
        for (Sku sku : skus) {
            totalPay += sku.getPrice() * numMap.get(sku.getId());
            OrderDetail detail = new OrderDetail();
            detail.setOrderId(orderId);
            detail.setSkuId(sku.getId());
            detail.setImage(StringUtils.substringBefore(sku.getImages(),","));
            detail.setNum(numMap.get(sku.getId()));
            detail.setOwnSpec(sku.getOwnSpec());
            detail.setTitle(sku.getTitle());
            detail.setPrice(sku.getPrice());
            details.add(detail);
        }
        //总金额
        order.setTotalPay(totalPay);
        //实付金额：总金额 + 邮费 - 优惠金额
        order.setActualPay(totalPay-order.getPostFee()-0);
        //订单保存到数据库
        int count = orderMapper.insertSelective(order);
        if(count != 1){
            throw new SmException(ExceptionEnum.ORDER_CREATE_ERROR);
        }
        //新增订单详情
        count = orderDetailMapper.insertList(details);
        if(count != details.size()){
            throw new SmException(ExceptionEnum.ORDER_CREATE_ERROR);
        }
        //新增订单状态
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setCreateTime(order.getCreateTime());
        orderStatus.setOrderId(orderId);
        orderStatus.setStatus(OrderStatusEnum.UN_PAY.value());
        count = orderStatusMapper.insertSelective(orderStatus);
        if(count != 1){
            throw new SmException(ExceptionEnum.ORDER_CREATE_ERROR);
        }
        //删除购物车信息

        //减库存
        List<CartDTO> cartDTOs = orderDTO.getCarts();
        goodClient.decreaseStock(cartDTOs);
        return  orderId;
    }


    public Order queryOrderById(Long id) {
        //查询订单
        Order order = orderMapper.selectByPrimaryKey(id);
        if(order == null){
            //订单不存在
            throw new SmException(ExceptionEnum.ORDER_CREATE_ERROR);
        }
        //查询订单详情
        OrderDetail detail = new OrderDetail();
        detail.setOrderId(id);
        List<OrderDetail> details = orderDetailMapper.select(detail);
        if(CollectionUtils.isEmpty(details)){
            //订单不存在
            throw new SmException(ExceptionEnum.ORDER_CREATE_ERROR);
        }
        order.setOrderDetails(details);
        OrderStatus orderStatus = orderStatusMapper.selectByPrimaryKey(id);
        if(orderStatus == null){
            //订单不存在
            throw new SmException(ExceptionEnum.ORDER_CREATE_ERROR);
         }
        order.setOrderStatus(orderStatus);
        return order;
    }

    public String createOrderUrl(Long orderId) {
        //查询订单
        Order order = queryOrderById(orderId);
        Integer status = order.getOrderStatus().getStatus();
        //订单状态异常
        if(status != OrderStatusEnum.UN_PAY.value()){
            throw new SmException(ExceptionEnum.ORDER_CREATE_ERROR);
        }
        //支付金额
        Long totalPay = order.getActualPay();
        String desc = order.getOrderDetails().get(0).getTitle();
        String url = payHelper.createOrder(orderId, totalPay, desc);
        return url;
    }

    public void handlerNotify(Map<String, String> result) {
        //1 数据校验
        payHelper.isSuccess(result);
        //2 校验签名
        payHelper.isValidSign(result);
        //3 校验金额
        String totalFeeStr = result.get("total_fee");
        String tradeNo = result.get("out_trade_no");
        if (StringUtils.isEmpty(totalFeeStr)){
            throw new SmException(ExceptionEnum.INVALID_ORDER_PARAM);
        }
        //3.1 获取结果中的金额
        Long totalFee = Long.valueOf(totalFeeStr);
        //3.2 获取订单中的金额
        Long orderid = Long.valueOf(tradeNo);
        Order order = orderMapper.selectByPrimaryKey(orderid);
        if (totalFee != order.getActualPay()) {
            //金额不符
            throw new SmException(ExceptionEnum.INVALID_ORDER_PARAM);
        }
        //4 修改订单状态
        OrderStatus status = new OrderStatus();
        status.setStatus(OrderStatusEnum.PAYED.value());
        status.setOrderId(orderid);
        status.setPaymentTime(new Date());
        int count = orderStatusMapper.updateByPrimaryKeySelective(status);
        if (count != 1){
            throw new SmException(ExceptionEnum.ORDER_STATUS_UPLOAD_ERROR);
        }
        log.info("[订单回调]，订单支付成功！ ，订单编号:{}",orderid);
    }

    public PayState queryOrderState(Long orderId) {
        //查询订单状态
        OrderStatus orderStatus = orderStatusMapper.selectByPrimaryKey(orderId);
        Integer status = orderStatus.getStatus();
        //判断是否支付
        if (status != OrderStatusEnum.UN_PAY.value()){
            // 如果已经支付，实际上就就已经支付好了
            return PayState.SUCCESS;
        }
        //如果未支付，不一定是未支付，必须去微信查询支付状态
        return payHelper.queryPayState(orderId);
    }
}
