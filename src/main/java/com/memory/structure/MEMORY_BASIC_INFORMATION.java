package com.memory.structure;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;

import java.util.Arrays;
import java.util.List;

/**
 * 作者:Code菜鸟
 * 技术交流QQ:969422014
 * CSDN博客:http://blog.csdn.net/qq969422014
 * <p>
 * 该Java类用于描述MEMORY_BASIC_INFORMATION结构体
 * <p>
 * MEMORY_BASIC_INFORMATION结构的C++描述如下
 * <p>
 * VirtualQueryEx函数的第三个参数
 * <p>
 * typedef struct _MEMORY_BASIC_INFORMATION {
 * PVOID BaseAddress; // 区域基地址。
 * PVOID AllocationBase; // 分配基地址。
 * DWORD AllocationProtect; // 区域被初次保留时赋予的保护属性。
 * SIZE_T RegionSize; // 区域大小（以字节为计量单位）。
 * DWORD State; // 状态 MEM_FREE、MEM_RESERVE或 MEM_COMMIT。
 * DWORD Protect; // 保护属性。
 * DWORD Type; // 类型。
 * } MEMORY_BASIC_INFORMATION, *PMEMORY_BASIC_INFORMATION;
 * <p>
 * 具体详情,请参阅MSDN文档:
 * http://msdn.microsoft.com/en-us/library/windows/desktop/aa366775%28v=vs.85%29.aspx
 **/
public class MEMORY_BASIC_INFORMATION extends Structure {
    //这个常量是state的其中一个值,表示内存已物理分配
    public static final int MEM_COMMIT = 0x1000;

    //这个常量是protect的其中一个值,表示可读写内存
    public static final int PAGE_READWRITE = 0x04;

    //这个常量是state的其中一个值,表示空闲状态
    public static final int MEM_FREE = 0x10000;

    //这个常量是type的其中一个值,表示该内存区域是私有的
    public static final int MEM_PRIVATE = 0x20000;

    public Pointer baseAddress;

    public Pointer allocationBase;

    public int allocationProtect;

    public int regionSize;

    public int state;

    public int protect;

    public int type;
    //其它常量请参阅MSDN文档

    @Override
    protected List<String> getFieldOrder() {
        return Arrays.asList("baseAddress",
                             "allocationBase", "allocationProtect", "regionSize",
                             "state", "protect", "type");
    }
}
