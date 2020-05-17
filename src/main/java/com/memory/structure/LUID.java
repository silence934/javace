package com.memory.structure;

import java.util.Arrays;
import java.util.List;

import com.sun.jna.Structure;

/**
 * 作者:Code菜鸟
 * 技术交流QQ:969422014
 * CSDN博客:http://blog.csdn.net/qq969422014
 * 
 * 该Java类用于描述LUID结构体,MSDN解释:代表一个本地局部唯一值
 * 
 * LUID结构的C++描述如下
 * 
 * 在这里,这个结构体作为LUID_AND_ATTRIBUTES结构体中的一个值,具体详情,请参阅本包中的LUID_AND_ATTRIBUTES结构类
 * 
 * typedef struct _LUID 
 * {
 * 	DWORD LowPart; //整数型 低三十二位
 *  LONG HighPart; //整数型 高三十二位
 * } 
 * LUID, *PLUID;
 * 
 * 具体详情,请参阅MSDN文档:
 * http://msdn.microsoft.com/en-us/library/windows/desktop/aa379261%28v=vs.85%29.aspx
 * **/
public class LUID extends Structure 
{
	public int LowPart;
	public int HighPart;

	protected List<String> getFieldOrder() 
	{
		return Arrays.asList(new String[] { "LowPart", "HighPart" });
	}
}
