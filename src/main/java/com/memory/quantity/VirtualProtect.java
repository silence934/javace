package com.memory.quantity;

/**
 * 作者:Code菜鸟
 * 技术交流QQ:969422014
 * CSDN博客:http://blog.csdn.net/qq969422014
 * <p>
 * 该接口对VirtualProtectEx函数中flNewProtect参数的定义
 * <p>
 * 具体情况,请参阅MSDN文档
 * http://msdn.microsoft.com/zh-cn/library/aa366899%28v=vs.85%29.aspx
 **/
public interface VirtualProtect {
    //读写
    public static final int PAGE_READWRITE = 0x04;
    //仅读
    public static final int PAGE_READONLY = 0x02;
    //读写和执行
    public static final int PAGE_EXECUTE_READWRITE = 0x40;
    //写拷贝和执行
    public static final int PAGE_EXECUTE_WRITECOPY = 0x80;
}
