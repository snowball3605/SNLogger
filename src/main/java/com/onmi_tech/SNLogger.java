package com.onmi_tech;

import java.io.*;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

/**
 * A simple colored logging utility for Java console output.
 * Supports different log levels and ANSI-colored output.
 */
public class SNLogger {
    // ANSI escape codes
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_CYAN = "\u001B[36m";

    private final PrintStream fileOut;
    private final PrintStream originalOut;

    private final LogLevel minLevel;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Constructs a CustomLogger with a minimum log level.
     * @param minLevel the minimum log level to display
     */
    public SNLogger(LogLevel minLevel) {
        this.minLevel = minLevel;

        String time = new SimpleDateFormat("yyyyMMdd_HH").format(Calendar.getInstance().getTime());
        try {
            fileOut = new PrintStream(new FileOutputStream("output_" + time + ".log", true), true);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        originalOut = System.out;

        final String ansiRegex = "\u001B\\[[;\\d]*m";
        // 同時輸出 Console + File
        PrintStream dualOut = new PrintStream(new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                originalOut.write(b);
                fileOut.write(b);
            }

            @Override
            public void write(byte[] b, int off, int len) throws IOException {
                String s = new String(b, off, len);
                // Console 原樣輸出
                originalOut.print(s);
                // 檔案去掉顏色碼再輸出
                String clean = s.replaceAll(ansiRegex, "");
                fileOut.print(clean);
            }

            @Override
            public void flush() throws IOException {
                originalOut.flush();
                fileOut.flush();
            }
        }, true);

        System.setOut(dualOut);
    }

    /**
     * Logs a message at the specified level with appropriate color.
     * @param level the log level
     * @param message the message to log
     */
    public void log(LogLevel level, String message) {
        if (level.getLevel() >= minLevel.getLevel()) {
            String timestamp = LocalDateTime.now().format(formatter);
            String color = getColor(level);
            String path = SNLogger.class.getProtectionDomain().getCodeSource().getLocation().getPath();
            try {
                String d = URLDecoder.decode(path, "UTF-8");
                String file = d.substring(d.lastIndexOf("/") + 1);
                System.out.println(d);
                String logMessage = String.format("%s[%s] %s: %s%s", color, timestamp, level, message, ANSI_RESET);

                System.out.println(logMessage);
                fileOut.flush();
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Determines the ANSI color code based on the log level.
     * @param level the log level
     * @return the ANSI color code
     */
    private String getColor(LogLevel level) {
        switch (level) {
            case DEBUG:
                return ANSI_CYAN;
            case INFO:
                return ANSI_GREEN;
            case WARN:
                return ANSI_YELLOW;
            case ERROR:
                return ANSI_RED;
            default:
                return ANSI_RESET;
        }
    }

    /**
     * Logs a message at DEBUG level.
     * @param message the message to log
     */
    public void debug(String message) {
        log(LogLevel.DEBUG, message);
    }

    /**
     * Logs a message at INFO level.
     * @param message the message to log
     */
    public void info(String message) {
        log(LogLevel.INFO, message);
    }

    /**
     * Logs a message at WARN level.
     * @param message the message to log
     */
    public void warn(String message) {
        log(LogLevel.WARN, message);
    }

    /**
     * Logs a message at ERROR level.
     * @param message the message to log
     */
    public void error(String message) {
        log(LogLevel.ERROR, message);
    }
}