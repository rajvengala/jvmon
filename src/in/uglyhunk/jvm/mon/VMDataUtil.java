package in.uglyhunk.jvm.mon;

import java.lang.management.ClassLoadingMXBean;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.locks.Lock;
import java.util.logging.Level;

public class VMDataUtil {

    public static String getVMInfo() {

        Lock mxBeanStoreLock = MXBeanStore.getMXBeanStoreLock();
        mxBeanStoreLock.lock();
        StringBuilder outputBuilder = new StringBuilder();
        try {
            String snapshotTime = new SimpleDateFormat("HH:mm:ss").format(new Date());
            String vmIds = getVmIds();
            if (vmIds != null) {
                String memMXBeanData = memMXBeanDataUtil();
                String classLoadingMXBeanData = classLoadingMXBeanDataUtil();
                String threadMXBeanData = threadMXBeanDataUtil();
                String[] vmId = vmIds.split(",");
                String[] memMXBeanDataPerVM = memMXBeanData.split(",");
                String[] classLoadingMXBeanDataPerVM = classLoadingMXBeanData.split(",");
                String[] threadMXBeanDataPerVM = threadMXBeanData.split(",");
                int vmCount = 0;
                int memMXIndex = 0;
                int classMXIndex = 0;
                int threadMXIndex = 0;
                outputBuilder.append(snapshotTime).append(",");
                while (vmCount < vmId.length) {
                    outputBuilder.append(vmId[vmCount]).append(",").
                            append(memMXBeanDataPerVM[memMXIndex]).append(",").append(memMXBeanDataPerVM[memMXIndex + 1]).
                            append(",").append(memMXBeanDataPerVM[memMXIndex + 2]).append(",").
                            append(memMXBeanDataPerVM[memMXIndex + 3]).append(",").
                            append(classLoadingMXBeanDataPerVM[classMXIndex]).append(",").
                            append(classLoadingMXBeanDataPerVM[classMXIndex + 1]).append(",").
                            append(classLoadingMXBeanDataPerVM[classMXIndex + 2]).append(",").
                            append(threadMXBeanDataPerVM[threadMXIndex]).append(",").
                            append(threadMXBeanDataPerVM[threadMXIndex + 1]).append(",").
                            append(threadMXBeanDataPerVM[threadMXIndex + 2]).append(",").
                            append(threadMXBeanDataPerVM[threadMXIndex + 3]).append(",");

                    vmCount++;
                    memMXIndex += 4;
                    classMXIndex += 3;
                    threadMXIndex += 4;
                }
            }
        } finally {
            mxBeanStoreLock.unlock();
        }
        return outputBuilder.toString();
    }

    private static String getVmIds() {
        HashMap<Integer, MemoryMXBean> memoryMXBeanMap = MXBeanStore.getMemoryMXBeanMap();
        Iterator<Entry<Integer, MemoryMXBean>> itrMemoryMXBean = memoryMXBeanMap.entrySet().iterator();

        StringBuilder vmIdBuffer = new StringBuilder();
        while (itrMemoryMXBean.hasNext()) {
            Entry<Integer, MemoryMXBean> tempRow = itrMemoryMXBean.next();
            vmIdBuffer.append(tempRow.getKey()).append(",");
        }
        return (vmIdBuffer.length() == 0) ? null : vmIdBuffer.toString();
    }

    private static String memMXBeanDataUtil() {
        StringBuilder memMXBeanOutput = new StringBuilder();

        HashMap<Integer, MemoryMXBean> memoryMXBeanMap = MXBeanStore.getMemoryMXBeanMap();
        Iterator<Entry<Integer, MemoryMXBean>> itrMemoryMXBean = memoryMXBeanMap.entrySet().iterator();

        while (itrMemoryMXBean.hasNext()) {
            Entry<Integer, MemoryMXBean> tempRow = itrMemoryMXBean.next();
            MemoryMXBean memMXBean = tempRow.getValue();
            Integer vmId = tempRow.getKey();
            try {

                MemoryUsage heapMemUsage = memMXBean.getHeapMemoryUsage();
                MemoryUsage nonHeapMemUsage = memMXBean.getNonHeapMemoryUsage();

                long usedHeap = heapMemUsage.getUsed() / (1024 * 1024); // in MB
                long committedHeap = heapMemUsage.getCommitted() / (1024 * 1024); // in MB

                long usedNonHeap = nonHeapMemUsage.getUsed() / (1024 * 1024); // in MB
                long committedNonHeap = nonHeapMemUsage.getCommitted() / (1024 * 1024); // in MB

                memMXBeanOutput.append(usedHeap).append(",").append(committedHeap).append(",").append(usedNonHeap).
                        append(",").append(committedNonHeap).append(",");

            } catch (Exception e) {
                memMXBeanOutput.append("0,0,0,0,");
                itrMemoryMXBean.remove();
                Main.logger.log(Level.FINE, e.toString(), e);
                Main.logger.log(Level.FINE, "VM with proc_id{0} is dead while reading memory MX Beans", vmId);
            }
        }
        return (memMXBeanOutput.length() == 0) ? null : memMXBeanOutput.toString();
    }

