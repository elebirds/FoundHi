package net.mcpes.foundhi.plugin.rpg.rpg;

import cn.nukkit.item.Item;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.plugin.PluginManager;
import cn.nukkit.utils.Config;
import net.mcpes.foundhi.plugin.rpg.rpg.commands.*;
import net.mcpes.foundhi.plugin.rpg.rpg.listener.AttackListener;
import net.mcpes.foundhi.plugin.rpg.rpg.listener.PlayerListener;
import net.mcpes.foundhi.plugin.rpg.rpg.task.SendPopupTask;

import java.io.File;

/**
 * @author yui
 * @date 2017/10/12
 * @since RPG
 */

public class RPGMain extends PluginBase {
    public static final String PluginName = "§l§e[§6R§eP§dG§e]";
    public static Item fragment1;
    public static Item fragment2;
    public static Item fragment3;
    public static Item fragment4;
    public static Item fragment5;
    private static File files;
    private static RPGMain instance;
    private static DataManager dataManager;

    public static RPGMain getInstance() {
        return instance;
    }

    public static DataManager getDataManager() {
        return dataManager;
    }

    public static File getPlayerFile() {
        return new File(files + "/Players");
    }

    public static File getFiles() {
        return files;
    }

    @Override
    public void onLoad() {
        instance = this;
        dataManager = DataManager.INSTANCE;
        files = getDataFolder();
        this.saveResource("Roles.yml");
        this.saveResource("Arms.yml");
        this.saveResource("Level.yml");
        this.saveResource("Armors.yml");
        this.saveResource("Pieces.yml");
        this.saveResource("ConfigBase.yml");
        this.saveResource("BuildModes/1.yml");
    }

    @Override
    public void onEnable() {
        this.prepareConfigData();
        this.registerEvents();
        this.registerCommands();
        this.getServer().getScheduler().scheduleRepeatingTask(this, new SendPopupTask(this), 10);
        getLogger().info(PluginName + "插件加载成功，作者Yui233，QQ：2363935539");
    }

    private void registerCommands() {
        getServer().getCommandMap().register("weapon", new RPGCommands("weapon"));
        getServer().getCommandMap().register("dh", new TradingCommand("dh"));
        getServer().getCommandMap().register("sp", new FragmentCommand("sp"));
        getServer().getCommandMap().register("role", new RoleCommand("role"));
        getServer().getCommandMap().register("build", new BuildCommand("build"));
    }

    private void prepareConfigData() {
        new File(getFiles() + "/Players/").mkdirs();
        Config armsConfig = new Config(getFiles() + "/Arms.yml", Config.YAML);
        Config armorsConfig = new Config(getFiles() + "/Armors.yml", Config.YAML);
        Config piecesConfig = new Config(getFiles() + "/Pieces.yml", Config.YAML);
        Config rolesConfig = new Config(getFiles() + "/Roles.yml", Config.YAML);
        Config levelConfig = new Config(getFiles() + "/Level.yml", Config.YAML);
        Config configBase = new Config(getFiles() + "/ConfigBase.yml", Config.YAML);
        dataManager.setRolesData(rolesConfig.getAll());
        dataManager.setArmsData(armsConfig.getAll());
        dataManager.setArmorsData(armorsConfig.getAll());
        dataManager.setPieces(piecesConfig.getAll());
        dataManager.setLevelData(levelConfig.getAll());
        dataManager.setBaseConfig(configBase);
        dataManager.init();
    }

    private void registerEvents() {
        PluginManager pluginManager = this.getServer().getPluginManager();
        pluginManager.registerEvents(new PlayerListener(), this);
        pluginManager.registerEvents(new AttackListener(), this);
    }
}
