package com.momo.rocketmq.domian;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Order implements Serializable {
    private Long orderId;
    private String status;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Order() {
    }
    public Order(Long orderId,String status) {
        this.orderId=orderId;
        this.status=status;
    }
    public static List<Order> init(){
        List<Order> list=new ArrayList<>();
        list.add(new Order(111L,"订单已创建"));
        list.add(new Order(111L,"订单已支付"));
        list.add(new Order(222L,"订单已创建"));
        list.add(new Order(111L,"订单已完成"));
        list.add(new Order(222L,"订单已支付"));
        list.add(new Order(333L,"订单已创建"));
        list.add(new Order(222L,"订单已完成"));
        list.add(new Order(333L,"订单已支付"));
        list.add(new Order(333L,"订单已完成"));
        return  list;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", status='" + status + '\'' +
                '}';
    }
}
