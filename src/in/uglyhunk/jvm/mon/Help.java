package in.uglyhunk.jvm.mon;

import java.io.File;

public class Help {

    public static void printUsage() {
        
        System.out.println("Usage : " + newline + newline +
                           " java -cp " + classPath + "<options>:{<console_output>} <frequency> <num_of_updates> <vmdesc1,...> <vmscan_rate>" + newline);
        
        System.out.println("where, " + newline +
                           " options - q/f/b" + newline +
                           "    q - list the running JVMs" + newline +
                           "    f - output only to file" + newline +
                           "    b - output to console and file" + newline +
                           "    console_output - m/t/c/g (console_output sub-switch will be ignored if options switch is \"q or f\")" + newline +
                           "        m - memory (heap + non-heap)" + newline +
                           "        t - threads" + newline +
                           "        c - classes" + newline +
                           "        n - compilation " + newline +
                           "        o - garbage collection and memory pools" + newline +
                           "        defaults to memory if console_output is omitted" + newline + newline +
                           " frequency - metrics ready frequency" + newline + newline +
                           " num_of_updates - number of readings" + newline + newline +
                           " vmdesc1,vmdesc2,... - description of JVM(s) to scan for" + newline + newline +
                           " vmscan_rate - scan frequency for JVMs matching descriptions in arg4" + newline);

        System.out.println("Examples: " + newline + newline +
                           " 1. Query JVMs running in the system" + newline +
                           "\t java " + classPath + " in.uglyhunk.jvm.mon.Main q" + newline + newline +
                           " 2. Monitor all JVMs with description oracle.oats and jconsole every 15 seconds for an hour while scanning for new JVMs every minute" + newline +
                           "\t java " + classPath + " in.uglyhunk.jvm.mon.Main f 15 240 oracle.oats,jconsole 60" + newline + newline);
    }
    
    public static String newline = System.getProperty("line.separator");
    
    public static String classPath = "." + File.pathSeparator + "jvmon.jar" + File.pathSeparator + "<JDK_HOME>" +
                                     File.separator + "lib" + File.separator + "tools.jar in.uglyhunk.jvm.mon.Main ";
}
