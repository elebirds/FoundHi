package net.mcpes.foundhi.plugin.game.bedwars.config;

import cn.nukkit.utils.Config;
import net.mcpes.foundhi.plugin.game.bedwars.SBedWars;

import java.io.File;

/**
 * @author hhm @ SBedWars Project
 **/
public class PlayerConfig {
    public String name;
    private Config config;

    public PlayerConfig(String name) {
        this.name = name;
        this.config = new Config(new File(SBedWars.getInstance().getDataFolder() + "/gamePlayers/" + name + ".yml"), 2);
        this.newPlayer();
    }

    private void newPlayer() {
        if (config.getAll().size() == 0) {
            getConfig().set("win", 0);
            getConfig().set("lose", 0);
            getConfig().set("draw", 0);
            getConfig().set("kill", 0);
            getConfig().set("death", 0);
            getConfig().set("core", 0);
            getConfig().save();
        }
    }

    public Config getConfig() {
        return config;
    }
}
