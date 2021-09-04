package com.memory.impl;

import com.memory.constant.DataType;
import com.memory.constant.WayOfComparison;
import com.memory.entity.SearchCondition;
import com.memory.wnd.MainWnd;
import lombok.Data;

import javax.swing.*;

/**
 * @author: silence
 * @Date: 2021/9/4 13:56
 * @Description:
 */
@Data
public class MemorySearchRunnable {

    private static SearchCondition before(MainWnd mainWnd) {
        mainWnd.openProcessButton.setEnabled(false);
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
        DataType searchDataType = (DataType) mainWnd.searchDataType.getSelectedItem();
        WayOfComparison way = (WayOfComparison) mainWnd.comparisonWay.getSelectedItem();
        String expectedValue = mainWnd.searchText.getText().trim();

        return SearchCondition.builder()
                .way(way)
                .dataType(searchDataType)
                .value(Double.parseDouble(expectedValue))
                .build();
    }

    private static void after(MainWnd mainWnd) {
        mainWnd.stopButton.setVisible(false);
        mainWnd.firstSearchButton.setEnabled(true);
        mainWnd.lastSearchButton.setEnabled(true);
        mainWnd.writeMemoryButton.setEnabled(true);
        mainWnd.killButton.setEnabled(true);
        mainWnd.resetButton.setEnabled(true);
        mainWnd.progressBar.setIndeterminate(false);
        mainWnd.memoryRangeBoBox.setEnabled(true);
        mainWnd.openProcessButton.setEnabled(true);
    }


    public static class FirstSearchRunnable implements Runnable {

        private final MainWnd mainWnd;

        private final MemorySearchImpl memorySearch;

        public FirstSearchRunnable(MainWnd mainWnd) {
            this.mainWnd = mainWnd;
            this.memorySearch = new MemorySearchImpl();
        }

        @Override
        public void run() {
            try {
                SearchCondition searchCondition = before(mainWnd);
                mainWnd.statusLabel.setText("正在扫描进程" + mainWnd.currentProcess.getName() + "请稍后...");
                memorySearch.firstSearch(mainWnd.currentProcess.getProcessID(), searchCondition, mainWnd.range, mainWnd);
                //结束TABLE更新线程
                mainWnd.statusLabel.setText("进程" + mainWnd.currentProcess.getName() + "扫描完成!");
                after(mainWnd);
            } catch (Exception e) {
                e.printStackTrace();
                mainWnd.resetWindow();
                JOptionPane.showMessageDialog(mainWnd, "搜索线程异常信息:" + e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        }

    }

    public static class NextSearchRunnable implements Runnable {

        private final MainWnd mainWnd;

        private final MemorySearchImpl memorySearch;

        public NextSearchRunnable(MainWnd mainWnd) {
            this.mainWnd = mainWnd;
            this.memorySearch = new MemorySearchImpl();
        }

        @Override
        public void run() {
            try {
                SearchCondition searchCondition = before(mainWnd);
                mainWnd.statusLabel.setText("正在扫描进程" + mainWnd.currentProcess.getName() + "变化的内存值,请稍后...");
                mainWnd.lastSearchButton.setEnabled(false);
                memorySearch.nextSearch(mainWnd.currentProcess.getProcessID(), searchCondition, mainWnd);
                mainWnd.statusLabel.setText("进程" + mainWnd.currentProcess.getName() + "扫描完成!");
                after(mainWnd);
            } catch (Exception e) {
                e.printStackTrace();
                mainWnd.resetWindow();
                JOptionPane.showMessageDialog(mainWnd, "搜索线程异常信息:" + e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        }

    }

}
