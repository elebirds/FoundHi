package net.mcpes.foundhi.plugin.game.bedwars.config;

import cn.nukkit.utils.Config;
import net.mcpes.foundhi.plugin.game.bedwars.SBedWars;
import net.mcpes.foundhi.plugin.game.bedwars.data.RoomData;

import java.io.File;
import java.util.LinkedHashMap;

/**
 * @author hhm
 * @date 2017/7/13
 * @since SBedWars
 */

public class RoomConfig {
    private final int id;
    private File file;
    private Config config;

    public RoomConfig(int id) {
        this.id = id;
        this.file = new File(SBedWars.getInstance().getDataFolder() + "/rooms/" + id + ".yml");
        this.config = new Config(this.file, 2);
    }

    public Config getConfig() {
        return config;
    }

    public int getId() {
        return id;
    }

    public void addRoom(RoomData data) {
        LinkedHashMap<String, Object> room = new LinkedHashMap<>();
        room.put("id", data.getId());
        room.put("disName", data.getDisName());
        room.put("min-player", data.getMin());
        room.put("max-player", data.getMax());
        room.put("wait-time", data.getWaitTime());
        room.put("wait-pos", data.getWaitPos());
        room.put("game-time", data.getGameTime());
        room.put("game-world", data.getGameWorld());
        room.put("stop-pos", data.getStopPos());
        room.put("voidKill", data.getVoidKill());
        room.put("teamData", data.getTeamData());
        room.put("sign-pos", data.getSignPos());
        room.put("diamond-pos", data.getDiamondPos());
        room.put("gold-pos", data.getGoldPos());
        room.put("silver-pos", data.getSilverPos());
        room.put("diamond-drop-speed", data.getDiamondDropSpeed());
        room.put("gold-drop-speed", data.getGoldDropSpeed());
        room.put("silver-drop-speed", data.getSilverDropSpeed());
        this.getConfig().setAll(room);
        this.getConfig().save();
    }

    @SuppressWarnings("all")
    public void delRoom() {
        this.getConfig().setAll(new LinkedHashMap<>());
        this.getConfig().save();
        this.file.delete();
    }
}
