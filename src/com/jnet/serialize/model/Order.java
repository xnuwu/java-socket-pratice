package com.jnet.serialize.model;

import java.io.Serializable;

/**
 * @author Xunwu Yang 2021-01-09
 * @version 1.0.0
 */
public class Order implements Serializable {
    private Customer customer;
    private String orderName;

    public Order(String orderName) {
        this.orderName = orderName;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderName='" + orderName + '\'' +
                '}';
    }
}
