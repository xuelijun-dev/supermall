package com.supermall.item.service;

import com.supermall.common.enums.ExceptionEnum;
import com.supermall.common.exception.SmException;
import com.supermall.item.mapper.SkuMapper;
import com.supermall.item.mapper.StockMapper;
import com.supermall.item.pojo.Sku;
import com.supermall.item.pojo.Stock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SkuService {
    @Autowired
    private SkuMapper skuMapper;
    @Autowired
    private StockMapper stockMapper;
    public  List<Sku> querySkuBySpuId(Long spuid) {
        Sku sku = new Sku();
        sku.setSpuId(spuid);
        List<Sku> list = skuMapper.select(sku);
        if (CollectionUtils.isEmpty(list)){
            throw new SmException(ExceptionEnum.SKU_NOT_FOUNT);
        }
        //查询库存
//        for(Sku s:list){
//            Stock stock = stockMapper.selectByPrimaryKey(s.getId());
//            if (list == null){
//                throw new SmException(ExceptionEnum.SKU_STOCK_NOT_FOUNT);
//            }
//            s.setStock(stock.getStock());
//        }
        List<Long> ids = list.stream().map(Sku::getId).collect(Collectors.toList());
        List<Stock> stocks = stockMapper.selectByIdList(ids);
        if(CollectionUtils.isEmpty(stocks)){
            throw new SmException(ExceptionEnum.SKU_STOCK_NOT_FOUNT);
        }
        Map<Long, Integer> stockMap = stocks.stream().collect(Collectors.toMap(Stock::getSkuId, Stock::getStock));
        list.forEach(s -> s.setStock(stockMap.get(s.getId())));
        return  list;
    }

    public List<Sku> querySkuByIds(List<Long> ids) {
        List<Sku> skus = skuMapper.selectByIdList(ids);
        if(CollectionUtils.isEmpty(skus)){
            throw new SmException(ExceptionEnum.SKU_STOCK_NOT_FOUNT);
        }
        loadSkuStock(skus);
        return  skus;
    }

    private void loadSkuStock(List<Sku> skus) {
        List<Long> ids = skus.stream().map(Sku::getId).collect(Collectors.toList());
        List<Stock> stocks = stockMapper.selectByIdList(ids);
        if (CollectionUtils.isEmpty(stocks)) {
            throw new SmException(ExceptionEnum.SKU_STOCK_NOT_FOUNT);
        }
        Map<Long, Integer> stockMap = stocks.stream().collect(Collectors.toMap(Stock::getSkuId, Stock::getStock));
        skus.forEach(s -> s.setStock(stockMap.get(s.getId())));
    }
}
