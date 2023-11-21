package net.mcpes.foundhi.core.utils;

import net.mcpes.foundhi.core.app.FoundHiAPIMain;
import net.mcpes.foundhi.core.scheduler.AsyncTask;
import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author Nukkit team
 * @date 2017/7/6
 * @since SummitSynchronization
 */

public class MainLogger extends AsyncTask {
    private static MainLogger i;
    protected final String logPath;
    protected final ConcurrentLinkedQueue<String> logBuffer = new ConcurrentLinkedQueue<>();
    private final Map<TextFormat, String> replacements = new EnumMap<>(TextFormat.class);
    private final TextFormat[] colors = TextFormat.values();
    protected boolean shutdown;

    public MainLogger() {
        i = this;
        this.logPath = new File(FoundHiAPIMain.INSTANCE.getDATA_PATH() + "/foundhilog.log").getAbsolutePath();
    }

    public static MainLogger getLogger() {
        return i;
    }

    @Override
    public void onRun() {
        AnsiConsole.systemInstall();
        File logFile = new File(logPath);
        if (!logFile.exists()) {
            try {
                logFile.createNewFile();
            } catch (IOException e) {
                this.logException(e);
            }
        } else {
            long date = logFile.lastModified();
            String newName = new SimpleDateFormat("Y-M-d HH.mm.ss").format(new Date(date)) + ".log";
            File oldLogs = new File(FoundHiAPIMain.INSTANCE.getDATA_PATH(), "logs");
            if (!oldLogs.exists()) {
                oldLogs.mkdirs();
            }
            logFile.renameTo(new File(oldLogs, newName));
            logFile = new File(logPath);
            if (!logFile.exists()) {
                try {
                    logFile.createNewFile();
                } catch (IOException e) {
                    this.logException(e);
                }
            }
        }
        replacements.put(TextFormat.BLACK, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.BLACK).boldOff().toString());
        replacements.put(TextFormat.DARK_BLUE, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.BLUE).boldOff().toString());
        replacements.put(TextFormat.DARK_GREEN, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.GREEN).boldOff().toString());
        replacements.put(TextFormat.DARK_AQUA, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.CYAN).boldOff().toString());
        replacements.put(TextFormat.DARK_RED, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.RED).boldOff().toString());
        replacements.put(TextFormat.DARK_PURPLE, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.MAGENTA).boldOff().toString());
        replacements.put(TextFormat.GOLD, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.YELLOW).boldOff().toString());
        replacements.put(TextFormat.GRAY, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.WHITE).boldOff().toString());
        replacements.put(TextFormat.DARK_GRAY, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.BLACK).bold().toString());
        replacements.put(TextFormat.BLUE, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.BLUE).bold().toString());
        replacements.put(TextFormat.GREEN, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.GREEN).bold().toString());
        replacements.put(TextFormat.AQUA, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.CYAN).bold().toString());
        replacements.put(TextFormat.RED, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.RED).bold().toString());
        replacements.put(TextFormat.LIGHT_PURPLE, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.MAGENTA).bold().toString());
        replacements.put(TextFormat.YELLOW, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.YELLOW).bold().toString());
        replacements.put(TextFormat.WHITE, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.WHITE).bold().toString());
        replacements.put(TextFormat.BOLD, Ansi.ansi().a(Ansi.Attribute.UNDERLINE_DOUBLE).toString());
        replacements.put(TextFormat.STRIKETHROUGH, Ansi.ansi().a(Ansi.Attribute.STRIKETHROUGH_ON).toString());
        replacements.put(TextFormat.UNDERLINE, Ansi.ansi().a(Ansi.Attribute.UNDERLINE).toString());
        replacements.put(TextFormat.ITALIC, Ansi.ansi().a(Ansi.Attribute.ITALIC).toString());
        replacements.put(TextFormat.RESET, Ansi.ansi().a(Ansi.Attribute.RESET).toString());
        do {
            flushBuffer(logFile);
        } while (!this.shutdown);
        flushBuffer(logFile);
    }

    private void flushBuffer(File logFile) {
        if (logBuffer.isEmpty()) {
            try {
                synchronized (this) {
                    wait(25000); // Wait for next message
                }
                Thread.sleep(5); // Buffer for 5ms to reduce back and forth between disk
            } catch (InterruptedException ignore) {
            }
        }
        try {
            Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(logFile, true), StandardCharsets.UTF_8), 1024);
            Date now = new Date();
            String fileDateFormat = new SimpleDateFormat("Y-M-d HH:mm:ss ").format(now);
            while (!logBuffer.isEmpty()) {
                String message = logBuffer.poll();
                if (message != null) {
                    writer.write(fileDateFormat);
                    writer.write(TextFormat.clean(message));
                    writer.write("\r\n");
                }
            }
            writer.flush();
            writer.close();
        } catch (Exception e) {
            this.logException(e);
        }
    }

    public void logException(Exception e) {
        this.alert(Utils.getExceptionMessage(e));
    }

    public void out(String msg) {
        Date now = new Date();
        String consoleDateFormat = new SimpleDateFormat("HH:mm:ss ").format(now);
        logBuffer.add(msg);
        System.out.println(colorize(TextFormat.AQUA + consoleDateFormat + TextFormat.RESET + msg + TextFormat.RESET));
    }

    private String colorize(String string) {
        if (string.indexOf(TextFormat.ESCAPE) < 0) {
            return string;
        } else if (FoundHiAPIMain.INSTANCE.getANSI()) {
            for (TextFormat color : colors) {
                if (replacements.containsKey(color)) {
                    string = string.replaceAll("(?i)" + color, replacements.get(color));
                } else {
                    string = string.replaceAll("(?i)" + color, "");
                }
            }
        } else {
            return TextFormat.clean(string);
        }
        return string + Ansi.ansi().reset();
    }

    public void alert(String message) {
        out(TextFormat.RED + "[ALERT] " + message);
    }

    public void info(String msg) {
        out(TextFormat.WHITE + "[INFO] " + msg);
    }

    public void error(String msg) {
        out(TextFormat.DARK_RED + "[ERROR] " + msg);
    }

    public void debug(String msg) {
        out(TextFormat.GRAY + "[DEBUG] " + msg);
    }

    public void warning(String msg) {
        out(TextFormat.YELLOW + "[WARNING] " + msg);
    }

    public void notice(String msg) {
        out(TextFormat.AQUA + "[NOTICE] " + msg);
    }

    public void critical(String message) {
        out(TextFormat.RED + "[CRITICAL] " + message);
    }

    public void critical(String message, Throwable t) {
        this.critical(message + "\r\n" + Utils.getExceptionMessage(t));
    }
}