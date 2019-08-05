package com.supermall.sms.mq;

import com.supermall.sms.config.SmsProperties;
import com.supermall.sms.utils.SmsUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
public class SmsListenerTest {

    @Autowired
    AmqpTemplate amqpTemplate;

    @Autowired
    SmsUtils smsUtils;

    @Autowired
    SmsProperties prop;
    @Test
    public void testSms() throws InterruptedException {
        Map<String,String> msg = new HashMap<>();
        msg.put("phone","13229223989");
        msg.put("code","8888");
        amqpTemplate.convertAndSend("sm.sms.exchenge","sms.verify.code",msg);
        Thread.sleep(10000);
    }


}