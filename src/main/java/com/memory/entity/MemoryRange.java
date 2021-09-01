package com.memory.entity;

import com.sun.jna.Pointer;

/**
 * 作者:Code菜鸟
 * 技术交流QQ:969422014
 * CSDN博客:http://blog.csdn.net/qq969422014
 * <p>
 * 保存内存的范围
 **/
public class MemoryRange {
    private Pointer minValue;
    private Pointer maxValue;

    public Pointer getMinValue() {
        return minValue;
    }

    public void setMinValue(Pointer minValue) {
        this.minValue = minValue;
    }

    public Pointer getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(Pointer maxValue) {
        this.maxValue = maxValue;
    }
}
