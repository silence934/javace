package com.memory.constant;

/**
 * @author: silence
 * @Date: 2021/9/3 22:41
 * @Description:
 */
public enum DataType {
    INT("int", 4),
    SHORT("short", 2),
    LONG("long", 8),
    FLOAT("float", 4),
    DOUBLE("double", 8),
    BYTE("byte", 1),
    ;

    private final String typeName;
    private final int dataSize;

    DataType(String typeName, int dataSize) {
        this.typeName = typeName;
        this.dataSize = dataSize;
    }

    public static DataType[] getAll() {
        return new DataType[]{DataType.INT, DataType.SHORT, DataType.LONG, DataType.FLOAT, DataType.DOUBLE, DataType.BYTE};
    }

    public String getTypeName() {
        return typeName;
    }

    public int getDataSize() {
        return dataSize;
    }

    @Override
    public String toString() {
        return typeName;
    }
}
