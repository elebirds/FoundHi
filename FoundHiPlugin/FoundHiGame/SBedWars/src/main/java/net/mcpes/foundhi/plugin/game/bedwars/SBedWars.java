package net.mcpes.foundhi.plugin.game.bedwars;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.blockentity.BlockEntitySign;
import cn.nukkit.entity.weather.EntityLightning;
import cn.nukkit.item.Item;
import cn.nukkit.level.Location;
import cn.nukkit.level.Sound;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.DoubleTag;
import cn.nukkit.nbt.tag.FloatTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.scheduler.AsyncTask;
import net.mcpes.foundhi.plugin.game.bedwars.command.BedWarsCommand;
import net.mcpes.foundhi.plugin.game.bedwars.config.MasterConfig;
import net.mcpes.foundhi.plugin.game.bedwars.config.RankingConfig;
import net.mcpes.foundhi.plugin.game.bedwars.config.RoomConfig;
import net.mcpes.foundhi.plugin.game.bedwars.data.RoomData;
import net.mcpes.foundhi.plugin.game.bedwars.game.BedWars;
import net.mcpes.foundhi.plugin.game.bedwars.game.ReviveTask;
import net.mcpes.foundhi.plugin.game.bedwars.gui.GUIShopBase;
import net.mcpes.foundhi.plugin.game.bedwars.listener.*;
import net.mcpes.foundhi.plugin.game.bedwars.utils.FileFunction;
import net.mcpes.foundhi.plugin.game.bedwars.utils.PlayerObject;
import net.mcpes.foundhi.plugin.game.bedwars.utils.RankingList;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import static net.mcpes.foundhi.plugin.game.bedwars.utils.Utils.cast;

/**
 * @author hhm
 * @date 2017/7/13
 * @since SBedWars
 */

public class SBedWars extends PluginBase implements SBedWarsAPI {
    public static final String DEFAULT_TITLE = "§l§f >> §6S§eB§de§3d§6W§aa§4r§5s§e §f<< §6";
    @SuppressWarnings("all")
    public static final String VERSION = "0.2.2";
    public static final String GAMESTATUS_WAIT = "§6等待加入";
    public static final String GAMESTATUS_SOON = "§6即将开始";
    public static final String GAMESTATUS_START = "§6游戏开始";
    public static final String GAMESTATUS_STOP = "§6游戏停止";
    public static final String GAMESTATUS_RELOAD = "§6重载房间";
    public static HashMap<Integer, BedWars> games = new HashMap<>();
    public static HashMap<Integer, RoomData> rooms = new HashMap<>();
    public static HashMap<Integer, HashMap<String, CopyOnWriteArrayList<Player>>> gamePlayers = new HashMap<>();
    public static HashMap<String, Integer> gaming = new HashMap<>();
    public static HashMap<String, String> touch = new HashMap<>();
    public static HashMap<String, RoomData> add = new HashMap<>();
    public static HashMap<String, PlayerObject> players = new HashMap<>();
    public static HashMap<String, HashMap<String, Integer>> rankingData = new HashMap<>();
    public static HashMap<String, Double> kdData = new HashMap<>();
    public static ConcurrentHashMap<Player, Integer> revive = new ConcurrentHashMap<>();
    public static ArrayList<String> dataSignSetting = new ArrayList<>();
    public static Item diamond;
    public static Item gold;
    public static Item silver;
    public static Item emerald;
    public static MasterConfig master;
    private static SBedWars instance;

    public static SBedWars getInstance() {
        return instance;
    }

    @SuppressWarnings("all")
    public static CompoundTag getNBT() {
        return new CompoundTag()
                .putList(new ListTag<>("Pos").add(new DoubleTag("", 0.0D)).add(new DoubleTag("", 0.0D)).add(new DoubleTag("", 0.0D)))
                .putList((new ListTag<>("Motion")).add(new DoubleTag("", 0.0D)).add(new DoubleTag("", 0.0D)).add(new DoubleTag("", 0.0D)))
                .putList((new ListTag<>("Rotation")).add(new FloatTag("", 0.0F)).add(new FloatTag("", 0.0F)));
    }

