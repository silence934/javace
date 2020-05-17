package com.memory.entity;

/**
 * 作者:Code菜鸟
 * 技术交流QQ:969422014
 * CSDN博客:http://blog.csdn.net/qq969422014
 * <p>
 * API函数调用结果返回对象
 **/
public class ExecuteResult {
    private int lastError;//错误代码
    private String message;//消息
    private Object value;//返回值

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getLastError() {
        return lastError;
    }

    public void setLastError(int lastError) {
        this.lastError = lastError;
    }
}
