package org.miea04.core.logs;

/*
 * StyleFormatter
 *
 * @author MieMie
 */
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.*;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.lang.management.ManagementFactory;

public class StyleFormatter extends Formatter {

    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

    private static final String PID = getPid();

    private static String getPid() {
        try {
            String name = ManagementFactory.getRuntimeMXBean().getName();
            return name.split("@")[0];
        } catch (Exception e) {
            return "????";
        }
    }

    public static void initLogging() {
        Logger root = Logger.getLogger("");
        for (Handler h : root.getHandlers()) {
            root.removeHandler(h);
        }

        try (InputStream ins = StyleFormatter.class.getResourceAsStream("/logging.properties")) {
            LogManager.getLogManager().readConfiguration(ins);
        } catch (IOException e) {
            System.err.println("无法加载日志配置文件，文件日志将不可用：" + e.getMessage());
        }

        StreamHandler consoleHandler = new StreamHandler(System.out, new StyleFormatter()) {
            @Override
            public void publish(LogRecord record) {
                super.publish(record);
                flush();
            }
        };
        consoleHandler.setLevel(Level.INFO);
        root.addHandler(consoleHandler);

        root.setLevel(Level.INFO);
    }

    @Override
    public String format(LogRecord record) {
        String timestamp = ZonedDateTime.now().format(DATE_FORMAT);
        String level = String.format("%-5s", record.getLevel().getName());
        String thread = "[" + Thread.currentThread().getName() + "]";

        String methodPath = getMethodPath(record);

        String message = record.getMessage();
        String throwable = "";
        if (record.getThrown() != null) {
            throwable = "\n" + stackTraceToString(record.getThrown());
        }

        return String.format("%s %s %s --- %s %s : %s%s%n",
                timestamp, level, PID, thread, methodPath, message, throwable);
    }

    private String getAbbreviatedLoggerName(String fullName) {
        if (fullName == null || fullName.isEmpty()) {
            return "unknown";
        }
        String[] parts = fullName.split("\\.");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < parts.length - 1; i++) {
            if (!parts[i].isEmpty()) {
                sb.append(parts[i].charAt(0)).append(".");
            }
        }
        sb.append(parts[parts.length - 1]);
        return sb.toString();
    }

    private String getMethodPath(LogRecord record) {
        String className = record.getSourceClassName();
        String methodName = record.getSourceMethodName();

        if (className == null) {
            return "unknown";
        }

        String simpleName = className.substring(className.lastIndexOf('.') + 1);
        String fileName = simpleName + ".java";

        StackTraceElement[] stack = new Throwable().getStackTrace();
        int lineNumber = -1;

        for (StackTraceElement element : stack) {
            if (className.equals(element.getClassName()) &&
                    (methodName == null || methodName.equals(element.getMethodName()))) {
                lineNumber = element.getLineNumber();
                break;
            }
        }

        if (methodName == null) {
            return simpleName;
        }

        if (lineNumber > 0) {
            return String.format("%s.%s(%s:%d)", simpleName, methodName, fileName, lineNumber);
        } else {
            return String.format("%s.%s()", simpleName, methodName);
        }
    }

    private String stackTraceToString(Throwable t) {
        StringBuilder sb = new StringBuilder();
        sb.append(t.toString()).append("\n");
        for (StackTraceElement e : t.getStackTrace()) {
            sb.append("\tat ").append(e).append("\n");
        }
        return sb.toString();
    }
}
