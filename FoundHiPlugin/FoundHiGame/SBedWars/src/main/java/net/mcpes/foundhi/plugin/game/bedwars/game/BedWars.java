package net.mcpes.foundhi.plugin.game.bedwars.game;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.BlockSignPost;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.blockentity.BlockEntitySign;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.data.EntityMetadata;
import cn.nukkit.entity.item.EntityItem;
import cn.nukkit.entity.passive.EntityVillager;
import cn.nukkit.event.player.PlayerDeathEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.Location;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.DoubleTag;
import cn.nukkit.nbt.tag.FloatTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.network.protocol.SetEntityDataPacket;
import cn.nukkit.scheduler.PluginTask;
import cn.nukkit.utils.TextFormat;
import net.mcpes.foundhi.plugin.game.bedwars.SBedWars;
import net.mcpes.foundhi.plugin.game.bedwars.SBedWarsAPI;
import net.mcpes.foundhi.plugin.game.bedwars.config.RoomConfig;
import net.mcpes.foundhi.plugin.game.bedwars.data.RoomData;
import net.mcpes.foundhi.plugin.game.bedwars.entity.EntityEnderCrystal;
import net.mcpes.foundhi.plugin.game.bedwars.utils.FloatingTextPacket;
import net.mcpes.foundhi.plugin.game.bedwars.utils.RankingList;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

import static net.mcpes.foundhi.plugin.game.bedwars.SBedWars.*;
import static net.mcpes.foundhi.plugin.game.bedwars.utils.Utils.cast;

/**
 * @author hhm
 * @date 2017/7/13
 * @since SBedWars
 */

public class BedWars {
    public HashMap<String, HashMap<String, Object>> playerData;
    private HashMap<Integer, CopyOnWriteArrayList<String>> teams;
    private int id;
    private int gameMode;
    private RoomData data;

    public BedWars(int id) {
        this.id = id;
        if (rooms.containsKey(id)) {
            this.data = rooms.get(id);
            this.init();
        }
    }

    private void init() {
        gamePlayers.remove(this.id);
        HashMap<String, CopyOnWriteArrayList<Player>> roomPlayers = new HashMap<>();
        roomPlayers.put("alive", new CopyOnWriteArrayList<>());
        roomPlayers.put("all", new CopyOnWriteArrayList<>());
        roomPlayers.put("spectator", new CopyOnWriteArrayList<>());
        gamePlayers.put(this.id, roomPlayers);
        games.put(this.id, this);
        this.gameMode = 1;
        this.teams = new HashMap<>();
        this.playerData = new HashMap<>();
        data.setGameLevel();
        data.setDiamondLocation();
        data.setEmeraldLocation();
        data.setGoldLocation();
        data.setSignLocation();
        data.setWaitLocation();
        data.setStopLocation();
        data.setSilverLocation();
        data.setTeamData(cast(new RoomConfig(this.id).getConfig().getAll().get("teamData")), 1);
        for (Integer id : data.getTeamData().keySet()) {
            this.teams.put(id, new CopyOnWriteArrayList<>());
        }
    }

    public synchronized void onJoin(Player player) {
        if (this.getAllPlayers().contains(player)) return;
        if (this.getAlivePlayers().size() < data.getMax()) {
            if (this.gameMode == 1) {
                gaming.put(player.getName(), this.id);
                playerData.put(player.getName(), new HashMap<String, Object>() {{
                    put("kill", 0);
                    put("death", 0);
                    put("core", 0);
                }});
                this.addPlayer(player);
                this.resetSign();
                player.setGamemode(2);
                player.setTitleAnimationTimes(20, 50, 20);
                player.setSubtitle("§6§lPowered by hhm");
                player.sendTitle(DEFAULT_TITLE);
                player.resetTitleSettings();
                SBedWarsAPI.getInstance().broadcastMessage(this.getAllPlayers(), "§7<§b" + player.getName() + "§7> §6加入了房间! (" + this.getAllPlayers().size() + "/" + data.getMax() + ")");
                player.teleport(data.getWaitLocation());
                player.getInventory().clearAll();
                if (this.getAllPlayers().size() == data.getMin()) {
                    this.setGameMode(2);
                    Server.getInstance().getScheduler().scheduleRepeatingTask(new WaitTask(this.data, this), 20);
                }
                player.sendMessage(DEFAULT_TITLE + "成功加入" + id + "号房间");
            } else {
                player.sendMessage(DEFAULT_TITLE + "房间当前状态无法加入");
            }
        } else {
            player.sendMessage(DEFAULT_TITLE + "房间人数达到最大值,无法加入");
        }
    }

