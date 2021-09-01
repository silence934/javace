package com.memory.structure;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;

import java.util.Arrays;
import java.util.List;

/**
 * 作者:Code菜鸟
 * 技术交流QQ:969422014
 * CSDN博客:http://blog.csdn.net/qq969422014
 * <p>
 * 该Java类用于描述 PROCESSENTRY32 结构体
 * <p>
 * Process32Next函数的第二个参数
 * <p>
 * LPPROCESSENTRY32 结构的C++描述如下
 * <p>
 * typedef struct tagPROCESSENTRY32
 * {
 * DWORD dwSize; 结构体的大小
 * DWORD cntUsage; 引用计数,已不在使用,总是为0
 * DWORD th32ProcessID; 进程ID
 * ULONG_PTR th32DefaultHeapID; 进程默认堆ID,已不在使用,总是为0
 * DWORD th32ModuleID; 进程模块ID ,已不在使用,总是为0
 * DWORD cntThreads; 进程开启的线程计数
 * DWORD th32ParentProcessID; 父进程ID
 * LONG pcPriClassBase; 优先权
 * DWORD dwFlags; 已不在使用,总是为0
 * TCHAR szExeFile[MAX_PATH]; 进程的可执行文件名称。要获得可执行文件的完整路径，应调用Module32First函数，再检查其返回的MODULEENTRY32结构的szExePath成员。但是，如果被调用进程是一个64位程序，您必须调用QueryFullProcessImageName函数去获取64位进程的可执行文件完整路径名
 * } PROCESSENTRY32, *PPROCESSENTRY32;
 * <p>
 * 具体详情,请参阅MSDN文档:
 * http://msdn.microsoft.com/en-us/library/windows/desktop/ms684839
 **/
public class PROCESSENTRY32 extends Structure {
    public int dwSize;
    public int cntUsage;
    public int th32ProcessID;
    public int th32DefaultHeapID;
    public int th32ModuleID;
    public int cntThreads;
    public int th32ParentProcessID;
    public int pcPriClassBase;
    public int dwFlags;
    //这个260长度是C++ windef.h文件中定义的常量#define MAX_PATH 260
    public char[] szExeFile = new char[260];

    public PROCESSENTRY32() {
        this.dwSize = this.size();
    }

    public PROCESSENTRY32(Pointer memory) {
        super(memory);
        this.read();
    }

    @Override
    protected List<String> getFieldOrder() {
        return Arrays.asList("dwSize", "cntUsage",
                             "th32ProcessID", "th32DefaultHeapID", "th32ModuleID",
                             "cntThreads", "th32ParentProcessID", "pcPriClassBase",
                             "dwFlags", "szExeFile");
    }

    public String getSzExeFileStr() {
        return Native.toString(szExeFile);
    }
}
