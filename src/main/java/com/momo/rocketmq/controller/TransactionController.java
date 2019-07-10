package com.momo.rocketmq.controller;

import com.momo.rocketmq.JavaMessageService.JmsConfig;
import com.momo.rocketmq.JavaMessageService.TransactionProducer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransactionController {
    @Autowired
    private TransactionProducer transactionProducer;
    @RequestMapping("/callback/transaction")
    public Object callback(String text,String param) throws MQClientException {
        Message msg=new Message(JmsConfig.TOPIC,"tag1",text+"_key",text.getBytes());
        SendResult sendResult=transactionProducer.getProducer()
                .sendMessageInTransaction(msg,param);
        System.out.printf("发送结果=%s, sendResult=%s \n", sendResult.getSendStatus(), sendResult.toString());
        return "发送成功";
    }
}
