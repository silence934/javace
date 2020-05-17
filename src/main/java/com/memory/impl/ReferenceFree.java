package com.memory.impl;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.PointerType;

/**
 * 指针内存释放实现类,该类用于手动释放调用C++函数时,所产生的内存指针!防止内存泄露
 * 作者:Code菜鸟
 * 技术交流QQ:969422014
 * CSDN博客:http://blog.csdn.net/qq969422014
 **/
public class ReferenceFree {
    private ReferenceFree() {
    }

    ;

    public static void free(PointerType p) {
        //释放指针
        Native.free(Pointer.nativeValue(p.getPointer()));
        //如果释放指针后,不调用以下代码,Java GC时会发生不可预料的错误,导致程序闪退!
        Pointer.nativeValue(p.getPointer(), 0);
    }

    public static void free(Pointer p) {
        //释放指针
        Native.free(Pointer.nativeValue(p));
        //如果释放指针后,不调用以下代码,Java GC时会发生不可预料的错误,导致程序闪退!
        Pointer.nativeValue(p, 0);
    }
}
