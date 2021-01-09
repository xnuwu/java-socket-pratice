package com.jnet.reflect;

import com.jnet.reflect.model.Customer;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author Xunwu Yang 2021-01-09
 * @version 1.0.0
 */
public class ReflectCopy {

    public static void main(String[] args) throws Exception {
        Customer customer = new Customer("xunwu", 29);
        customer.setId(1L);
        System.out.println("origin:" + customer);

        ReflectCopy reflectCopy = new ReflectCopy();
        Customer target = (Customer) reflectCopy.copy(customer);
        System.out.println("copy:" + target);
    }

    public Object copy(Object object) throws Exception {

        Class classType = object.getClass();

        Object target = classType.getConstructor(new Class[] {}).newInstance(new Object[] {});

        Field[] fields = classType.getDeclaredFields();
        for(Field field : fields) {

            String fieldName = field.getName();
            String getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            String setMethodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);

            Method getMethod = classType.getMethod(getMethodName, new Class[] {});
            Method setMethod = classType.getMethod(setMethodName, new Class[] {field.getType()});

            Object value = getMethod.invoke(object, new Object[] {});
            System.out.println(fieldName + ":" + value);
            setMethod.invoke(target, new Object[] {value});
        }
        return target;
    }
}
