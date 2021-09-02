package com.memory.wnd;

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

    public JList<OSProcess> list;

    public MainWnd mainWnd;

    public ProcessChooseWnd(MainWnd mainWnd) {
        super(mainWnd, true);
        this.mainWnd = mainWnd;
        setTitle("鼠标双击需要打开的进程");
        setResizable(false);
        setBounds(100, 100, 242, 330);
        setLocationRelativeTo(null);
        getContentPane().setLayout(new BorderLayout());
        JPanel contentPanel = new JPanel();
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(null);

        ProcessChooseWndEvent event = new ProcessChooseWndEvent(this);

        //todo 刷新
        list = new JList<>();
        list.setModel(getProcess(process ->process.getName().contains("Plants") ));
        list.addMouseListener(event.processListMouseClient());

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(0, 0, 236, 298);
        scrollPane.setViewportView(list);
        contentPanel.add(scrollPane);

    }


    private DefaultListModel<OSProcess> getProcess(Predicate<OSProcess> filter){

        DefaultListModel<OSProcess> defaultListModel=new DefaultListModel<>();

        SystemInfo systemInfo=new SystemInfo();
        OperatingSystem operatingSystem = systemInfo.getOperatingSystem();

         operatingSystem.getProcesses().stream().filter(filter)
                .sorted(Comparator.comparing(OSProcess::getName))
                 .forEach(defaultListModel::addElement);

         return defaultListModel;
    }



}
