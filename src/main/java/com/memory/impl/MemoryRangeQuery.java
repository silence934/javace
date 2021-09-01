package com.memory.impl;

import com.memory.entity.ExecuteResult;
import com.memory.entity.MemoryRange;
import com.memory.interfaces.Kernel32_DLL;
import com.memory.quantity.CreateToolhelp32Snapshot;
import com.memory.quantity.OpenProcess;
import com.memory.structure.MODULEENTRY32;
import com.memory.structure.SYSTEM_INFO;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Kernel32Util;
import com.sun.jna.platform.win32.Tlhelp32;
import com.sun.jna.ptr.IntByReference;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 内存范围查询实现类
 * 作者:Code菜鸟
 * 技术交流QQ:969422014
 * CSDN博客:http://blog.csdn.net/qq969422014
 */
@Slf4j
public class MemoryRangeQuery {
    /**
     * 查询进程在内存中的开始地址与结束地址
     **/
    public ExecuteResult queryProcessRange1(int pid) {
        ExecuteResult executeResult = new ExecuteResult();
        //创建内存范围对象
        MemoryRange range = new MemoryRange();
        //创建进程模版快照，查询应用程序的在内存中的基地址
        int handleModule = Kernel32_DLL.INSTANCE.CreateToolhelp32Snapshot(CreateToolhelp32Snapshot.TH32CS_SNAPMODULE, pid);
        //快照执行结果
        int lastError = Kernel32_DLL.INSTANCE.GetLastError();
        executeResult.setLastError(lastError);


        //判断结果
        {
            if (lastError == 5) {
                executeResult.setMessage("无法打开进程,系统Debug权限获取失败,请以管理员方式重新运行程序!");
                return executeResult;
            } else if (lastError == 299) {
                //如果为299，说明只有部分权限,判断该进程是否是64位进程  声明INT指针，保存IsWow64Process返回的值
                IntByReference Wow64Process = new IntByReference();
                int handle = Kernel32_DLL.INSTANCE.OpenProcess(OpenProcess.PROCESS_ALL_ACCESS, false, pid);
                if (Kernel32_DLL.INSTANCE.IsWow64Process(handle, Wow64Process)) {
                    //如果为64位进程，那么久获取系统的内存范围
                    if (Wow64Process.getValue() == 0) {
                        executeResult = querySystemRange();
                    }
                } else {
                    executeResult.setMessage("无法打开该进程,错误代码:" + lastError);
                }
                //释放内存
                ReferenceFree.free(Wow64Process);
                Kernel32_DLL.INSTANCE.CloseHandle(handle);
                return executeResult;
            } else if (lastError != 0) {
                executeResult.setMessage("无法打开该进程,OpenProcess函数返回错误码:" + lastError);
                return executeResult;
            }
        }


        try {
            MODULEENTRY32 lpme = new MODULEENTRY32();
            if (Kernel32_DLL.INSTANCE.Module32First(handleModule, lpme)) {
               // range.setMinValue(lpme.modBaseAddr);
                if (Kernel32_DLL.INSTANCE.Module32Next(handleModule, lpme)) {
                //    range.setMaxValue(lpme.modBaseAddr);
                }
            }
            //执行结果返回值
            executeResult.setValue(range);
            //执行结果
            lastError = Kernel32_DLL.INSTANCE.GetLastError();
//            if (range.getMinValue() == 0 && lastError != 0) {
//                executeResult.setLastError(lastError);
//                log.error("Module32Next失败,错误代码:" + lastError);
//                executeResult.setMessage("Module32Next失败,错误代码:" + lastError);
//            }

        } finally {
            //释放快照
            Kernel32_DLL.INSTANCE.CloseHandle(handleModule);
        }
        return executeResult;
    }


    /**
     * 查询进程在内存中的开始地址与结束地址
     **/
    public ExecuteResult queryProcessRange(int pid) {


        ExecuteResult executeResult = new ExecuteResult();
        MemoryRange range = new MemoryRange();

        List<Tlhelp32.MODULEENTRY32W> modules = Kernel32Util.getModules(pid);


        range.setMinValue(modules.get(0).modBaseAddr);
        range.setMaxValue(modules.get(modules.size()-1).modBaseAddr);

        executeResult.setValue(range);


        return executeResult;
    }




    /**
     * 查询当前系统的可搜索的开始地址与结束地址
     **/
    public ExecuteResult querySystemRange() {
        ExecuteResult executeResult = new ExecuteResult();
        //创建内存范围对象
        MemoryRange range = new MemoryRange();
        //创建描述系统信息的结构
        SYSTEM_INFO info = new SYSTEM_INFO();
        //获取系统内存范围
        Kernel32_DLL.INSTANCE.GetSystemInfo(info);
        range.setMinValue(info.lpMinimumApplicationAddress);
        range.setMaxValue(info.lpMaximumApplicationAddress);
        //返回值
        executeResult.setValue(range);
        //调用结果
        int lastError = Kernel32_DLL.INSTANCE.GetLastError();
        if (lastError != 0) {
            executeResult.setLastError(lastError);
            executeResult.setMessage("获取系统内存地址范围失败,错误代码:" + lastError);
        }
        return executeResult;
    }
}
