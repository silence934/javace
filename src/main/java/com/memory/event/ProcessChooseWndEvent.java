package com.memory.event;

import com.memory.entity.ExecuteResult;
import com.memory.entity.MemoryRange;
import com.memory.impl.MemoryRangeQuery;
import com.memory.wnd.ProcessChooseWnd;
import com.sun.jna.Pointer;
import oshi.software.os.OSProcess;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * 进程选择窗口事件处理
 * 作者:Code菜鸟
 * 技术交流QQ:969422014
 * CSDN博客:http://blog.csdn.net/qq969422014
 * @author fucong
 */
public class ProcessChooseWndEvent {
    private final ProcessChooseWnd processChooseWnd;

    public ProcessChooseWndEvent(ProcessChooseWnd processChooseWnd) {
        this.processChooseWnd = processChooseWnd;
    }


    /**
     * 进程列表鼠标双击选中事件
     **/
    public MouseAdapter processListMouseClient() {
        return new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    //获取点击值
                    OSProcess osProcess = processChooseWnd.list.getSelectedValue();
                    if (osProcess == null) {
                        return;
                    }
                    processChooseWnd.setVisible(false);
                    //判断内存范围下拉框是否显示正确
                    if (processChooseWnd.mainWnd.memoryRangecomBoBox.getSelectedIndex() != 0) {
                        processChooseWnd.mainWnd.memoryRangecomBoBox.setSelectedIndex(0);
                    }
                    //获取程序在内存中的范围
                    MemoryRangeQuery query = new MemoryRangeQuery();
                    ExecuteResult executeResult = query.queryProcessRange(osProcess);
                    if (executeResult.getLastError() != 0) {
                        JOptionPane.showMessageDialog(processChooseWnd.mainWnd, executeResult.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
                        processChooseWnd.mainWnd.memoryStartAddress.setText("0x0");
                        processChooseWnd.mainWnd.memoryEndAddress.setText("0x0");
                        return;
                    }
                    MemoryRange range = (MemoryRange) executeResult.getValue();
                    processChooseWnd.mainWnd.memoryStartAddress.setText(""+Pointer.nativeValue(range.getMinValue()));
                    processChooseWnd.mainWnd.memoryEndAddress.setText(""+Pointer.nativeValue(range.getMaxValue()));
                    processChooseWnd.mainWnd.range=range;
                    //界面显示处理
                    processChooseWnd.mainWnd.memoryRangecomBoBox.setEnabled(true);
                    processChooseWnd.mainWnd.statusLabel.setText(osProcess.getName());
                    processChooseWnd.mainWnd.currentProcess = osProcess;
                    processChooseWnd.mainWnd.memoryAddressText.setEditable(true);
                    processChooseWnd.mainWnd.killButton.setEnabled(true);
                    processChooseWnd.mainWnd.writeMemoryButton.setEnabled(true);
                }
            }
        };
    }
}
