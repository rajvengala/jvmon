package in.uglyhunk.jvm.mon;

public class ConsoleOutputFormat implements OutputFormat {

    public ConsoleOutputFormat(String vmSubSystem){
        subSystem = vmSubSystem;
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
                
            } else if(subSystem.equalsIgnoreCase("n")){
                
                System.out.printf(compHeaderFormat, header[13]);
                
            } else if(subSystem.equalsIgnoreCase("o")){
                
                System.out.printf("");
            
            } else {    
            
                System.out.printf(memHeaderFormat, header[2], header[3], header[4], header[5]); // default
            
            }
            firstTime = false;
        }

        String[] countersList = output.split(";");
        for(String countersPerVM : countersList){
            String counters[] = countersPerVM.split(",");
            int totalCounters = counters.length;
            
            System.out.println();
            System.out.printf(rowPrefixFormat, counters[0], counters[1]);

            if(subSystem.equalsIgnoreCase("m")){

                System.out.printf(memHeaderFormat, counters[2], counters[3], counters[4], counters[5]);

            } else if(subSystem.equalsIgnoreCase("c")){

                System.out.printf(clasHeaderFormat, counters[6], counters[7], counters[8]);

            } else if(subSystem.equalsIgnoreCase("t")){

                System.out.printf(thdHeaderFormat, counters[9], counters[10], counters[11], counters[12]);

            } else if(subSystem.equalsIgnoreCase("n")){

                System.out.printf(compHeaderFormat, counters[13]);

            } else if(subSystem.equalsIgnoreCase("o")) {
                for(int i=14; i < totalCounters; i++)
                    System.out.printf(counters[i] + "  ");
            } else {

                System.out.printf(memHeaderFormat, counters[2], counters[3], counters[4], counters[5]);
            }

        }
        System.out.println();
    }
    private boolean firstTime = true;
    private static String subSystem;
    private static final String rowPrefixFormat = "%-10s %-7s ";
    private static final String memHeaderFormat = "%-6s %-6s %-11s %-11s ";
    private static final String clasHeaderFormat = "%-12s %-12s %-13s ";
    private static final String thdHeaderFormat = "%-6s %-7s %-8s %-12s ";
    private static final String compHeaderFormat = "%-12s";
    private static final String[] header = {"timestamp", "proc_id", // mandatory columns
                                            "usd_hp", "com_hp", "usd_non-hp", "com_non-hp", // memory
                                            "cur_ld_clas", "tot_ld_clas", "tot_uld_clas", // classes
                                            "d_thd", "pk_thd", "cur_thd", "tot_std_thd",
                                            "tot_comp_tm"}; // threads
    
    
}
