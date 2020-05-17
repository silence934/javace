package com.memory.wnd;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.UIManager;

import com.memory.entity.Process;
import com.memory.event.MainWndEvent;

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
    public Process currentProcess = null;
    public JButton firstSearchButton;
    public JButton lastSearchButton;
    public JProgressBar progressBar;
    public JComboBox searchType;
    public DefaultTableModel tableModel = null;
    public JButton writeMemoryButton;
    public JButton killButton;
    public JComboBox searchDataType;
    public JButton resetButton;
    public JButton stopButton;
    public JLabel searchResultLabel;
    public JTextField memoryStartAddress;
    public JTextField memoryEndAddress;
    public JComboBox memoryRangecomBoBox;
    private MainWndEvent event = new MainWndEvent(this);

    public static void main(String[] args) throws Exception {
        UIManager.setLookAndFeel("com.jtattoo.plaf.aluminium.AluminiumLookAndFeel");
        UIManager.put("InternalFrame.titleFont", new java.awt.Font("宋体", 0, 12));
        EventQueue.invokeLater(new Runnable() {
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

    public MainWnd() {
        setTitle("內存修改工具1.0");
        setResizable(false);
        getContentPane().setLayout(null);

        JLabel lblNewLabel = new JLabel("\u5F53\u524D\u8FDB\u7A0B:");
        lblNewLabel.setBounds(10, 10, 56, 15);
        getContentPane().add(lblNewLabel);

        statusLabel = new JLabel("\u6CA1\u6709\u9009\u62E9\u4EFB\u4F55\u8FDB\u7A0B");
        statusLabel.setBounds(66, 10, 272, 15);
        getContentPane().add(statusLabel);

        progressBar = new JProgressBar();
        progressBar.setBounds(10, 30, 426, 19);
        getContentPane().add(progressBar);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(10, 84, 183, 304);
        getContentPane().add(scrollPane);

        table = new JTable();
        tableModel = new DefaultTableModel(
                new Object[][]{
                },
                new String[]{
                        "\u5167\u5B58\u5730\u5740", "\u503C"
                }
        );
        table.addMouseListener(event.tableMouseClick());
        table.setModel(tableModel);
        table.getColumnModel().getColumn(0).setPreferredWidth(78);
        scrollPane.setViewportView(table);

        searchResultLabel = new JLabel("\u641C\u7D22\u7ED3\u679C:");
        searchResultLabel.setBounds(10, 59, 426, 15);
        getContentPane().add(searchResultLabel);

        JButton openProcessButton = new JButton("\u6253\u5F00\u8FDB\u7A0B");
        openProcessButton.addActionListener(event.openProcessButton());
        openProcessButton.setBounds(203, 84, 233, 23);
        getContentPane().add(openProcessButton);

        JLabel label = new JLabel("\u8F93\u5165\u641C\u7D22\u503C:");
        label.setBounds(203, 117, 63, 15);
        getContentPane().add(label);

        searchText = new JTextField();
        searchText.setBounds(270, 114, 166, 21);
        getContentPane().add(searchText);
        searchText.setColumns(10);

        JLabel label_1 = new JLabel("\u641C\u7D22\u7C7B\u578B:");
        label_1.setBounds(213, 142, 53, 15);
        getContentPane().add(label_1);

        searchType = new JComboBox();
        searchType.setModel(new DefaultComboBoxModel(new String[]{"精确值", "比搜索值大", "比搜索值小"}));
        searchType.setBounds(270, 139, 166, 21);
        getContentPane().add(searchType);

        JLabel lblNewLabel_2 = new JLabel("\u6570\u636E\u7C7B\u578B:");
        lblNewLabel_2.setBounds(213, 170, 53, 15);
        getContentPane().add(lblNewLabel_2);

        searchDataType = new JComboBox();
        searchDataType.setModel(new DefaultComboBoxModel(new String[]{"整数 int", "短整数 short", "长整数 long", "单精度浮点 float", "双精度浮点 double", "字节 byte"}));
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

        JLabel label_2 = new JLabel("-----------------------\u5167\u5B58\u64CD\u4F5C-----------------------");
        label_2.setBounds(203, 323, 233, 15);
        getContentPane().add(label_2);

        JLabel label_3 = new JLabel("\u5185\u5B58\u5730\u5740:");
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

        writeMemoryButton = new JButton("\u5199\u5165\u5185\u5B58");
        writeMemoryButton.setEnabled(false);
        writeMemoryButton.setBounds(270, 401, 81, 23);
        writeMemoryButton.addActionListener(event.updateMemoryValueButton());
        getContentPane().add(writeMemoryButton);

        JLabel lblQq = new JLabel("QQ:969422014");
        lblQq.setBounds(349, 434, 87, 15);
        getContentPane().add(lblQq);

        killButton = new JButton("杀死进程");
        killButton.addActionListener(event.killProcessButton());
        killButton.setEnabled(false);
        killButton.setBounds(355, 401, 81, 23);
        getContentPane().add(killButton);

        resetButton = new JButton("清空搜索結果");
        resetButton.setEnabled(false);
        resetButton.setBounds(80, 401, 113, 23);
        resetButton.addActionListener(event.resetButton());
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

        memoryRangecomBoBox = new JComboBox();
        memoryRangecomBoBox.setEnabled(false);
        memoryRangecomBoBox.setModel(new DefaultComboBoxModel(new String[]{"进程所占内存范围", "整个系统内存范围", "自定义內存范围"}));
        memoryRangecomBoBox.setBounds(270, 198, 166, 21);
        memoryRangecomBoBox.addItemListener(event.memoryRangeItem());
        getContentPane().add(memoryRangecomBoBox);

        initialize();
    }

    private void initialize() {
        setBounds(100, 100, 452, 484);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        addWindowListener(event.openLoad());
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
        stopButton.setVisible(false);
        memoryRangecomBoBox.setEnabled(false);
        memoryRangecomBoBox.setSelectedIndex(0);
        memoryStartAddress.setText("");
        memoryEndAddress.setText("");
        resetButton.setEnabled(false);
    }
}