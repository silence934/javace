package com.memory.impl;

import java.util.ArrayList;
import java.util.List;

import com.memory.interfaces.Kernel32_DLL;
import com.memory.quantity.CreateToolhelp32Snapshot;
import com.memory.structure.PROCESSENTRY32;
import com.memory.entity.ExecuteResult;
import com.memory.entity.Process;

/**
 * 获取系统进程实现类
 * 作者:Code菜鸟
 * 技术交流QQ:969422014
 * CSDN博客:http://blog.csdn.net/qq969422014
 * **/
public class LoadSystemProcessInfo
{
	/**
	 * 得到系统进程列表
	 * */
	public ExecuteResult getProcess()
	{
		ExecuteResult executeResult = new ExecuteResult();
		//获取结果集
		List<Process> list = new ArrayList<Process>();
		//创建当前系统进程快照,返回快照句柄,具体参考com.memory.interfaces.Kernel32_DLL中的描述
		int processHandle = Kernel32_DLL.INSTANCE.CreateToolhelp32Snapshot(CreateToolhelp32Snapshot.TH32CS_SNAPPROCESS, 0);
		//快照結果
		int lastError = Kernel32_DLL.INSTANCE.GetLastError();
		if(processHandle==0 || lastError!=0)
		{
			executeResult.setLastError(lastError);
			executeResult.setMessage("获取系统进程信息失败,错误代码:"+lastError);
			return executeResult;
		}
		try 
		{
			//创建进程结构体,用于保存进程的相关信息,具体参考com.memory.entity.Process中的描述
			PROCESSENTRY32 lppe = new PROCESSENTRY32();
			//根据快照句柄遍历系统进程
			while(Kernel32_DLL.INSTANCE.Process32Next(processHandle, lppe))
			{
				Process temp = new Process();
				temp.setProcessName(lppe.getSzExeFileStr());
				temp.setPid(lppe.th32ProcessID);
				list.add(temp);
			}
			if(list.size()!=0)
			{
				executeResult.setValue(list);
			}
			else
			{
				lastError = Kernel32_DLL.INSTANCE.GetLastError();
				executeResult.setLastError(lastError);
				executeResult.setMessage("获取系统进程信息失败,错误代码:"+lastError);
			}
		} 
		finally
		{
			//释放句柄资源
			Kernel32_DLL.INSTANCE.CloseHandle(processHandle);
		}
		return executeResult;
	}
}
