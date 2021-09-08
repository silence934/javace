package com.memory.event;

import com.memory.entity.ExecuteResult;
import com.memory.entity.MemoryRange;
import com.memory.impl.*;
import com.memory.wnd.MainWnd;
import com.sun.jna.Pointer;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.event.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * MainWnd窗口的事件响应类
 * 作者:Code菜鸟
 * 技术交流QQ:969422014
 * CSDN博客:http://blog.csdn.net/qq969422014
 *
 * @author fucong
 */
@Slf4j
public class MainWndEvent {

    public static final ExecutorService EXECUTOR_SERVICE = Executors.newSingleThreadExecutor();
    private final MainWnd mainWnd;
    private final KillProcess kill;
    private final MemoryWrite memoryWriter;

    public MainWndEvent(MainWnd mainWnd) {
        this.mainWnd = mainWnd;
        this.kill = new KillProcess();
        this.memoryWriter = new MemoryWrite();
    }


    /**
     * 内存搜索按钮点击事件
     **/
    public ActionListener firstSearchButton() {
        return e -> {
            //如果用户没有选择进程,那么这个值就是NULL
            if (mainWnd.currentProcess == null) {
                JOptionPane.showMessageDialog(mainWnd, "请先打开进程!", "ERROR", JOptionPane.ERROR_MESSAGE);
                return;
            }
            checkTest(mainWnd.searchText.getText().trim());
            EXECUTOR_SERVICE.submit(new MemorySearchRunnable.FirstSearchRunnable(mainWnd));
        };
    }


    /**
     * 在次搜索按钮点击事件
     **/
    public ActionListener lastSearchButton() {
        return e -> {
            if (mainWnd.tableModel.getRowCount() == 0) {
                return;
            }
            checkTest(mainWnd.searchText.getText().trim());
            EXECUTOR_SERVICE.submit(new MemorySearchRunnable.NextSearchRunnable(mainWnd));
        };
    }

