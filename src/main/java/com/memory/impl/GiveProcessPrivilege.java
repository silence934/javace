package com.memory.impl;

import com.memory.entity.ExecuteResult;
import com.memory.interfaces.Advapi32_DLL;
import com.memory.interfaces.Kernel32_DLL;
import com.memory.quantity.OpenProcessToken;
import com.memory.structure.HANDLEByReference;
import com.memory.structure.LUID;
import com.memory.structure.LUID_AND_ATTRIBUTES;
import com.memory.structure.TOKEN_PRIVILEGES;

/**
 * 给予进程特权实现类
 * 作者:Code菜鸟
 * 技术交流QQ:969422014
 * CSDN博客:http://blog.csdn.net/qq969422014
 **/
public class GiveProcessPrivilege {
    /**
     * processHandle 需要给予的进程句柄
     * privilegeValue 特权值,详情请参阅LookupPrivilegeValue接口
     **/
    public ExecuteResult give(int processHandle, String privilegeValue) {
        ExecuteResult executeResult = new ExecuteResult();
        //创建令牌句柄指針,用于保存OpenProcessToken函数返回的令牌
        HANDLEByReference tokenHandle = new HANDLEByReference();
        try {
            //打开进程令牌,用于查询和修改令牌
            if (Advapi32_DLL.INSTANCE.OpenProcessToken(processHandle, OpenProcessToken.TOKEN_ADJUST_PRIVILEGES | OpenProcessToken.TOKEN_QUERY, tokenHandle)) {
                //创建一个令牌特权,初始化为1,用于保存LookupPrivilegeValue函数返回的令牌特权
                TOKEN_PRIVILEGES tkp = new TOKEN_PRIVILEGES(1);
                //初始化令牌特LUID值
                tkp.Privileges[0] = new LUID_AND_ATTRIBUTES();
                tkp.Privileges[0].Luid = new LUID();
                tkp.Privileges[0].Attributes = OpenProcessToken.SE_PRIVILEGE_ENABLED;
                //查看系统权限的特权值,返回到tkp LUID
                if (Advapi32_DLL.INSTANCE.LookupPrivilegeValue(null, privilegeValue, tkp.Privileges[0].Luid)) {
                    //告诉系统启用该令牌
                    Advapi32_DLL.INSTANCE.AdjustTokenPrivileges(tokenHandle.getValue(), false, tkp, tkp.size(), null, null);
                }
            }
        } finally {
            //释放令牌指针
            ReferenceFree.free(tokenHandle);
            //获取执行结果
            executeResult.setLastError(Kernel32_DLL.INSTANCE.GetLastError());
            //释放句柄资源
            Kernel32_DLL.INSTANCE.CloseHandle(processHandle);
        }
        return executeResult;
    }
}
