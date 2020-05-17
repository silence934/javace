package com.memory.structure;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;

import java.util.Arrays;
import java.util.List;

/**
 * 作者:Code菜鸟
 * 技术交流QQ:969422014
 * CSDN博客:http://blog.csdn.net/qq969422014
 * 
 * 该Java类用于描述TOKEN_PRIVILEGES结构体
 * 
 * AdjustTokenPrivileges函数的第三个参数与第五个参数
 * 
 * TOKEN_PRIVILEGES结构的C++描述如下
 * typedef struct _TOKEN_PRIVILEGES
 * {
 * ULONG PrivilegeCount; //特权数组个数
 * LUID_AND_ATTRIBUTES Privileges[ANYSIZE_ARRAY]; //数组.类型为LUID_AND_ATTRIBUTES,这里可以理解为特权标示LUID号
 * } 
 * TOKEN_PRIVILEGES, *PTOKEN_PRIVILEGES;
 * 
 * 具体详情,请参阅MSDN文档:
 * http://msdn.microsoft.com/en-us/library/windows/desktop/aa379630(v=vs.85).aspx
 * **/
public class TOKEN_PRIVILEGES extends Structure 
{
	public int PrivilegeCount;
	public LUID_AND_ATTRIBUTES[] Privileges;

	public TOKEN_PRIVILEGES() 
	{
		this(0);
	}

	public TOKEN_PRIVILEGES(int nbOfPrivileges) 
	{
		this.PrivilegeCount = (int) nbOfPrivileges;
		this.Privileges = new LUID_AND_ATTRIBUTES[nbOfPrivileges];
	}

	public TOKEN_PRIVILEGES(Pointer p)
	{
		super(p);
		int count = p.getInt(0L);
		this.PrivilegeCount = (int) count;
		this.Privileges = new LUID_AND_ATTRIBUTES[count];
		this.read();
	}
	
	protected List<String> getFieldOrder() 
	{
		return Arrays.asList(new String[] { "PrivilegeCount", "Privileges" });
	}
}
