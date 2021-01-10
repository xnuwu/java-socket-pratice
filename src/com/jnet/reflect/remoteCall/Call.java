package com.jnet.reflect.remoteCall;

import java.io.Serializable;
import java.util.Arrays;

/**
 * @author Xunwu Yang 2021-01-10
 * @version 1.0.0
 */
public class Call implements Serializable {

    private String className;
    private String methodName;
    private Class[] paramType;
    private Object[] paramValue;
    private Object result;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class[] getParamType() {
        return paramType;
    }

    public void setParamType(Class[] paramType) {
        this.paramType = paramType;
    }

    public Object[] getParamValue() {
        return paramValue;
    }

    public void setParamValue(Object[] paramValue) {
        this.paramValue = paramValue;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "Call{" +
                "className='" + className + '\'' +
                ", methodName='" + methodName + '\'' +
                ", paramType=" + Arrays.toString(paramType) +
                ", paramValue=" + Arrays.toString(paramValue) +
                ", result=" + result +
                '}';
    }
}
