package in.uglyhunk.jvm.mon;

import java.io.File;

public class Help {

    public static void printUsage() {

        System.out.println(newline + "Usage : " + newline + newline
                + " java -cp " + classPath + "{q|[f|b]} [{frequency} {num_of_updates} {vmdesc1,vmdesc2,vmdesc3..} {vmscan_rate}]");

        System.out.println(newline + "where, " + newline
                + " q - list the running JVMs" + newline
                + newline + " f - output only to file" + newline
                + newline + " b - output to console and file" + newline
                + newline + " frequency - metrics ready frequency" + newline
                + newline + " num_of_updates - number of readings" + newline
                + newline + " vmdesc1,vmdesc2,... - description of JVM(s) to scan for" + newline
                + newline + " vmscan_rate - scan frequency for JVMs matching descriptions in arg4" + newline);

        System.out.println("Examples: " + newline + newline
                + " 1. Query JVMs running in the system" + newline
                + "\t java " + classPath + " in.uglyhunk.jvm.mon.Main q" + newline + newline
                + " 2. Monitor all JVMs with description oracle.oats and jconsole every 15 seconds for an hour while scanning for new JVMs every minute" + newline
                + "\t java " + classPath + " in.uglyhunk.jvm.mon.Main f 15 240 oracle.oats,jconsole 60" + newline);
    }
    
    public static String newline = System.getProperty("line.separator");
    public static String classPath = "." + File.pathSeparator + "jvmon.jar" + File.pathSeparator + "<JDK_HOME>"
                                         + File.separator + "lib" + File.separator + "tools.jar in.uglyhunk.jvm.mon.Main ";
}
