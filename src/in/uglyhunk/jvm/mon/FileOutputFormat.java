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

            String[] vmCounters = output.split(",");
            for (int i = 1; i < vmCounters.length; i = i + 12) {
                csvLogFile.println(vmCounters[0] + ","
                        + vmCounters[i + 0] + ",,"
                        + vmCounters[i + 1] + ","
                        + vmCounters[i + 2] + ","
                        + vmCounters[i + 3] + ","
                        + vmCounters[i + 4] + ",,"
                        + vmCounters[i + 5] + ","
                        + vmCounters[i + 6] + ","
                        + vmCounters[i + 7] + ",,"
                        + vmCounters[i + 8] + ","
                        + vmCounters[i + 9] + ","
                        + vmCounters[i + 10] + ","
                        + vmCounters[i + 11]);
            }

        } catch (Exception e) {
            Main.logger.log(Level.SEVERE, e.toString(), e);
            System.exit(1);
        }
    }

    public static void createCSVFile(String timestamp) {
        try {
            csvFilePath = System.getProperty("user.dir") + File.separator + "jvmon_" + timestamp + ".csv";
            csvLogFile = new PrintWriter(new FileWriter(csvFilePath), true);
            csvLogFile.println(header);
        } catch (Exception e) {
            Main.logger.log(Level.SEVERE, e.toString(), e);
            System.exit(1);
        }
    }

    public static String getCSVFilePath() {
        return csvFilePath;
    }
    private static PrintWriter csvLogFile = null;
    private static String csvFilePath;
    private static String header = "timestamp,proc_id,,used_heap(MB),comm_heap(MB),used_non-heap(MB),comm_non-heap(MB),,"
            + "curr_loaded_classes,tot_loaded_classes,tot_unloaded_classes,,daemon_thrd_count,"
            + "peak_thrd_count,current_thrd_count,total_started_thrd_count";
}
