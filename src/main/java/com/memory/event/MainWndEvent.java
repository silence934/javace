package com.memory.event;

import com.memory.entity.ExecuteResult;
import com.memory.entity.MemoryRange;
import com.memory.impl.*;
import com.memory.interfaces.Kernel32_DLL;
import com.memory.quantity.LookupPrivilegeValue;
import com.memory.wnd.MainWnd;
import com.memory.wnd.ProcessChooseWnd;

import javax.swing.*;
import java.awt.event.*;

/**
 * MainWnd窗口的事件响应类
 * 作者:Code菜鸟
 * 技术交流QQ:969422014
 * CSDN博客:http://blog.csdn.net/qq969422014
 **/
public class MainWndEvent {
    private MainWnd mainWnd;
    private ProcessChooseWnd processChooseWnd = null;
    private MemorySearchThread memorySearchThread = null;
    private MemoryWrite memoryWriter = null;
    private KillProcess kill = null;

    public MainWndEvent(MainWnd mainWnd) {
        this.mainWnd = mainWnd;
    }

    /**
     * 打开进程按钮
     **/
    public ActionListener openProcessButton() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //判断当前是否正在执行扫描动作L
                if (memorySearchThread != null
                        && memorySearchThread.isAlive()) {
                    JOptionPane.showMessageDialog(mainWnd, "扫描进行中,请等待扫描完成!", "提示", JOptionPane.QUESTION_MESSAGE);
                    return;
                }
                //对象NULL值判断
                if (processChooseWnd == null) {
                    processChooseWnd = new ProcessChooseWnd(mainWnd);
                }
                processChooseWnd.setVisible(true);
            }
        };
    }

    /**
     * 内存搜索按钮点击事件
     **/
    public ActionListener firstSearchButton() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //如果用户没有选择进程,那么这个值就是NULL
                if (mainWnd.currentProcess == null) {
                    JOptionPane.showMessageDialog(mainWnd, "请先打开进程!", "ERROR", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                //获取用户输入的查询值
                String text = mainWnd.searchText.getText().trim();
                //判断用户输入的值是否合法
                if (!text.matches("\\-{0,1}[0-9]+\\.*[0-9]*")) {
                    JOptionPane.showMessageDialog(mainWnd, "搜索值输入错误!", "ERROR", JOptionPane.ERROR_MESSAGE);
                    mainWnd.searchText.getCaret().setVisible(true);
                    mainWnd.searchText.requestFocus();
                    mainWnd.searchText.selectAll();
                    return;
                }
                //判断内存地址范围是否合法,这个判断只针对自定义内存范围
                if (mainWnd.memoryRangecomBoBox.getSelectedIndex() == 2) {
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
                //执行查询线程,0代表内存搜索
                memorySearchThread = new MemorySearchThread(mainWnd, text, 0);
                memorySearchThread.start();
            }
        };
    }

    /**
     * 在次搜索按钮点击事件
     **/
    public ActionListener lastSearchButton() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //判断查询列表中是否有值,如果没值,则表明用户没有执行内存查询操作,则该按钮无效
                if (mainWnd.tableModel.getRowCount() == 0) {
                    return;
                }
                //执行查询线程,1代表内存变化搜索
                memorySearchThread = new MemorySearchThread(mainWnd, "0", 1);
                memorySearchThread.start();
            }
        };
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
        return new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (memoryWriter == null) {
                    memoryWriter = new MemoryWrite();
                }
                //获取输入框的值
                String lpBaseAddress = mainWnd.memoryAddressText.getText().trim();
                String value = mainWnd.memoryUpdateValue.getText().trim();
                //判断输入值是否合法
                if ((!lpBaseAddress.startsWith("0x"))) {
                    JOptionPane.showMessageDialog(mainWnd, "1.请先搜索內存地址,然後点击左边的列表选择内存地址\n\n2.手动输入正确的内存地址!\n\n注:内存地址均为16进制且以0x开头!", "提示", JOptionPane.INFORMATION_MESSAGE);
                    return;
                } else if (!value.matches("\\-{0,1}[0-9]+\\.*[0-9]*")) {
                    JOptionPane.showMessageDialog(mainWnd, "修改值只能是数字类型!", "ERROR", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                try {
                    //执行写入内存的操作
                    ExecuteResult result = memoryWriter.write(mainWnd.currentProcess.getPid(), Integer.parseInt(lpBaseAddress.replace("0x", ""), 16), value, mainWnd.memoryRangecomBoBox.getSelectedIndex());
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
            }
        };
    }

    /**
     * 杀死进程点击事件
     */
    public ActionListener killProcessButton() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int answer = JOptionPane.showConfirmDialog(mainWnd, "确定杀死进程" + mainWnd.currentProcess.getProcessName() + "?", "提示", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (answer != JOptionPane.YES_OPTION) {
                    return;
                }
                if (kill == null) {
                    kill = new KillProcess();
                }

                ExecuteResult executeResult = kill.kill(mainWnd.currentProcess.getPid());
                if (executeResult.getLastError() != 0) {
                    JOptionPane.showMessageDialog(mainWnd, executeResult.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
                } else {
                    mainWnd.resetWindow();
                }
            }
        };
    }

    /**
     * 重置按钮
     */
    public ActionListener resetButton() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainWnd.resetWindow();
            }
        };
    }

    /**
     * 停止扫描按钮事件
     **/
    public ActionListener stopButton() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int answer = JOptionPane.showConfirmDialog(mainWnd, "确定停止当前内存扫描?不建议这么操作!", "提示", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (answer != JOptionPane.YES_OPTION) {
                    return;
                }
                //判断当前是否正在执行扫描动作
                if (memorySearchThread != null
                        && memorySearchThread.isAlive()) {
                    if (!memorySearchThread.stopRun()) {
                        JOptionPane.showMessageDialog(mainWnd, "终止任务失败!", "ERROR", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
                //界面操作
                mainWnd.statusLabel.setText("进程" + mainWnd.currentProcess.getProcessName() + "扫描完成!");
                mainWnd.stopButton.setVisible(false);
                mainWnd.firstSearchButton.setEnabled(true);
                mainWnd.lastSearchButton.setEnabled(true);
                mainWnd.writeMemoryButton.setEnabled(true);
                mainWnd.killButton.setEnabled(true);
                mainWnd.progressBar.setIndeterminate(false);
                mainWnd.memoryRangecomBoBox.setEnabled(true);
                mainWnd.resetButton.setEnabled(true);
            }
        };
    }

    /**
     * 窗口加载事件,获取Debug特权
     **/
    public WindowAdapter openLoad() {
        return new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                GiveProcessPrivilege give = new GiveProcessPrivilege();
                give.give(Kernel32_DLL.INSTANCE.GetCurrentProcess(), LookupPrivilegeValue.SeDebugPrivilege);
            }
        };
    }

    /**
     * 内存范围点击事件
     **/
    public ItemListener memoryRangeItem() {
        return new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() != ItemEvent.SELECTED) {
                    return;
                }
                //判断输入框是否已启动
                if (mainWnd.memoryStartAddress.isEditable()) {
                    mainWnd.memoryStartAddress.setEditable(false);
                    mainWnd.memoryEndAddress.setEditable(false);
                }
                //判断是否获取系统内存范围
                if (mainWnd.memoryRangecomBoBox.getSelectedIndex() == 0) {
                    MemoryRangeQuery query = new MemoryRangeQuery();
                    ExecuteResult executeResult = query.queryProcessRange(mainWnd.currentProcess.getPid());
                    if (executeResult.getLastError() != 0) {
                        JOptionPane.showMessageDialog(mainWnd, "读取进程的开始地址与结束地址失败,错误代码:" + executeResult.getLastError(), "ERROR", JOptionPane.ERROR_MESSAGE);
                        mainWnd.memoryStartAddress.setText("0x0");
                        mainWnd.memoryEndAddress.setText("0x0");
                        return;
                    }
                    MemoryRange range = (MemoryRange) executeResult.getValue();
                    processChooseWnd.mainWnd.memoryStartAddress.setText("0x" + Long.toString(range.getMinValue(), 16).toUpperCase());
                    processChooseWnd.mainWnd.memoryEndAddress.setText("0x" + Long.toString(range.getMaxValue(), 16).toUpperCase());
                } else if (mainWnd.memoryRangecomBoBox.getSelectedIndex() == 1) {
                    MemoryRangeQuery query = new MemoryRangeQuery();
                    ExecuteResult executeResult = query.querySystemRange();
                    if (executeResult.getLastError() != 0) {
                        JOptionPane.showMessageDialog(mainWnd, executeResult.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    MemoryRange range = (MemoryRange) executeResult.getValue();
                    mainWnd.memoryStartAddress.setText("0x" + Long.toString(range.getMinValue(), 16).toUpperCase());
                    mainWnd.memoryEndAddress.setText("0x" + Long.toString(range.getMaxValue(), 16).toUpperCase());
                } else if (mainWnd.memoryRangecomBoBox.getSelectedIndex() == 2) {
                    mainWnd.memoryStartAddress.setEditable(true);
                    mainWnd.memoryEndAddress.setEditable(true);
                    mainWnd.memoryStartAddress.setText("");
                    mainWnd.memoryEndAddress.setText("");
                }
            }
        };
    }
}
