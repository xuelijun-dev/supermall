package com.supermall.search.mq;

import com.supermall.search.service.SearchService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ItemListenner {

    @Autowired
    private SearchService searchService;
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name="search.item.insert.queue",durable = "true"),
            exchange = @Exchange(name = "sm.item.exchange",type = ExchangeTypes.TOPIC,durable = "true"),
            key = {"item.insert","item.update"}
            ))
    public void listenInsertOrUpdate(Long spuId){
        if(spuId == null){
            return;
        }
        //处理消息，对索引库进行新增或修改
        searchService.createOrUpdateIndex(spuId);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name="search.item.delete.queue",durable = "true"),
            exchange = @Exchange(name = "sm.item.exchange",type = ExchangeTypes.TOPIC,durable = "true"),
            key = {"item.delete"}
    ))
    public void listenDelete(Long spuId){
        if(spuId == null){
            return;
        }
        //处理消息，对索引库进行删除
        searchService.deleteIndex(spuId);
    }
}
