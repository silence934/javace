package com.memory.impl;

import com.memory.entity.ExecuteResult;
import com.memory.interfaces.Kernel32_DLL;
import com.memory.quantity.OpenProcess;
import com.memory.structure.MEMORY_BASIC_INFORMATION;
import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.BaseTSD;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.IntByReference;

/**
 * 内存的写实现类
 * 作者:Code菜鸟
 * 技术交流QQ:969422014
 * CSDN博客:http://blog.csdn.net/qq969422014
 */
public class MemoryWrite {
    /**
     * 写入内存实现方法
     * pid 进程ID
     * lpBaseAddress 写入地址
     * value 写入值
     * dataType 数据类型,这个值确定value的实际数据类型
     **/
    public ExecuteResult write(int pid, Pointer lpBaseAddress, String value, int dataType) {
        ExecuteResult result = new ExecuteResult();

        WinNT.HANDLE hProcess = Kernel32.INSTANCE.OpenProcess(WinNT.PROCESS_ALL_ACCESS , false, pid);


        try {
            //判断内存地址是否合法幷且是否满足读写权限
            WinNT.MEMORY_BASIC_INFORMATION lpBuffer=new WinNT.MEMORY_BASIC_INFORMATION();
            Kernel32.INSTANCE.VirtualQueryEx(hProcess, lpBaseAddress, lpBuffer, new BaseTSD.SIZE_T(lpBuffer.size()));

            if (lpBuffer.state.intValue() != WinNT.MEM_COMMIT){
                result.setLastError(-1);
                result.setMessage("内存地址不存!");
                return result;
            }
            if (lpBuffer.protect.intValue() != WinNT.PAGE_READWRITE) {
                result.setLastError(-1);
                result.setMessage("该内存无法读写!");
                return result;
            }


            //新内存地址,用于写入内存用
            Pointer updatePointer = null;
            int size = 4;
            switch (dataType) {
                //整形int
                case 0:
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
                default:
            }
            //写入内存
            boolean writeResult =  Kernel32.INSTANCE.WriteProcessMemory(hProcess, lpBaseAddress, updatePointer, size, new IntByReference(0));
            //是否写入成功
            int lastError = Kernel32.INSTANCE.GetLastError();
            if ((!writeResult) || lastError != 0) {
                result.setLastError(lastError);
                result.setMessage("内存写入发生错误,错误代码:" + lastError);
                return result;
            }
            result.setLastError(0);
            result.setMessage("写入成功!");
            return result;
        } catch (Exception e) {
            result.setLastError(-1);
            result.setMessage("写入失败,请检查输入值是否正确或超出范围!\n错误代码:" + e.getMessage());
        } finally {
            Kernel32.INSTANCE.CloseHandle(hProcess);
        }
        return result;
    }
}
