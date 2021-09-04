package com.memory.impl;

import com.memory.constant.DataType;
import com.memory.entity.ExecuteResult;
import com.memory.wnd.DefaultTableModel;
import com.memory.wnd.MainWnd;
import com.sun.jna.Pointer;

import javax.swing.*;

/**
 * 内存搜索线程
 * 作者:Code菜鸟
 * 技术交流QQ:969422014
 * CSDN博客:http://blog.csdn.net/qq969422014
 **/
public class MemorySearchThread implements Runnable {
    private final MainWnd mainWnd;

    private final MemorySearchImpl memorySearch;
    //列表更新Table线程
    public JTableValueUpdate tableValueUpdate;
    //0代表内存搜索,1代表搜索变动
    private int searchType;


    public MemorySearchThread(MainWnd mainWnd) {
        this.mainWnd = mainWnd;
        this.memorySearch = null;//MemorySearchImpl.getInstance();
        this.searchType = 0;
        this.tableValueUpdate = new JTableValueUpdate(mainWnd, this.memorySearch);
    }


    public void setSearchType(int searchType) {
        this.searchType = searchType;
    }

    @Override
    public void run() {
        try {
            //界面显示处理
            mainWnd.firstSearchButton.setEnabled(false);
            mainWnd.searchDataType.setEnabled(false);
            mainWnd.killButton.setEnabled(false);
            mainWnd.writeMemoryButton.setEnabled(false);
            mainWnd.resetButton.setEnabled(false);
            mainWnd.stopButton.setVisible(true);
            mainWnd.progressBar.setIndeterminate(true);
            mainWnd.memoryRangeBoBox.setEnabled(false);
            mainWnd.searchResultLabel.setText("搜索结果:");
            //进程ID
            int pid = mainWnd.currentProcess.getProcessID();
            //搜索的数据类型,0=INT 1=Short 2=long 3=float 4=double 5=byte
            DataType searchDataType = (DataType) mainWnd.searchDataType.getSelectedItem();
            //与搜索值比较 0等于,1大于,2小于-> -1小于
            int equalsSearchValue = mainWnd.comparisonWay.getSelectedIndex();
            equalsSearchValue = equalsSearchValue == 2 ? -1 : equalsSearchValue;

            //判断是内存扫描还是变化扫描
            if (searchType == 0) {
                // memorySearch.searchResult.clear();
                mainWnd.statusLabel.setText("正在扫描进程" + mainWnd.currentProcess.getName() + "请稍后...");
                mainWnd.tableModel.setRowCount(0);
                //搜索的开始地址
                Pointer startAddress = mainWnd.range.getMinValue();
                //搜索的结束地址
                Pointer endAddress = mainWnd.range.getMaxValue();
                //更新TABLE列表
                tableValueUpdate.start();
                //执行搜索
                // ExecuteResult executeResult = memorySearch.search(pid, mainWnd.searchText.getText().trim(), searchDataType, equalsSearchValue, startAddress, endAddress);
                //结束TABLE更新线程
                tableValueUpdate.stopRun();
                tableValueUpdate.join();
//                if (executeResult.getLastError() != 0) {
//                    JOptionPane.showMessageDialog(mainWnd, executeResult.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
//                    mainWnd.resetWindow();
//                    return;
//                }
                mainWnd.statusLabel.setText("进程" + mainWnd.currentProcess.getName() + "扫描完成!");
            } else if (searchType == 1) {
                mainWnd.statusLabel.setText("正在扫描进程" + mainWnd.currentProcess.getName() + "变化的内存值,请稍后...");
                mainWnd.lastSearchButton.setEnabled(false);
                //获取当前Table列表搜索的内存的值
                DefaultTableModel model = null;// mainWnd.tableModel;
                if (model.getRowCount() == 0) {
                    JOptionPane.showMessageDialog(mainWnd, "搜索列表为空!", "ERROR", JOptionPane.ERROR_MESSAGE);
                    mainWnd.resetWindow();
                    return;
                }
                //清空Table列表
                mainWnd.tableModel.setRowCount(0);
                //更新TABLE列表
                tableValueUpdate.start();
                //开始搜索
                ExecuteResult executeResult = null;// memorySearch.search(pid, mainWnd.searchText.getText().trim(), searchDataType, equalsSearchValue);
                //结束TABLE更新线程
                tableValueUpdate.stopRun();
                tableValueUpdate.join();
                if (executeResult.getLastError() != 0) {
                    JOptionPane.showMessageDialog(mainWnd, executeResult.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
                    mainWnd.resetWindow();
                    return;
                }
                mainWnd.statusLabel.setText("进程" + mainWnd.currentProcess.getName() + "扫描完成!");
            }
            //界面显示处理
            mainWnd.stopButton.setVisible(false);
            mainWnd.firstSearchButton.setEnabled(true);
            mainWnd.lastSearchButton.setEnabled(true);
            mainWnd.writeMemoryButton.setEnabled(true);
            mainWnd.killButton.setEnabled(true);
            mainWnd.resetButton.setEnabled(true);
            mainWnd.progressBar.setIndeterminate(false);
            mainWnd.memoryRangeBoBox.setEnabled(true);
        } catch (Exception e) {
            mainWnd.resetWindow();
            JOptionPane.showMessageDialog(mainWnd, "搜索线程异常信息:" + e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    //任务终止
    @SuppressWarnings("deprecation")
    public boolean stopRun() {
        try {
            // stop();
            // join();
            tableValueUpdate.stopRun();
            tableValueUpdate.join();
            return true;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }
}

/**
 * JTabele显示更新线程,该线程监听MemorySearchImpl类中searchResult的SIZE变化
 **/
class JTableValueUpdate extends Thread {
    private final MemorySearchImpl memorySearchImpl;
    private final MainWnd mainWnd;
    private boolean isRun = true;

    public JTableValueUpdate(MainWnd mainWnd, MemorySearchImpl memorySearchImpl) {
        this.memorySearchImpl = memorySearchImpl;
        this.mainWnd = mainWnd;
    }

    @Override
    @SuppressWarnings("BusyWait")
    public void run() {
        try {
//            int size = 0;
//            while (isRun) {
//                //如果memorySearchImpl中的searchResult的SIZE发生变化,就更新到Table列表
//                if (memorySearchImpl.searchResult.size() != size) {
//                    size = memorySearchImpl.searchResult.size();
//                    MemoryValue result = memorySearchImpl.searchResult.get(size - 1);
//                    //mainWnd.tableModel.addRow(new String[]{Long.toString(Pointer.nativeValue(result.getAddress()), 16), result.getValue()});
//                }
//                Thread.sleep(100);
//            }
//            //检查是否有遗漏添加的,如果有遗漏,重新填充Table列表
//            if (memorySearchImpl.searchResult.size() != mainWnd.tableModel.getRowCount()) {
//                //清空Table列表
//                mainWnd.tableModel.setRowCount(0);
//                for (MemoryValue temp : memorySearchImpl.searchResult) {
//                    //mainWnd.tableModel.addRow(new String[]{Long.toString(Pointer.nativeValue(temp.getAddress()), 16), temp.getValue()});
//                }
//            }
//            //显示搜索结果
//            mainWnd.searchResultLabel.setText("搜索结果:共计检索内存" + memorySearchImpl.memoryScore + "条,符合条件" + mainWnd.tableModel.getRowCount() + "条记录!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopRun() {
        isRun = false;
    }
}
