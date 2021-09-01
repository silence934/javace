package com.memory.event;

import com.memory.entity.ExecuteResult;
import com.memory.entity.MemoryRange;
import com.memory.entity.Process;
import com.memory.impl.LoadSystemProcessInfo;
import com.memory.impl.MemoryRangeQuery;
import com.memory.wnd.ProcessChooseWnd;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

/**
 * 进程选择窗口事件处理
 * 作者:Code菜鸟
 * 技术交流QQ:969422014
 * CSDN博客:http://blog.csdn.net/qq969422014
 */
public class ProcessChooseWndEvent {
    private ProcessChooseWnd processChooseWnd;

    public ProcessChooseWndEvent(ProcessChooseWnd processChooseWnd) {
        this.processChooseWnd = processChooseWnd;
    }

    /**
     * 窗口每次setVisible(true)时,加载系统进程到list控件中
     */
    public WindowAdapter windowActivated() {
        return new WindowAdapter() {
            @Override
            @SuppressWarnings("unchecked")
            public void windowActivated(WindowEvent e) {
                //获取系统进程列表
                ExecuteResult result = new LoadSystemProcessInfo().getProcess();
                if (result.getLastError() != 0) {
                    JOptionPane.showMessageDialog(processChooseWnd, result.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
                    System.exit(0);//退出程序
                    return;
                }
                List<Process> list = (List<Process>) result.getValue();
                processChooseWnd.model.clear();
                //显示到列表
                for (Process p : list) {
                    processChooseWnd.model.addElement(p);
                }
            }
        };
    }

    /**
     * 进程列表鼠标双击选中事件
     **/
    public MouseAdapter processListMouseDClient() {
        return new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    //获取点击值
                    Object object = processChooseWnd.list.getSelectedValue();
                    if (object == null) {
                        return;
                    }
                    processChooseWnd.setVisible(false);
                    Process p = (Process) object;
                    //判断内存范围下拉框是否显示正确
                    if (processChooseWnd.mainWnd.memoryRangecomBoBox.getSelectedIndex() != 0) {
                        processChooseWnd.mainWnd.memoryRangecomBoBox.setSelectedIndex(0);
                    }
                    //获取程序在内存中的范围
                    MemoryRangeQuery query = new MemoryRangeQuery();
                    ExecuteResult executeResult = query.queryProcessRange(p.getPid());
                    if (executeResult.getLastError() != 0) {
                        JOptionPane.showMessageDialog(processChooseWnd.mainWnd, executeResult.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
                        processChooseWnd.mainWnd.memoryStartAddress.setText("0x0");
                        processChooseWnd.mainWnd.memoryEndAddress.setText("0x0");
                        return;
                    }
                    MemoryRange range = (MemoryRange) executeResult.getValue();
                    processChooseWnd.mainWnd.memoryStartAddress.setText(range.getMinValue().toString());
                    processChooseWnd.mainWnd.memoryEndAddress.setText(range.getMaxValue().toString());
                    processChooseWnd.mainWnd.range=range;
                    //界面显示处理
                    processChooseWnd.mainWnd.memoryRangecomBoBox.setEnabled(true);
                    processChooseWnd.mainWnd.statusLabel.setText(p.getProcessName());
                    processChooseWnd.mainWnd.currentProcess = p;
                    processChooseWnd.mainWnd.memoryAddressText.setEditable(true);
                    processChooseWnd.mainWnd.killButton.setEnabled(true);
                    processChooseWnd.mainWnd.writeMemoryButton.setEnabled(true);
                }
            }
        };
    }
}
