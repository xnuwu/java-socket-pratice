package com.jnet.serialize.model;

import com.sun.org.apache.xpath.internal.operations.Or;

import java.io.Serializable;

/**
 * @author Xunwu Yang 2021-01-09
 * @version 1.0.0
 */
public class Order implements Serializable {

    private static final long serialVersionUID = 1;

    private Customer customer;
    private String orderName;
    private Double price;

    public Order() {
    }

    public Order(String orderName) {
        this(orderName, 0.0);
    }

    public Order(String orderName, Double price) {
        this.orderName = orderName;
        this.price = price;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Order{" +
                ", orderName='" + orderName + '\'' +
                ", price=" + price +
                '}';
    }
}
