package net.mcpes.foundhi.plugin.rpg.rpg;

import cn.nukkit.item.Item;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.utils.Config;
import net.mcpes.foundhi.plugin.rpg.rpg.manager.*;
import net.mcpes.foundhi.plugin.rpg.rpg.role.RolesManager;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yui
 * @date 2017/10/12
 * @since RPG
 */

@SuppressWarnings("unchecked")
public class DataManager {
    public static final DataManager INSTANCE = new DataManager();

    private Map<String, Object> rolesData = new HashMap<>();
    private Map<String, RolesManager> roles = new HashMap<>();

    private Map<String, Object> armsData = new HashMap<>();
    private Map<String, ArmsManager> arms = new HashMap<>();

    private Map<String, Object> armorsData = new HashMap<>();
    private Map<String, ArmorsManager> armors = new HashMap<>();

    private Map<String, PlayerManager> players = new HashMap<>();

    private Map<String, Object> levelData = new HashMap<>();
    private Map<Integer, LevelManager> level = new HashMap<>();

    private Map<String, BuildManager> buildModes = new HashMap<>();

    private Map<Integer, Integer> mobExp = new HashMap<>();
    private Config baseConfig;

    private DataManager() {
    }

    public boolean containPlayerManager(String name) {
        return players.containsKey(name);
    }

    public PlayerManager getPlayerManager(String name) {
        return players.get(name);
    }

    public LevelManager getLevelManager(int level) {
        return this.level.get(level);
    }

    public void addPlayer(PlayerManager playerManager) {
        String playerName = playerManager.getPlayer().getName().toLowerCase();
        players.put(playerName, playerManager);
    }

    public void removePlayer(String playerName) {
        players.remove(playerName);
    }

    void setLevelData(Map<String, Object> levelData) {
        this.levelData = levelData;
    }

    void setRolesData(Map<String, Object> rolesData) {
        this.rolesData = rolesData;
    }

    void setArmsData(Map<String, Object> armsData) {
        this.armsData = armsData;
    }

    void setArmorsData(Map<String, Object> armorsData) {
        this.armorsData = armorsData;
    }

    void setBaseConfig(Config baseConfig) {
        this.baseConfig = baseConfig;
    }

    void setPieces(Map<String, Object> pieces) {
        String[] m = pieces.get("精良碎片ID").toString().split(":");
        RPGMain.fragment1 = Item.get(Integer.valueOf(m[0]), Integer.valueOf(m[1]), 1).setCustomName(pieces.get("精良碎片名称").toString()).setCustomBlockData(new CompoundTag().putInt("piece", 1));
        m = pieces.get("稀有碎片ID").toString().split(":");
        RPGMain.fragment2 = Item.get(Integer.valueOf(m[0]), Integer.valueOf(m[1]), 1).setCustomName(pieces.get("稀有碎片名称").toString()).setCustomBlockData(new CompoundTag().putInt("piece", 2));
        m = pieces.get("史诗碎片ID").toString().split(":");
        RPGMain.fragment3 = Item.get(Integer.valueOf(m[0]), Integer.valueOf(m[1]), 1).setCustomName(pieces.get("史诗碎片名称").toString()).setCustomBlockData(new CompoundTag().putInt("piece", 3));
        m = pieces.get("传说碎片ID").toString().split(":");
        RPGMain.fragment4 = Item.get(Integer.valueOf(m[0]), Integer.valueOf(m[1]), 1).setCustomName(pieces.get("传说碎片名称").toString()).setCustomBlockData(new CompoundTag().putInt("piece", 4));
        m = pieces.get("神迹碎片ID").toString().split(":");
        RPGMain.fragment5 = Item.get(Integer.valueOf(m[0]), Integer.valueOf(m[1]), 1).setCustomName(pieces.get("神迹碎片名称").toString()).setCustomBlockData(new CompoundTag().putInt("piece", 5));
    }

