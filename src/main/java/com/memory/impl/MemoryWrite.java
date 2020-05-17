package com.memory.impl;

import com.memory.entity.ExecuteResult;
import com.memory.interfaces.Kernel32_DLL;
import com.memory.quantity.OpenProcess;
import com.memory.structure.MEMORY_BASIC_INFORMATION;
import com.sun.jna.Memory;
import com.sun.jna.Pointer;

/**
 * 内存的写实现类
 * 作者:Code菜鸟
 * 技术交流QQ:969422014
 * CSDN博客:http://blog.csdn.net/qq969422014
 * */
public class MemoryWrite 
{	
	/**
	 * 写入内存实现方法
	 * pid 进程ID
	 * lpBaseAddress 写入地址
	 * value 写入值
	 * dataType 数据类型,这个值确定value的实际数据类型
	 * **/
	public ExecuteResult write(int pid,int lpBaseAddress,String value,int dataType)
	{
		ExecuteResult result = new ExecuteResult();
		int hProcess = Kernel32_DLL.INSTANCE.OpenProcess(OpenProcess.PROCESS_ALL_ACCESS, false, pid);
		//判断进程句柄是否打开成功
		int lastError = Kernel32_DLL.INSTANCE.GetLastError();
		result.setLastError(lastError);
		if(lastError==5)
		{
			result.setMessage("进程拒绝访问,可能是系统Debug权限获取失败,请以管理员方式重新运行程序!");
			return result;
		}
		else if(lastError!=0)
		{
			result.setMessage("无法打开该进程,错误代码:"+lastError);
			return result;
		}
		try 
		{
			//判断内存地址是否合法幷且是否满足读写权限
			MEMORY_BASIC_INFORMATION lpBuffer = new MEMORY_BASIC_INFORMATION();
			Kernel32_DLL.INSTANCE.VirtualQueryEx(hProcess, lpBaseAddress, lpBuffer, lpBuffer.size());
			if(!(lpBuffer.state == MEMORY_BASIC_INFORMATION.MEM_COMMIT 
					&& lpBuffer.protect == MEMORY_BASIC_INFORMATION.PAGE_READWRITE))
			{
				result.setLastError(-1);
				result.setMessage("内存地址不存在或者该内存无法读写!");
				return result;
			}
			//新内存地址,用于写入内存用
			Pointer updatePointer = null;
			int size = 4;
			switch(dataType)
			{
			//整形int
			case 0:
				size = 4;
				updatePointer = new Memory(size);
				updatePointer.setInt(0, Integer.parseInt(value));
				break;
			//短整形short
			case 1:
				size = 2;
				updatePointer = new Memory(size);
				updatePointer.setShort(0, Short.parseShort(value));
				break;
			//长整形Long
			case 2:
				size = 8;
				updatePointer = new Memory(size);
				updatePointer.setLong(0, Long.parseLong(value));
				break;
			//单精度浮点 float
			case 3:
				size = 4;
				updatePointer = new Memory(size);
				updatePointer.setFloat(0, Float.parseFloat(value));
				break;
			//双精度浮点 double
			case 4:
				size = 8;
				updatePointer = new Memory(size);
				updatePointer.setDouble(0, Double.parseDouble(value));
				break;
			//字节byte
			case 5:
				size = 1;
				updatePointer = new Memory(size);
				updatePointer.setByte(0, Byte.parseByte(value));
				break;
			}
			//写入内存
			boolean writeResult = Kernel32_DLL.INSTANCE.WriteProcessMemory(hProcess, lpBaseAddress, updatePointer, size, 0);
			//是否写入成功
			lastError = Kernel32_DLL.INSTANCE.GetLastError();
			if((!writeResult) || lastError!=0)
			{
				result.setLastError(lastError);
				result.setMessage("内存写入发生错误,错误代码:"+lastError);
				return result;
			}
			result.setLastError(0);
			result.setMessage("写入成功!");
			return result;
		} 
		catch (Exception e)
		{
			result.setLastError(-1);
			result.setMessage("写入失败,请检查输入值是否正确或超出范围!\n错误代码:"+e.getMessage());
		}
		finally
		{
			Kernel32_DLL.INSTANCE.CloseHandle(hProcess);
		}
		return result;
	}
}
