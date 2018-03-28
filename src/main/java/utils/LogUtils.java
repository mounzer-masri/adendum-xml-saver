package utils;

import org.apache.log4j.Logger;
import sun.rmi.runtime.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Imona Andoid on 13.6.2017.
 */
public class LogUtils {
    private LogUtils() {
    }

    public static void info(String msg) {
        Logger logger = Logger.getLogger("");
        logger.info(msg);
    }
    public static void warning(String msg, String fileName) {
        Logger logger = Logger.getLogger(msg);
        logger.warn(msg);
    }
}
