package net.mcpes.foundhi.plugin.game.bedwars.config;

import cn.nukkit.Server;
import cn.nukkit.level.Location;
import cn.nukkit.utils.SimpleConfig;
import net.mcpes.foundhi.plugin.game.bedwars.SBedWars;

/**
 * @author hhm
 * @date 2017/7/22
 * @since SBedWars
 */

public class MasterConfig extends SimpleConfig {
    @Path(value = "金币奖励数量")
    public int money = 10000;

    @Path(value = "KD排行榜位置")
    public String kdRanking = "";

    @Path(value = "击杀排行榜位置")
    public String killRanking = "";

    @Path(value = "拆床排行榜位置")
    public String breakRanking = "";

    @Path(value = "胜利排行榜位置")
    public String winRanking = "";

    @Path(value = "服主")
    public String realOwner = "hhm";

    @Path(value = "数据牌子查看")
    public String dataSign = "";

    @Path(value = "复活时间")
    public int reviveTime = 5;


    public MasterConfig() {
        super(SBedWars.getInstance());
    }


    public static Location getLocation(String m) {
        String[] a = m.split(":");
        if (a.length != 4) return null;
        if (Server.getInstance().isLevelLoaded(a[3])) {
            return new Location(Integer.parseInt(a[0]), Integer.parseInt(a[1]), Integer.parseInt(a[2]), Server.getInstance().getLevelByName(a[3]));
        } else return null;
    }
}
