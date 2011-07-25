package in.uglyhunk.jvm.mon;

import java.lang.management.ClassLoadingMXBean;
import java.lang.management.CompilationMXBean;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.ThreadMXBean;
import java.util.ArrayList;
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

    public static HashMap<Integer, ThreadMXBean> getThreadMXBeanMap() {
        return threadMXBeanMap;
    }
    
    public static HashMap<Integer, CompilationMXBean> getCompilationMXBeanMap() {
        return compilationMXBeanMap;
    }
    
    public static HashMap<Integer, ArrayList<GarbageCollectorMXBean>> getGCMXBeanMap() {
        return gcMXBeanMap;
    }
    
    public static HashMap<Integer, ArrayList<MemoryPoolMXBean>> getMemPoolMXBeanMap() {
        return memPoolMXBeanMap;
    }
    
    private static HashMap<Integer, MemoryMXBean> memoryMXBeanMap = new HashMap<Integer, MemoryMXBean>();
    private static HashMap<Integer, ClassLoadingMXBean> classLoadingMXBeanMap = new HashMap<Integer, ClassLoadingMXBean>();
    private static HashMap<Integer, ThreadMXBean> threadMXBeanMap = new HashMap<Integer, ThreadMXBean>();
    private static HashMap<Integer, CompilationMXBean> compilationMXBeanMap = new HashMap<Integer, CompilationMXBean>();
    private static HashMap<Integer, ArrayList<GarbageCollectorMXBean>> gcMXBeanMap = new HashMap<Integer, ArrayList<GarbageCollectorMXBean>>();
    private static HashMap<Integer, ArrayList<MemoryPoolMXBean>> memPoolMXBeanMap = new HashMap<Integer, ArrayList<MemoryPoolMXBean>>();
    
    private static Lock mxBeanStoreLock = new ReentrantLock();
}
