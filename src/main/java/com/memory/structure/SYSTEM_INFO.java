package com.memory.structure;

import java.util.Arrays;
import java.util.List;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;

/**
 * 作者:Code菜鸟
 * 技术交流QQ:969422014
 * CSDN博客:http://blog.csdn.net/qq969422014
 * 
 * 该Java类用于描述SYSTEM_INFO结构体
 * 
 * GetSystemInfo函数的参数
 * 
 * SYSTEM_INFO结构的C++描述如下
 * typedef struct _SYSTEM_INFO {
 * union {
 * 		DWORD dwOemId; //已废弃,保留应该是为了向前系统的兼容
 * 		struct {
 * 				WORD wProcessorArchitecture; //指定系统中的中央处理器的体系结构
 * 				WORD wReserved; /保留保留字段
 * 		};
 * };
 * DWORD dwPageSize; //指定页面的大小
 * LPVOID lpMinimumApplicationAddress; //指向应用程序和DLL可以访问的最低内存地址
 * LPVOID lpMaximumApplicationAddress; //指向应用程序和DLL可以访问的最高内存地址
 * DWORD dwActiveProcessorMask; //系统中装配了的中央处理器的掩码
 * DWORD dwNumberOfProcessors; //CPU数量
 * DWORD dwProcessorType; //CPU类型
 * DWORD dwAllocationGranularity; //被分配的虚拟内存空间的粒度
 * WORD wProcessorLevel; //指定系统体系结构依赖的处理器级别
 * WORD wProcessorRevision; //指定系统体系结构依赖的处理器修订版本号
 * } SYSTEM_INFO;
 * 
 * 具体详情,请参阅MSDN文档:
 * http://msdn.microsoft.com/en-us/library/windows/desktop/ms724958%28v=vs.85%29.aspx
 * **/
public class SYSTEM_INFO extends Structure 
{
	public int processorArchitecture;
	public int dwPageSize;
	public Pointer lpMinimumApplicationAddress;
	public Pointer lpMaximumApplicationAddress;
	public int dwActiveProcessorMask;
	public int dwNumberOfProcessors;
	public int dwProcessorType;
	public int dwAllocationGranularity;
	public int wProcessorLevel;
	public int wProcessorRevision;

	protected List<String> getFieldOrder() 
	{
		return Arrays.asList(new String[] { "processorArchitecture",
				"dwPageSize", "lpMinimumApplicationAddress",
				"lpMaximumApplicationAddress", "dwActiveProcessorMask",
				"dwNumberOfProcessors", "dwProcessorType",
				"dwAllocationGranularity", "wProcessorLevel",
				"wProcessorRevision" });
	}
}