    private void checkTest(String text) {
        //判断用户输入的值是否合法
        if (!text.matches("-?[0-9]+\\.*[0-9]*")) {
            JOptionPane.showMessageDialog(mainWnd, "搜索值输入错误!", "ERROR", JOptionPane.ERROR_MESSAGE);
            mainWnd.searchText.getCaret().setVisible(true);
            mainWnd.searchText.requestFocus();
            mainWnd.searchText.selectAll();
            return;
        }
        //判断内存地址范围是否合法,这个判断只针对自定义内存范围
        if (mainWnd.memoryRangeBoBox.getSelectedIndex() == 2) {
            String startAddress = mainWnd.memoryStartAddress.getText().trim();
            String endAddress = mainWnd.memoryEndAddress.getText().trim();
            //是否为空
            if (startAddress.length() < 1 || endAddress.length() < 1) {
                JOptionPane.showMessageDialog(mainWnd, "请输入有效的内存范围!", "ERROR", JOptionPane.ERROR_MESSAGE);
                return;
            }
            //是否符合格式
            if ((!startAddress.matches("0x{1}[0-9a-fA-F]+")) || (!endAddress.matches("0x{1}[0-9a-fA-F]+"))) {
                JOptionPane.showMessageDialog(mainWnd, "内存地址格式错误,请检查是否以0x开头的十六进制的值!\n注意:0x中的x应该为小写!", "ERROR", JOptionPane.ERROR_MESSAGE);
                return;
            }
            //长度是否符合
            if (startAddress.replace("0x", "").length() > 9 || endAddress.replace("0x", "").length() > 9) {
                JOptionPane.showMessageDialog(mainWnd, "搜索的内存地址超出范围！", "ERROR", JOptionPane.ERROR_MESSAGE);
                return;
            }
            //范围是否符合
            int startAddressInt = Integer.parseInt(startAddress.replace("0x", ""), 16);
            int endAddressInt = Integer.parseInt(endAddress.replace("0x", ""), 16);
            if (startAddressInt >= endAddressInt) {
                JOptionPane.showMessageDialog(mainWnd, "开始的内存地址不能大于或等于结束的内存地址！", "ERROR", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
    }

    /**
     * Table列表的鼠标左键点击事件
     **/
    public MouseAdapter tableMouseClick() {
        return new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //获取鼠标点击Table列表的值
                if (e.getButton() == MouseEvent.BUTTON1) {
                    mainWnd.memoryAddressText.setText((String) mainWnd.tableModel.getValueAt(mainWnd.table.getSelectedRow(), 0));
                    mainWnd.memoryUpdateValue.setText((String) mainWnd.tableModel.getValueAt(mainWnd.table.getSelectedRow(), 1));
                }
            }
        };
    }

    /**
     * 内存修改点击事件
     */
    public ActionListener updateMemoryValueButton() {
        return e -> {
            //获取输入框的值
            String lpBaseAddress = mainWnd.memoryAddressText.getText().trim();
            String value = mainWnd.memoryUpdateValue.getText().trim();
            //判断输入值是否合法
            if (!value.matches("-?[0-9]+\\.*[0-9]*")) {
                JOptionPane.showMessageDialog(mainWnd, "修改值只能是数字类型!", "ERROR", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                //执行写入内存的操作
                ExecuteResult result = memoryWriter.write(mainWnd.currentProcess.getProcessID(),
                        new Pointer(Long.parseLong(lpBaseAddress.replace("0x", ""), 16)),
                        value, mainWnd.memoryRangeBoBox.getSelectedIndex());
                if (result.getLastError() != 0) {
                    JOptionPane.showMessageDialog(mainWnd, result.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                //更新Tabel列表
                for (int i = 0; i < mainWnd.tableModel.getRowCount(); i++) {
                    if (lpBaseAddress.equals(mainWnd.tableModel.getValueAt(i, 0))) {
                        mainWnd.tableModel.setValueAt(value, i, 1);
                        break;
                    }
                }
                JOptionPane.showMessageDialog(mainWnd, "写入成功!", "温馨提示", JOptionPane.INFORMATION_MESSAGE);

            } catch (NumberFormatException e1) {
                JOptionPane.showMessageDialog(mainWnd, "请输入正确的内存地址!", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        };
    }

    /**
     * 杀死进程点击事件
     */
    public ActionListener killProcessButton() {
        return e -> {
            int answer = JOptionPane.showConfirmDialog(mainWnd, "确定杀死进程" + mainWnd.currentProcess.getName() + "?", "提示", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (answer != JOptionPane.YES_OPTION) {
                return;
            }

            ExecuteResult executeResult = kill.kill(mainWnd.currentProcess.getProcessID());
            if (executeResult.getLastError() != 0) {
                JOptionPane.showMessageDialog(mainWnd, executeResult.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
            } else {
                mainWnd.resetWindow();
            }
        };
    }

    /**
     * 停止扫描按钮事件
     **/
    public ActionListener stopButton() {
        return e -> {
            int answer = JOptionPane.showConfirmDialog(mainWnd, "确定停止当前内存扫描?不建议这么操作!", "提示", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (answer != JOptionPane.YES_OPTION) {
                return;
            }
            MemorySearchImpl.setStop(true);
            //界面操作
            mainWnd.statusLabel.setText("进程" + mainWnd.currentProcess.getName() + "扫描完成!");
            mainWnd.stopButton.setVisible(false);
            mainWnd.firstSearchButton.setEnabled(true);
            mainWnd.lastSearchButton.setEnabled(true);
            mainWnd.writeMemoryButton.setEnabled(true);
            mainWnd.killButton.setEnabled(true);
            mainWnd.progressBar.setIndeterminate(false);
            mainWnd.memoryRangeBoBox.setEnabled(true);
            mainWnd.resetButton.setEnabled(true);
        };
    }

    /**
     * 内存范围点击事件
     **/
    public ItemListener memoryRangeItem() {
        return e -> {
            if (e.getStateChange() != ItemEvent.SELECTED) {
                return;
            }
            //判断输入框是否已启动
            if (mainWnd.memoryStartAddress.isEditable()) {
                mainWnd.memoryStartAddress.setEditable(false);
                mainWnd.memoryEndAddress.setEditable(false);
            }
            //判断是否获取系统内存范围
            if (mainWnd.memoryRangeBoBox.getSelectedIndex() == 0) {
                MemoryRangeQuery query = new MemoryRangeQuery();
                ExecuteResult executeResult = query.queryProcessRange(mainWnd.currentProcess);
                if (executeResult.getLastError() != 0) {
                    JOptionPane.showMessageDialog(mainWnd, "读取进程的开始地址与结束地址失败,错误代码:" + executeResult.getLastError(), "ERROR", JOptionPane.ERROR_MESSAGE);
                    mainWnd.memoryStartAddress.setText("0x0");
                    mainWnd.memoryEndAddress.setText("0x0");
                    return;
                }
                MemoryRange range = (MemoryRange) executeResult.getValue();
                mainWnd.processChooseWnd.mainWnd.memoryStartAddress.setText(range.getMinValue().toString());
                mainWnd.processChooseWnd.mainWnd.memoryEndAddress.setText(range.getMaxValue().toString());
            } else if (mainWnd.memoryRangeBoBox.getSelectedIndex() == 1) {
                MemoryRangeQuery query = new MemoryRangeQuery();
                ExecuteResult executeResult = query.querySystemRange();
                if (executeResult.getLastError() != 0) {
                    JOptionPane.showMessageDialog(mainWnd, executeResult.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                MemoryRange range = (MemoryRange) executeResult.getValue();
                mainWnd.memoryStartAddress.setText(range.getMinValue().toString());
                mainWnd.memoryEndAddress.setText(range.getMaxValue().toString());
            } else if (mainWnd.memoryRangeBoBox.getSelectedIndex() == 2) {
                mainWnd.memoryStartAddress.setEditable(true);
                mainWnd.memoryEndAddress.setEditable(true);
                mainWnd.memoryStartAddress.setText("");
                mainWnd.memoryEndAddress.setText("");
            }
        };
    }
}