    public void onQuit(Player player, boolean online) {
        this.delPlayer(player);
        gaming.remove(player.getName());
        SBedWarsAPI.getInstance().broadcastMessage(this.getAllPlayers(), "§7<§b" + player.getName() + "§7> §6退出了房间! (" + this.getAlivePlayers().size() + "/" + data.getMax() + ")");
        player.getInventory().clearAll();
        if (online) {
            player.setHealth(player.getMaxHealth());
            player.clearTitle();
            player.removeAllEffects();
            player.getInventory().clearAll();
            player.setSpawn(Server.getInstance().getDefaultLevel().getSafeSpawn());
            player.teleport(this.data.getStopLocation());
            player.sendMessage(DEFAULT_TITLE + "§7你已退出游戏房间");
            player.sendMessage(DEFAULT_TITLE + "§7已经将您传送到游戏结束区域");
            player.sendMessage(DEFAULT_TITLE + "§4已经为您自动恢复背包物品");
        } else {
            player.setHealth(player.getMaxHealth());
            player.clearTitle();
            player.removeAllEffects();
            player.getInventory().clearAll();
            player.setSpawn(Server.getInstance().getDefaultLevel().getSafeSpawn());
            player.teleport(this.data.getStopLocation());
        }
    }

    public void onDeath(Player player, PlayerDeathEvent event) {
        event.setCancelled(true);
        SBedWars.callThunder(player.getLocation());
        String msg;
        if (player.getKiller() != null) {
            if (player.getKiller() instanceof Player) {
                msg = player.getDisplayName() + " §3被" + ((Player) player.getKiller()).getDisplayName() + " §3终结了" + "!";
                HashMap<String, Object> kd = playerData.get(player.getKiller().getName());
                kd.put("kill", ((int) kd.get("kill")) + 1);
                SBedWars.players.get(player.getKiller().getName()).addKill();
            } else {
                msg = player.getDisplayName() + " §3被" + player.getKiller().getName() + " §3终结了" + "!";
            }
        } else {
            msg = player.getDisplayName() + " §3不知道怎么就一命呜呼了!";
        }
        HashMap<String, Object> kd = playerData.get(player.getName());
        kd.put("death", ((int) kd.get("death")) + 1);
        SBedWars.players.get(player.getName()).addDeath();
        SBedWarsAPI.getInstance().broadcastMessage(this.getAllPlayers(), msg);
        SBedWars.getInstance().broadcastTitle(player, 20, 30, 20, "§c你阵亡了!", msg);
        for (Player player1 : this.getAllPlayers()) {
            if (player1 != player) {
                SBedWarsAPI.getInstance().broadcastTitle(this.getAllPlayers(), 20, 30, 20, "", msg);
            }
        }
        int teamID = this.getTeam(player.getName());
        HashMap<String, Object> teamData = rooms.get(id).getTeamData().get(teamID);
        if (teamData.containsKey("kill") && (boolean) teamData.get("kill")) {
            player.sendMessage(DEFAULT_TITLE + "§3你的队伍核心不在,你不能重生!");
            this.getTeam(teamID).remove(player.getName());
            this.getAlivePlayers().remove(player);
            if (teamID != -1) {
                if (this.getTeam(teamID).size() == 0) {
                    SBedWarsAPI.getInstance().broadcastMessage(this.getAllPlayers(), "\n  §l§f团灭 > 队伍:" + data.getTeamData().get(teamID).get("name") + " §6已被淘汰!");
                }
            }
            player.getInventory().clearAll();
            this.onSpectate(player);
        } else {
            event.setKeepInventory(false);
            player.getInventory().clearAll();
            int reviveTime = master.reviveTime;
            revive.put(player, reviveTime);
            player.teleport(player.getSpawn());
            player.setGamemode(3);
        }
        for (Item item : event.getDrops()) {
            if (item.getId() == 265 || item.getId() == 264 || item.getId() == 336 || item.getId() == Item.EMERALD) {
                player.level.dropItem(player.getLocation(), item);
            }
        }
        event.setDrops(new Item[]{});
        event.setCancelled(true);
    }

    public void onRevive(Player player) {
        player.teleport((Location) data.getTeamData().get(getTeam(player.getName())).get("gameLocation"));
        player.setGamemode(0);
        int health = 20;
        player.setMaxHealth(health);
        player.setHealth(health);
    }

