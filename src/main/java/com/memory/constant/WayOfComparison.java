package com.memory.constant;

/**
 * @author: silence
 * @Date: 2021/9/4 14:27
 * @Description:
 */

public enum WayOfComparison {

    EQUALITY("精确值", 0),
    LESS("比搜索值大", 1),
    GREATER("比搜索值小", -1);

    private final String name;
    private final Integer value;

    WayOfComparison(String name, Integer value) {
        this.name = name;
        this.value = value;
    }

    public static WayOfComparison[] getAll() {
        return new WayOfComparison[]{EQUALITY, LESS, GREATER};
    }

    public String getName() {
        return name;
    }

    public Integer getValue() {
        return value;
    }

    @Override
    public String toString() {
        return name;
    }

}
