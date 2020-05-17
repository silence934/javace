package com.memory.quantity;

/**
 * 作者:Code菜鸟
 * 技术交流QQ:969422014
 * CSDN博客:http://blog.csdn.net/qq969422014
 * 
 * 该接口对LookupPrivilegeValue函数中lpName参数的定义
 * 
 * 具体情况,请参阅MSDN文档
 * http://msdn.microsoft.com/en-us/library/windows/desktop/aa379180%28v=vs.85%29.aspx
 * **/
public interface LookupPrivilegeValue 
{
	//Debug权限
	public static final String SeDebugPrivilege = "SeDebugPrivilege";
	//从远程系统强制关机
	public static final String SeRemoteShutdownPrivilege = "SeRemoteShutdownPrivilege";
	//此权限的用户可以修改进程优先级
	public static final String SeIncreaseBasePriorityPrivilege = "SeIncreaseBasePriorityPrivilege";
	//权限的用户可以加载设备驱动程序
	public static final String SeLoadDriverPrivilege = "SeLoadDriverPrivilege";
	//具有此用户权限的用户可以查看和清除安全日志
	public static final String SeSecurityPrivilege = "SeSecurityPrivilege";
	//具有此用户权限的用户可以更改与一个已启动子进程相关联的默认令牌
	public static final String SeAssignPrimaryTokenPrivilege = "SeAssignPrimaryTokenPrivilege";
	//还原文件和目录
	public static final String SeRestorePrivilege = "SeRestorePrivilege";
	//具有此用户权限的用户可以关闭系统以开始新设备驱动程序的安装
	public static final String SeShutdownPrivilege = "SeShutdownPrivilege";
	//具有此用户权限的用户可以通过获取 文件系统磁盘上的对象或文件的所有权,来访问这些对象或文件
	public static final String SeTakeOwnershipPrivilege = "SeTakeOwnershipPrivilege";
}
