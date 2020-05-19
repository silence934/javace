package com.memory.entity;

/**
 * 作者:Code菜鸟
 * 技术交流QQ:969422014
 * CSDN博客:http://blog.csdn.net/qq969422014
 * <p>
 * 用于保存进程名字与进程ID的类
 **/
public class Process implements Comparable<Process> {
    private String processName;
    private int pid;

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String toString() {
        return pid +"-------"+ processName;
    }

    public int compareTo(Process o) {
        return o.processName.compareTo(this.processName);
    }
}
