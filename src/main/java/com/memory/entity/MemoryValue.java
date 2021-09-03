package com.memory.entity;

import lombok.Data;

/**
 * 作者:Code菜鸟
 * 技术交流QQ:969422014
 * CSDN博客:http://blog.csdn.net/qq969422014
 * <p>
 * 用于保存内存的地址及其值的一个类
 **/
@Data
public class MemoryValue {

    private long address;

    private String value;

    private String address16;


    public String toString() {
        return address16 + " " + value;
    }
}
