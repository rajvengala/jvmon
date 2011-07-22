package in.uglyhunk.jvm.mon;

import java.lang.management.ClassLoadingMXBean;
import java.lang.management.MemoryMXBean;
import java.util.HashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MXBeanStore {

    public static Lock getMXBeanStoreLock() {
        return mxBeanStoreLock;
    }

    public static HashMap<Integer, MemoryMXBean> getMemoryMXBeanMap() {
        return memoryMXBeanMap;
    }

    public static HashMap<Integer, ClassLoadingMXBean> getClassLoadingMXBeanMap() {
        return classLoadingMXBeanMap;
    }

    public static HashMap<Integer, java.lang.management.ThreadMXBean> getThreadMXBeanMap() {
        return threadMXBeanMap;
    }
    private static HashMap<Integer, MemoryMXBean> memoryMXBeanMap = new HashMap<Integer, MemoryMXBean>();
    private static HashMap<Integer, ClassLoadingMXBean> classLoadingMXBeanMap = new HashMap<Integer, ClassLoadingMXBean>();
    private static HashMap<Integer, java.lang.management.ThreadMXBean> threadMXBeanMap = new HashMap<Integer, 
                                                                                        java.lang.management.ThreadMXBean>();
    private static Lock mxBeanStoreLock = new ReentrantLock();
}
