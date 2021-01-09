package com.jnet.serialize;

import java.io.*;
import java.util.Date;
import java.util.Objects;

/**
 * @author Xunwu Yang 2021-01-09
 * @version 1.0.0
 */
public class ObjectSaver {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("objectFile.obj"));

        String obj1 = "hello";
        Date obj2 = new Date();
        Customer obj3 = new Customer("xunwu", 22);

        out.writeObject(obj1);
        out.writeObject(obj2);
        out.writeObject(obj3);
        out.flush();
        out.close();

        ObjectInputStream in = new ObjectInputStream(new FileInputStream("objectFile.obj"));
        String obj11 = (String) in.readObject();
        System.out.println("obj11:" + obj11);
        System.out.println("obj1 == obj11:" + (obj1 == obj11));

        Date obj12 = (Date) in.readObject();
        System.out.println("obj12:" + obj12);
        System.out.println("obj2 == obj12:" + (obj2 == obj12));

        Customer obj13 = (Customer) in.readObject();
        System.out.println("obj3:" + obj13);
        System.out.println("obj3 == obj13:" + (obj3 == obj13));
        in.close();
    }
}

class Customer implements Serializable {
    private String name;
    private Integer age;

    private Customer() {}

    public Customer(String name, Integer age) {
        this.name = name;
        this.age = age;
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
                ", hashcode=" + hashCode() +
                '}';
    }
}
