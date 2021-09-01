package com.memory.impl;

import com.memory.entity.ExecuteResult;
import com.memory.entity.MemoryValue;
import com.memory.interfaces.Kernel32_DLL;
import com.memory.quantity.OpenProcess;
import com.memory.quantity.VirtualProtect;
import com.memory.structure.MEMORY_BASIC_INFORMATION;
import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.BaseTSD;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.IntByReference;

import java.util.*;


/**
 * 内存搜索实现类
 * 作者:Code菜鸟
 * 技术交流QQ:969422014
 * CSDN博客:http://blog.csdn.net/qq969422014
 */
public class MemorySearchImpl {
    //统计内存扫描数量
    public int memoryScore = 0;

    //保存搜索
    public List<MemoryValue> searchResult = Collections.synchronizedList(new ArrayList<MemoryValue>());




    /**
     * 值搜索
     * pid 进程ID
     * value 需要搜索的值
     * searchDataType 搜索的实际数据类型 0=INT 1=Short 2=long 3=float 4=double 5=byte
     * equalsSearchValue 与搜索值相比较 0等于,1大于,2小于
     * startBaseAddr 搜索开始的内存基址
     * endBaseAddr 搜索结束的内存基址
     * increasing 搜索地址的递增量
     **/
    public ExecuteResult search(int pid, String searchValue, int searchDataType, int equalsSearchValue, Pointer startBaseAddr, Pointer endBaseAddr) {
        if (searchResult.size() != 0) {
            searchResult.clear();
        }
        ExecuteResult executeResult = new ExecuteResult();
        memoryScore = 0;
        //根据进程ID,打开进程,返回进程句柄
        //int handle = Kernel32_DLL.INSTANCE.OpenProcess(OpenProcess.PROCESS_ALL_ACCESS, false, pid);

        WinNT.HANDLE hProcess = Kernel32.INSTANCE.OpenProcess(WinNT.PROCESS_ALL_ACCESS , false, pid);


        //判断进程句柄是否打开成功
        int lastError = Kernel32_DLL.INSTANCE.GetLastError();
        executeResult.setLastError(lastError);
        if (lastError == 5) {
            executeResult.setMessage("无法打开进程,系统Debug权限获取失败,请以管理员方式重新运行程序!");
            return executeResult;
        } else if (lastError != 0) {
            executeResult.setMessage("无法打开该进程,OpenProcess函数返回错误码:" + lastError);
            return executeResult;
        }

        //保存查询内存结果信息的结构体类
        //MEMORY_BASIC_INFORMATION memoryInfo = new MEMORY_BASIC_INFORMATION();

        WinNT.MEMORY_BASIC_INFORMATION information=new WinNT.MEMORY_BASIC_INFORMATION();
        try {
            //根据基址遍历内存
            int m=0,n=0;
            while (Pointer.nativeValue(startBaseAddr) <= Pointer.nativeValue(endBaseAddr)) {
                m++;
                //读取内存信息
                 // int vqe = Kernel32_DLL.INSTANCE.VirtualQueryEx(handle, startBaseAddr, memoryInfo, size);

                BaseTSD.SIZE_T size_t = Kernel32.INSTANCE.VirtualQueryEx(hProcess,
                        startBaseAddr, information, new BaseTSD.SIZE_T(information.size()));

                if (size_t.intValue() == 0) {
                    break;
                }
                //判断内存是否已提交,非空闲内存
                if (information.state.intValue() == WinNT.MEM_COMMIT) {
                    //更改内存保护属性为可写可读,成功返回TRUE,执行这个函数,OpenProcess函数必须为PROCESS_ALL_ACCESS
                    boolean vpe = true;//Kernel32_DLL.INSTANCE.VirtualProtectEx(Pointer.nativeValue(hProcess.getPointer()), Pointer.nativeValue(startBaseAddr), information.regionSize.intValue(), WinNT.PAGE_READWRITE, information.protect.intValue());

                    int i1 = Kernel32_DLL.INSTANCE.GetLastError();
                    System.out.println("error"+i1);

                    //判断内存是否可读可写
                    if (vpe || information.protect.intValue() == WinNT.PAGE_READWRITE) {
                        n++;
                        //声明一块内存空间,保存读取内存块的值,这个空间的大小与内存块大小相同
                        Pointer buffer = new Memory(information.regionSize.longValue());

                        //判断是否读取成功
                        if ( Kernel32.INSTANCE.ReadProcessMemory(hProcess, startBaseAddr, buffer,
                                information.regionSize.intValue(), new IntByReference(0))) {
                            //对比的值
                            double searchValueDouble = Double.parseDouble(searchValue);
                            //根据搜索类型查找对应数据
                            for (int i = 0; i < information.regionSize.intValue(); i += 4) {
                                double memoryValue = buffer.getInt(i);
                                //统计内存数量
                                memoryScore++;
                                //与搜索值相比较释放符合条件 0等于,1大于,2小于
                                if ((equalsSearchValue == 0 && memoryValue == searchValueDouble) ||
                                        (equalsSearchValue == 1 && memoryValue > searchValueDouble) ||
                                        (equalsSearchValue == 2 && memoryValue < searchValueDouble)) {
                                    MemoryValue temp = new MemoryValue();
                                    temp.setAddress(Pointer.nativeValue(startBaseAddr) + i);
                                    temp.setAddress16("0x" + Long.toString((Pointer.nativeValue(startBaseAddr) + i), 16).toUpperCase());
                                    temp.setValue(memoryValue + "");
                                    searchResult.add(temp);
                                }
                            }
                            break;
                        }


                        //释放内存
                        ReferenceFree.free(buffer);
                    }
                }
                //设置基地址偏移
                startBaseAddr = new Pointer( Pointer.nativeValue(information.baseAddress) + information.regionSize.longValue());
            }

            System.out.println(m);
            System.out.println(n);
        } catch (Exception e) {
            e.printStackTrace();
            executeResult.setLastError(-1);
            executeResult.setMessage("内存地址扫描错误!\n" + e.getMessage());
            return executeResult;
        } finally {
            //释放资源
            Kernel32.INSTANCE.CloseHandle(hProcess);
        }
        return executeResult;
    }

