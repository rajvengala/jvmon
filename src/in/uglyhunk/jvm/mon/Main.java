/**
 * 
 */
package in.uglyhunk.jvm.mon;

import java.io.File;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Timer;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * @author uglyhunk
 *
 */
public class Main {

    /**
     * @param args
     */
    public static void main(String[] args) {
        try {
      
            createHandlers();
            validateArgs(args);

            Date date = new Date();
            String timestamp = new SimpleDateFormat("dd_MMM_yyyy_HH_mm_ss").format(date);
            String csvFilePath = FileOutputFormat.createCSVFile(timestamp);

            // initialise VM scanner thread
            new Timer().schedule(new ScanVM(targetVMDesc), 500, vmScanFrequency);
            
            // schedule LogRotator to run every 24 hours
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
            int minOfHour = calendar.get(Calendar.MINUTE);
            long initLogRotatorDelay = ((60 - minOfHour) + ((23 - hourOfDay) * 60)) * 60 * 1000;
            new Timer().scheduleAtFixedRate(new LogRotator(), initLogRotatorDelay, dayInMSec);
            
            logger.log(Level.FINE, "Next log rotator in {0} msec.", initLogRotatorDelay);

            while (MXBeanStore.getMemoryMXBeanMap().isEmpty()) {
                logger.log(Level.INFO, "Searching for JVMs...");
                Thread.sleep(5000);
            }
            logger.log(Level.INFO, "Monitoring in progres...");

            if (!quietMode) {
                co = new ConsoleOutputFormat(consoleVMSubSystem);
            }

            while (true) {
                String output = VMDataUtil.getVMInfo();
                if (output.length() > 1) {
                    fo.writeOutput(output);
                    if (!quietMode) {
                        co.writeOutput(output);
                    }
                }

                if (updateCounter > updateLimit) {
                    break;
                } else {
                    updateCounter++;
                }

                Thread.sleep(updateFrequency);
            }
            
            // convert csv file to js for graphing
            CSV2JS.convert(csvFilePath);
            
        } catch (NumberFormatException nfe) {
            logger.log(Level.SEVERE, nfe.toString(), nfe);
            System.exit(1);
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.toString(), e);
            System.exit(1);
        }
        
        
        logger.log(Level.INFO, "Complete");
        System.exit(0);
    }

    private static void validateArgs(String[] args) {
      
        
        int argsSize = args.length;
        logger.log(Level.FINE, "Argument size - {0}", argsSize);
        
        if ((argsSize > 5) || ((argsSize > 2) && (argsSize < 5))) {
            logger.severe("Incorrect number of arguments");
            System.exit(0);
        }

        if (argsSize == 5) {
            // process arg1
            String[] options = args[0].split(":");
            if(options.length <= 2) {
                if (options[0].equalsIgnoreCase("b")) {
                    quietMode = false;
                } else if(!options[0].equalsIgnoreCase("f")) {
                    logger.severe("Invalid argument, options should be \'b\' or \'f\'");
                    System.exit(0);
                }
            }
            if(options.length == 2){
                if(options[1].length() == 1) {
                    consoleVMSubSystem = options[1];
                } else {
                    logger.severe("Invalid argument, console_subsystem should be one of \'m\', \'c\', \'t\', \'n\', \'o\'");
                    System.exit(0);
                }
            }
            
            // process arg2
            int arg1 = Integer.parseInt(args[1]);
            if (arg1 > 0) {
                updateFrequency = arg1 * 1000;
            }

            // process arg3
            int arg2 = Integer.parseInt(args[2]);
            if (arg2 > 0) {
                updateLimit = arg2;
            }

            // process arg4
            targetVMDesc = args[3];

            // process arg5
            int arg4 = Integer.parseInt(args[4]);
            if (arg4 > 0) {
                vmScanFrequency = arg4 * 1000;
            }

            return;
        }

        if ((argsSize == 1) && (args[0].equalsIgnoreCase("q"))) {
            RunningJVMs.printList();
            System.exit(0);
        } else {
            logger.severe("argument to query should be \'q\'");
            System.exit(0);
        }

        if (argsSize == 0) {
            Help.printUsage();
            System.exit(0);
        }
        
    }
    
    private static void createHandlers(){
        
        logger.setUseParentHandlers(false);
        logger.setLevel(Level.FINE);
        
        consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(Level.INFO); 
        logger.addHandler(consoleHandler);
        
        
        try{
            String hostname = InetAddress.getLocalHost().getHostName();
            String errLogFileDir = System.getProperty("user.dir") + File.separator + ".." + File.separator + "logs" + File.separator;
            fileHandler = new FileHandler(errLogFileDir + hostname + "_jvmon_err_%g.log", MAX_BYTES, MAX_FILES);
            System.out.println("Error log directory - " + errLogFileDir);
            
        } catch(Exception e){
            logger.severe(e.toString());
            System.exit(1);
        }
         
        // set this to FINE in production
        fileHandler.setLevel(Level.FINE);
        fileHandler.setFormatter(new SimpleFormatter());
        logger.addHandler(fileHandler);
   
    }
    
    private static ConsoleHandler consoleHandler;
    private static FileHandler fileHandler;
    public static final Logger logger = Logger.getLogger("in.uglyhunk.jvm.mon");
    private static boolean quietMode = true; // quiet mode - only to file
    private static int updateFrequency = 5 * 1000; // milliseconds
    private static int updateLimit = 720; // max updates, 1 hour
    private static String targetVMDesc = "java"; // all jvm processes
    private static long vmScanFrequency = 30 * 1000; // milliseconds
    private static int updateCounter = 1;
    private static FileOutputFormat fo = new FileOutputFormat();
    private static ConsoleOutputFormat co;
    private static long dayInMSec = 86400 * 1000;
    private static final int MAX_BYTES = 500 * 1024; // 500 KB
    private static final int MAX_FILES = 10; // max log file count
    private static String consoleVMSubSystem = "h"; // heap counters by default
}
