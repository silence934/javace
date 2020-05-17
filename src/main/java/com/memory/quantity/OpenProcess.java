package com.memory.quantity;

/**
 * 作者:Code菜鸟
 * 技术交流QQ:969422014
 * CSDN博客:http://blog.csdn.net/qq969422014
 * 
 * 该接口对OpenProcess函数中dwDesiredAccess参数的定义
 * 
 * 具体情况,请参阅MSDN文档
 * http://msdn.microsoft.com/en-us/library/windows/desktop/ms684880%28v=vs.85%29.aspx
 * **/
public interface OpenProcess 
{
	//进程所有可能允许的权限
	public static final int PROCESS_ALL_ACCESS = 0x1F0FFF;
	//进程允许读
	public static final int PROCESS_VM_WRITE = 0x0020;
	//进程允许写
	public static final int PROCESS_VM_READ = 0x0010;
}
