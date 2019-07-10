package com.momo.rocketmq.JavaMessageService;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.*;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.List;

//@Component
public class OrderConsumer {
    private DefaultMQPushConsumer consumer;

    private String consumerGroup = "order_consumer_group";

    public OrderConsumer() throws MQClientException {

        consumer = new DefaultMQPushConsumer(consumerGroup);
        consumer.setNamesrvAddr(JmsConfig.NAME_SERVER);
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);
        consumer.subscribe(JmsConfig.NEW_TOPIC1, "*");
        consumer.registerMessageListener(new MessageListenerOrderly() {
            @Override
            public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs, ConsumeOrderlyContext context) {
               try {
                   MessageExt msg = msgs.get(0);
                   System.out.printf("%s Receive New Messages: %s %n", Thread.currentThread().getName(), new String(msgs.get(0).getBody()));

                   //做业务逻辑操作 TODO
                   return ConsumeOrderlyStatus.SUCCESS;
               }catch (Exception e){
                   return ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT;
               }
            }
        });
        consumer.start();
        System.out.println("consumer start ...");
    }

}
