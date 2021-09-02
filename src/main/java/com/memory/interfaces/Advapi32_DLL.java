package com.memory.interfaces;

import com.memory.structure.LUID;
import com.memory.structure.TOKEN_PRIVILEGES;
import com.sun.jna.Native;
import com.sun.jna.ptr.ByReference;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.win32.StdCallLibrary;
import com.sun.jna.win32.W32APIOptions;

/**
 * 作者:Code菜鸟
 * 技术交流QQ:969422014
 * CSDN博客:http://blog.csdn.net/qq969422014
 * <p>
 * 该接口映射windows系统中的Advapi32.dll中的各个函数API
 **/
public interface Advapi32_DLL extends StdCallLibrary {
    /**
     * 加载DLL文件库
     * 指定編碼為UNICODE_OPTIONS,不然要亂碼
     */
    Advapi32_DLL INSTANCE = Native.load("Advapi32", Advapi32_DLL.class
            , W32APIOptions.UNICODE_OPTIONS);

    /**
     * 该函数用来打开与进程相关联的访问令牌
     * <p>
     * C++函数原型
     * BOOL OpenProcessToken(
     * __in HANDLE ProcessHandle, //要修改访问权限的进程句柄
     * __in DWORD DesiredAccess, //指定你要进行的操作类型
     * __out PHANDLE TokenHandle //返回的访问令牌指针
     * )
     * <p>
     * 函数具体详情,请参阅MSDN文档
     * http://msdn.microsoft.com/en-us/library/windows/desktop/aa379295(v=vs.85).aspx
     **/
    boolean OpenProcessToken(int ProcessHandle, int DesiredAccess, ByReference TokenHandle);

    /**
     * 该函数用于查看系统权限的特权值
     * <p>
     * C++函数原型
     * BOOL LookupPrivilegeValue(
     * LPCTSTR lpSystemName, //表示所要查看的系统名,NULL为本地系统
     * LPCTSTR lpName,//指定特权的名称
     * PLUID lpLuid //接收所返回的制定特权名称的信息,返回信息到一个LUID结构体里
     * );
     * <p>
     * 函数具体详情,请参阅MSDN文档
     * http://msdn.microsoft.com/en-us/library/windows/desktop/aa379180%28v=vs.85%29.aspx
     **/
    boolean LookupPrivilegeValue(String lpSystemName, String lpName, LUID lpLuid);

    /**
     * 该函数用于启用或禁止,指定访问令牌的特权
     * <p>
     * C++函数原型
     * BOOL WINAPI AdjustTokenPrivileges(
     * HANDLE            TokenHandle, //包含特权的句柄
     * BOOL              DisableAllPrivileges, //禁用所有权限标志
     * PTOKEN_PRIVILEGES NewState, //新特权信息的指针(结构体)
     * DWORD             BufferLength, //缓冲数据大小
     * PTOKEN_PRIVILEGES PreviousState, //接收被改变特权当前状态的Buffer
     * PDWORD            ReturnLength //接收PreviousState缓存区要求的大小
     * );
     * <p>
     * 函数具体详情,请参阅MSDN文档
     * http://msdn.microsoft.com/en-us/library/aa375202%28VS.85%29.aspx
     **/
    boolean AdjustTokenPrivileges(int TokenHandle, boolean DisableAllPrivileges, TOKEN_PRIVILEGES NewState, int BufferLength, TOKEN_PRIVILEGES PreviousState, IntByReference ReturnLength);
}
