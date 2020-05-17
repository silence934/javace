package com.memory.structure;

import com.sun.jna.Pointer;
import com.sun.jna.ptr.ByReference;

/**
 * 作者:Code菜鸟
 * 技术交流QQ:969422014
 * CSDN博客:http://blog.csdn.net/qq969422014
 * <p>
 * 该Java类用于描述C++ HANDLE句柄,这个句柄是指针类型
 **/
public class HANDLEByReference extends ByReference {
    /**
     * 无参构造,设置句柄为0,为空句柄
     **/
    public HANDLEByReference() {
        this(0);
    }

    /**
     * 有参构造,初始化句柄指针大小,同时初始化的默认值
     * h 句柄的值
     **/
    public HANDLEByReference(int h) {
        //初始化指针的大小为4字节
        super(4);
        this.setValue(h);
    }

    /**
     * 设置句柄指针的值
     */
    public void setValue(int h) {
        this.getPointer().setPointer(0L, h != 0 ? new Pointer(h) : new Pointer(0));
    }

    /**
     * 获取句柄指针的值
     */
    public int getValue() {
        Pointer p = this.getPointer().getPointer(0L);
        if (p == null) {
            return 0;
        } else {
            return (int) Pointer.nativeValue(p);
        }
    }
}
