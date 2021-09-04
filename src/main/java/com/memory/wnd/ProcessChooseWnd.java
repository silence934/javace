package com.memory.wnd;

import com.memory.entity.CeProcess;
import com.memory.event.ProcessChooseWndEvent;
import oshi.SystemInfo;
import oshi.software.os.OSProcess;
import oshi.software.os.OperatingSystem;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Comparator;
import java.util.function.Predicate;

/**
 * 作者:Code菜鸟
 * 技术交流QQ:969422014
 * CSDN博客:http://blog.csdn.net/qq969422014
 * <p>
 * 进程选择界面
 * @author fucong
 */
public class ProcessChooseWnd extends JDialog {

    private static final long serialVersionUID = 1L;

    public JList<CeProcess> list;

    public MainWnd mainWnd;

    public ProcessChooseWnd(MainWnd mainWnd) {
        super(mainWnd, true);
        this.mainWnd = mainWnd;
        setTitle("鼠标双击需要打开的进程");
        setResizable(false);
        setBounds(100, 100, 242, 330);
        setLocationRelativeTo(null);
        // getContentPane().setLayout(new BorderLayout());


        ProcessChooseWndEvent event = new ProcessChooseWndEvent(this);
        DefaultListModel<CeProcess> listModel = new DefaultListModel<>();
        list = new JList<>();
        list.setModel(listModel);
        list.addMouseListener(event.processListMouseClient());

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(0, 25, 236, 298);
        scrollPane.setViewportView(list);

        JTextField searchText = new JTextField();
        searchText.setBounds(1, 1, 154, 23);
        searchText.setColumns(10);

        JButton searchButton = new JButton("搜索");
        searchButton.setBounds(155, 1, 81, 23);
        searchButton.setVisible(true);
        searchButton.addActionListener((e) -> {
            // MainWndEvent.EXECUTOR_SERVICE.submit(new ScanProcessRunner(listModel, p -> true));
            new ScanProcessRunner(listModel, p -> p.getName().contains(searchText.getText().trim())).run();
        });

        JPanel contentPanel = new JPanel();
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPanel.setLayout(null);
        contentPanel.add(scrollPane);
        contentPanel.add(searchButton);
        contentPanel.add(searchText);
        getContentPane().add(contentPanel, BorderLayout.CENTER);

    }


    private static class ScanProcessRunner implements Runnable {

        private final DefaultListModel<CeProcess> listModel;

        private final Predicate<OSProcess> predicate;

        public ScanProcessRunner(DefaultListModel<CeProcess> listModel, Predicate<OSProcess> predicate) {
            this.listModel = listModel;
            this.predicate = predicate;
        }

        @Override
        public void run() {
            listModel.clear();
            SystemInfo systemInfo = new SystemInfo();
            OperatingSystem operatingSystem = systemInfo.getOperatingSystem();

            operatingSystem.getProcesses().stream().filter(predicate)
                    .sorted(Comparator.comparing(OSProcess::getName))
                    .forEach(p -> listModel.addElement(new CeProcess(p)));
        }
    }


}
