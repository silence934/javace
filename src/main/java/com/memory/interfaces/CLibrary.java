package com.memory.interfaces;

import com.sun.jna.Library;
import com.sun.jna.Native;

/**
 * @author: fucong
 * @Date: 2021/8/31 11:03
 * @Description:
 */
public interface CLibrary extends Library {

    CLibrary INSTANCE = Native.load("ApplicationServices", CLibrary.class);

    //int GetFrontProcess(LongByReference processSerialNumber);

    //int GetProcessPID(LongByReference processSerialNumber, IntByReference pid);
}
