package com.memory.entity;

/**
 * 作者:Code菜鸟
 * 技术交流QQ:969422014
 * CSDN博客:http://blog.csdn.net/qq969422014
 * <p>
 * 用于保存内存的地址及其值的一个类
 **/
public class MemoryValue {
    private long address;
    private String value;
    private String address16;

    public String getAddress16() {
        return address16;
    }

    public void setAddress16(String address16) {
        this.address16 = address16;
    }

    public long getAddress() {
        return address;
    }

    public void setAddress(long address) {
        this.address = address;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String toString() {
        return address16 + " " + value;
    }
}