    private static String classLoadingMXBeanDataUtil() {
        StringBuilder classLoadingMXBeanOutput = new StringBuilder();

        HashMap<Integer, ClassLoadingMXBean> classLoadingMXBeanMap = MXBeanStore.getClassLoadingMXBeanMap();
        Iterator<Entry<Integer, ClassLoadingMXBean>> itrClassLoadingMXBean = classLoadingMXBeanMap.entrySet().iterator();

        while (itrClassLoadingMXBean.hasNext()) {
            Entry<Integer, ClassLoadingMXBean> tempRow = itrClassLoadingMXBean.next();
            Integer vmId = tempRow.getKey();
            ClassLoadingMXBean classLoadingMXBean = tempRow.getValue();
            try {
                long currentLoadedClasses = classLoadingMXBean.getLoadedClassCount();
                long totalLoadedClasses = classLoadingMXBean.getTotalLoadedClassCount();
                long totalUnLoadedClasses = classLoadingMXBean.getUnloadedClassCount();

                classLoadingMXBeanOutput.append(currentLoadedClasses).append(",").append(totalLoadedClasses).
                        append(",").append(totalUnLoadedClasses).append(",");
            } catch (Exception e) {
                classLoadingMXBeanOutput.append("0,0,0,");
                itrClassLoadingMXBean.remove();
                Main.logger.log(Level.FINE, e.toString(), e);
                Main.logger.log(Level.FINE, "VM with proc_id{0} is dead while reading classLoading MX Beans", vmId);
            }
        }
        return (classLoadingMXBeanOutput.length() == 0) ? null : classLoadingMXBeanOutput.toString();
    }

    private static String threadMXBeanDataUtil() {
        StringBuilder threadMXBeanOutput = new StringBuilder();

        HashMap<Integer, java.lang.management.ThreadMXBean> threadMXBeanMap = MXBeanStore.getThreadMXBeanMap();
        Iterator<Entry<Integer, java.lang.management.ThreadMXBean>> itrThreadMXBean = threadMXBeanMap.entrySet().iterator();

        while (itrThreadMXBean.hasNext()) {
            Entry<Integer, java.lang.management.ThreadMXBean> tempRow = itrThreadMXBean.next();
            Integer vmId = tempRow.getKey();
            java.lang.management.ThreadMXBean threadMXBean = tempRow.getValue();
            try {
                int daemonThreadCount = threadMXBean.getDaemonThreadCount(); // current live daemon threads
                int peakThreadCount = threadMXBean.getPeakThreadCount();
                int currentThreadCount = threadMXBean.getThreadCount(); // current thread count (live + daemon)
                long totalStartedThreadCount = threadMXBean.getTotalStartedThreadCount(); // total number of threads started

                threadMXBeanOutput.append(daemonThreadCount).append(",").append(peakThreadCount).append(",").
                        append(currentThreadCount).append(",").append(totalStartedThreadCount).append(",");

            } catch (Exception e) {
                threadMXBeanOutput.append("0,0,0,0,");
                itrThreadMXBean.remove();
                Main.logger.log(Level.FINE, e.toString(), e);
                Main.logger.log(Level.FINE, "VM with proc_id{0} is dead while reading thread MX Beans", vmId);
            }
        }
        return (threadMXBeanOutput.length() == 0 ? null : threadMXBeanOutput.toString());
    }
}
