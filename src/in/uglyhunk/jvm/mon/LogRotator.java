package in.uglyhunk.jvm.mon;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimerTask;

public class LogRotator extends TimerTask {

    @Override
    public void run() {
        errLogFlag = true;
        csvLogFlag = true;
        timestamp = new SimpleDateFormat("dd_MMM_yyyy_HH_mm_ss").format(new Date());
    }

    public static boolean getErrLogFlag() {
        return errLogFlag;
    }

    public static boolean getCSVLogFlag() {
        return csvLogFlag;
    }

    public static void setErrLogFlag(boolean flag) {
        errLogFlag = flag;
    }

    public static void setCSVLogFlag(boolean flag) {
        csvLogFlag = flag;
    }

    public static String getNewTimeStamp() {
        return timestamp;
    }
    private static String timestamp;
    private static boolean errLogFlag = false;
    private static boolean csvLogFlag = false;
}
