package com.jnet.serialize.model;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

/**
 * @author Xunwu Yang 2021-01-09
 * @version 1.0.0
 */
public class Customer implements Serializable {

    private static int COUNT = 0;
    private String name;
    private Integer age;
    private transient String password;

    private List<Order> orderList = new LinkedList<>();

    private Customer() {}

    public Customer(String name, Integer age) {
        this.name = name;
        this.age = age;
        COUNT++;
        Random random = new Random();
        password = String.valueOf(random.nextInt(10000000));
    }

    public void addOrder(Order order) {
        order.setCustomer(this);
        this.orderList.add(order);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Customer)) return false;
        Customer customer = (Customer) o;
        return Objects.equals(name, customer.name) &&
                Objects.equals(age, customer.age);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, age);
    }

    @Override
    public String toString() {
        return "Customer{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", password='" + password + '\'' +
                ", COUNT='" + COUNT + '\'' +
                ", orderList=" + orderList +
                '}';
    }
}
