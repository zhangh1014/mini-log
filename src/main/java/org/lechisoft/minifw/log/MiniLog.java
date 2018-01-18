package org.lechisoft.minifw.log;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.PropertyConfigurator;

public class MiniLog {
    private static final String DEFAULT_LOGGER = "syslogger";
    private static Map<String, Log> mapLogs = null;

    static {
        mapLogs = new HashMap<String, Log>();
    }

    public static void configure() {
        // find log4j.properties in classpath
        String classPath = MiniLog.class.getResource("/").getPath();
        String filePath = getLog4jProperties(classPath);
        if ("".equals(filePath)) {
            MiniLog.error("can not find log4j.properties.");
        } else {
            MiniLog.debug("load " + filePath + ".");

            URL url = MiniLog.class.getResource(filePath);
            // commons.logging会自动扫描classpath下log4j.jar，并加载log4j.properties
            // 这个行为早于ServletContext的启动，而ServletContext启动时会设置log4j的日志输出路径
            // 重新加载配置
            PropertyConfigurator.configure(url);
            mapLogs = new HashMap<String, Log>();
        }
    }

    private static Log getLog(String name) {
        Log log = null;
        if (null == mapLogs.get(name)) {
            log = LogFactory.getLog(name);
            mapLogs.put(name, log);
        } else {
            log = mapLogs.get(name);
        }
        return log;
    }

    private static String getLog4jProperties(String path) {        
        File file = new File(path);
        if (!file.exists()) {
            return "";
        }

        if (file.isFile() && "log4j.properties".equals(file.getName())) {
            String filepath = file.getPath();
            String pattern = "^.*\\\\classes(.*log4j\\.properties)$";
            Pattern r = Pattern.compile(pattern);
            Matcher m = r.matcher(filepath);
            if (m.find()) {
                return m.group(1).replace("\\", "/");
            }
            return "";
        }

        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File file2 : files) {
                String val = getLog4jProperties(file2.getAbsolutePath());
                if (!"".equals(val)) {
                    return val;
                }
            }
        }
        return "";
    }

    /**
     * 输出调试日志
     * 
     * @param 日志内容
     */
    public static void debug(String name, String msg) {
        debug(name, msg, null);
    }

    /**
     * 输出调试日志
     * 
     * @param 日志内容
     * @param 异常对象
     */
    public static void debug(String name, String msg, Throwable t) {
        msg = Thread.currentThread().getId() + " ~_~ " + msg;
        if (t == null) {
            getLog(name).debug(msg);
        } else {
            getLog(name).debug(msg, t);
        }
    }

    public static void debug(String msg) {
        debug(DEFAULT_LOGGER, msg, null);
    }

    public static void debug(String msg, Throwable t) {
        debug(DEFAULT_LOGGER, msg, t);
    }

    /**
     * 输出运行日志
     * 
     * @param 日志内容
     * @param 异常对象
     */
    public static void info(String name, String msg, Throwable t) {
        msg = Thread.currentThread().getId() + " ^_^ " + msg;
        if (t == null) {
            getLog(name).info(msg);
        } else {
            getLog(name).info(msg, t);
        }
    }

    public static void info(String name, String msg) {
        info(name, msg, null);
    }

    public static void info(String msg, Throwable t) {
        info(DEFAULT_LOGGER, msg, t);
    }

    public static void info(String msg) {
        info(DEFAULT_LOGGER, msg, null);
    }

    /**
     * 输出警告日志
     * 
     * @param 日志内容
     * @param 异常对象
     */
    public static void warn(String name, String msg, Throwable t) {
        msg = Thread.currentThread().getId() + " →_→ " + msg;
        if (t == null) {
            getLog(name).warn(msg);
        } else {
            getLog(name).warn(msg, t);
        }
    }

    public static void warn(String name, String msg) {
        warn(name, msg, null);
    }

    public static void warn(String msg, Throwable t) {
        warn(DEFAULT_LOGGER, msg, t);
    }

    public static void warn(String msg) {
        warn(DEFAULT_LOGGER, msg, null);
    }

    /**
     * 输出错误日志
     * 
     * @param 日志内容
     * @param 异常对象
     */
    public static void error(String name, String msg, Throwable t) {
        msg = Thread.currentThread().getId() + " >_< " + msg;
        if (t == null) {
            getLog(name).error(msg);
        } else {
            getLog(name).error(msg, t);
        }
    }

    public static void error(String name, String msg) {
        warn(name, msg, null);
    }

    public static void error(String msg, Throwable t) {
        error(DEFAULT_LOGGER, msg, t);
    }

    public static void error(String msg) {
        error(DEFAULT_LOGGER, msg, null);
    }

    /**
     * 输出严重错误日志
     * 
     * @param 日志内容
     * @param 异常对象
     */
    public static void fatal(String logger, String msg, Throwable t) {
        msg = Thread.currentThread().getId() + " >﹏<  " + msg;
        if (t == null) {
            getLog(logger).fatal(msg);
        } else {
            getLog(logger).fatal(msg, t);
        }
    }

    public static void fatal(String name, String msg) {
        fatal(name, msg, null);
    }

    public static void fatal(String msg, Throwable t) {
        fatal(DEFAULT_LOGGER, msg, t);
    }

    public static void fatal(String msg) {
        fatal(DEFAULT_LOGGER, msg, null);
    }

}
