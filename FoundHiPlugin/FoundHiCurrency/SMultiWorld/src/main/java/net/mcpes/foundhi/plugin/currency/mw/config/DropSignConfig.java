package net.mcpes.foundhi.plugin.currency.mw.config;

import cn.nukkit.utils.Config;
import net.mcpes.foundhi.plugin.currency.mw.SMultiWorld;

import java.util.ArrayList;

public class DropSignConfig {
    private static Config config;

    public static void init() {
        config = new Config(SMultiWorld.getInstance().getDataFolder() + "/drops.yml", 2);
    }

    public static Config getConfig() {
        return config;
    }

    public static ArrayList<String> getSign() {
        return config.get("sign", new ArrayList<>());
    }
}
