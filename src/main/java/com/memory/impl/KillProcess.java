package com.memory.impl;

import com.memory.entity.ExecuteResult;
import com.memory.interfaces.Kernel32_DLL;
import com.memory.quantity.OpenProcess;
import com.sun.jna.ptr.IntByReference;

/**
 * 进程杀死实现类
 * 作者:Code菜鸟
 * 技术交流QQ:969422014
 * CSDN博客:http://blog.csdn.net/qq969422014
 */
public class KillProcess {
    /**
     * 具体解释,请查看com.memory.interfaces.Kernel32_DLL接口中的描述
     * pid 进程ID,这个值可以通过任务管理器查看或通过CreateToolhelp32Snapshot函数获取
     **/
    public ExecuteResult kill(int pid) {
        ExecuteResult executeResult = new ExecuteResult();
        int hProcess = Kernel32_DLL.INSTANCE.OpenProcess(OpenProcess.PROCESS_ALL_ACCESS, false, pid);
        //INT指针,保存GetExitCodeProcess函数调用成功后,返回的程序退出值
        IntByReference lpExitCode = new IntByReference();
        try {
            //获取程序的退出代码
            if (Kernel32_DLL.INSTANCE.GetExitCodeProcess(hProcess, lpExitCode)) {
                //退出程序
                Kernel32_DLL.INSTANCE.TerminateProcess(hProcess, lpExitCode.getValue());
            }
        } finally {
            //释放INT指针
            ReferenceFree.free(lpExitCode);
            Kernel32_DLL.INSTANCE.CloseHandle(hProcess);
        }
        //获取执行结果
        int lastError = Kernel32_DLL.INSTANCE.GetLastError();
        executeResult.setLastError(lastError);
        if (lastError != 0)
            executeResult.setMessage("杀死进程时发生错误,错误代码:" + lastError);
        return executeResult;
    }
}
