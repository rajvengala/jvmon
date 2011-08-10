package in.uglyhunk.jvm.mon;

import java.io.File;
import java.lang.management.ClassLoadingMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.ThreadMXBean;
import java.util.HashMap;
import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.locks.Lock;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.attach.VirtualMachineDescriptor;
import java.lang.management.CompilationMXBean;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.OperatingSystemMXBean;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ScanVM extends TimerTask {

    public ScanVM(String targetVMDesc) {
        this.targetVMDescs = targetVMDesc.split(",");
    }

    @Override
    public void run() {

        try {
            HashMap<String, OperatingSystemMXBean> osMXBeanMap = MXBeanStore.getOSMXBeanMap();
            HashMap<String, MemoryMXBean> memoryMXBeanMap = MXBeanStore.getMemoryMXBeanMap();
            HashMap<String, ClassLoadingMXBean> classLoadingMXBeanMap = MXBeanStore.getClassLoadingMXBeanMap();
            HashMap<String, ThreadMXBean> threadMXBeanMap = MXBeanStore.getThreadMXBeanMap();
            HashMap<String, CompilationMXBean> compilationMXBeanMap = MXBeanStore.getCompilationMXBeanMap();
            HashMap<String, ArrayList<GarbageCollectorMXBean>> gcMXBeanMap = MXBeanStore.getGCMXBeanMap();
            HashMap<String, ArrayList<MemoryPoolMXBean>> memPoolMXBeanMap = MXBeanStore.getMemPoolMXBeanMap();

            List<VirtualMachineDescriptor> vmDescriptorList = VirtualMachine.list();

            Lock mxBeanStoreLock = MXBeanStore.getMXBeanStoreLock();
            mxBeanStoreLock.lock();
            try {
                for (VirtualMachineDescriptor vmDesc : vmDescriptorList) {
                    String vmDisplayName = vmDesc.displayName();
                    for (String targetVMDesc : targetVMDescs) {
                        if (vmDisplayName.contains(targetVMDesc) && !vmDisplayName.contains(localVMDesc)) {
                            String vmId = vmDesc.id() + "_" + targetVMDesc;
                            if (!memoryMXBeanMap.containsKey(vmId)) {
                                VirtualMachine vm = VirtualMachine.attach(vmDesc);
                                String localConnectorAddr = vm.getAgentProperties()
                                                            .getProperty("com.sun.management.jmxremote.localConnectorAddress");
                                if (localConnectorAddr == null) {
                                    String javaHome = vm.getSystemProperties().getProperty("java.home");
                                    String agentJar = javaHome + File.separator + "lib" + File.separator + "management-agent.jar";
                                    vm.loadAgent(agentJar);
                                    localConnectorAddr = vm.getAgentProperties()
                                                        .getProperty("com.sun.management.jmxremote.localConnectorAddress");
                                }
                                vm.detach();

                                JMXServiceURL serviceURL = new JMXServiceURL(localConnectorAddr);
                                JMXConnector connector = JMXConnectorFactory.connect(serviceURL);
                                MBeanServerConnection mbsc = connector.getMBeanServerConnection();

                                // os bean
                                ObjectName osMXObject = new ObjectName(ManagementFactory.OPERATING_SYSTEM_MXBEAN_NAME);
                                OperatingSystemMXBean osMXBean = ManagementFactory.newPlatformMXBeanProxy(mbsc,
                                        osMXObject.toString(), OperatingSystemMXBean.class);
                                osMXBeanMap.put(vmId, osMXBean);
                                
                                
                                // memory bean
                                ObjectName memoryMXObject = new ObjectName(ManagementFactory.MEMORY_MXBEAN_NAME);
                                MemoryMXBean memoryMXBean = ManagementFactory.newPlatformMXBeanProxy(mbsc,
                                        memoryMXObject.toString(), MemoryMXBean.class);
                                memoryMXBeanMap.put(vmId, memoryMXBean);
                                
                                // class bean
                                ObjectName classLoadingMXObject = new ObjectName(ManagementFactory.CLASS_LOADING_MXBEAN_NAME);
                                ClassLoadingMXBean classLoadingMXBean = ManagementFactory.newPlatformMXBeanProxy(mbsc,
                                        classLoadingMXObject.toString(), ClassLoadingMXBean.class);
                                classLoadingMXBeanMap.put(vmId, classLoadingMXBean);

                                // thread bean
                                ObjectName threadMXObject = new ObjectName(ManagementFactory.THREAD_MXBEAN_NAME);
                                ThreadMXBean threadMXBean = ManagementFactory.newPlatformMXBeanProxy(mbsc,
                                        threadMXObject.toString(), ThreadMXBean.class);
                                threadMXBeanMap.put(vmId, threadMXBean);
                                
                                // compilation bean
                                ObjectName compilationMXObject = new ObjectName(ManagementFactory.COMPILATION_MXBEAN_NAME);
                                CompilationMXBean compilationMXBean = ManagementFactory.newPlatformMXBeanProxy(mbsc, 
                                         compilationMXObject.toString(), CompilationMXBean.class);
                                compilationMXBeanMap.put(vmId, compilationMXBean);
                                
                                // gc bean
                                ObjectName gcObjectNames = new ObjectName(ManagementFactory.GARBAGE_COLLECTOR_MXBEAN_DOMAIN_TYPE + ",*");
                                ArrayList<GarbageCollectorMXBean> gcMXBeanList = new ArrayList<GarbageCollectorMXBean>();
                                for (ObjectName gcMXObject : mbsc.queryNames(gcObjectNames, null)) {
                                        GarbageCollectorMXBean gcMXBean = ManagementFactory.newPlatformMXBeanProxy(
                                                            mbsc, gcMXObject.getCanonicalName(), GarbageCollectorMXBean.class); 
                                        gcMXBeanList.add(gcMXBean);
                                }
                                gcMXBeanMap.put(vmId, gcMXBeanList);
                                
                                // memory pool bean
                                ArrayList<MemoryPoolMXBean> memPoolMXBeanList = new ArrayList<MemoryPoolMXBean>();
                                ObjectName memPoolObjectNames = new ObjectName(ManagementFactory.MEMORY_POOL_MXBEAN_DOMAIN_TYPE + ",*");
                                for (ObjectName memPoolMXObject : mbsc.queryNames(memPoolObjectNames, null)) {
                                    MemoryPoolMXBean memPoolMXBean = ManagementFactory.newPlatformMXBeanProxy(
                                                                mbsc, memPoolMXObject.getCanonicalName(), MemoryPoolMXBean.class); 
                                
                                    memPoolMXBeanList.add(memPoolMXBean);
                                }
                                memPoolMXBeanMap.put(vmId, memPoolMXBeanList);
                            }
                        }
                    }
                }
            } finally {
                mxBeanStoreLock.unlock();
            }

        } catch (Exception e) {
            System.out.println("Could not connect to target VM(s). Please check the log file for details");
            logger.log(Level.SEVERE, e.toString(), e);
        }
    
    }
    
    private String targetVMDescs[] = null;
    private String localVMDesc = "in.uglyhunk.jvm.mon.Main";
    private static final Logger logger = Main.getLogger();
}
