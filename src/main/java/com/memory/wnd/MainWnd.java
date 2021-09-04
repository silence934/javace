package com.memory.wnd;

import com.jtattoo.plaf.aluminium.AluminiumLookAndFeel;
import com.memory.constant.DataType;
import com.memory.constant.WayOfComparison;
import com.memory.entity.MemoryRange;
import com.memory.event.MainWndEvent;
import oshi.software.os.OSProcess;

import javax.swing.*;
import java.awt.*;

/**
 * 作者:Code菜鸟
 * 技术交流QQ:969422014
 * CSDN博客:http://blog.csdn.net/qq969422014
 * <p>
 * 主窗体显示界面
 **/
public class MainWnd extends JFrame {
    private static final long serialVersionUID = 1L;
    public JTable table;
    public JTextField searchText;
    public JTextField memoryAddressText;
    public JTextField memoryUpdateValue;
    public JLabel statusLabel;
    public OSProcess currentProcess = null;
    public JButton firstSearchButton;
    public JButton lastSearchButton;
    public JProgressBar progressBar;
    public JComboBox<WayOfComparison> comparisonWay;
    public MemoryValueTable tableModel;
    public JButton writeMemoryButton;
    public JButton killButton;
    public JComboBox<DataType> searchDataType;
    public JButton resetButton;
    public JButton stopButton;
    public JLabel searchResultLabel;
    public JButton openProcessButton;
    public JTextField memoryStartAddress;
    public JTextField memoryEndAddress;
    public JComboBox<String> memoryRangeBoBox;
    public MemoryRange range;
    public ProcessChooseWnd processChooseWnd;

