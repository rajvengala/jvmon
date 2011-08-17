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
    
    public OperatingSystemMXBean getOSMXBean() {
        return osMXBean;
    }
    
    public void setOSMXBean(OperatingSystemMXBean _osMXBean){
        osMXBean = _osMXBean;
    }

    public MemoryMXBean getMemoryMXBean() {
        return memoryMXBean;
    }
    
    public void setMemoryMXBean(MemoryMXBean _memoryMXBean){
        memoryMXBean = _memoryMXBean;
    }

    public ClassLoadingMXBean getClassLoadingMXBean() {
        return classLoadingMXBean;
    }
    
    public void setClassLoadingMXBean(ClassLoadingMXBean _classLoadingMXBean) {
        classLoadingMXBean = _classLoadingMXBean;
    }
    
    
    public ThreadMXBean getThreadMXBean() {
        return threadMXBean;
    }
    
    public void setThreadMXBean(ThreadMXBean _threadMXBean) {
        threadMXBean = _threadMXBean;
    }
    
    
    public CompilationMXBean getCompilationMXBean() {
        return compilationMXBean;
    }
    
    public void setCompilationMXBean(CompilationMXBean _compilationMXBean) {
        compilationMXBean = _compilationMXBean;
    }
    
    
    public ArrayList<GarbageCollectorMXBean> getGCMXBean() {
        return gcMXBean;
    }
    
    public void setGCMXBean(ArrayList<GarbageCollectorMXBean> _gcMXBean) {
        gcMXBean = _gcMXBean;
    }
    
    public ArrayList<MemoryPoolMXBean> getMemPoolMXBean() {
        return memPoolMXBean;
    }
    
    public void setMemPoolMXBean(ArrayList<MemoryPoolMXBean> _memPoolMXBean) {
        memPoolMXBean = _memPoolMXBean;
    }
    
    public static HashMap<String, MXBeanStore> getMXBeanStoreObjectMap(){
        return mxBeanStoreObjectMap;
    }
       
    private OperatingSystemMXBean osMXBean;
    private MemoryMXBean memoryMXBean;
    private ClassLoadingMXBean classLoadingMXBean;
    private ThreadMXBean threadMXBean;
    private CompilationMXBean compilationMXBean;
    private ArrayList<GarbageCollectorMXBean> gcMXBean = new ArrayList<GarbageCollectorMXBean>();
    private ArrayList<MemoryPoolMXBean> memPoolMXBean = new ArrayList<MemoryPoolMXBean>();
    
    private static HashMap<String, MXBeanStore> mxBeanStoreObjectMap = new HashMap<String, MXBeanStore>();
    private static Lock mxBeanStoreLock = new ReentrantLock();
}