    /**
     * 再次搜索实现
     * pid 进程ID
     * addressList 搜索的内存地址列表
     * searchDataType 搜索的数据类型
     **/
    public ExecuteResult search(int pid, List<MemoryValue> addressList, int searchDataType) {
        ExecuteResult executeResult = new ExecuteResult();
        if (searchResult.size() != 0) {
            searchResult.clear();
        }
        memoryScore = 0;
        //获取进程句柄
        int handle = Kernel32_DLL.INSTANCE.OpenProcess(OpenProcess.PROCESS_ALL_ACCESS, false, pid);
        try {
            //保存读取的新值
            Map<String, MemoryValue> tableValueMap = new HashMap<String, MemoryValue>();
            //声明一块内存,保存读取值
            Pointer readResult = new Memory(1024);
            for (int i = 0; i < addressList.size(); i++) {
                memoryScore++;
                //将0xffff table中的值转换为int类型
                int temp = Integer.parseInt(addressList.get(i).getAddress16().replace("0x", ""), 16);
                if (Kernel32_DLL.INSTANCE.ReadProcessMemory(handle, temp, readResult, 1024, 0)) {
                    MemoryValue m = new MemoryValue();
                    m.setAddress(temp);
                    m.setAddress16("0x" + (Integer.toString(temp, 16).toUpperCase()));
                    //根据搜索类型读取对应数据
                    switch (searchDataType) {
                        //整形int
                        case 0:
                            m.setValue(readResult.getInt(0) + "");
                            break;
                        //短整形short
                        case 1:
                            m.setValue(readResult.getShort(0) + "");
                            break;
                        //长整形Long
                        case 2:
                            m.setValue(readResult.getLong(0) + "");
                            break;
                        //单精度浮点 float
                        case 3:
                            m.setValue(readResult.getFloat(0) + "");
                            break;
                        //双精度浮点 double
                        case 4:
                            m.setValue(readResult.getDouble(0) + "");
                            break;
                        //字节byte
                        case 5:
                            m.setValue(readResult.getByte(0) + "");
                            break;
                    }
                    tableValueMap.put(m.getAddress16(), m);
                }
            }
            //释放内存
            ReferenceFree.free(readResult);
            //移除列表中没有发生变化的内存值
            for (int i = 0; i < addressList.size(); i++) {
                String key = addressList.get(i).getAddress16();
                String value = addressList.get(i).getValue();
                if (tableValueMap.get(key) != null
                        && Double.parseDouble(tableValueMap.get(key).getValue()) == Double.parseDouble(value)) {
                    tableValueMap.remove(key);
                }
            }
            //搜索结果
            for (String key : tableValueMap.keySet()) {
                searchResult.add(tableValueMap.get(key));
            }
            executeResult.setLastError(Kernel32_DLL.INSTANCE.GetLastError());
            if (executeResult.getLastError() != 0) {
                executeResult.setMessage("搜索内存发生错误!错误代码:" + executeResult.getLastError());
            }
        } catch (Exception e) {
            e.printStackTrace();
            executeResult.setLastError(-1);
            executeResult.setMessage("内存地址扫描错误!\n" + e.getMessage());
        } finally {
            //资源释放
            Kernel32_DLL.INSTANCE.CloseHandle(handle);
        }
        return executeResult;
    }
}
