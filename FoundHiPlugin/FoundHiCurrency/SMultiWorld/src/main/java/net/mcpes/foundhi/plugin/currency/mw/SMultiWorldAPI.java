package net.mcpes.foundhi.plugin.currency.mw;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.level.Level;
import cn.nukkit.level.generator.Generator;
import net.mcpes.foundhi.plugin.currency.mw.config.WorldConfig;
import net.mcpes.foundhi.plugin.currency.mw.utils.FileFunction;
import net.mcpes.foundhi.plugin.currency.mw.utils.World;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

/**
 * @author hhm
 * @date 2017/7/7
 * @since SMultiWorld
 */

public class SMultiWorldAPI {
    public void loadAllWorld() {
        File[] files = new File(SMultiWorld.getInstance().getDataFolder().getParentFile().getParentFile() + "/worlds/").listFiles();
        if (files == null) return;
        for (File file : files) {
            if (file.isDirectory()) {
                File dat = new File(file + "/level.dat");
                if (dat.exists()) {
                    this.loadWorld(file.getName());
                } else {
                    SMultiWorld.info("世界'" + file.getName() + "'的level.dat不存在,无法加载");
                }
            }
        }
    }

    public ArrayList<String> getWorlds() {
        ArrayList<String> worlds = new ArrayList<>();
        for (Level level : Server.getInstance().getLevels().values()) {
            worlds.add(level.getName());
        }
        return worlds;
    }

    public int unloadWorld(String name) {
        if (!Server.getInstance().isLevelLoaded(name)) {
            return 3;
        } else {
            Server.getInstance().unloadLevel(Server.getInstance().getLevelByName(name));
            return !Server.getInstance().isLevelLoaded(name) ? 1 : 2;
        }
    }

    public int removeWorld(String name) {
        File file = new File(SMultiWorld.getInstance().getServer().getDataPath() + "/worlds/" + name + "/");
        if (file.exists()) {
            if (Server.getInstance().isLevelLoaded(name)) {
                try {
                    Map<Long, Player> players = Server.getInstance().getLevelByName(name).getPlayers();
                    if (players.size() > 0) {
                        players.forEach((k, v) -> {
                            v.teleport(Server.getInstance().getDefaultLevel().getSpawnLocation());
                        });
                    }
                } catch (Exception e) {
                    // 屏蔽
                }
                Server.getInstance().unloadLevel(Server.getInstance().getLevelByName(name));
                if (!Server.getInstance().isLevelLoaded(name)) {
                    FileFunction.remove(file);
                    return 1;
                } else {
                    return 2;
                }
            } else {
                FileFunction.remove(file);
                return 1;
            }
        } else {
            return 3;
        }
    }

    public boolean loadWorld(String name) {
        WorldConfig wc = new WorldConfig(name);
        wc.init();
        wc.getConfig().reload();
        SMultiWorld.worlds.put(name, World.fromMap(name, wc.getConfig().getAll()));
        if (Server.getInstance().isLevelLoaded(name)) {
            SMultiWorld.info("世界'" + name + "'已经加载,无需再次加载");
            return false;
        }
        if (Server.getInstance().loadLevel(name)) {
            SMultiWorld.info("世界'" + name + "'加载成功");
            return true;
        } else {
            SMultiWorld.info("世界'" + name + "'加载失败,原因未知");
        }
        return false;
    }

    public void reloadConfig(String name) {
        WorldConfig wc = new WorldConfig(name);
        wc.init();
        wc.reload();
        SMultiWorld.worlds.put(name, World.fromMap(name, wc.getConfig().getAll()));
    }

    public int newWorld(String name) {
        return this.newWorld(name, 1);
    }

    public int newWorld(String name, long seed) {
        return this.newWorld(name, seed, "normal");
    }

    public int newWorld(String name, long seed, String type) {
        return "normal".equals(type) ? this.newWorld(name, seed, 1) : ("flat".equals(type) ? this.newWorld(name, seed, 2) : ("nether".equals(type) ? this.newWorld(name, seed, 3) : ("void".equals(type) ? this.newWorld(name, seed, 4) : 5)));
    }

    public int newWorld(String name, long seed, int type) {
        if (Server.getInstance().isLevelLoaded(name)) {
            return 5;
        }
        if (Server.getInstance().loadLevel(name)) {
            return 4;
        }
        return Server.getInstance().generateLevel(name, seed, Generator.getGenerator(type)) ? (Server.getInstance().loadLevel(name) ? 1 : 2) : 3;

    }
}
