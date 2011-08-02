package in.uglyhunk.jvm.mon;

import java.util.List;

import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.attach.VirtualMachineDescriptor;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RunningJVMs {

    public static void printList() {
        try {
            List<VirtualMachineDescriptor> vmdescs = VirtualMachine.list();
            if (!vmdescs.isEmpty()) {

                System.out.println("===========================================================================");
                System.out.println("procId\tdisplayName");
                System.out.println("===========================================================================");
                for (VirtualMachineDescriptor vmdesc : vmdescs) {
                    String displayName = vmdesc.displayName();
                    String procId = vmdesc.id();
                    System.out.println(procId + "\t" + displayName + newline);
                }
            } else {
                logger.log(Level.INFO, "No running JVM instances found{0}", newline);
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString(), e);
        }
    }
    public static String newline = System.getProperty("line.separator");
    private static final Logger logger = Main.getLogger();
}
