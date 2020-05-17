package com.memory.quantity;

/**
 * 作者:Code菜鸟
 * 技术交流QQ:969422014
 * CSDN博客:http://blog.csdn.net/qq969422014
 * <p>
 * 该接口对CreateToolhelp32Snapshot函数中dwFlags参数的定义
 * <p>
 * 具体情况,请参阅MSDN文档
 * http://msdn.microsoft.com/en-us/library/windows/desktop/ms682489%28v=vs.85%29.aspx
 **/
public interface CreateToolhelp32Snapshot {
    //在快照中包含在th32ProcessID中指定的进程的所有的堆
    public static final int TH32CS_SNAPHEAPLIST = 0x1;
    //在快照中包含系统中所有的进程
    public static final int TH32CS_SNAPPROCESS = 0x2;
    //在快照中包含系统中所有的线程
    public static final int TH32CS_SNAPTHREAD = 0x4;
    //在快照中包含在th32ProcessID中指定的进程的所有的模块
    public static final int TH32CS_SNAPMODULE = 0x8;
    //在快照中包含系统中所有的进程和线程
    public static final int TH32CS_SNAPALL = (TH32CS_SNAPHEAPLIST | TH32CS_SNAPPROCESS | TH32CS_SNAPTHREAD | TH32CS_SNAPMODULE);
    //声明快照句柄是可继承的
    public static final int TH32CS_INHERIT = 0x80000000;
}
