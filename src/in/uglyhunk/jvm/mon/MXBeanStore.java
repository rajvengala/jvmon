package in.uglyhunk.jvm.mon;

import java.lang.management.ClassLoadingMXBean;
import java.lang.management.CompilationMXBean;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.ThreadMXBean;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MXBeanStore {

    public static Lock getMXBeanStoreLock() {
        return mxBeanStoreLock;
    }
    
    public static HashMap<String, OperatingSystemMXBean> getOSMXBeanMap() {
        return osMXBeanMap;
    }

    public static HashMap<String, MemoryMXBean> getMemoryMXBeanMap() {
        return memoryMXBeanMap;
    }

    public static HashMap<String, ClassLoadingMXBean> getClassLoadingMXBeanMap() {
        return classLoadingMXBeanMap;
    }

    public static HashMap<String, ThreadMXBean> getThreadMXBeanMap() {
        return threadMXBeanMap;
    }
    
    public static HashMap<String, CompilationMXBean> getCompilationMXBeanMap() {
        return compilationMXBeanMap;
    }
    
    public static HashMap<String, ArrayList<GarbageCollectorMXBean>> getGCMXBeanMap() {
        return gcMXBeanMap;
    }
    
    public static HashMap<String, ArrayList<MemoryPoolMXBean>> getMemPoolMXBeanMap() {
        return memPoolMXBeanMap;
    }
    
    private static HashMap<String, OperatingSystemMXBean> osMXBeanMap = new HashMap<String, OperatingSystemMXBean>();
    private static HashMap<String, MemoryMXBean> memoryMXBeanMap = new HashMap<String, MemoryMXBean>();
    private static HashMap<String, ClassLoadingMXBean> classLoadingMXBeanMap = new HashMap<String, ClassLoadingMXBean>();
    private static HashMap<String, ThreadMXBean> threadMXBeanMap = new HashMap<String, ThreadMXBean>();
    private static HashMap<String, CompilationMXBean> compilationMXBeanMap = new HashMap<String, CompilationMXBean>();
    private static HashMap<String, ArrayList<GarbageCollectorMXBean>> gcMXBeanMap = new HashMap<String, ArrayList<GarbageCollectorMXBean>>();
    private static HashMap<String, ArrayList<MemoryPoolMXBean>> memPoolMXBeanMap = new HashMap<String, ArrayList<MemoryPoolMXBean>>();
    
    private static Lock mxBeanStoreLock = new ReentrantLock();
}
