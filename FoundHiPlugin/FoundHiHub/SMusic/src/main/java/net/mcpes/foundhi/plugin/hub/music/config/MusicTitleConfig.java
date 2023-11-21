package net.mcpes.foundhi.plugin.hub.music.config;


import cn.nukkit.level.Location;
import cn.nukkit.utils.Config;
import net.mcpes.foundhi.plugin.hub.music.SMusic;

import java.io.File;
import java.util.ArrayList;

/**
 * @author hhm
 */

public class MusicTitleConfig {
    public static Config config;
    private static File file;

    public static void init() {
        file = new File(SMusic.getInstance().getDataFolder() + "/musicTitle.yml");
        config = new Config(file, 2);
    }

    public static Config getConfig() {
        return config;
    }

    public static ArrayList<String> getPos() {
        return config.get("pos") == null ? new ArrayList<>() : (ArrayList<String>) config.get("pos");
    }

    public static void addPos(Location location) {
        ArrayList<String> a = getPos();
        a.add((int) location.getX() + ":" + (int) location.getY() + ":" + (int) location.getZ() + ":" + location.getLevel().getName());
        config.set("pos", a);
        config.save();
    }

    public static void delPos(Location location) {
        ArrayList<String> a = getPos();
        a.remove((int) location.getX() + ":" + (int) location.getY() + ":" + (int) location.getZ() + ":" + location.getLevel().getName());
        config.set("pos", a);
        config.save();
    }
}
