package com.supermall.order.web;

import com.supermall.order.dto.OrderDTO;
import com.supermall.order.pojo.Order;
import com.supermall.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class OrderController {
   @Autowired
    OrderService orderService;

    /**
     * 创建订单
     * @param orderDTO
     * @return
     */
    @PostMapping
    public ResponseEntity<Long> createOrder(@RequestBody OrderDTO orderDTO){
       //创建订单
       return  ResponseEntity.ok(orderService.createOrder(orderDTO));
    }
    @GetMapping("{id}")
    public ResponseEntity<Order> queryOrderById(@PathVariable("id") Long id){
        return ResponseEntity.ok(orderService.queryOrderById(id));
    }

    @GetMapping("/url/{id}")
    public ResponseEntity<String> createOrderUrl(@PathVariable("id") Long orderId){
        return  ResponseEntity.ok(orderService.createOrderUrl(orderId));
    }

    @GetMapping("page/test")
    public ResponseEntity<String> search(){
        return ResponseEntity.ok("hello");
    }
    /**
     *查询支付状态
     * @param orderId
     * @return
     */
    @GetMapping("/state/{id}")
    public ResponseEntity<Integer> queryOrderState(@PathVariable("id") Long orderId){
        return ResponseEntity.ok(orderService.queryOrderState(orderId).getValue());
    }
}