    @SuppressWarnings("all")
    public void onSpectate(Player player) {
        this.getSpectatorPlayers().add(player);
        player.setGamemode(3);
        player.sendMessage(DEFAULT_TITLE + "§3你已经进入观战模式!");
        player.teleport(data.getWaitLocation());
    }

    void onStart() {
        this.distributionTeam();
        for (Integer id : this.teams.keySet()) {
            CopyOnWriteArrayList<String> teamPlayers = this.teams.get(id);
            for (String player : teamPlayers) {
                Player p = Server.getInstance().getPlayerExact(player);
                p.teleport(((Location) this.data.getTeamData().get(id).get("gameLocation")).add(0, 1.5, 0));
                p.setGamemode(0);
                p.setMaxHealth(20);
                p.setHealth(p.getMaxHealth());
                p.removeAllEffects();
            }
        }
        this.shopSpawnToAll();
        this.initEnderCrystal();
        SBedWars.getInstance().broadcastMessage(this.getAllPlayers(), "§l§a-------------------\n  §6起床战争:\n§e保护你的床并摧毁敌人的床。通过各种资源生成器收集银,金,铜来获得强大的游戏物品,让你和你的队伍变得更加强大!");
        SBedWarsAPI.getInstance().broadcastMessage(this.getAllPlayers(), "§l§7-------------------");
        SBedWarsAPI.getInstance().broadcastMessage(this.getAllPlayers(), DEFAULT_TITLE + "§2Game Starts!");
        SBedWarsAPI.getInstance().broadcastMessage(this.getAllPlayers(), "§l§7-------------------");
        this.setGameMode(3);
        this.resetSign();
        Server.getInstance().getScheduler().scheduleRepeatingTask(new GameTask(this.data, this), 20);
    }

    private void onStop(boolean win) {
        SBedWars.getInstance().getServer().getLogger().info(DEFAULT_TITLE + this.id + "号房间已经停止游戏");
        this.setGameMode(4);
        this.shopDespawnToAll();
        this.deSpawnEnderCrystal();
        for (Player player : this.getAllPlayers()) {
            if (win) {
                players.get(player.getName()).addLose();
            } else {
                players.get(player.getName()).addDraw();
            }
            player.sendMessage(DEFAULT_TITLE + "§5***************");
            player.sendMessage(DEFAULT_TITLE + "§6游戏已经结束!");
            player.sendMessage(DEFAULT_TITLE + "§6奖励金币" + master.money * 2 + "枚!");
            player.sendMessage(DEFAULT_TITLE + "§5***************");
            //Money.getInstance().addMoney(player.getName(), master.money);
            this.onQuit(player, false);
            player.setGamemode(0);
            player.getInventory().clearAll();
        }
        RankingList.update(1);
        RankingList.update(2);
        RankingList.update(3);
        this.resetSign();
        this.resetRoom();
    }

    void onWin(int id) {
        for (String name : this.teams.get(id)) {
            Player player = Server.getInstance().getPlayerExact(name);
            player.sendMessage(DEFAULT_TITLE + "§5***************");
            player.sendMessage(DEFAULT_TITLE + "§6恭喜你,获得了最后的胜利");
            player.sendMessage(DEFAULT_TITLE + "§6奖励金币" + master.money * 2 + "枚!");
            player.sendMessage(DEFAULT_TITLE + "§5***************");
            player.setTitleAnimationTimes(20, 30, 20);
            player.setSubtitle("§6你所在的队伍获得了最终胜利!");
            player.sendTitle(DEFAULT_TITLE);
            player.setSpawn(Server.getInstance().getDefaultLevel().getSafeSpawn());
            //TODO
            player.teleport(this.data.getStopLocation());
            player.getInventory().clearAll();
            players.get(name).addWin();
            //Money.getInstance().addMoney(name, master.money * 2);
            this.onQuit(player, false);
        }
        SBedWarsAPI.getInstance().broadcastTitle(getAllPlayers(), 0, 30, 0, "", TextFormat.colorize("&6队伍 &e" + rooms.get(this.id).getTeamData().get(id).get("name") + " &b获得了胜利!"));
        this.onStop(true);
    }

    void onDraw() {
        SBedWarsAPI.getInstance().broadcastMessage(this.getAllPlayers(), DEFAULT_TITLE + "平局!");
        SBedWarsAPI.getInstance().broadcastTitle(getAllPlayers(), 0, 30, 0, "", TextFormat.colorize("&6平局!"));
        this.onStop(false);
    }

