package in.uglyhunk.jvm.mon;

public class ConsoleOutputFormat implements OutputFormat {

    @Override
    public void writeOutput(String output) {
        if (firstTime) {
            System.out.printf("\n%-10s %-7s %-6s %-6s %-11s %-11s %-12s %-12s %-13s %-6s %-7s %-8s %-12s",
                    COL1, COL2, COL3, COL4, COL5, COL6, COL7,
                    COL8, COL9, COL10, COL11, COL12, COL13);
            firstTime = false;
        }

        String[] vmCounters = output.split(",");
        for (int i = 1; i < vmCounters.length; i = i + 12) {
            System.out.println();
            System.out.printf("%-10s %-7s %-6s %-6s %-11s %-11s %-12s %-12s %-13s %-6s %-7s %-8s %-12s",
                    vmCounters[0],
                    vmCounters[i + 0],
                    vmCounters[i + 1],
                    vmCounters[i + 2],
                    vmCounters[i + 3],
                    vmCounters[i + 4],
                    vmCounters[i + 5],
                    vmCounters[i + 6],
                    vmCounters[i + 7],
                    vmCounters[i + 8],
                    vmCounters[i + 9],
                    vmCounters[i + 10],
                    vmCounters[i + 11]);
        }
        System.out.println();
    }
    private boolean firstTime = true;
    private static final String COL1 = "timestamp";
    private static final String COL2 = "proc_id";
    private static final String COL3 = "usd_hp";
    private static final String COL4 = "com_hp";
    private static final String COL5 = "usd_non-hp";
    private static final String COL6 = "com_non-hp";
    private static final String COL7 = "cur_ld_clas";
    private static final String COL8 = "tot_ld_clas";
    private static final String COL9 = "tot_uld_clas";
    private static final String COL10 = "d_thd";
    private static final String COL11 = "pk_thd";
    private static final String COL12 = "cur_thd";
    private static final String COL13 = "tot_std_thd";
}
