package com.memory.wnd;

import java.awt.BorderLayout;

import javax.swing.DefaultListModel;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import javax.swing.JList;

import com.memory.event.ProcessChooseWndEvent;

/**
 * 作者:Code菜鸟
 * 技术交流QQ:969422014
 * CSDN博客:http://blog.csdn.net/qq969422014
 * 
 * 进程选择界面
 * */
public class ProcessChooseWnd extends JDialog 
{

	private static final long serialVersionUID = 1L;
	private JPanel contentPanel = new JPanel();
	public JList list;
	public DefaultListModel model = new DefaultListModel();
	public MainWnd mainWnd = null;
	private ProcessChooseWndEvent event = new ProcessChooseWndEvent(this);
	
	public ProcessChooseWnd(MainWnd mainWnd)
	{
		super(mainWnd,true);
		this.mainWnd = mainWnd;
		setTitle("鼠标双击需要打开的进程");
		setResizable(false);
		setBounds(100, 100, 242, 330);
		setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 0, 236, 298);
		contentPanel.add(scrollPane);
		
		list = new JList();
		list.addMouseListener(event.processListMouseDClient());
		scrollPane.setViewportView(list);
		list.setModel(model);
		
		addWindowListener(event.windowActivated());
	}
}
