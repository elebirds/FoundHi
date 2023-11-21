package net.mcpes.foundhi.plugin.currency.mw;

import cn.nukkit.level.generator.Flat;
import cn.nukkit.level.generator.Generator;
import cn.nukkit.level.generator.Nether;
import cn.nukkit.level.generator.Normal;
import cn.nukkit.plugin.PluginBase;
import net.mcpes.foundhi.plugin.currency.mw.command.TransferCommand;
import net.mcpes.foundhi.plugin.currency.mw.command.WorldCommand;
import net.mcpes.foundhi.plugin.currency.mw.config.DropSignConfig;
import net.mcpes.foundhi.plugin.currency.mw.config.MasterConfig;
import net.mcpes.foundhi.plugin.currency.mw.generator.VoidGenerator;
import net.mcpes.foundhi.plugin.currency.mw.listener.PlayerListener;
import net.mcpes.foundhi.plugin.currency.mw.listener.WorldListener;
import net.mcpes.foundhi.plugin.currency.mw.task.ClearTask;
import net.mcpes.foundhi.plugin.currency.mw.utils.World;

import java.util.HashMap;

/**
 * @author hhm
 * @date 2017/7/7
 * @since SMultiWorld
 */

public class SMultiWorld extends PluginBase {
    public static final String DEFAULT_TITLE = "§l§7|§bS§a多§c世界§7| §6";
    public static HashMap<String, World> worlds = new HashMap<>();
    public static String useMoneyCore = null;
    public static HashMap<String, String> clicked = new HashMap<>();
    private static SMultiWorld instance;
    private static SMultiWorldAPI api;

    public static SMultiWorld getInstance() {
        return instance;
    }

    public static SMultiWorldAPI getApi() {
        return api;
    }

    public static void info(String message) {
        instance.getServer().getLogger().info(DEFAULT_TITLE + message);
    }

    @Override
    public void onLoad() {
        instance = this;
        api = new SMultiWorldAPI();
        Generator.addGenerator(Normal.class, "normal", 1);
        Generator.addGenerator(Flat.class, "flat", 2);
        Generator.addGenerator(Nether.class, "nether", 3);
        Generator.addGenerator(VoidGenerator.class, "void", 4);
        MasterConfig mc = new MasterConfig();
        DropSignConfig.init();
        mc.load();
        mc.save();
        switch (mc.moneyCore) {
            case "Money":
            case "money":
                useMoneyCore = "Money";
                break;
        }
        info("加载成功");
    }

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        this.getServer().getPluginManager().registerEvents(new WorldListener(), this);
        this.getServer().getCommandMap().register("world", new WorldCommand());
        this.getServer().getCommandMap().register("w", new TransferCommand());
        this.getServer().getScheduler().scheduleRepeatingTask(new ClearTask(this), 20);
        api.loadAllWorld();
        info("启动成功");
    }

    @Override
    public void onDisable() {
        info("关闭成功");
    }
}
