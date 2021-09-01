package com.memory.structure;

import java.util.Arrays;
import java.util.List;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.platform.win32.Tlhelp32;

/**
 * 作者:Code菜鸟
 * 技术交流QQ:969422014
 * CSDN博客:http://blog.csdn.net/qq969422014
 * <p>
 * 该Java类用于描述MODULEENTRY32结构体
 * <p>
 * 函数Module32First与Module32Next的第二个参数
 * <p>
 * MODULEENTRY32结构的C++描述如下
 * <p>
 * typedef struct tagMODULEENTRY32
 * {
 * DWORD dwSize; //指定结构的长度,果你不初始化的dwSize，Module32First与Module32Next将失败
 * DWORD th32ModuleID; //此成员已经不再被使用，通常被设置为1
 * DWORD th32ProcessID; //检查的进程标识符
 * DWORD GlblcntUsage; //全局模块的使用计数，即模块的总载入次数
 * DWORD ProccntUsage; //全局模块的使用计数,与GlblcntUsage相同
 * BYTE *modBaseAddr; //在其所属的进程范围内模块的基址。
 * DWORD modBaseSize; //模块的大小
 * HMODULE hModule; //所属进程的范围内，模块句柄
 * TCHAR szModule[MAX_PATH]; //模块名
 * TCHAR szExePath[MAX_PATH]; //模块的路径
 * } MODULEENTRY32, *PMODULEENTRY32, *LPMODULEENTRY32;
 * <p>
 * 具体详情,请参阅MSDN文档:
 * http://msdn.microsoft.com/en-us/library/windows/desktop/ms684225(v=vs.85).aspx
 **/
public class MODULEENTRY32 extends Structure {
    public int dwSize;
    public long th32ModuleID;
    public long th32ProcessID;
    public long GlblcntUsage;
    public long ProccntUsage;
    public long modBaseAddr;
    public long modBaseSize;
    public long hModule;
    //MAX_MODUE_NAME32常量255
    public char[] szModule = new char[256];
    //这个260长度是C++ windef.h文件中定义的常量#define MAX_PATH 260
    public char[] szExePath = new char[260];

    public MODULEENTRY32() {
        this.dwSize = this.size();
    }

    public String getSzModuleStr() {
        return Native.toString(szModule);
    }

    public String getSzExePath() {
        return Native.toString(szExePath);
    }

    @Override
    protected List<String> getFieldOrder() {
        return Arrays.asList(new String[]{"dwSize", "th32ModuleID",
                "th32ProcessID", "GlblcntUsage", "ProccntUsage",
                "modBaseAddr", "modBaseSize", "hModule",
                "szModule", "szExePath"});
    }
}
