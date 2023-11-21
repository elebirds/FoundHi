package net.mcpes.foundhi.plugin.currency.mw.config;

import cn.nukkit.utils.Config;
import net.mcpes.foundhi.plugin.currency.mw.SMultiWorld;

import java.io.File;
import java.util.ArrayList;

/**
 * @author hhm
 * @date 2017/7/8
 * @since SMultiWorld
 */

public class WorldConfig {
    private Config config;
    private String name;

    public WorldConfig(String name) {
        this.name = name;
        config = new Config(new File(SMultiWorld.getInstance().getDataFolder() + "/worlds/" + name + ".yml"), 2);
    }

    public static boolean exists(String name) {
        return new File(SMultiWorld.getInstance().getDataFolder() + "/worlds/" + name + ".yml").exists();
    }

    public Config getConfig() {
        return config;
    }

    public void init() {
        if (exists(name) && config.getAll().size() == 15) {
            config.set("世界是否可破坏", true);
            config.set("世界是否可放置", true);
            config.save();
        }
        if (exists(name) && config.getAll().keySet().size() != 0) return;
        ArrayList<Integer> gameMode = new ArrayList<>();
        gameMode.add(0);
        gameMode.add(1);
        config.set("PVP伤害开启", true);
        config.set("PVE伤害开启", true);
        config.set("农作物生长", true);
        config.set("死亡掉落", false);
        config.set("岩浆流动", false);
        config.set("水流动", false);
        config.set("TNT爆炸破坏方块", false);
        config.set("苦力怕爆炸破坏方块", false);
        config.set("世界是否可破坏", true);
        config.set("世界是否可放置", true);
        config.set("移动检测", false);
        config.set("地图游戏模式", gameMode);
        config.set("可破环与放置人员", new ArrayList<>());
        config.set("地图最大人数", -1);
        config.set("进入地图花费", 0);
        config.set("进入世界消息", "{%player},欢迎你来到{%worldName}世界");
        config.set("离开世界消息", "{%not}");
        config.set("天气", -1);
        config.set("丢弃物品", true);
        config.save();
    }

    public void reload() {
        config.reload();
    }
}
