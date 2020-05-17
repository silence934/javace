package com.memory.interfaces;

import com.memory.structure.MEMORY_BASIC_INFORMATION;
import com.memory.structure.MODULEENTRY32;
import com.memory.structure.PROCESSENTRY32;
import com.memory.structure.SYSTEM_INFO;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.win32.StdCallLibrary;
import com.sun.jna.win32.W32APIOptions;

/**
 * 作者:Code菜鸟
 * 技术交流QQ:969422014
 * CSDN博客:http://blog.csdn.net/qq969422014
 * <p>
 * 该接口映射windows系统中的Kernel32.dll中的各个函数API
 **/
public interface Kernel32_DLL extends StdCallLibrary {
    /**
     * 加载DLL文件库
     * 指定編碼為UNICODE_OPTIONS,不然要亂碼
     */
    Kernel32_DLL INSTANCE = (Kernel32_DLL) Native.load("Kernel32",
            Kernel32_DLL.class, W32APIOptions.UNICODE_OPTIONS);

    /**
     * 该函数用于获取系统进程列表,进程ID等快照信息,成功返回快照句柄
     * <p>
     * C++函数原型
     * HANDLE WINAPI CreateToolhelp32Snapshot(
     * DWORD dwFlags, //用来指定“快照”中需要返回的对象，可以是com.memory.interfaces.CreateToolhelp32Snapshot类中的TH32CS_SNAPPROCESS等
     * DWORD th32ProcessID //一个进程ID号，用来指定要获取哪一个进程的快照，当获取系统进程列表或获取 当前进程快照时可以设为0
     * );
     * <p>
     * th32ProcessID 指定将要快照的进程ID
     * 如果该参数为0表示快照当前进程
     * 该参数只有在设置了TH32CS_SNAPHEAPLIST或者TH32CS_SNAPMODULE后才有效
     * 在其他情况下该参数被忽略，所有的进程都会被快照
     * <p>
     * 函数具体详情,请参阅MSDN文档
     * http://msdn.microsoft.com/en-us/library/windows/desktop/ms682489%28v=vs.85%29.aspx
     **/
    int CreateToolhelp32Snapshot(int dwFlags, int th32ProcessID);

    /**
     * 该函数与CreateToolhelp32Snapshot函数配合使用,用于遍历快照信息
     * <p>
     * C++函數原型
     * BOOL WINAPI Process32Next(
     * HANDLE hSnapshot, //函數CreateToolhelp32Snapshot 返回的句柄
     * LPPROCESSENTRY32 lppe  //指向PROCESSENTRY32结构的指针
     * );
     * <p>
     * 函数具体详情,请参阅MSDN文档
     * http://msdn.microsoft.com/en-us/library/ms886773.aspx
     **/
    boolean Process32Next(int hSnapshot, PROCESSENTRY32 lppe);

    /**
     * 此函数检索与进程相关联的第一个模块的信息，该函数与CreateToolhelp32Snapshot配合使用
     * CreateToolhelp32Snapshot函数的第一个参数必须包含TH32CS_SNAPMODULE参数，否将获取失败
     * <p>
     * C++函数原型
     * BOOL WINAPI Module32First(
     * HANDLE hSnapshot,  //CreateToolhelp32Snapshot函数返回的快照句柄
     * LPMODULEENTRY32 lpme //MODULEENTRY32结构的指针。用来返回数据,详情请查看该结构体的描述
     * );
     * <p>
     * 函数具体详情,请参阅MSDN文档
     * https://msdn.microsoft.com/en-us/library/windows/desktop/ms684218(v=vs.85).aspx
     **/
    boolean Module32First(int hSnapshot, MODULEENTRY32 lpme);

    /**
     * 此函数检索与进程相关联的下一个模块的信息，该函数Module32First与CreateToolhelp32Snapshot配合使用
     * <p>
     * C++函数原型
     * BOOL WINAPI Module32Next(
     * HANDLE          hSnapshot, //CreateToolhelp32Snapshot函数返回的快照句柄
     * LPMODULEENTRY32 lpme //MODULEENTRY32结构的指针。用来返回数据,详情请查看该结构体的描述
     * );
     * <p>
     * 函数具体详情,请参阅MSDN文档
     * https://msdn.microsoft.com/en-us/library/windows/desktop/ms684221(v=vs.85).aspx
     **/
    boolean Module32Next(int hSnapshot, MODULEENTRY32 lpme);

    /**
     * 句柄释放函数,用于释放句柄资源
     * <p>
     * C++函數原型
     * BOOL CloseHandle(
     * HANDLE hObject //代表一个已打开对象handle
     * );
     * <p>
     * 函数具体详情,请参阅MSDN文档
     * http://msdn.microsoft.com/en-us/library/ms724211%28VS.85%29.aspx
     **/
    boolean CloseHandle(int arg0);


    /**
     * 进程打开函数,打开成功返回句柄
     * <p>
     * C++函数原型
     * HANDLE OpenProcess(
     * DWORD dwDesiredAccess, //渴望得到的访问权限（标志）
     * BOOL bInheritHandle, // 是否继承句柄
     * DWORD dwProcessId// 进程标示符
     * );
     * <p>
     * 函数具体详情,请参阅MSDN文档
     * http://msdn.microsoft.com/en-us/library/ms684320%28VS.85%29.aspx
     **/
    int OpenProcess(int dwDesiredAccess, boolean bInheritHandle, int dwProcessId);

