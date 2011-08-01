package in.uglyhunk.jvm.mon;

import java.lang.management.ClassLoadingMXBean;
import java.lang.management.CompilationMXBean;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryUsage;
import java.lang.management.ThreadMXBean;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.locks.Lock;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VMDataUtil {

    public static String getVMInfo() {

        Lock mxBeanStoreLock = MXBeanStore.getMXBeanStoreLock();
        mxBeanStoreLock.lock();
        StringBuilder outputBuilder = new StringBuilder();
        try {
            String snapshotTime = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date());
            String vmIds = getVmIds();
            if (vmIds != null) {
                String memMXBeanData = memMXBeanDataUtil();
                String classLoadingMXBeanData = classLoadingMXBeanDataUtil();
                String threadMXBeanData = threadMXBeanDataUtil();
                String compilationMXBeanData = compilationMXBeanDataUtil();
                String gcMXBeanData = gcMXBeanDataUtil();
                String memPoolMXBeanData = memPoolMXBeanDataUtil();
                
                String[] vmId = vmIds.split(";");
                String[] memMXBeanDataPerVM = memMXBeanData.split(";");
                String[] classLoadingMXBeanDataPerVM = classLoadingMXBeanData.split(";");
                String[] threadMXBeanDataPerVM = threadMXBeanData.split(";");
                String[] compilationMXBeanDataPerVM = compilationMXBeanData.split(";");
                String[] gcMXBeanDataPerVM = gcMXBeanData.split(";");
                String[] memPoolMXBeanDataPerVM = memPoolMXBeanData.split(";");
                
                int vmCount = 0;
                
                while (vmCount < vmId.length) {
                    outputBuilder.append(snapshotTime).append(",")
                            .append(vmId[vmCount]).append(",")
                            .append(memMXBeanDataPerVM[vmCount]).append(",")
                            .append(classLoadingMXBeanDataPerVM[vmCount]).append(",")
                            .append(threadMXBeanDataPerVM[vmCount]).append(",")
                            .append(compilationMXBeanDataPerVM[vmCount]).append(",")
                            .append(gcMXBeanDataPerVM[vmCount]).append(",")
                            .append(memPoolMXBeanDataPerVM[vmCount]).append(";");
                    
                    vmCount++;
                }

            }
        } finally {
            mxBeanStoreLock.unlock();
        }
        return outputBuilder.toString();
    }

    private static String getVmIds() {
        memoryMXBeanMap = MXBeanStore.getMemoryMXBeanMap();
        Iterator<Entry<String, MemoryMXBean>> itrMemoryMXBean = memoryMXBeanMap.entrySet().iterator();

        StringBuilder vmIdBuffer = new StringBuilder();
        while (itrMemoryMXBean.hasNext()) {
            Entry<String, MemoryMXBean> tempRow = itrMemoryMXBean.next();
            vmIdBuffer.append(tempRow.getKey()).append(";");
        }
        return (vmIdBuffer.length() == 0) ? null : vmIdBuffer.toString();
    }

    private static String memMXBeanDataUtil() {
        StringBuilder memMXBeanOutput = new StringBuilder();

        memoryMXBeanMap = MXBeanStore.getMemoryMXBeanMap();
        Iterator<Entry<String, MemoryMXBean>> itrMemoryMXBean = memoryMXBeanMap.entrySet().iterator();
        
        while (itrMemoryMXBean.hasNext()) {
            Entry<String, MemoryMXBean> tempRow = itrMemoryMXBean.next();
            MemoryMXBean memMXBean = tempRow.getValue();
            String vmId = tempRow.getKey();
            try {

                MemoryUsage heapMemUsage = memMXBean.getHeapMemoryUsage();
                MemoryUsage nonHeapMemUsage = memMXBean.getNonHeapMemoryUsage();

                long usedHeap = heapMemUsage.getUsed() / (1024 * 1024); // in MB
                long committedHeap = heapMemUsage.getCommitted() / (1024 * 1024); // in MB

                long usedNonHeap = nonHeapMemUsage.getUsed() / (1024 * 1024); // in MB
                long committedNonHeap = nonHeapMemUsage.getCommitted() / (1024 * 1024); // in MB

                memMXBeanOutput.append(usedHeap).append(",").
                                append(committedHeap).append(",").
                                append(usedNonHeap).append(",").
                                append(committedNonHeap).append(";");

            } catch (Exception e) {
                memMXBeanOutput.append("0,0,0,0;");
                itrMemoryMXBean.remove();
                logger.log(Level.FINE, e.toString(), e);
                logger.log(Level.FINE, "VM with proc_id{0} is dead while reading memory MX Beans", vmId);
            }
        }
        return (memMXBeanOutput.length() == 0) ? null : memMXBeanOutput.toString();
    }

    private static String classLoadingMXBeanDataUtil() {
        StringBuilder classLoadingMXBeanOutput = new StringBuilder();

        classLoadingMXBeanMap = MXBeanStore.getClassLoadingMXBeanMap();
        Iterator<Entry<String, ClassLoadingMXBean>> itrClassLoadingMXBean = classLoadingMXBeanMap.entrySet().iterator();

        while (itrClassLoadingMXBean.hasNext()) {
            Entry<String, ClassLoadingMXBean> tempRow = itrClassLoadingMXBean.next();
            String vmId = tempRow.getKey();
            ClassLoadingMXBean classLoadingMXBean = tempRow.getValue();
            try {
                long currentLoadedClasses = classLoadingMXBean.getLoadedClassCount();
                long totalLoadedClasses = classLoadingMXBean.getTotalLoadedClassCount();
                long totalUnLoadedClasses = classLoadingMXBean.getUnloadedClassCount();

                classLoadingMXBeanOutput.append(currentLoadedClasses).append(",").
                                        append(totalLoadedClasses).append(",").
                                        append(totalUnLoadedClasses).append(";");
            } catch (Exception e) {
                classLoadingMXBeanOutput.append("0,0,0;");
                itrClassLoadingMXBean.remove();
                if(memoryMXBeanMap.containsKey(vmId)){
                    memoryMXBeanMap.remove(vmId);
                }
                logger.log(Level.FINE, e.toString(), e);
                logger.log(Level.FINE, "VM with proc_id{0} is dead while reading classLoading MX Beans", vmId);
            }
        }
        return (classLoadingMXBeanOutput.length() == 0) ? null : classLoadingMXBeanOutput.toString();
    }

    private static String threadMXBeanDataUtil() {
        StringBuilder threadMXBeanOutput = new StringBuilder();

        threadMXBeanMap = MXBeanStore.getThreadMXBeanMap();
        Iterator<Entry<String, ThreadMXBean>> itrThreadMXBean = threadMXBeanMap.entrySet().iterator();

        while (itrThreadMXBean.hasNext()) {
            Entry<String, ThreadMXBean> tempRow = itrThreadMXBean.next();
            String vmId = tempRow.getKey();
            ThreadMXBean threadMXBean = tempRow.getValue();
            try {
                int daemonThreadCount = threadMXBean.getDaemonThreadCount(); // current live daemon threads
                int peakThreadCount = threadMXBean.getPeakThreadCount();
                int currentThreadCount = threadMXBean.getThreadCount(); // current thread count (live + daemon)
                long totalStartedThreadCount = threadMXBean.getTotalStartedThreadCount(); // total number of threads started

                threadMXBeanOutput.append(daemonThreadCount).append(",").
                                    append(peakThreadCount).append(",").
                                    append(currentThreadCount).append(",").
                                    append(totalStartedThreadCount).append(";");

            } catch (Exception e) {
                threadMXBeanOutput.append("0,0,0,0;");
                itrThreadMXBean.remove();
                
                if(memoryMXBeanMap.containsKey(vmId)){
                    memoryMXBeanMap.remove(vmId);
                }
                if(classLoadingMXBeanMap.containsKey(vmId)){
                    classLoadingMXBeanMap.remove(vmId);
                }
                
                logger.log(Level.FINE, e.toString(), e);
                logger.log(Level.FINE, "VM with proc_id{0} is dead while reading thread MX Beans", vmId);
            }
        }
        return (threadMXBeanOutput.length() == 0 ? null : threadMXBeanOutput.toString());
    }
    
    private static String compilationMXBeanDataUtil() {
        StringBuilder compilationMXBeanOutput = new StringBuilder();
        
        compilationMXBeanMap = MXBeanStore.getCompilationMXBeanMap();
        Iterator<Entry<String, CompilationMXBean>> itrCompilationMXBean = compilationMXBeanMap.entrySet().iterator();

        while (itrCompilationMXBean.hasNext()) {
            Entry<String, CompilationMXBean> tempRow = itrCompilationMXBean.next();
            String vmId = tempRow.getKey();
            CompilationMXBean compilationMXBean = tempRow.getValue();
            try {
                long totalCompilationTime = compilationMXBean.getTotalCompilationTime();
                compilationMXBeanOutput.append(totalCompilationTime).append(";");
            } catch(Exception e){
                compilationMXBeanOutput.append("0;");
                itrCompilationMXBean.remove();
                
                if(memoryMXBeanMap.containsKey(vmId)){
                    memoryMXBeanMap.remove(vmId);
                }
                if(classLoadingMXBeanMap.containsKey(vmId)){
                    classLoadingMXBeanMap.remove(vmId);
                }
                if(threadMXBeanMap.containsKey(vmId)){
                    threadMXBeanMap.remove(vmId);
                }
                logger.log(Level.FINE, e.toString(), e);
                logger.log(Level.FINE, "VM with proc_id{0} is dead while reading compilation MX Beans", vmId);
            }
        }
        return (compilationMXBeanOutput.length() == 0 ? null : compilationMXBeanOutput.toString());
    }
    
    private static String gcMXBeanDataUtil() {
        StringBuilder gcMXBeanOutput = new StringBuilder();
        
        gcMXBeanMap = MXBeanStore.getGCMXBeanMap();
        Iterator<Entry<String, ArrayList<GarbageCollectorMXBean>>> itrGCMXBean = gcMXBeanMap.entrySet().iterator();
        
        while (itrGCMXBean.hasNext()) {
            gcMXBeanOutput.append(" ");
            Entry<String, ArrayList<GarbageCollectorMXBean>> tempRow = itrGCMXBean.next();
            String vmId = tempRow.getKey();
            ArrayList<GarbageCollectorMXBean> gcBeanList = tempRow.getValue();
            int gcFieldCount = gcBeanList.size() * 3;
            try{
                boolean firstItr = true;
                for(GarbageCollectorMXBean gcBean : gcBeanList){
                    String gcName = gcBean.getName();
                    long gcCollectionCount = gcBean.getCollectionCount();
                    long gcCollectionTime = gcBean.getCollectionTime();
                    if(!firstItr){
                        gcMXBeanOutput.append(",");
                    }
                    gcMXBeanOutput.append(gcName).append(",").
                                    append(gcCollectionCount).append(",").
                                    append(gcCollectionTime);
                    firstItr = false;
                }
                gcMXBeanOutput.append(";");
            } catch (Exception e){
                int counter = 1;
                gcMXBeanOutput.append("0");
                while(counter < gcFieldCount){
                    gcMXBeanOutput.append(",0");
                    counter++;
                }
                gcMXBeanOutput.append(";");
                itrGCMXBean.remove();
                
                if(memoryMXBeanMap.containsKey(vmId)){
                    memoryMXBeanMap.remove(vmId);
                }
                if(classLoadingMXBeanMap.containsKey(vmId)){
                    classLoadingMXBeanMap.remove(vmId);
                }
                if(threadMXBeanMap.containsKey(vmId)){
                    threadMXBeanMap.remove(vmId);
                }
                if(compilationMXBeanMap.containsKey(vmId)){
                    compilationMXBeanMap.remove(vmId);
                }
                
                logger.log(Level.FINE, e.toString(), e);
                logger.log(Level.FINE, "VM with proc_id{0} is dead while reading GC MX Beans", vmId);
            }
        }
        
        return (gcMXBeanOutput.length() == 0 ? null : gcMXBeanOutput.toString());
    }
    
    private static String memPoolMXBeanDataUtil() {
        StringBuilder memPoolMXBeanOutput = new StringBuilder();
        
        memoryPoolMXBeanMap = MXBeanStore.getMemPoolMXBeanMap();
        Iterator<Entry<String, ArrayList<MemoryPoolMXBean>>> itrMemPoolMXBean = memoryPoolMXBeanMap.entrySet().iterator();
        
        while (itrMemPoolMXBean.hasNext()) {
            memPoolMXBeanOutput.append(" ");
            Entry<String, ArrayList<MemoryPoolMXBean>> tempRow = itrMemPoolMXBean.next();
            String vmId = tempRow.getKey();
            ArrayList<MemoryPoolMXBean> memPoolBeanList = tempRow.getValue();
            int memPoolFieldCount = memPoolBeanList.size() * 3;
            try {
                boolean firstItr = true;
                for(MemoryPoolMXBean memPoolBean : memPoolBeanList){
                    MemoryUsage memUsage = memPoolBean.getUsage();
                    String memPoolName = memPoolBean.getName();
                    long usedMemory = memUsage.getUsed() / (1024 * 1024);
                    long commMemory = memUsage.getCommitted() / (1024 * 1024);
                    if(!firstItr){
                        memPoolMXBeanOutput.append(",");
                    }
                    memPoolMXBeanOutput.append(memPoolName).append(",").
                                        append(usedMemory).append(",").
                                        append(commMemory);
                    firstItr = false;
                }
                memPoolMXBeanOutput.append(";");
            } catch(Exception e){
                int counter = 1;
                memPoolMXBeanOutput.append("0");
                while(counter < memPoolFieldCount){
                    memPoolMXBeanOutput.append(",0");
                    counter++;
                }
                memPoolMXBeanOutput.append(";");
                
                itrMemPoolMXBean.remove();
                
                
                if(memoryMXBeanMap.containsKey(vmId)){
                    memoryMXBeanMap.remove(vmId);
                }
                if(classLoadingMXBeanMap.containsKey(vmId)){
                    classLoadingMXBeanMap.remove(vmId);
                }
                if(threadMXBeanMap.containsKey(vmId)){
                    threadMXBeanMap.remove(vmId);
                }
                if(compilationMXBeanMap.containsKey(vmId)){
                    compilationMXBeanMap.remove(vmId);
                }
                if(gcMXBeanMap.containsKey(vmId)){
                    gcMXBeanMap.remove(vmId);
                }
                logger.log(Level.FINE, e.toString(), e);
                logger.log(Level.FINE, "VM with proc_id{0} is dead while reading MemoryPool MX Beans", vmId);
            }
        }
        return (memPoolMXBeanOutput.length() == 0 ? null : memPoolMXBeanOutput.toString());
    }

    
   private static HashMap<String, MemoryMXBean> memoryMXBeanMap;
   private static HashMap<String, ClassLoadingMXBean> classLoadingMXBeanMap;
   private static HashMap<String, ThreadMXBean> threadMXBeanMap;
   private static HashMap<String, CompilationMXBean> compilationMXBeanMap; 
   private static HashMap<String, ArrayList<GarbageCollectorMXBean>> gcMXBeanMap;
   private static HashMap<String, ArrayList<MemoryPoolMXBean>> memoryPoolMXBeanMap; 
   private static final Logger logger = Main.getLogger();
}
