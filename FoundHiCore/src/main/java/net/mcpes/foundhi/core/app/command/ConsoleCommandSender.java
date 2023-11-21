package net.mcpes.foundhi.core.app.command;


import net.mcpes.foundhi.core.utils.MainLogger;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class ConsoleCommandSender implements CommandSender {

    public boolean isPlayer() {
        return false;
    }

    @Override
    public void sendMessage(String message) {
        for (String line : message.split("\n")) {
            MainLogger.getLogger().info(line);
        }
    }

    @Override
    public String getName() {
        return "CONSOLE";
    }

    @Override
    public boolean isOp() {
        return true;
    }

    @Override
    public void setOp(boolean value) {

    }
}
