package net.mcpes.foundhi.plugin.rpg.rpg.manager;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.ConsoleCommandSender;
import cn.nukkit.item.Item;
import cn.nukkit.utils.Config;
import net.mcpes.foundhi.plugin.rpg.rpg.RPGMain;
import net.mcpes.foundhi.plugin.rpg.rpg.Utils;
import net.mcpes.foundhi.plugin.rpg.rpg.role.RolesManager;

import java.util.List;

/**
 * @author yui
 * @date 2017/10/14
 * @since RPG
 */

public class PlayerManager {
    private Player player;
    private Config config;
    private String roleID;//职业
    private int level;//等级
    private int exp;//经验
    private int MaxHealth;//血量上限
    private int MaxBlue;//蓝量上限
    private int blue;//蓝
    private int addExp = 0;//经验增加百分比

    public PlayerManager(Player player) {
        this.player = player;
        this.initData();
        if (this.haveRole()) {
            this.updateHealth();
            this.updateAddExp();
        }
    }

    public Player getPlayer() {
        return player;
    }

    public boolean haveRole() {
        return roleID != null;
    }

    public String getRoleID() {
        return roleID;
    }

    public void setRoleID(String roleID) {
        this.roleID = roleID;
        this.setMaxHealth(this.getRolesManager().getMaxHealth());
        this.setMaxBlue(this.getRolesManager().getMaxBlue());
        this.blue = this.getMaxBlue();
        this.updateHealth();
        for (String command : getRolesManager().getCommands()) {
            if (command.contains("@player")) {
                command = command.replace("@player", this.getPlayer().getName());
            }
            Server.getInstance().dispatchCommand(new ConsoleCommandSender(), command);
        }
    }

    public RolesManager getRolesManager() {
        return RPGMain.getDataManager().getRolesManager(this.roleID);
    }

    public LevelManager getLevelManager() {
        return RPGMain.getDataManager().getLevelManager(this.level);
    }

    public int getExp() {
        return exp;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
        player.setExperience(player.getExperience(), level);
    }

    public int getBlue() {
        return blue;
    }

    public void setBlue(int blue) {
        this.blue = blue;
    }

    public int getMaxHealth() {
        return MaxHealth;
    }

    public void setMaxHealth(int maxHealth) {
        MaxHealth = maxHealth;
        player.setMaxHealth(maxHealth);
    }

    public int getMaxBlue() {
        return MaxBlue;
    }

    public void setMaxBlue(int maxBlue) {
        MaxBlue = maxBlue;
    }

    public void updateAddExp() {
        if (Utils.hasArmsInHand(player)) {
            this.addExp += this.getArmsManager().getExpAdd() / 100;
        }
        List<Item> equipments = Utils.isInArmors(player);
        if (equipments != null) {
            for (Item item : equipments) {
                ArmorsManager armors = RPGMain.getDataManager().getArmorsManager(item);
                this.addExp += armors.getExpAdd() / 100;
            }
        }
    }

    public void addExp(int exp) {
        if (this.level == RPGMain.getDataManager().getMaxLevel()) {
            return;
        }
        this.exp = this.upgradeCalculation(exp);
    }

    public ArmsManager getArmsManager() {
        Item handItem = player.getInventory().getItemInHand();
        return RPGMain.getDataManager().getArmsManager(handItem);
    }

    private void updateHealth() {
        this.player.setMaxHealth(this.getMaxHealth());
    }

    private void initData() {
        config = new Config(RPGMain.getPlayerFile() + "/" + this.player.getName() + ".yml", Config.YAML);
        if (config.getAll().isEmpty()) {
            config.set("RoleID", "~");
            config.set("Level", 1);
            config.set("EXP", 0);
            config.set("MaxHealth", 20);
            config.set("MaxBlue", 20);
            config.set("Health", 20);
            config.set("Blue", 0);
            config.save();
        }
        if (!config.get("RoleID").toString().equals("~")) {
            this.roleID = config.getString("RoleID");
        } else {
            this.roleID = null;
        }
        this.level = config.getInt("Level");
        this.exp = config.getInt("EXP");
        this.MaxHealth = config.getInt("MaxHealth");
        this.MaxBlue = config.getInt("MaxBlue");
        this.blue = config.getInt("Blue");
    }

    public void saveAll() {
        String r;
        if (haveRole()) {
            r = roleID;
        } else {
            r = "~";
        }
        config.set("RoleID", r);
        config.set("Level", level);
        config.set("EXP", exp);
        config.set("MaxHealth", MaxHealth);
        config.set("MaxBlue", MaxBlue);
        config.set("Blue", blue);
        config.save();
    }

    private int upgradeCalculation(int exp) {
        int newExp = this.exp + exp * (1 + addExp);
        int most = this.getLevelManager().getExp();
        while (newExp >= most) {
            newExp = newExp - most;
            if (this.level < RPGMain.getDataManager().getMaxLevel()) {
                this.level++;
                this.setMaxHealth(this.getMaxHealth() + this.getLevelManager().getHealthAdd());
                this.setMaxBlue(this.getMaxBlue() + this.getLevelManager().getBlueAdd());
                this.setBlue(this.getMaxBlue());
                this.updateHealth();
                this.player.setHealth(this.getMaxHealth());
                this.getLevelManager().dispatchCommands(this.player.getName());
                if (this.level != RPGMain.getDataManager().getMaxLevel()) {
                    player.sendMessage(RPGMain.PluginName + "§e恭喜你升级到" + this.level + "级，再接再厉！");
                } else {
                    player.sendMessage(RPGMain.PluginName + "§e恭喜您，经历千辛万苦终于满级啦！");
                    Server.getInstance().broadcastMessage(RPGMain.PluginName + "§e恭喜玩家" + player.getName() + "成功满级！");
                }
            }
            most = this.getLevelManager().getExp();
        }
        return newExp;
    }
}
