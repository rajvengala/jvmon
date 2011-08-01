package in.uglyhunk.jvm.mon;

import java.io.File;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.net.InetAddress;

public class FileOutputFormat implements OutputFormat {

    @Override
    public void writeOutput(String output) throws Exception{

        if (LogRotator.getCSVLogFlag()) {
            createCSVFile(LogRotator.getNewTimeStamp());
            LogRotator.setCSVLogFlag(false);
        }

        String[] vmCounters = output.split(";");
        for(String countersPerVM : vmCounters)
            csvLogFile.println(countersPerVM);
    }

    public static String createCSVFile(String timestamp) throws Exception {
        
        String hostname = InetAddress.getLocalHost().getHostName();
        String csvFileName = hostname + "_jvmon_" + timestamp + ".csv";
        String csvFileLoc = Main.getJVMONLogDir() +  File.separator + csvFileName;
        csvLogFile = new PrintWriter(new FileWriter(csvFileLoc), true);
        System.out.println("CSV file location - " + csvFileLoc);
        csvLogFile.println(header);
        return csvFileName;
    }

    private static PrintWriter csvLogFile = null;
    private static String header = "timestamp,proc_id," + 
                                    "used_heap(MB),comm_heap(MB),used_non-heap(MB),comm_non-heap(MB)," +
                                    "curr_loaded_classes,tot_loaded_classes,tot_unloaded_classes," + 
                                    "daemon_thrd_count,peak_thrd_count,current_thrd_count,total_started_thrd_count," + 
                                    "tot_compilation_time(ms),";
          
}
