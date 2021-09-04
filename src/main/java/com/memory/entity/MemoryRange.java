package com.memory.entity;

import com.sun.jna.Pointer;
import lombok.Data;

/**
 * 作者:Code菜鸟
 * 技术交流QQ:969422014
 * CSDN博客:http://blog.csdn.net/qq969422014
 * <p>
 * 保存内存的范围
 **/
@Data
public class MemoryRange {
    private Pointer minValue;
    private Pointer maxValue;
}
