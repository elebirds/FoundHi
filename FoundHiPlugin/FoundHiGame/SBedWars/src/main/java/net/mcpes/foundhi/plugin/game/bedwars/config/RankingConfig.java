package net.mcpes.foundhi.plugin.game.bedwars.config;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.utils.Config;
import net.mcpes.foundhi.plugin.game.bedwars.SBedWars;
import net.mcpes.foundhi.plugin.game.bedwars.utils.PlayerObject;

import java.io.File;
import java.text.DecimalFormat;
import java.util.HashMap;

import static net.mcpes.foundhi.plugin.game.bedwars.SBedWars.*;

/**
 * @author hhm @ SBedWars Project
 **/
public class RankingConfig {
    private static File file;
    private static Config config;

    static {
        file = new File(SBedWars.getInstance().getDataFolder() + "/ranking.yml");
        config = new Config(file, 2);
    }

    private String name;

    public RankingConfig(String name) {
        this.name = name;
        this.initP();
    }

    public static void init() {
        rankingData.put("kill", getData("kill"));
        rankingData.put("break", getData("break"));
        rankingData.put("death", getData("death"));
        rankingData.put("win", getData("win"));
        kdData = config.get("kd", new HashMap<>());
    }

    private static HashMap<String, Integer> getData(String name) {
        if (config.get(name) == null) config.set(name, new HashMap<>());
        return config.get(name, new HashMap<>());
    }

    public static void save() {
        config.set("kill", rankingData.get("kill"));
        config.set("break", rankingData.get("break"));
        config.set("death", rankingData.get("death"));
        config.set("win", rankingData.get("win"));
        config.set("kd", kdData);
        config.save();
    }

    private void initP() {
        HashMap<String, Integer> data = rankingData.get("kill");
        data.put(name, 0);
        rankingData.put("kill", data);
        data = rankingData.get("break");
        data.put(name, 0);
        rankingData.put("break", data);
        data = rankingData.get("win");
        data.put(name, 0);
        rankingData.put("win", data);
        data = rankingData.get("death");
        data.put(name, 0);
        rankingData.put("death", data);
        kdData.put(name, 0.00);
    }

    public void addKill() {
        HashMap<String, Integer> data = rankingData.get("kill");
        if (data.containsKey(name)) {
            data.put(name, data.get(name) + 1);
        } else {
            data.put(name, 1);
        }
        rankingData.put("kill", data);
        this.setKD();
    }


    public void addWin() {
        HashMap<String, Integer> data = rankingData.get("win");
        if (data.containsKey(name)) {
            data.put(name, data.get(name) + 1);
        } else {
            data.put(name, 1);
        }
        rankingData.put("win", data);
        this.setKD();
    }

    public void addBreak() {
        HashMap<String, Integer> data = rankingData.get("break");
        if (data.containsKey(name)) {
            data.put(name, data.get(name) + 1);
        } else {
            data.put(name, 1);
        }
        rankingData.put("break", data);
    }

    public void addDeath() {
        HashMap<String, Integer> data = rankingData.get("death");
        if (data.containsKey(name)) {
            data.put(name, data.get(name) + 1);
        } else {
            data.put(name, 1);
        }
        rankingData.put("death", data);
        this.setKD();
    }

    private void setKD() {
        int kill;
        int death;
        Player player = Server.getInstance().getPlayerExact(name);
        if (player != null && player.isOnline() && players.containsKey(name)) {
            kill = players.get(name).getKill();
            death = players.get(name).getDeath();
        } else {
            PlayerObject pd = new PlayerObject(name);
            kill = pd.getKill();
            death = pd.getDeath();
        }
        kdData.put(name, Double.valueOf(new DecimalFormat("0.00").format(((double) kill) / (death == 0 ? 1.0d : ((double) death)))));
    }
}
