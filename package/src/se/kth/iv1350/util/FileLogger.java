package se.kth.iv1350.util;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Prints log messages to a file. The log file will be in the current directory
 * and will be called {@code error-log.txt}.
 */
public class FileLogger {
    private PrintWriter logFile;

    /**
     * Creates a new instance and opens the log file. An existing log file will be appended to.
     */
    public FileLogger() {
        try {
            logFile = new PrintWriter(new FileWriter("error-log.txt", true), true);
        } catch (IOException e) {
            System.err.println("Could not create logger: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Writes a log entry describing a thrown exception, including
     * the time it occurred and the full stack trace.
     *
     * @param exception The exception to log.
     */
    public void logException(Exception exception) {
        StringBuilder sb = new StringBuilder();
        sb.append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        sb.append(" - ERROR: ");
        sb.append(exception.getMessage());
        logFile.println(sb);
        exception.printStackTrace(logFile);
        logFile.println();
    }
}