    private void shopSpawnToAll() {
        for (int key : data.getTeamData().keySet()) {
            Location source = (Location) data.getTeamData().get(key).get("shopLocation");
            FullChunk chunk = source.getLevel().getChunk((int) source.x >> 4, (int) source.z >> 4, true);
            CompoundTag nbt = new CompoundTag().putList(new ListTag<DoubleTag>("Pos").add(new DoubleTag("", source.x)).add(new DoubleTag("", source.y)).add(new DoubleTag("", source.z)))
                    .putList(new ListTag<DoubleTag>("Motion").add(new DoubleTag("", 0)).add(new DoubleTag("", 0)).add(new DoubleTag("", 0)))
                    .putList(new ListTag<FloatTag>("Rotation").add(new FloatTag("", 0)).add(new FloatTag("", 0)))
                    .putInt("bedwars", id);
            EntityVillager villager = new EntityVillager(chunk, nbt);
            villager.setRotation(source.yaw, source.pitch);
            villager.setNameTag(DEFAULT_TITLE + "Shop");
            SetEntityDataPacket npk = new SetEntityDataPacket();
            long flags = 114688L;
            npk.metadata = (new EntityMetadata()).putLong(0, flags).putString(4, DEFAULT_TITLE + "Shop");
            npk.eid = villager.getId();
            for (Player player : getAllPlayers()) {
                villager.spawnTo(player);
                player.dataPacket(npk);
            }
            HashMap<String, Object> team = data.getTeamData().get(key);
            team.put("villager", villager);
        }
    }

    private void shopDespawnToAll() {
        for (int key : data.getTeamData().keySet()) {
            ((EntityVillager) data.getTeamData().get(key).get("villager")).despawnFromAll();
        }
    }

