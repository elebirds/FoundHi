package net.mcpes.foundhi.plugin.currency.mw.utils;

import java.util.ArrayList;
import java.util.Map;

/**
 * @author hhm
 * @date 2017/7/7
 * @since SMultiWorld
 */

public class World {
    private String name;
    private boolean pvp;
    private boolean pve;
    private boolean grow;
    private boolean drop;
    private boolean water_flow;
    private boolean lava_flow;
    private boolean tnt_boom;
    private boolean creeper_boom;
    private boolean move_check;
    private boolean destroy;
    private boolean place;
    private ArrayList<Integer> gameMode;
    private int maxPlayer;
    private int money;
    private String join_message;
    private String quit_message;
    private ArrayList<String> can_destroyAndPlace;
    private int weather;
    private boolean dropItem;

    public World(String name, Map<String, Object> data) {
        this(name, Boolean.valueOf(data.get("PVP伤害开启").toString()), Boolean.valueOf(data.get("PVE伤害开启").toString()), Boolean.valueOf(data.get("农作物生长").toString()), Boolean.valueOf(data.get("死亡掉落").toString()), Boolean.valueOf(data.get("水流动").toString()), Boolean.valueOf(data.get("岩浆流动").toString()), Boolean.valueOf(data.get("移动检测").toString()), Boolean.valueOf(data.get("TNT爆炸破坏方块").toString()), Boolean.valueOf(data.get("苦力怕爆炸破坏方块").toString()), Boolean.valueOf(data.get("世界是否可破坏").toString()), Boolean.valueOf(data.get("世界是否可放置").toString()), (ArrayList<Integer>) data.get("地图游戏模式"), (ArrayList<String>) data.get("可破环与放置人员"), Integer.valueOf(data.get("地图最大人数").toString()), Integer.valueOf(data.get("进入地图花费").toString()), data.get("进入世界消息").toString(), data.get("离开世界消息").toString(), Integer.valueOf(data.getOrDefault("天气", -1).toString()),
                Boolean.valueOf(data.getOrDefault("丢弃物品", true).toString()));
    }

    private World(String name, boolean pvp, boolean pve, boolean grow, boolean drop, boolean water_flow, boolean lava_flow, boolean move_check, boolean tnt_boom, boolean creeper_boom, boolean destroy, boolean place, ArrayList<Integer> gameMode, ArrayList<String> can_destroyAndPlace, int maxPlayer, int money, String join_message, String quit_message, int weather, boolean dropItem) {
        this.name = name;
        this.pve = pve;
        this.pvp = pvp;
        this.gameMode = gameMode;
        this.grow = grow;
        this.drop = drop;
        this.water_flow = water_flow;
        this.lava_flow = lava_flow;
        this.tnt_boom = tnt_boom;
        this.creeper_boom = creeper_boom;
        this.maxPlayer = maxPlayer;
        this.money = money;
        this.join_message = join_message;
        this.quit_message = quit_message;
        this.move_check = move_check;
        this.can_destroyAndPlace = can_destroyAndPlace;
        this.destroy = destroy;
        this.place = place;
        this.dropItem = dropItem;
        this.weather = weather;
    }

    public static World fromMap(String name, Map<String, Object> data) {
        return new World(name, Boolean.valueOf(data.get("PVP伤害开启").toString()),
                Boolean.valueOf(data.get("PVE伤害开启").toString()),
                Boolean.valueOf(data.get("农作物生长").toString()),
                Boolean.valueOf(data.get("死亡掉落").toString()),
                Boolean.valueOf(data.get("水流动").toString()),
                Boolean.valueOf(data.get("岩浆流动").toString()),
                Boolean.valueOf(data.get("移动检测").toString()),
                Boolean.valueOf(data.get("TNT爆炸破坏方块").toString()),
                Boolean.valueOf(data.get("苦力怕爆炸破坏方块").toString()),
                Boolean.valueOf(data.get("世界是否可破坏").toString()),
                Boolean.valueOf(data.get("世界是否可放置").toString()),
                (ArrayList<Integer>) data.get("地图游戏模式"),
                (ArrayList<String>) data.get("可破环与放置人员"),
                Integer.valueOf(data.get("地图最大人数").toString()),
                Integer.valueOf(data.get("进入地图花费").toString()),
                data.get("进入世界消息").toString(),
                data.get("离开世界消息").toString(),
                Integer.valueOf(data.getOrDefault("天气", -1).toString()),
                Boolean.valueOf(data.getOrDefault("丢弃物品", true).toString()));
    }

    public String getName() {
        return name;
    }

    public ArrayList<Integer> getGameMode() {
        return gameMode;
    }

    public int getMaxPlayer() {
        return maxPlayer;
    }

    public String getJoin_message() {
        return join_message;
    }

    public String getQuit_message() {
        return quit_message;
    }

    public int getMoney() {
        return money;
    }

    public boolean isGrow() {
        return grow;
    }

    public boolean isCreeper_boom() {
        return creeper_boom;
    }

    public boolean isTnt_boom() {
        return tnt_boom;
    }

    public boolean isDrop() {
        return drop;
    }

    public boolean isLava_flow() {
        return lava_flow;
    }

    public boolean isPve() {
        return pve;
    }

    public boolean isPvp() {
        return pvp;
    }

    public boolean isWater_flow() {
        return water_flow;
    }

    public boolean isMove_check() {
        return move_check;
    }

    public boolean isDestroy() {
        return destroy;
    }

    public boolean isPlace() {
        return place;
    }

    public ArrayList<String> getCan_destroyAndPlace() {
        return can_destroyAndPlace;
    }

    public int getWeather() {
        return weather;
    }

    public boolean isDropItem() {
        return dropItem;
    }
}