package net.mcpes.foundhi.core.app.command;

import jline.console.ConsoleReader;
import net.mcpes.foundhi.core.app.APIFunction;
import net.mcpes.foundhi.core.scheduler.AsyncTask;

import java.io.IOException;

;

/**
 * author: MagicDroidX
 * Nukkit
 */
public class CommandReader extends AsyncTask {
    public static CommandReader instance;
    private ConsoleReader reader;
    private boolean running = true;
    private ConsoleCommandSender consoleSender;

    public CommandReader() {
        if (instance != null) {
            throw new RuntimeException("CommandReader is already initialized!");
        }
        try {
            this.consoleSender = new ConsoleCommandSender();
            this.reader = new ConsoleReader();
            reader.setPrompt("> ");
            instance = this;
        } catch (IOException e) {
        }
    }

    public static CommandReader getInstance() {
        return instance;
    }

    public ConsoleCommandSender getConsoleSender() {
        return consoleSender;
    }

    public void shutdown() {
        this.running = false;
    }

    @Override
    public void onRun() {
        String line;
        try {
            while (running) {
                Thread.sleep(50);
                while ((line = reader.readLine()) != null) {
                    if (!line.trim().isEmpty()) {
                        APIFunction.getInstance().dispatchCommand(this.consoleSender, line);
                    }
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
