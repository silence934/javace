package com.memory.wnd;

/**
 * 作者:Code菜鸟
 * 技术交流QQ:969422014
 * CSDN博客:http://blog.csdn.net/qq969422014
 * <p>
 * 让JTable变成可以选择,但是不可以编辑的框
 **/
public class DefaultTableModel extends javax.swing.table.DefaultTableModel {
    private static final long serialVersionUID = 1L;

    public DefaultTableModel(Object[][] data, Object[] columnNames) {
        //这里一定要覆盖父类的构造方法,否则不能实例myTableModel
        super(data, columnNames);
    }

    public boolean isCellEditable(int row, int column) {
        //父类的方法里面是 return true的,所以就可以编辑了
        return false;
    }
}