    public MainWnd() {
        setTitle("內存修改工具1.0");
        setResizable(false);
        getContentPane().setLayout(null);

        JLabel lblNewLabel = new JLabel("当前进程:");
        lblNewLabel.setBounds(10, 10, 56, 15);
        getContentPane().add(lblNewLabel);

        statusLabel = new JLabel("没有选择任何进程");
        statusLabel.setBounds(66, 10, 272, 15);
        getContentPane().add(statusLabel);

        progressBar = new JProgressBar();
        progressBar.setBounds(10, 30, 426, 19);
        getContentPane().add(progressBar);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(10, 84, 183, 304);
        getContentPane().add(scrollPane);

        table = new JTable();
        tableModel = new MemoryValueTable();
        MainWndEvent event = new MainWndEvent(this);
        table.addMouseListener(event.tableMouseClick());
        table.setModel(tableModel);
        table.getColumnModel().getColumn(0).setPreferredWidth(78);
        scrollPane.setViewportView(table);

        searchResultLabel = new JLabel("搜索结果:");
        searchResultLabel.setBounds(10, 59, 426, 15);
        getContentPane().add(searchResultLabel);

        openProcessButton = new JButton("打开进程");
        processChooseWnd = new ProcessChooseWnd(this);
        openProcessButton.addActionListener(e -> processChooseWnd.setVisible(true));
        openProcessButton.setBounds(203, 84, 233, 23);
        getContentPane().add(openProcessButton);

        JLabel label = new JLabel("输入搜索值:");
        label.setBounds(203, 117, 63, 15);
        getContentPane().add(label);

        searchText = new JTextField();
        searchText.setBounds(270, 114, 166, 21);
        searchText.setColumns(10);
        getContentPane().add(searchText);

        JLabel searchType = new JLabel("搜索类型:");
        searchType.setBounds(213, 142, 53, 15);
        getContentPane().add(searchType);

        this.comparisonWay = new JComboBox<>();
        this.comparisonWay.setModel(new DefaultComboBoxModel<>(WayOfComparison.getAll()));
        this.comparisonWay.setBounds(270, 139, 166, 21);
        getContentPane().add(this.comparisonWay);

        JLabel lblNewLabel_2 = new JLabel("数据类型:");
        lblNewLabel_2.setBounds(213, 170, 53, 15);
        getContentPane().add(lblNewLabel_2);

        searchDataType = new JComboBox<>();
        searchDataType.setModel(new DefaultComboBoxModel<>(DataType.getAll()));
        searchDataType.setBounds(270, 167, 166, 21);
        getContentPane().add(searchDataType);

        firstSearchButton = new JButton("开始搜索");
        firstSearchButton.addActionListener(event.firstSearchButton());
        firstSearchButton.setBounds(270, 280, 81, 23);
        getContentPane().add(firstSearchButton);

        lastSearchButton = new JButton("搜索变化");
        lastSearchButton.setEnabled(false);
        lastSearchButton.addActionListener(event.lastSearchButton());
        lastSearchButton.setBounds(355, 280, 81, 23);
        getContentPane().add(lastSearchButton);

        JLabel label_2 = new JLabel("-----------------------內存操作-----------------------");
        label_2.setBounds(203, 323, 233, 15);
        getContentPane().add(label_2);

        JLabel label_3 = new JLabel("内存地址:");
        label_3.setBounds(213, 348, 53, 15);
        getContentPane().add(label_3);

        memoryAddressText = new JTextField();
        memoryAddressText.setEditable(false);
        memoryAddressText.setColumns(10);
        memoryAddressText.setBounds(270, 345, 166, 21);
        getContentPane().add(memoryAddressText);

        JLabel label_4 = new JLabel("內存的值:");
        label_4.setBounds(213, 373, 53, 15);
        getContentPane().add(label_4);

        memoryUpdateValue = new JTextField();
        memoryUpdateValue.setColumns(10);
        memoryUpdateValue.setBounds(270, 370, 166, 21);
        getContentPane().add(memoryUpdateValue);

        writeMemoryButton = new JButton("写入内存");
        writeMemoryButton.setEnabled(false);
        writeMemoryButton.setBounds(270, 401, 81, 23);
        writeMemoryButton.addActionListener(event.updateMemoryValueButton());
        getContentPane().add(writeMemoryButton);


        killButton = new JButton("杀死进程");
        killButton.addActionListener(event.killProcessButton());
        killButton.setEnabled(false);
        killButton.setBounds(355, 401, 81, 23);
        getContentPane().add(killButton);

        resetButton = new JButton("清空搜索結果");
        resetButton.setEnabled(false);
        resetButton.setBounds(80, 401, 113, 23);
        resetButton.addActionListener((e) -> resetWindow());
        getContentPane().add(resetButton);

        stopButton = new JButton("停止扫描");
        stopButton.setBounds(349, 6, 87, 23);
        stopButton.setVisible(false);
        stopButton.addActionListener(event.stopButton());
        getContentPane().add(stopButton);

        JLabel label_5 = new JLabel("开始地址:");
        label_5.setBounds(213, 227, 53, 15);
        getContentPane().add(label_5);

        memoryStartAddress = new JTextField();
        memoryStartAddress.setEditable(false);
        memoryStartAddress.setBounds(270, 224, 166, 21);
        getContentPane().add(memoryStartAddress);
        memoryStartAddress.setColumns(10);

        JLabel label_6 = new JLabel("结束地址:");
        label_6.setBounds(213, 252, 53, 15);
        getContentPane().add(label_6);

        memoryEndAddress = new JTextField();
        memoryEndAddress.setEditable(false);
        memoryEndAddress.setColumns(10);
        memoryEndAddress.setBounds(270, 249, 166, 21);
        getContentPane().add(memoryEndAddress);

        JLabel label_7 = new JLabel("内存范围:");
        label_7.setBounds(213, 202, 53, 15);
        getContentPane().add(label_7);

        memoryRangeBoBox = new JComboBox<>();
        memoryRangeBoBox.setEnabled(false);
        memoryRangeBoBox.setModel(new DefaultComboBoxModel<>(new String[]{"进程所占内存范围", "整个系统内存范围", "自定义內存范围"}));
        memoryRangeBoBox.setBounds(270, 198, 166, 21);
        memoryRangeBoBox.addItemListener(event.memoryRangeItem());
        getContentPane().add(memoryRangeBoBox);

        initialize();
    }

    public static void main(String[] args) throws Exception {
        UIManager.setLookAndFeel(new AluminiumLookAndFeel());
        //UIManager.setLookAndFeel("com.jtattoo.plaf.aluminium.AluminiumLookAndFeel");
        UIManager.put("InternalFrame.titleFont", new java.awt.Font("宋体", 0, 12));
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    MainWnd window = new MainWnd();
                    window.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initialize() {
        setBounds(100, 100, 452, 484);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public void resetWindow() {
        statusLabel.setText("没有选择任何进程");
        tableModel.setRowCount(0);
        lastSearchButton.setEnabled(false);
        writeMemoryButton.setEnabled(false);
        killButton.setEnabled(false);
        currentProcess = null;
        memoryAddressText.setText("");
        memoryUpdateValue.setText("");
        searchDataType.setEnabled(true);
        searchResultLabel.setText("搜索结果:");
        progressBar.setIndeterminate(false);
        firstSearchButton.setEnabled(true);
        openProcessButton.setEnabled(true);
        stopButton.setVisible(false);
        memoryRangeBoBox.setEnabled(false);
        memoryRangeBoBox.setSelectedIndex(0);
        memoryStartAddress.setText("");
        memoryEndAddress.setText("");
        resetButton.setEnabled(false);
    }
}
