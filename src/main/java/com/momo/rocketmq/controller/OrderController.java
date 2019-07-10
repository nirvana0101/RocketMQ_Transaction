package com.momo.rocketmq.controller;

import com.momo.rocketmq.JavaMessageService.JmsConfig;
import com.momo.rocketmq.JavaMessageService.PayProducer;
import com.momo.rocketmq.domian.Order;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

@RestController
public class OrderController {
//    private static final String topic="Myfrist_pay_topic";
    @Autowired
    private PayProducer payProducer;
    @RequestMapping("/callback")
    public  Object callback(String text) throws InterruptedException, RemotingException, MQClientException, MQBrokerException {
        List<Order> list=Order.init();
        for(Order order:list) {
            Message msg = new Message(JmsConfig.NEW_TOPIC1, "",
                    order.getOrderId().toString(),order.toString().getBytes() );
            SendResult sendResult =  payProducer.getProducer().send(msg, new MessageQueueSelector() {
                @Override
                public MessageQueue select(List<MessageQueue> mqs, Message msg, Object arg) {
                    //取模,运算实际上是计算两数相除以后的余数,保证id相同则余数相同，且不会大于队列总数
                    int index=(int)((long)arg % mqs.size());
                    return mqs.get(index);
                }
            },order.getOrderId());
            System.out.printf("发送结果=%s, sendResult=%s ,orderid=%s, type=%s\n", sendResult.getSendStatus(),
                    sendResult.toString(),order.getOrderId(),order.getStatus());
        }
        return new HashMap<>();
    }
}
