package com.momo.rocketmq.JavaMessageService;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.stereotype.Component;

import java.util.concurrent.*;

/**
 * 事务消息生产者
 */
@Component
public class TransactionProducer {
    private String producerGroup="transaction_producer_group";
    private TransactionMQProducer producer = null;
    private TransactionListener transactionListener=new TransactionListenerImpl();
    /**
     * corePoolSize 核心线程池大小
     * maximumPoolSize 最大线程池大小
     * keepAliveTime  线程最大空闲时间
     * BlockingQueue<Runnable>  线程等待队列
     * ThreadFactory 线程创建工厂,自定义线程池的时候，在这里给线程加个名称
     */
    private ExecutorService executorService=new ThreadPoolExecutor(2, 5, 100, TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(2000), new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            Thread thread=new Thread(r);
            thread.setName("client-transaction-msg-check-thread");
            return thread;
        }
    });
    public TransactionProducer() throws MQClientException {
        //事务消息生产者，需要传递四个参数，分别是：生产者组、nameServer、监听器、线程组
        producer=new TransactionMQProducer(producerGroup);
        producer.setNamesrvAddr(JmsConfig.NAME_SERVER);
        producer.setTransactionListener(transactionListener);
        producer.setExecutorService(executorService);
        producer.start();
    }

    public TransactionMQProducer getProducer() {
        return producer;
    }

    //发送完半消息后执行
    class TransactionListenerImpl implements TransactionListener{
        @Override
        public LocalTransactionState executeLocalTransaction(Message msg, Object arg) {
            System.out.println("====executeLocalTransaction=======");
            System.out.println(msg.toString());
            int status = Integer.parseInt(arg.toString());
            //模拟事务执行
            switch(status){
                //成功
                case 1 : return LocalTransactionState.COMMIT_MESSAGE;
                //失败
                case 2 : return LocalTransactionState.ROLLBACK_MESSAGE;
                //宕机或其他时，回查消息
                default: return LocalTransactionState.UNKNOW;
            }
        }
        //回查消息
        @Override
        public LocalTransactionState checkLocalTransaction(MessageExt msg) {
            System.out.println("====executeLocalTransaction=======");
            System.out.println(msg.toString());
            return LocalTransactionState.COMMIT_MESSAGE;
        }
    }
}
