package com.memory.structure;

import java.util.Arrays;
import java.util.List;

import com.sun.jna.Structure;

/**
 * 作者:Code菜鸟
 * 技术交流QQ:969422014
 * CSDN博客:http://blog.csdn.net/qq969422014
 * <p>
 * 该Java类用于描述LUID_AND_ATTRIBUTES结构体,MSDN解释:代表一个本地唯一标示符LUID和它的属性
 * <p>
 * MEMORY_BASIC_INFORMATION结构的C++描述如下
 * <p>
 * 在这里,这个结构体作为TOKEN_PRIVILEGES结构体中的一个值,具体详情,请参阅本包中的TOKEN_PRIVILEGES结构类
 * <p>
 * typedef struct _LUID_AND_ATTRIBUTES
 * {
 * LUID Luid; //LUID值
 * DWORD Attributes; //标识了LUID属性,取决于LUID的定义和使用
 * }
 * LUID_AND_ATTRIBUTES;
 * <p>
 * 具体详情,请参阅MSDN文档:
 * http://msdn.microsoft.com/zh-tw/vstudio/aa379263
 **/
public class LUID_AND_ATTRIBUTES extends Structure {
    public LUID Luid;
    public int Attributes;

    protected List<String> getFieldOrder() {
        return Arrays.asList(new String[]{"Luid", "Attributes"});
    }

    public LUID_AND_ATTRIBUTES() {
    }

    public LUID_AND_ATTRIBUTES(LUID luid, int attributes) {
        this.Luid = luid;
        this.Attributes = attributes;
    }
}
