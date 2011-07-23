package in.uglyhunk.jvm.mon;

public class ConsoleOutputFormat implements OutputFormat {

    public ConsoleOutputFormat(String consoleSubSystem){
        subSystem = consoleSubSystem;
    }
    
    @Override
    public void writeOutput(String output) {
        if (firstTime) {
            System.out.printf(rowPrefixFormat, header[0], header[1]);
            
            if(subSystem.equalsIgnoreCase("m")) {
                
                System.out.printf(memHeaderFormat, header[2], header[3], header[4], header[5]);
                
            } else if(subSystem.equalsIgnoreCase("c")){
                
                System.out.printf(clasHeaderFormat, header[6], header[7], header[8]); 
                
            } else if(subSystem.equalsIgnoreCase("t")){
                
                System.out.printf(thdHeaderFormat, header[9], header[10], header[11], header[12]);
                
            } else if(subSystem.equalsIgnoreCase("g")){
                
                ;
                
            } else {
                
                System.out.printf(memHeaderFormat, header[2], header[3], header[4], header[5]); // default
                
            }
            firstTime = false;
        }

        String[] vmCounters = output.split(",");
        System.out.println();
        System.out.printf(rowPrefixFormat, vmCounters[0], vmCounters[1]);
        
        if(subSystem.equalsIgnoreCase("m")){
            
            System.out.printf(memHeaderFormat, vmCounters[2], vmCounters[3], vmCounters[4], vmCounters[5]);
        
        } else if(subSystem.equalsIgnoreCase("c")){
            
            System.out.printf(clasHeaderFormat, vmCounters[6], vmCounters[7], vmCounters[8]);
        
        } else if(subSystem.equalsIgnoreCase("t")){
            
            System.out.printf(thdHeaderFormat, vmCounters[9], vmCounters[10], vmCounters[11], vmCounters[12]);
        
        } else {
        
            System.out.printf(memHeaderFormat, vmCounters[2], vmCounters[3], vmCounters[4], vmCounters[5]);
        }
        
        System.out.println();
    }
    private boolean firstTime = true;
    private static String subSystem;
    private static final String rowPrefixFormat = "%-10s %-7s ";
    private static final String memHeaderFormat = "%-6s %-6s %-11s %-11s ";
    private static final String clasHeaderFormat = "%-12s %-12s %-13s ";
    private static final String thdHeaderFormat = "%-6s %-7s %-8s %-12s ";
    private static final String[] header = {"timestamp", "proc_id", // mandatory columns
                                            "usd_hp", "com_hp", "usd_non-hp", "com_non-hp", // memory
                                            "cur_ld_clas", "tot_ld_clas", "tot_uld_clas", // classes
                                            "d_thd", "pk_thd", "cur_thd", "tot_std_thd"}; // threads
    
    
}
