package com.memory.wnd;

import com.memory.entity.MemoryValue;
import com.sun.jna.Pointer;

import javax.swing.table.AbstractTableModel;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author: silence
 * @Date: 2021/9/4 14:56
 * @Description:
 */
public class MemoryValueTable extends AbstractTableModel implements Serializable {


    private List<MemoryValue> values;


    public MemoryValueTable() {
        values = Collections.synchronizedList(new ArrayList<>());
    }

    @Override
    public String getColumnName(int column) {
        return column == 0 ? "內存地址" : "内存内容";
    }


    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return columnIndex == 0 ? Pointer.class : String.class;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        MemoryValue memoryValue = values.get(rowIndex);
        if (columnIndex == 0) {
            memoryValue.setAddress((Pointer) aValue);
        } else {
            memoryValue.setValue((String) aValue);
        }
    }


    @Override
    public int getRowCount() {
        return values.size();
    }

    public void setRowCount(int rowCount) {
        int old = values.size();
        values = values.subList(0, rowCount);
        fireTableRowsDeleted(rowCount, Math.max(0, old - 1));
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        MemoryValue memoryValue = values.get(rowIndex);
        if (columnIndex == 0) {
            return Long.toString(Pointer.nativeValue(memoryValue.getAddress()), 16);
        } else {
            return memoryValue.getValue();
        }
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    public void addRow(MemoryValue memoryValue) {
        values.add(memoryValue);
        fireTableRowsInserted(values.size(), values.size());
    }


    public List<MemoryValue> getValues() {
        return values;
    }
}
