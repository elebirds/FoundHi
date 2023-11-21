package net.mcpes.foundhi.core.app.command;

/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/4/10
 * version 1.0
 */
public class PacketCommandSender implements CommandSender {
    private int result = 0;

    @Override
    public void sendMessage(String message) {

    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    @Override
    public boolean isOp() {
        return true;
    }

    @Override
    public void setOp(boolean value) {

    }

    @Override
    public boolean isPlayer() {
        return false;
    }

    @Override
    public String getName() {
        return "PacketCommandSender";
    }
}
