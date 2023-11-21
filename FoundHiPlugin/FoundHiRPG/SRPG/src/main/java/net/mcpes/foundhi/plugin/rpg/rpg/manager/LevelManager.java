package net.mcpes.foundhi.plugin.rpg.rpg.manager;

import cn.nukkit.Server;
import cn.nukkit.command.ConsoleCommandSender;

import java.util.List;
import java.util.Map;

/**
 * @author yui
 * @date 2018/2/11
 * @since RPG
 */

@SuppressWarnings("unchecked")
public class LevelManager {
    private int level;
    private int exp;
    private String prefix;
    private int healthAdd;
    private int blueAdd;
    private List<String> commands;

    public LevelManager(int level, Map<String, Object> data) {
        this.level = level;
        this.commands = (List<String>) data.get("Commands");
        this.prefix = (String) data.get("prefix");
        this.exp = (int) data.get("EXP");
        this.healthAdd = (int) data.get("healthAdd");
        this.blueAdd = (int) data.get("blueAdd");
    }

    public void dispatchCommands(String playerName) {
        for (String command : this.getCommands()) {
            if (command.contains("@player")) {
                command = command.replace("@player", playerName);
            }
            Server.getInstance().dispatchCommand(new ConsoleCommandSender(), command);
        }
    }

    public int getBlueAdd() {
        return blueAdd;
    }

    public int getHealthAdd() {
        return healthAdd;
    }

    public List<String> getCommands() {
        return commands;
    }

    public int getLevel() {
        return level;
    }

    public int getExp() {
        return exp;
    }

    public String getPrefix() {
        return prefix;
    }
}
