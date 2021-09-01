package com.memory.entity;

import lombok.Data;

/**
 * 作者:Code菜鸟
 * 技术交流QQ:969422014
 * CSDN博客:http://blog.csdn.net/qq969422014
 * <p>
 * API函数调用结果返回对象
 *
 * @author fucong*/
@Data
public class ExecuteResult {

    /**
     * 错误代码
     */
    private int lastError;

    private String message;

    private Object value;

}
