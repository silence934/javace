package com.memory.entity;

import lombok.Data;
import oshi.software.os.OSProcess;

/**
 * @author: silence
 * @Date: 2021/9/4 17:20
 * @Description:
 */
@Data
public class CeProcess {

    private OSProcess process;

    public CeProcess(OSProcess process) {
        this.process = process;
    }

    @Override
    public String toString() {
        return process.getName();
    }
}
