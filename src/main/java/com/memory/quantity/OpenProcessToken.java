package com.memory.quantity;

/**
 * 作者:Code菜鸟
 * 技术交流QQ:969422014
 * CSDN博客:http://blog.csdn.net/qq969422014
 * 
 * 该接口对OpenProcessToken函数中DesiredAccess参数的定义
 * 
 * 具体情况,请参阅MSDN文档
 * http://msdn.microsoft.com/en-us/library/windows/desktop/aa379295(v=vs.85).aspx
 * **/
public interface OpenProcessToken 
{
	//特权激活
	public static final int SE_PRIVILEGE_ENABLED = 0x00000002;
	//令牌主要归属
	public static final int TOKEN_ASSIGN_PRIMARY = 0x00000001;
	//复制令牌
	public static final int TOKEN_DUPLICATE = 0x00000002;
	//模仿令牌
	public static final int TOKEN_IMPERSONATE = 0x00000004;
	//查询令牌
	public static final int TOKEN_QUERY = 0x00000008;
	//查询令牌源
	public static final int TOKEN_QUERY_SOURCE = 0x00000010;
	//调整令牌特权
	public static final int TOKEN_ADJUST_PRIVILEGES = 0x00000020;
	//调整令牌组
	public static final int TOKEN_ADJUST_GROUPS = 0x00000040;
	//调整令牌默认
	public static final int TOKEN_ADJUST_DEFAULT = 0x00000080;
	//调整令牌会话ID
	public static final int TOKEN_ADJUST_SESSIONID = 0x00000100;
	public static final int STANDARD_RIGHTS_READ = 0x00020000;
	public static final int STANDARD_RIGHTS_REQUIRED = 0x000F0000;
	public static final int TOKEN_READ = (STANDARD_RIGHTS_READ | TOKEN_QUERY);
	//全部访问权限
	public static final int TOKEN_ALL_ACCESS = (STANDARD_RIGHTS_REQUIRED | TOKEN_ASSIGN_PRIMARY |
			TOKEN_DUPLICATE | TOKEN_IMPERSONATE | TOKEN_QUERY | TOKEN_QUERY_SOURCE |
			TOKEN_ADJUST_PRIVILEGES | TOKEN_ADJUST_GROUPS | TOKEN_ADJUST_DEFAULT | TOKEN_ADJUST_SESSIONID);
}