    private void resetRoom() {
        this.setGameMode(5);
        this.resetSign();
        games.remove(this.id);
        Server.getInstance().getLogger().info(DEFAULT_TITLE + "§5正在重载" + this.id + "号房间");
        try {
            SBedWarsAPI.getInstance().loadWorld(this.data.getGameWorld());
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.init();
        this.resetSign();
        Server.getInstance().getLogger().info(DEFAULT_TITLE + "§6重载" + this.id + "号房间成功！");
    }

    private void resetSign() {
        for (Location location : data.getSignLocation()) {
            if (!(location.getLevel().getBlock(location) instanceof BlockSignPost)) return;
            BlockEntity entity = location.getLevel().getBlockEntity(location);
            if (!(entity instanceof BlockEntitySign)) continue;
            BlockEntitySign sign = (BlockEntitySign) entity;
            String status;
            switch (this.getGameMode()) {
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
            sign.setText(DEFAULT_TITLE, status, "§l§b房间: §e" + this.id, "§l§c<§f---§b" + this.getAllPlayers().size() + "§7/" + data.getMax() + "§f---§c>");
        }
    }

    public CopyOnWriteArrayList<Player> getAllPlayers() {
        return gamePlayers.get(this.id).get("all");
    }

    public CopyOnWriteArrayList<Player> getAlivePlayers() {
        return gamePlayers.get(this.id).get("alive");
    }

    private CopyOnWriteArrayList<Player> getSpectatorPlayers() {
        return gamePlayers.get(this.id).get("spectator");
    }

    private void addPlayer(Player player) {
        this.getAlivePlayers().add(player);
        this.getAllPlayers().add(player);
    }

    private void delPlayer(Player player) {
        this.getAlivePlayers().remove(player);
        this.getAllPlayers().remove(player);
        this.getSpectatorPlayers().remove(player);
        try {
            CopyOnWriteArrayList<String> map = teams.get(this.getTeam(player.getName()));
            map.remove(player.getName());
            this.teams.put(this.getTeam(player.getName()), map);
        } catch (NullPointerException ignored) {
        }
    }

    public int getGameMode() {
        return gameMode;
    }

    private void setGameMode(int gameMode) {
        this.gameMode = gameMode;
    }

    public CopyOnWriteArrayList<String> getTeam(int id) {
        return this.teams.get(id);
    }

    private void choiceTeam(int id, String player) {
        boolean flag = true;
        for (CopyOnWriteArrayList<String> a : this.teams.values()) {
            if (a.size() + 1 < this.getTeam(id).size()) {
                flag = false;
                break;
            }
        }
        if (flag) {
            this.getTeam(id).add(player);
            Server.getInstance().getPlayerExact(player).setSpawn((Location) this.data.getTeamData().get(id).get("gameLocation"));
        }
    }

    public int getTeam(String player) {
        for (Integer id : this.teams.keySet()) {
            CopyOnWriteArrayList<String> players = this.teams.get(id);
            if (players.contains(player)) return id;
        }
        return -1;
    }

    private ArrayList<String> getNotHasTeamPlayers() {
        ArrayList<String> players = new ArrayList<>();
        for (Player player : this.getAlivePlayers()) {
            players.add(player.getName());
        }
        for (CopyOnWriteArrayList<String> player : this.teams.values()) {
            players.removeAll(player);
        }
        return players;
    }

    private void distributionTeam() {
        ArrayList<String> notHasTeamPlayers = this.getNotHasTeamPlayers();
        for (String player : notHasTeamPlayers) {
            int id = this.getMinId(this.getTeamSize());
            this.choiceTeam(id, player);
            Server.getInstance().getPlayerExact(player).sendMessage(DEFAULT_TITLE + "你没有选择队伍,系统已自动帮你选择队伍为" + this.data.getTeamData().get(id).get("name"));
        }
    }

    HashMap<Integer, Integer> getTeamSize() {
        HashMap<Integer, Integer> mdz = new HashMap<>();
        for (Integer id : this.teams.keySet()) {
            mdz.put(id, this.getTeam(id).size());
        }
        return mdz;
    }

    private int getMinId(HashMap<Integer, Integer> map) {
        if (map == null) return 0;
        Collection<Integer> c = map.values();
        Object[] obj = c.toArray();
        Arrays.sort(obj);
        int min = Integer.valueOf(obj[0].toString());
        for (Integer id : map.keySet()) {
            if (map.get(id) == min) return id;
        }
        return 0;
    }

    private void initEnderCrystal() {
        for (int key : data.getTeamData().keySet()) {
            if (this.getTeam(key).size() > 0) {
                Location pos = (Location) data.getTeamData().get(key).get("coreLocation");
                EntityEnderCrystal ec = new EntityEnderCrystal(pos);
                for (Player player : getAllPlayers()) {
                    ec.spawnTo(player);
                }
                data.getTeamData().get(key).put("ec", ec);
                long f = FloatingTextPacket.addFloatingText(
                        "§3队伍: " + data.getTeamData().get(key).get("name").toString() + "  §e能量水晶", "§a██████████   100%", pos.clone().add(0, 4));
                data.getTeamData().get(key).put("ecf", f);
            }
        }
    }

    private void deSpawnEnderCrystal() {
        for (int key : data.getTeamData().keySet()) {
            if (data.getTeamData().get(key).containsKey("ec")) {
                EntityEnderCrystal ec = (EntityEnderCrystal) data.getTeamData().get(key).get("ec");
                long eid = (long) data.getTeamData().get(key).get("ecf");
                FloatingTextPacket.removeFloatingText(eid);
                for (Player player : getAllPlayers()) {
                    ec.despawn(player);
                }
            }
        }
    }

    public boolean attackCrystal(Player player, Item item, long eid) {
        System.out.println(player.getName() + " Attack " + eid);
        int pid = this.getTeam(player.getName());
        int tid = -1;
        EntityEnderCrystal crystal = null;
        for (int key : data.getTeamData().keySet()) {
            if (data.getTeamData().get(key).containsKey("ec")) {
                EntityEnderCrystal ec = (EntityEnderCrystal) data.getTeamData().get(key).get("ec");
                System.out.println(key + ":" + ec.getEid());
                if (ec.getEid() == eid) {
                    tid = key;
                    crystal = ec;
                    break;
                }
            }
        }
        if (tid == -1) return false;
        if (pid == tid) {
            player.sendMessage(DEFAULT_TITLE + "你不能破坏你自己家里的核心");
            return true;
        }
        float damage = item.getAttackDamage();
        System.out.println("damage: " + damage);
        crystal.health -= damage;
        if (crystal.health <= 0) {
            //凉了
            for (Player player1 : this.getAllPlayers()) {
                crystal.despawn(player1);
            }
            data.getTeamData().get(tid).remove("ec");
            FloatingTextPacket.removeFloatingText((long) data.getTeamData().get(tid).get("ecf"));
            data.getTeamData().get(tid).remove("ecf");
            data.getTeamData().get(tid).put("kill", true);
            SBedWarsAPI.getInstance().broadcastTitle(this.getAllPlayers(), 30, 50, 30, DEFAULT_TITLE, TextFormat.colorize("&a队伍: &d" + data.getTeamData().get(tid).get("name") + "&b的核心被破坏"));
            SBedWarsAPI.getInstance().broadcastMessage(this.getAllPlayers(), "队伍:" + data.getTeamData().get(tid).get("name") + "的核心被破坏,此队伍将不可重生");
            SBedWars.players.get(player.getName()).addCore();
            this.playerData.get(player.getName()).put("core", (int) this.playerData.get(player.getName()).get("core") + 1);
        } else {
            this.updateCrystalFloatingText((long) data.getTeamData().get(tid).get("ecf"), crystal, tid);
        }
        return true;
    }

    private void updateCrystalFloatingText(long floatID, EntityEnderCrystal ec, int tid) {
        StringBuilder health = new StringBuilder();
        health.append("§a");
        for (int i = 10; i <= 100; i += 10) {
            if (ec.health >= i) {
                health.append("█");
            }
        }
        health.append("   ").append(ec.health).append("%");
        System.out.println(tid + " Update Floating Text :" + health.toString() + " ,Now Health:" + ec.health + " EID:" + floatID);
        FloatingTextPacket.setFloatingText(floatID, "§3队伍: " + data.getTeamData().get(tid).get("name").toString() + "  §e能量水晶", health.toString());
    }
}

class WaitTask extends PluginTask<SBedWars> {
    private int waitTick;
    private RoomData data;
    private BedWars game;

    WaitTask(RoomData data, BedWars game) {
        super(SBedWars.getInstance());
        this.data = data;
        this.game = game;
        this.waitTick = data.getWaitTime();
    }

    @Override
    public void onRun(int i) {
        this.waitTick--;
        if (game.getAlivePlayers().size() < this.data.getMin()) {
            SBedWarsAPI.getInstance().broadcastMessage(this.game.getAllPlayers(), "§c游戏人数不足，暂停开始游戏");
            this.cancel();
        }
        if (this.waitTick > 5 && game.getAlivePlayers().size() == this.data.getMax()) {
            this.waitTick = 5;
            SBedWarsAPI.getInstance().broadcastMessage(this.game.getAllPlayers(), "达到最大游戏人数,五秒后开始游戏");
        }
        switch (this.waitTick) {
            default:
                SBedWarsAPI.getInstance().broadcastTip(this.game.getAllPlayers(), DEFAULT_TITLE + "§6距离游戏开始还有" + waitTick + "秒");
                break;
            case 10:
                SBedWarsAPI.getInstance().broadcastTitle(this.game.getAllPlayers(), 0, 20, 0, DEFAULT_TITLE, "§l§210");
                SBedWarsAPI.getInstance().broadcastSound(this.game.getAllPlayers(), 1);
                break;
            case 9:
                SBedWarsAPI.getInstance().broadcastTitle(this.game.getAllPlayers(), 0, 20, 0, DEFAULT_TITLE, "§l§39");
                SBedWarsAPI.getInstance().broadcastSound(this.game.getAllPlayers(), 1);
                break;
            case 8:
                SBedWarsAPI.getInstance().broadcastTitle(this.game.getAllPlayers(), 0, 20, 0, DEFAULT_TITLE, "§l§48");
                SBedWarsAPI.getInstance().broadcastSound(this.game.getAllPlayers(), 1);
                break;
            case 7:
                SBedWarsAPI.getInstance().broadcastTitle(this.game.getAllPlayers(), 0, 20, 0, DEFAULT_TITLE, "§l§57");
                SBedWarsAPI.getInstance().broadcastSound(this.game.getAllPlayers(), 1);
                break;
            case 6:
                SBedWarsAPI.getInstance().broadcastTitle(this.game.getAllPlayers(), 0, 20, 0, DEFAULT_TITLE, "§l§66");
                SBedWarsAPI.getInstance().broadcastSound(this.game.getAllPlayers(), 1);
                break;
            case 5:
                SBedWarsAPI.getInstance().broadcastTitle(this.game.getAllPlayers(), 0, 20, 0, DEFAULT_TITLE, "§l§75");
                SBedWarsAPI.getInstance().broadcastSound(this.game.getAllPlayers(), 1);
                break;
            case 4:
                SBedWarsAPI.getInstance().broadcastTitle(this.game.getAllPlayers(), 0, 20, 0, DEFAULT_TITLE, "§l§a4");
                SBedWarsAPI.getInstance().broadcastSound(this.game.getAllPlayers(), 1);
                break;
            case 3:
                SBedWarsAPI.getInstance().broadcastTitle(this.game.getAllPlayers(), 0, 20, 0, DEFAULT_TITLE, "§l§b3");
                SBedWarsAPI.getInstance().broadcastSound(this.game.getAllPlayers(), 1);
                break;
            case 2:
                SBedWarsAPI.getInstance().broadcastTitle(this.game.getAllPlayers(), 0, 20, 0, DEFAULT_TITLE, "§l§c2");
                SBedWarsAPI.getInstance().broadcastSound(this.game.getAllPlayers(), 1);
                break;
            case 1:
                SBedWarsAPI.getInstance().broadcastTitle(this.game.getAllPlayers(), 0, 20, 0, DEFAULT_TITLE, "§l§d1");
                SBedWarsAPI.getInstance().broadcastSound(this.game.getAllPlayers(), 1);
                break;
            case 0:
                SBedWarsAPI.getInstance().broadcastTitle(this.game.getAllPlayers(), 0, 20, 0, DEFAULT_TITLE, "§l§eGO!");
                SBedWarsAPI.getInstance().broadcastSound(this.game.getAllPlayers(), 2);
                break;
        }
        if (this.waitTick <= 0) {
            this.game.onStart();
            this.cancel();
        }
    }
}

class GameTask extends PluginTask<SBedWars> {
    private int gameTick;
    private RoomData data;
    private BedWars game;
    private int tick;

    GameTask(RoomData data, BedWars game) {
        super(SBedWars.getInstance());
        this.data = data;
        this.game = game;
        this.gameTick = data.getGameTime();
    }

    private String int2Time(int time) {
        if (time < 60) {
            if (time < 10) {
                return "00:0" + time;
            } else {
                return "00:" + time;
            }
        } else {
            int min = (time - (time % 60)) / 60;
            return min + ":" + (time % 60 < 10 ? "0" + time % 60 : time % 60);
        }
    }

    @Override
    public void onRun(int currentTick) {
        this.tick++;
        this.gameTick--;
        this.checkTeam();
        for (Player player : this.game.getAlivePlayers()) {
            player.sendTip("                                                            " + DEFAULT_TITLE +
                    "\n                                              §f游戏时间:" + this.int2Time(this.tick)
                    + this.getTeamTip() + "\n" + this.getPersonalTip(player.getName())
            );
        }
        data.getGameLevel().setTime(1000);
        if (this.tick % data.getEmeraldDropSpeed() == 0) {
            for (Location location : data.getEmeraldLocation()) {
                if (location.level != null) {
                    int count = 0;
                    for (Entity entity : location.level.getEntities()) {
                        if (entity instanceof EntityItem) {
                            if (((EntityItem) entity).getItem().getId() == SBedWars.emerald.getId()) {
                                if (entity.distance(location) < 3) {
                                    count++;
                                }
                            }
                        }
                    }
                    if (count < 6) { //点位最大生成数
                        location.level.dropItem(location, SBedWars.emerald);
                    }
                }
            }
        }
        if (this.tick % data.getDiamondDropSpeed() == 0) {
            for (Location location : data.getDiamondLocation()) {
                if (location.level != null) {
                    int count = 0;
                    for (Entity entity : location.level.getEntities()) {
                        if (entity instanceof EntityItem) {
                            if (((EntityItem) entity).getItem().getId() == SBedWars.diamond.getId()) {
                                if (entity.distance(location) < 3) {
                                    count++;
                                }
                            }
                        }
                    }
                    if (count < 24) { //点位最大生成数
                        location.level.dropItem(location, SBedWars.diamond);
                    }
                }
            }
        }
        if (this.tick % data.getGoldDropSpeed() == 0) {
            for (Location location : data.getGoldLocation()) {
                if (location.level != null) {
                    int count = 0;
                    for (Entity entity : location.level.getEntities()) {
                        if (entity instanceof EntityItem) {
                            if (((EntityItem) entity).getItem().getId() == SBedWars.gold.getId()) {
                                if (entity.distance(location) < 3) {
                                    count++;
                                }
                            }
                        }
                    }
                    if (count < 96) { //点位最大生成数
                        location.level.dropItem(location, SBedWars.gold);
                    }
                }
            }
        }
        if (this.tick % data.getSilverDropSpeed() == 0) {
            for (Location location : data.getSilverLocation()) {
                if (location.level != null) {
                    int count = 0;
                    for (Entity entity : location.level.getEntities()) {
                        if (entity instanceof EntityItem) {
                            if (((EntityItem) entity).getItem().getId() == SBedWars.silver.getId()) {
                                if (entity.distance(location) < 3) {
                                    count++;
                                }
                            }
                        }
                    }
                    if (count < 192) { //点位最大生成数
                        location.level.dropItem(location, SBedWars.silver);
                    }
                }
            }
        }
        switch (this.gameTick) {
            case 10:
            case 9:
            case 8:
            case 7:
            case 6:
            case 5:
            case 4:
                SBedWarsAPI.getInstance().broadcastMessage(game.getAllPlayers(), DEFAULT_TITLE + "距离游戏结束还有" + this.gameTick + "秒");
                SBedWarsAPI.getInstance().broadcastSound(game.getAllPlayers(), 1);
                break;
            case 3:
                SBedWarsAPI.getInstance().broadcastMessage(game.getAllPlayers(), DEFAULT_TITLE + "距离游戏结束还有" + this.gameTick + "秒");
                SBedWarsAPI.getInstance().broadcastTitle(game.getAllPlayers(), 0, 20, 0, "§l§b3", null);
                SBedWarsAPI.getInstance().broadcastSound(game.getAllPlayers(), 1);
                break;
            case 2:
                SBedWarsAPI.getInstance().broadcastMessage(game.getAllPlayers(), DEFAULT_TITLE + "距离游戏结束还有" + this.gameTick + "秒");
                SBedWarsAPI.getInstance().broadcastTitle(game.getAllPlayers(), 0, 20, 0, "§l§e2", null);
                SBedWarsAPI.getInstance().broadcastSound(game.getAllPlayers(), 1);
                break;
            case 1:
                SBedWarsAPI.getInstance().broadcastMessage(game.getAllPlayers(), DEFAULT_TITLE + "距离游戏结束还有" + this.gameTick + "秒");
                SBedWarsAPI.getInstance().broadcastTitle(game.getAllPlayers(), 0, 20, 0, "§l§d1", null);
                SBedWarsAPI.getInstance().broadcastSound(game.getAllPlayers(), 1);
                break;
            case 0:
                SBedWarsAPI.getInstance().broadcastTitle(game.getAllPlayers(), 0, 20, 0, DEFAULT_TITLE, "§l§c游戏结束!");
                SBedWarsAPI.getInstance().broadcastMessage(game.getAllPlayers(), "§l§7-------------------");
                SBedWarsAPI.getInstance().broadcastMessage(game.getAllPlayers(), DEFAULT_TITLE + "§l§cGame Stops!");
                SBedWarsAPI.getInstance().broadcastMessage(game.getAllPlayers(), "§l§7-------------------");
                SBedWarsAPI.getInstance().broadcastSound(game.getAllPlayers(), 2);
                break;
        }
        if (this.gameTick <= 0) {
            game.onDraw();
            this.cancel();
        }
    }

    private String getTeamTip() {
        StringBuilder tip = new StringBuilder();
        for (int key : data.getTeamData().keySet()) {
            HashMap<String, Object> value = data.getTeamData().get(key);
            tip.append("\n                                              ");
            EntityEnderCrystal ec = (EntityEnderCrystal) data.getTeamData().get(key).get("ec");
            if ((!value.containsKey("ec")) || (boolean) value.getOrDefault("kill", false)) {
                tip.append("§6§l队伍:").append(value.get("name")).append(" §3剩余人数:").append(game.getTeam(key).size());
            } else {
                tip.append("§6§l队伍:").append(value.get("name")).append(" §a血量:").append(ec.health).append("%");
            }
        }
        tip.append("\n\n\n");
        return tip.toString();
    }

    private String getPersonalTip(String name) {
        return "                                              §b杀敌数:" + game.playerData.get(name).get("kill") + "\n" +
                "                                              §b死亡数:" + game.playerData.get(name).get("death") + "\n" +
                "                                              §4摧毁水晶数:" + game.playerData.get(name).get("core");
    }

    private void checkTeam() {
        HashMap<Integer, Integer> map = game.getTeamSize();
        int i = 0;
        int team = -1;
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            if (entry.getValue() == 0) {
                i++;
            } else {
                team = entry.getKey();
            }
        }
        if (i == map.size() - 1) {
            if (team != -1) {
                game.onWin(team);
                this.cancel();
            }
        }
    }
}