    public static void callThunder(Location location) {
        int x1 = (int) location.getX();
        int z1 = (int) location.getZ();
        EntityLightning t = new EntityLightning(location.getLevel().getChunk(x1 >> 4, z1 >> 4, false), getNBT());
        t.setPosition(location);
        t.spawnToAll();
    }

    @Override
    public void onLoad() {
        instance = this;
        master = new MasterConfig();
        master.load();
        master.save();
        gold = Item.get(266, 0, 1);
        gold.setCustomName(DEFAULT_TITLE + "§e金");
        silver = Item.get(265, 0, 1);
        silver.setCustomName(DEFAULT_TITLE + "§f银");
        diamond = Item.get(264, 0, 1);
        diamond.setCustomName(DEFAULT_TITLE + "§b钻石");
        emerald = Item.get(Item.EMERALD, 0, 1);
        emerald.setCustomName(DEFAULT_TITLE + "§2绿宝石");
        this.saveResource("shop.yml");
        this.initConfig();
        this.getServer().getLogger().info(DEFAULT_TITLE + VERSION + " 加载成功");
    }

    @Override
    public void onEnable() {
        RankingConfig.init();
        RankingList.init();
        GUIShopBase.init();
        this.getServer().getPluginManager().registerEvents(new NormalListener(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        this.getServer().getPluginManager().registerEvents(new SignListener(), this);
        this.getServer().getPluginManager().registerEvents(new FloatingListener(), this);
        this.getServer().getPluginManager().registerEvents(new GUIListener(), this);
        this.getServer().getCommandMap().register("bw", new BedWarsCommand());
        this.getServer().getScheduler().scheduleAsyncTask(this, new AsyncTask() {
            @Override
            public void onRun() {
                while (true) {
                    try {
                        Thread.sleep(20000L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    try {
                        RankingList.reInit();
                        RankingList.update(1);
                        RankingList.update(2);
                        RankingList.update(3);
                        RankingList.update(4);
                    } catch (Exception ignored) {
                    }
                    if (!Server.getInstance().isRunning()) {
                        break;
                    }
                }
            }
        });
        this.getServer().getScheduler().scheduleRepeatingTask(new ReviveTask(), 20);
        this.loadWorlds();
        this.loadAllRoom();
        this.getServer().getLogger().info(DEFAULT_TITLE + VERSION + " 启动成功");
    }

    @Override
    public void onDisable() {
        RankingConfig.save();
        this.getServer().getLogger().info(DEFAULT_TITLE + VERSION + " 关闭成功");
    }

    @SuppressWarnings("all")
    private void initConfig() {
        new File(this.getDataFolder() + "/rooms/").mkdirs();
        new File(this.getDataFolder() + "/worlds/").mkdirs();
    }

    @Override
    public void loadAllRoom() {
        String[] rns = new File(this.getDataFolder() + "/rooms/").list();
        if (rns == null) return;
        for (String rn : rns) {
            int r = Integer.valueOf(rn.replace(".yml", ""));
            if (this.loadRoom(r)) {
                this.getServer().getLogger().info(DEFAULT_TITLE + "加载" + r + "号房间成功!");
            } else {
                this.getServer().getLogger().info(DEFAULT_TITLE + "加载" + r + "号房间失败!");
            }
        }
    }

    @Override
    public boolean loadRoom(int id) {
        try {
            RoomConfig cr = new RoomConfig(id);
            this.getServer().loadLevel(cr.getConfig().getString("game-world"));
            if (this.getServer().isLevelLoaded(cr.getConfig().getString("game-world"))) {
                RoomData data = new RoomData(id);
                data.setDisName(cr.getConfig().getString("disName"));
                data.setMin(cr.getConfig().getInt("min-player"));
                data.setMax(cr.getConfig().getInt("max-player"));
                data.setWaitTime(cr.getConfig().getInt("wait-time"));
                data.setWaitPos(cr.getConfig().getString("wait-pos"));
                data.setGameTime(cr.getConfig().getInt("game-time"));
                data.setGameWorld(cr.getConfig().getString("game-world"));
                data.setStopPos(cr.getConfig().getString("stop-pos"));
                data.setVoidKill(cr.getConfig().getInt("voidKill"));
                data.setTeamData(cast(cr.getConfig().get("teamData")), 1);
                data.setSignPos(cast(cr.getConfig().get("sign-pos")));
                data.setEmeraldPos(cast(cr.getConfig().get("emerald-pos")));
                data.setDiamondPos(cast(cr.getConfig().get("diamond-pos")));
                data.setGoldPos(cast(cr.getConfig().get("gold-pos")));
                data.setSilverPos(cast(cr.getConfig().get("silver-pos")));
                data.setEmeraldDropSpeed(cr.getConfig().getInt("emerald-drop-speed"));
                data.setDiamondDropSpeed(cr.getConfig().getInt("diamond-drop-speed"));
                data.setGoldDropSpeed(cr.getConfig().getInt("gold-drop-speed"));
                data.setSilverDropSpeed(cr.getConfig().getInt("silver-drop-speed"));
                SBedWarsAPI.getInstance().loadWorld(data.getGameWorld());
                data.setGameLevel();
                data.setEmeraldLocation();
                data.setDiamondLocation();
                data.setGoldLocation();
                data.setSilverLocation();
                data.setSignLocation();
                data.setWaitLocation();
                data.setStopLocation();
                rooms.put(id, data);
            } else return false;
            return true;
        } catch (NullPointerException | IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public int getFreeID() {
        int max = 0;
        String[] rns = new File(this.getDataFolder() + "/rooms/").list();
        if (rns == null) return 1;
        int length = 0;
        for (String rn : rns) {
            if (max < Integer.valueOf(rn.replace(".yml", ""))) {
                max = Integer.valueOf(rn.replace(".yml", ""));
            }
            length++;
        }
        if (max <= length) {
            return max + 1;
        }
        for (int i = 1; i < max; i++) {
            if (!new File(this.getDataFolder() + "/rooms/" + i + ".yml").exists()) {
                return i;
            }
        }
        return max + 1;
    }

    @Override
    public void loadWorlds() {
        /*
        String[] ws = new File(this.getDataFolder() + "/worlds/").list();
        if (ws == null) return;
        for (String w : ws) {
            if (new File(this.getDataFolder() + "/worlds/" + w).isFile()) continue;
            try {
                this.loadWorld(w);
                this.getServer().getLogger().info(DEFAULT_TITLE + "成功加载地图:" + w);
            } catch (IOException e) {
                e.printStackTrace();
                this.getServer().getLogger().info(DEFAULT_TITLE + "加载地图:" + w + "失败");
            }
        }*/
    }

    @Override
    public void loadWorld(String worldName) throws IOException {
        if (!this.getServer().isLevelLoaded(worldName)) {
            FileFunction.copy(new File(this.getDataFolder() + "/worlds/" + worldName + "/").getCanonicalPath(), this.getDataFolder().getParentFile().getParentFile() + "/worlds/" + worldName + "/");
            this.getServer().loadLevel(worldName);
        } else {
            try {
                Map<Long, Player> players = Server.getInstance().getLevelByName(worldName).getPlayers();
                if (players.size() > 0) {
                    players.forEach((k, v) -> v.teleport(Server.getInstance().getDefaultLevel().getSpawnLocation()));
                }
            } catch (Exception e) {
                // 屏蔽
            }
            this.getServer().unloadLevel(this.getServer().getLevelByName(worldName));
            FileFunction.remove(new File(this.getDataFolder().getParentFile().getParentFile() + "/worlds/" + worldName + "/"));
            FileFunction.copy(new File(this.getDataFolder() + "/worlds/" + worldName + "/").getCanonicalPath(), this.getDataFolder().getParentFile().getParentFile() + "/worlds/" + worldName + "/");
            this.getServer().loadLevel(worldName);
        }
    }

    @Override
    public void broadcastMessage(CopyOnWriteArrayList<Player> p, String msg) {
        for (Player pl : p) {
            if (pl.isOnline()) {
                pl.sendMessage(msg);
            }
        }
    }

    @Override
    public void broadcastSound(CopyOnWriteArrayList<Player> p, int type) {
        switch (type) {
            case 1:
                for (Player pl : p) {
                    if (pl.isOnline()) {
                        pl.level.addSound(pl.getLocation(), Sound.RANDOM_CLICK);
                    }
                }
                break;
            case 2:
                for (Player pl : p) {
                    if (pl.isOnline()) {
                        pl.level.addSound(pl.getLocation(), Sound.RANDOM_ANVIL_USE);
                    }
                }
                break;
        }
    }

    @Override
    public void broadcastTip(CopyOnWriteArrayList<Player> p, String msg) {
        for (Player pl : p) {
            if (pl.isOnline())
                pl.sendTip(msg);
        }
    }

    @Override
    public void broadcastSpeak(CopyOnWriteArrayList<Player> p, String pn, String msg) {
        for (Player pl : p) {
            pl.sendMessage(DEFAULT_TITLE + "§7<§b" + pn + "§7>§6: " + msg);
        }
    }

    @Override
    public void broadcastTeamSpeak(CopyOnWriteArrayList<String> p, String pn, String msg) {
        for (String name : p) {
            Player pl = this.getServer().getPlayerExact(name);
            pl.sendMessage(DEFAULT_TITLE + "§7<§b" + pn + "§7>§6: " + msg);
        }
    }

    @Override
    public int isPlayerGaming(String name) {
        return gaming.getOrDefault(name, -1);
    }

    @Override
    public int isSign(String pos) {
        for (RoomData data : rooms.values()) {
            for (String b : data.getSignPos()) {
                if (pos.equals(b)) return data.getId();
            }
        }
        return -1;
    }

    @Override
    public void addSign(String pos, int id) {
        RoomConfig cr = new RoomConfig(id);
        ArrayList<String> sign = cast(cr.getConfig().get("sign-pos"));
        sign.add(pos);
        cr.getConfig().set("sign-pos", sign);
        cr.getConfig().save();
        RoomData data = rooms.get(id);
        data.setSignPos(sign);
        data.setSignLocation();
    }

    @Override
    public void delSign(String pos, int id) {
        RoomConfig cr = new RoomConfig(id);
        ArrayList<String> sign = cast(cr.getConfig().get("sign-pos"));
        sign.remove(pos);
        cr.getConfig().set("sign-pos", sign);
        cr.getConfig().save();
        RoomData data = rooms.get(id);
        data.setSignPos(sign);
        data.setSignLocation();
    }

    @Override
    public void resetSign(int id, Location pos) {
        BlockEntity entity = pos.level.getBlockEntity(pos);
        if (!(entity instanceof BlockEntitySign)) return;
        BlockEntitySign sign = (BlockEntitySign) entity;
        String status;
        if (games.containsKey(id)) {
            switch (games.get(id).getGameMode()) {
                default:
                    status = "§c未知错误";
                    break;
                case 1:
                    status = GAMESTATUS_WAIT;
                    break;
                case 2:
                    status = GAMESTATUS_SOON;
                    break;
                case 3:
                    status = GAMESTATUS_START;
                    break;
                case 4:
                    status = GAMESTATUS_STOP;
                    break;
                case 5:
                    status = GAMESTATUS_RELOAD;
                    break;
            }
            sign.setText(DEFAULT_TITLE, status, "§l§b房间: §e" + id, "§l§c<§f---§b" + games.get(id).getAllPlayers().size() + "§7/" + rooms.get(id).getMax() + "§f---§c>");
        } else {
            sign.setText(DEFAULT_TITLE, GAMESTATUS_WAIT, "§l§b房间: §e" + id, "§l§c<§f---§b0§7/" + rooms.get(id).getMax() + "§f---§c>");
        }
    }

    @Override
    public void broadcastTitle(CopyOnWriteArrayList<Player> p, int fadeIn, int stay, int fadeOut, String msg, String twoMsg) {
        for (Player pl : p) {
            this.broadcastTitle(pl, fadeIn, stay, fadeOut, msg, twoMsg);
        }
    }

    public void broadcastTitle(Player pl, int fadeIn, int stay, int fadeOut, String msg, String twoMsg) {
        if (pl.isOnline()) {
            pl.setTitleAnimationTimes(fadeIn, stay, fadeOut);
            if (twoMsg != null) {
                pl.setSubtitle(twoMsg);
            }
            pl.sendTitle(msg);
            pl.resetTitleSettings();
        }

    }
}