    void init() {
        for (String key : armsData.keySet()) {
            Map<String, Object> data = (Map<String, Object>) armsData.get(key);
            arms.put(key, new ArmsManager(key, data));
        }
        for (String key : armorsData.keySet()) {
            Map<String, Object> data = (Map<String, Object>) armorsData.get(key);
            armors.put(key, new ArmorsManager(key, data));
        }
        for (String key : rolesData.keySet()) {
            Map<String, Object> data = (Map<String, Object>) rolesData.get(key);
            roles.put(key, new RolesManager(key, data));
        }
        for (String key : levelData.keySet()) {
            Map<String, Object> data = (Map<String, Object>) levelData.get(key);
            level.put(Integer.valueOf(key), new LevelManager(Integer.valueOf(key), data));
        }
        for (Object one : baseConfig.getList("经验获取方法")) {
            String[] strings = Utils.getStringArray((String) one, ":");
            int health = Integer.valueOf(strings[0]);
            int exp = Integer.valueOf(strings[1]);
            this.mobExp.put(health, exp);
        }
        String path = RPGMain.getFiles() + "\\BuildModes\\";
        for (String name : Utils.getFiles(path)) {
            Config config = new Config(name, Config.YAML);
            String id = name.replace(path, "").replace(".yml", "");
            this.buildModes.put(id, new BuildManager(id, config.getAll()));
        }
    }

    public Map<String, ArmorsManager> getArmors() {
        return armors;
    }

    public Map<String, ArmsManager> getArms() {
        return arms;
    }

    public BuildManager getBuildManager(String mode) {
        return this.buildModes.get(mode);
    }

    public BuildManager getBuildManager(Item item) {
        ArmsManager armsManager = this.getArmsManager(item);
        String buildMode = armsManager.getBuildMode();
        return this.getBuildManager(buildMode);
    }

    public RolesManager getRolesManager(String key) {
        return roles.get(key);
    }

    public ArmsManager getArmsManager(String key) {
        return arms.get(key);
    }

    public ArmsManager getArmsManager(Item item) {
        return this.getArmsManager(item.getCustomBlockData().getString("ID"));
    }

    public ArmorsManager getArmorsManager(String key) {
        return armors.get(key);
    }

    public ArmorsManager getArmorsManager(Item item) {
        return this.getArmorsManager(item.getCustomBlockData().getString("ID"));
    }


    public Artifact getArtifact(String key) {
        Artifact artifact = null;
        if (existsArms(key)) {
            artifact = getArmsManager(key);
        } else if (existsArmors(key)) {
            artifact = getArmorsManager(key);
        }
        return artifact;
    }

    public Item getPiece(int type) {
        switch (type) {
            case 1:
                return RPGMain.fragment1;
            case 2:
                return RPGMain.fragment2;
            case 3:
                return RPGMain.fragment3;
            case 4:
                return RPGMain.fragment4;
            case 5:
                return RPGMain.fragment5;
        }
        return null;
    }

    public int getMaxLevel() {
        return this.levelData.keySet().size();
    }

    public String getTip(float money1, float money2, float money3, String job, int level, int exp, int maxExp) {
        String tip = baseConfig.getString("底部显示");
        tip = tip.replace("@money1", String.valueOf(money1))
                .replace("@money2", String.valueOf(money2))
                .replace("@money3", String.valueOf(money3))
                .replace("@job", job)
                .replace("@level", String.valueOf(level))
                .replace("@exp", String.valueOf(exp))
                .replace("@maxExp", String.valueOf(maxExp));
        return tip;
    }

    public int getRoleChangeMoney() {
        return baseConfig.getInt("转职金钱");
    }

    public Map<Integer, Integer> getMobExp() {
        return mobExp;
    }

    public boolean existsPlayer(String playerName) {
        return players.containsKey(playerName);
    }

    public boolean existsRole(String rolesID) {
        return rolesData.containsKey(rolesID);
    }

    public boolean existsPiece(Item item, int need, int type) {
        int id = -1;
        int damage = -1;
        switch (type) {
            case 1:
                id = RPGMain.fragment1.getId();
                damage = RPGMain.fragment1.getDamage();
                break;
            case 2:
                id = RPGMain.fragment2.getId();
                damage = RPGMain.fragment2.getDamage();
                break;
            case 3:
                id = RPGMain.fragment3.getId();
                damage = RPGMain.fragment3.getDamage();
                break;
            case 4:
                id = RPGMain.fragment4.getId();
                damage = RPGMain.fragment4.getDamage();
                break;
            case 5:
                id = RPGMain.fragment5.getId();
                damage = RPGMain.fragment5.getDamage();
                break;
        }
        return item.getId() == id && item.getDamage() == damage && item.getCount() >= need && item.getCustomBlockData() != null && item.getCustomBlockData().contains("piece") && item.getCustomBlockData().getInt("piece") == type;
    }

    public boolean existsArms(String armsID) {
        return armsData.containsKey(armsID);
    }

    public boolean existsArmors(String armorsID) {
        return armorsData.containsKey(armorsID);
    }
}
