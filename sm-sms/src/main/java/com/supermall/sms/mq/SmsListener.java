package com.supermall.sms.mq;

import com.supermall.common.utils.JsonUtils;
import com.supermall.sms.config.SmsProperties;
import com.supermall.sms.utils.SmsUtils;
import net.minidev.json.JSONUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Map;

@Component
@EnableConfigurationProperties(SmsProperties.class)
public class SmsListener {
    @Autowired
    SmsUtils smsUtils;
    @Autowired
    SmsProperties prop;
    @RabbitListener(bindings = @QueueBinding(value = @Queue(name="sms.verify.code.queue",durable = "true"),
    exchange = @Exchange(name = "sm.sms.exchange",type = ExchangeTypes.TOPIC),
    key = "sms.verify.code"))
    public void listenInsertOrUpdate(Map<String,String> msg){
        if(CollectionUtils.isEmpty(msg)){
            return;
        }
        String phone = msg.remove("phone");
        if(StringUtils.isBlank(phone)){
            return;
        }
        smsUtils.sendSms(phone,prop.getSignName(),prop.getTemplateCode(), JsonUtils.toString(msg));

    }


}
