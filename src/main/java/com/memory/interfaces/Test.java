package com.memory.interfaces;


import oshi.SystemInfo;
import oshi.software.os.OSProcess;
import oshi.software.os.OperatingSystem;

/**
 * @author: fucong
 * @Date: 2021/8/31 11:04
 * @Description:
 */
public class Test {

    public static void main(String[] args) {
        SystemInfo systemInfo=new SystemInfo();
        OperatingSystem operatingSystem = systemInfo.getOperatingSystem();

        operatingSystem.getManufacturer();

        for (OSProcess process : operatingSystem.getProcesses()) {
            System.out.println(process.getName()+"--"+process.getProcessID());
        }
    }
}
