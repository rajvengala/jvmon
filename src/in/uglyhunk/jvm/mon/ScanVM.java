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
import java.util.logging.Level;

public class ScanVM extends TimerTask {

    public ScanVM(String targetVMDesc) {
        this.targetVMDescs = targetVMDesc.split(",");
    }

    @Override
    public void run() {
        try {
            HashMap<Integer, MemoryMXBean> memoryMXBeanMap = MXBeanStore.getMemoryMXBeanMap();
            HashMap<Integer, ClassLoadingMXBean> classLoadingMXBeanMap = MXBeanStore.getClassLoadingMXBeanMap();
            HashMap<Integer, ThreadMXBean> threadMXBeanMap = MXBeanStore.getThreadMXBeanMap();

            List<VirtualMachineDescriptor> vmDescs = VirtualMachine.list();

            Lock mxBeanStoreLock = MXBeanStore.getMXBeanStoreLock();
            mxBeanStoreLock.lock();
            try {
                for (VirtualMachineDescriptor vmDesc : vmDescs) {
                    String vmDisplayName = vmDesc.displayName();
                    for (String targetVMDesc : targetVMDescs) {
                        if (vmDisplayName.contains(targetVMDesc) && !vmDisplayName.contains(localVMDesc)) {
                            Integer vmId = Integer.parseInt(vmDesc.id());
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

                                ObjectName memoryMXObject = new ObjectName(ManagementFactory.MEMORY_MXBEAN_NAME);
                                MemoryMXBean memoryMXBean = ManagementFactory.newPlatformMXBeanProxy(mbsc,
                                        memoryMXObject.toString(), MemoryMXBean.class);

                                ObjectName classLoadingMXObject = new ObjectName(ManagementFactory.CLASS_LOADING_MXBEAN_NAME);
                                ClassLoadingMXBean classLoadingMXBean = ManagementFactory.newPlatformMXBeanProxy(mbsc,
                                        classLoadingMXObject.toString(), ClassLoadingMXBean.class);

                                ObjectName threadMXObject = new ObjectName(ManagementFactory.THREAD_MXBEAN_NAME);
                                ThreadMXBean threadMXBean = ManagementFactory.newPlatformMXBeanProxy(mbsc,
                                        threadMXObject.toString(), ThreadMXBean.class);

                                memoryMXBeanMap.put(vmId, memoryMXBean);
                                classLoadingMXBeanMap.put(vmId, classLoadingMXBean);
                                threadMXBeanMap.put(vmId, threadMXBean);
                            }
                        }
                    }
                }
            } finally {
                mxBeanStoreLock.unlock();
            }

        } catch (Exception e) {
            Main.logger.info("Could not connect to target VM(s). Please check the log file for details");
            Main.logger.log(Level.FINE, e.toString(), e);
        }
    }
    private String targetVMDescs[] = null;
    private String localVMDesc = "in.uglyhunk.jvm.mon.Main";
}