    /**
     * 内存信息查询函数,能查询一块内存区域的相关信息,比如是否可读,是否存在等
     * <p>
     * C++函数原型
     * DWORD VirtualQueryEx(
     * HANDLE hProcess, //进程句柄
     * LPCVOID lpAddress, //查询的内存地址
     * PMEMORY_BASIC_INFORMATION lpBuffer, //用于接收查询后的内存信息
     * DWORD dwLength //PMEMORY_BASIC_INFORMATION结构体的大小
     * );
     * <p>
     * 函数具体详情,请参阅MSDN文档
     * http://msdn.microsoft.com/en-us/library/windows/desktop/aa366907%28v=vs.85%29.aspx
     **/
    int VirtualQueryEx(int hProcess, int lpAddress, MEMORY_BASIC_INFORMATION lpBuffer, int dwLength);


    /**
     * 内存读取函数,用于读取内存中的值
     * <p>
     * C++函數原型
     * BOOL ReadProcessMemory(
     * HANDLE hProcess,  //进程句柄
     * LPCVOID lpBaseAddress,//读取的内存地址
     * LPVOID lpBuffer,//内存地址. 存放读取结果
     * DWORD nSize,//读取字节数
     * LPDWORD lpNumberOfBytesRead//实际读取字节数
     * );
     * <p>
     * 函数具体详情,请参阅MSDN文档
     * http://msdn.microsoft.com/en-us/library/ms680553%28VS.85%29.aspx
     **/
    boolean ReadProcessMemory(int hProcess, int lpBaseAddress, Pointer lpBuffer, int nSize, int lpNumberOfBytesRead);

    /**
     * 内存修改函数,用于修改内存中的值
     * <p>
     * C++函数原型
     * BOOL WriteProcessMemory(
     * HANDLE hProcess,//进程句柄
     * LPVOID lpBaseAddress,//写入的内存地址
     * LPVOID lpBuffer,//写入的内存新地址,这个地址是新的值
     * DWORD nSize,//写入字节数
     * LPDWORD lpNumberOfBytesWritten//实际字节数
     * );
     * <p>
     * 函数具体详情,请参阅MSDN文档
     * http://msdn.microsoft.com/en-us/library/ms681674.aspx
     **/
    boolean WriteProcessMemory(int hProcess, int lpBaseAddress, Pointer lpBuffer, int nSize, int lpNumberOfBytesRead);

    /**
     * 进程杀死函数,用于杀死进程
     * <p>
     * C++函数原型
     * BOOL TerminateProcess(
     * HANDLE hProcess,//进程句柄
     * UINT uExitCode //进程终止码
     * );
     * <p>
     * 函数具体详情,请参阅MSDN文档
     * http://msdn.microsoft.com/en-us/library/ms686714%28VS.85%29.aspx
     **/
    boolean TerminateProcess(int hProcess, int uExitCode);

    /**
     * 获取进程的退出代码,与TerminateProcess函数配合使用
     * <p>
     * C++函数原型
     * BOOL WINAPI GetExitCodeProcess(
     * __in HANDLE hProcess, //想获取退出代码的一个进程的句柄
     * __out LPDWORD lpExitCode //退出程序的的整形变量值,获取后,保存在该处
     * );
     * <p>
     * 函数具体详情,请参阅MSDN文档
     * http://msdn.microsoft.com/en-us/library/ms683189.aspx
     **/
    boolean GetExitCodeProcess(int hProcess, IntByReference lpExitCode);


    /**
     * 改变在特定进程中内存区域的保护属性
     * <p>
     * C++函数原型
     * BOOL VirtualProtectEx(
     * HANDLE hProcess, // 要修改内存的进程句柄
     * LPVOID lpAddress, // 要修改内存的起始地址
     * DWORD dwSize, // 页区域大小
     * DWORD flNewProtect, // 新访问方式
     * PDWORD lpflOldProtect // 原保护属性
     * );
     * <p>
     * 函数具体详情,请参阅MSDN文档
     * http://msdn.microsoft.com/zh-cn/library/aa366899%28v=vs.85%29.aspx
     **/
    boolean VirtualProtectEx(int hProcess, int lpAddress, int dwSize, int flNewProtect, int lpflOldProtect);

    /**
     * 获取当前系统的信息
     * <p>
     * C++函数原型
     * void WINAPI GetSystemInfo
     * (
     * _Out_ LPSYSTEM_INFO lpSystemInfo //返回描述系统信息的结构体
     * );
     * <p>
     * 函数具体详情,请参阅MSDN文档
     * http://msdn.microsoft.com/en-us/library/windows/desktop/ms724381%28v=vs.85%29.aspx
     **/
    void GetSystemInfo(SYSTEM_INFO arg0);


    /**
     * 获取函数调用失败的错误信息
     * <p>
     * C++函数原型
     * DWORD GetLastError(VOID); //调用成功返回一个非零值
     * <p>
     * 函数具体详情,请参阅MSDN文档
     * http://msdn.microsoft.com/en-us/library/ms679360%28VS.85%29.aspx
     **/
    int GetLastError();


    /**
     * 获取当前进程句柄
     * <p>
     * C++函数原型
     * HANDLE WINAPI GetCurrentProcess(void); //调用成功,返回句柄
     * <p>
     * 函数具体详情,请参阅MSDN文档
     * http://msdn.microsoft.com/en-us/library/windows/desktop/ms683179%28v=vs.85%29.aspx
     **/
    int GetCurrentProcess();


    /**
     * 判断当前进程是否64位
     * <p>
     * C++函数原型
     * BOOL WINAPI IsWow64Process(
     * HANDLE hProcess,//检查的进程句柄
     * PBOOL  Wow64Process //检查结果，TRUE是64位，反之32位
     * );
     * <p>
     * 函数具体详情,请参阅MSDN文档
     * http://msdn.microsoft.com/en-us/library/ms684139
     **/
    boolean IsWow64Process(int hProcess, IntByReference Wow64Process);

}
