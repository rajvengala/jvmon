package in.uglyhunk.jvm.mon;

import java.io.File;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.util.logging.Level;

public class FileOutputFormat implements OutputFormat {

    @Override
    public void writeOutput(String output) {
        
        try {
            if (LogRotator.getCSVLogFlag()) {
                createCSVFile(LogRotator.getNewTimeStamp());
                LogRotator.setCSVLogFlag(false);
            }
            
            String[] vmCounters = output.split(";");
            for(String countersPerVM : vmCounters)
                csvLogFile.println(countersPerVM);
            
        } catch (Exception e) {
            Main.logger.log(Level.SEVERE, e.toString(), e);
            System.exit(1);
        }
       
    }

    public static void createCSVFile(String timestamp) {
       
        try {
            csvFileLoc = System.getProperty("user.dir") + File.separator + "jvmon_" + timestamp + ".csv";
            csvLogFile = new PrintWriter(new FileWriter(csvFileLoc), true);
            Main.logger.info("========================================");
            Main.logger.log(Level.INFO, "CSV file location - {0}", csvFileLoc);
            Main.logger.info("========================================");
            csvLogFile.println(header);
      
        } catch (Exception e) {
            Main.logger.log(Level.SEVERE, e.toString(), e);
            System.exit(1);
        }
       
    }

    private static PrintWriter csvLogFile = null;
    private static String csvFileLoc;
    private static String header = "timestamp,proc_id," + 
                                    "used_heap(MB),comm_heap(MB),used_non-heap(MB),comm_non-heap(MB)," +
                                    "curr_loaded_classes,tot_loaded_classes,tot_unloaded_classes," + 
                                    "daemon_thrd_count,peak_thrd_count,current_thrd_count,total_started_thrd_count," + 
                                    "tot_compilation_time,";
          
}
