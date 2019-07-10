package com.momo.rocketmq.JavaMessageService;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.springframework.stereotype.Component;

/**
 * 支付消息的生产者
 */
@Component
public class PayProducer {
    //指定一个组名
    private String producerGroup="pay_producer_group";
    //指定nameServer
//    private String nameServerAddress="114.115.215.115:9876";

    private DefaultMQProducer producer;
    public PayProducer() {
        this.producer=new DefaultMQProducer(producerGroup);
        producer.setNamesrvAddr(JmsConfig.NAME_SERVER);
        start();
    }

    public DefaultMQProducer getProducer() {
        return this.producer;
    }
    public void start(){
        try {
            this.producer.start();

        } catch (MQClientException e) {
            e.printStackTrace();
        }
    }
    public void shutdown(){
        this.producer.shutdown();
    }
}
