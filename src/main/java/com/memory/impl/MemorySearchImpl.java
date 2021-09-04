package com.memory.impl;

import com.memory.constant.DataType;
import com.memory.constant.WayOfComparison;
import com.memory.entity.MemoryRange;
import com.memory.entity.MemoryValue;
import com.memory.entity.SearchCondition;
import com.memory.interfaces.Kernel32_DLL;
import com.memory.wnd.MainWnd;
import com.memory.wnd.MemoryValueTable;
import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.BaseTSD;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.IntByReference;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;


@Slf4j
public class MemorySearchImpl {


    private static boolean isStop = false;

    public MemorySearchImpl() {

    }

    public static void setStop(boolean stop) {
        isStop = stop;
    }

    public void firstSearch(int pid, SearchCondition searchCondition, MemoryRange ranger, MainWnd mainWnd) {
        isStop = false;
        MemoryValueTable memoryValueTable = mainWnd.tableModel;
        memoryValueTable.setRowCount(0);

        //根据进程ID,打开进程,返回进程句柄
        WinNT.HANDLE hProcess = Kernel32.INSTANCE.OpenProcess(WinNT.PROCESS_ALL_ACCESS, false, pid);
        //判断进程句柄是否打开成功
        int lastError = Kernel32_DLL.INSTANCE.GetLastError();
        if (lastError == 5) {
            JOptionPane.showMessageDialog(mainWnd, "无法打开进程,系统Debug权限获取失败,请以管理员方式重新运行程序!", "ERROR", JOptionPane.ERROR_MESSAGE);
        } else if (lastError != 0) {
            JOptionPane.showMessageDialog(mainWnd, "无法打开该进程,OpenProcess函数返回错误码:" + lastError, "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        //对比的值
        double exceptionValue = searchCondition.getValue();

        WinNT.MEMORY_BASIC_INFORMATION information = new WinNT.MEMORY_BASIC_INFORMATION();
        try {
            Pointer startBaseAddress = ranger.getMinValue();
            //根据基址遍历内存
            int memoryScore = 0;
            while (Pointer.nativeValue(startBaseAddress) <= Pointer.nativeValue(ranger.getMaxValue())) {
                if (isStop) {
                    log.debug("收到停止消息");
                    break;
                }
                BaseTSD.SIZE_T size_t = Kernel32.INSTANCE.VirtualQueryEx(hProcess,
                        startBaseAddress, information, new BaseTSD.SIZE_T(information.size()));

                if (size_t.intValue() == 0) {
                    break;
                }
                //判断内存是否已提交,非空闲内存
                if (information.state.intValue() == WinNT.MEM_COMMIT) {
                    //更改内存保护属性为可写可读,成功返回TRUE,执行这个函数,OpenProcess函数必须为PROCESS_ALL_ACCESS
                    boolean vpe = Kernel32_DLL.INSTANCE.VirtualProtectEx(Pointer.nativeValue(hProcess.getPointer()), Pointer.nativeValue(startBaseAddress), information.regionSize.intValue(), WinNT.PAGE_READWRITE, information.protect.intValue());
                    //判断内存是否可读可写
                    if (vpe || information.protect.intValue() == WinNT.PAGE_READWRITE) {
                        //声明一块内存空间,保存读取内存块的值,这个空间的大小与内存块大小相同
                        Memory buffer = new Memory(information.regionSize.longValue());
                        //判断是否读取成功
                        if (Kernel32.INSTANCE.ReadProcessMemory(hProcess, startBaseAddress, buffer,
                                information.regionSize.intValue(), new IntByReference(0))) {
                            memoryScore += traverseMemory(startBaseAddress, buffer, memoryValueTable, searchCondition.getDataType(), searchCondition.getWay(), exceptionValue);
                        }
                        //释放内存
                        ReferenceFree.free(buffer);
                    }
                }
                //设置基地址偏移
                startBaseAddress = new Pointer(Pointer.nativeValue(information.baseAddress) + information.regionSize.longValue());
            }
            mainWnd.searchResultLabel.setText("搜索结果:共计检索内存" + memoryScore + "条,符合条件" + memoryValueTable.getRowCount() + "条记录!");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(mainWnd, "内存地址扫描错误!\n" + e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        } finally {
            //释放资源
            Kernel32.INSTANCE.CloseHandle(hProcess);
        }
    }


    public void nextSearch(int pid, SearchCondition searchCondition, MainWnd mainWnd) {
        isStop = false;
        MemoryValueTable memoryValueTable = mainWnd.tableModel;
        List<MemoryValue> addressList = new ArrayList<>(memoryValueTable.getValues());
        memoryValueTable.setRowCount(0);

        DataType dataType = searchCondition.getDataType();
        WayOfComparison way = searchCondition.getWay();
        double exceptionValue = searchCondition.getValue();

        WinNT.HANDLE hProcess = Kernel32.INSTANCE.OpenProcess(WinNT.PROCESS_ALL_ACCESS, false, pid);
        try {
            Memory buffer = new Memory(dataType.getDataSize());
            for (MemoryValue memoryValue : addressList) {
                if (isStop) {
                    log.debug("收到停止消息");
                    break;
                }
                if (Kernel32.INSTANCE.ReadProcessMemory(hProcess, memoryValue.getAddress(), buffer, (int) buffer.size(), new IntByReference(0))) {
                    double value = getMemoryValue(0, buffer, dataType);
                    //与搜索值相比较释放符合条件 0等于,1大于,-1小于
                    if ((way.getValue() == Double.compare(exceptionValue, value))) {
                        MemoryValue temp = new MemoryValue();
                        temp.setAddress(memoryValue.getAddress());
                        temp.setValue("" + value);
                        memoryValueTable.addRow(temp);
                    }
                }
            }
            ReferenceFree.free(buffer);
            int lastError = Kernel32.INSTANCE.GetLastError();
            if (lastError != 0) {
                JOptionPane.showMessageDialog(mainWnd, "搜索内存发生错误!错误代码:" + lastError, "ERROR", JOptionPane.ERROR_MESSAGE);
            }
            mainWnd.searchResultLabel.setText("搜索结果:共计检索内存" + addressList.size() + "条,符合条件" + memoryValueTable.getRowCount() + "条记录!");
        } catch (Exception e) {
            e.printStackTrace();
            e.printStackTrace();
            JOptionPane.showMessageDialog(mainWnd, "内存地址扫描错误!\n" + e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        } finally {
            //资源释放
            Kernel32.INSTANCE.CloseHandle(hProcess);
        }
    }


    private int traverseMemory(Pointer startBaseAddress, Memory buffer, MemoryValueTable memoryValueTable, DataType dataType, WayOfComparison way, double exceptionValue) {
        int memoryScore = 0;
        for (int i = 0; i < buffer.size(); i += dataType.getDataSize()) {
            if (isStop) {
                log.debug("收到停止消息");
                break;
            }
            memoryScore++;
            double memoryValue = getMemoryValue(i, buffer, dataType);
            if ((way.getValue() == Double.compare(exceptionValue, memoryValue))) {
                MemoryValue temp = new MemoryValue();
                temp.setAddress(startBaseAddress.share(i));
                temp.setValue("" + memoryValue);
                memoryValueTable.addRow(temp);
            }
        }
        return memoryScore;
    }


    private double getMemoryValue(int offset, Memory buffer, DataType dataType) {
        switch (dataType) {
            case INT:
                return buffer.getInt(offset);
            case BYTE:
                return buffer.getByte(offset);
            case LONG:
                return buffer.getLong(offset);
            case FLOAT:
                return buffer.getFloat(offset);
            case SHORT:
                return buffer.getShort(offset);
            case DOUBLE:
                return buffer.getDouble(offset);
            default:
                return 0;
        }
    }
}
