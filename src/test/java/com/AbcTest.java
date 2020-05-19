package com;

import com.memory.entity.ExecuteResult;
import com.memory.entity.Process;
import com.memory.impl.LoadSystemProcessInfo;

import java.util.List;

public class AbcTest {

    public static void main(String[] args) {

        ExecuteResult result = new LoadSystemProcessInfo().getProcess();

        List<Process> xx = (List<Process>) result.getValue();
        for (Process s : xx) {
            System.out.println(s.getProcessName());
        }
    }
}
