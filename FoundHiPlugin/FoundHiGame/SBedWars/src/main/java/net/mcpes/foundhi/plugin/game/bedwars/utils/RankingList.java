package net.mcpes.foundhi.plugin.game.bedwars.utils;

import cn.nukkit.level.Location;
import net.mcpes.foundhi.plugin.game.bedwars.config.MasterConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.mcpes.foundhi.plugin.game.bedwars.SBedWars.*;

/**
 * @author hhm @ SBedWars Project
 **/
public class RankingList {
    private static long KDRankingEid = -1;
    private static long KillRankingEid = -1;
    private static long BreakRankingEid = -1;
    private static long WinRankingEid = -1;
    private static String KDRankingPos;
    private static String KillRankingPos;
    private static String BreakRankingPos;
    private static String WinRankingPos;

    public static void init() {
        KDRankingPos = master.kdRanking;
        KillRankingPos = master.killRanking;
        BreakRankingPos = master.breakRanking;
        WinRankingPos = master.winRanking;
        Location kdL = MasterConfig.getLocation(master.kdRanking);
        if (kdL != null) KDRankingEid = FloatingTextPacket.addFloatingText("§eKD 大神榜", "", kdL);
        Location killL = MasterConfig.getLocation(master.killRanking);
        if (killL != null) KillRankingEid = FloatingTextPacket.addFloatingText("§e击杀 大神榜", "", killL);
        Location breakL = MasterConfig.getLocation(master.breakRanking);
        if (breakL != null) BreakRankingEid = FloatingTextPacket.addFloatingText("§e拆床 大神榜", "", breakL);
        Location winL = MasterConfig.getLocation(master.winRanking);
        if (winL != null) WinRankingEid = FloatingTextPacket.addFloatingText("§e胜利 大神榜", "", winL);
    }

    public static long getBreakRankingEid() {
        return BreakRankingEid;
    }

    public static long getKDRankingEid() {
        return KDRankingEid;
    }

    public static long getKillRankingEid() {
        return KillRankingEid;
    }

    public static long getWinRankingEid() {
        return WinRankingEid;
    }

    public static String getBreakRankingPos() {
        return BreakRankingPos;
    }

    public static String getKDRankingPos() {
        return KDRankingPos;
    }

    public static String getKillRankingPos() {
        return KillRankingPos;
    }

    public static String getWinRankingPos() {
        return WinRankingPos;
    }

    public static void reInit() {
        if (KDRankingEid == -1) {
            StringBuilder text = new StringBuilder();
            for (String r : getRanking(1)) text.append(r).append("\n");
            Location kdL = MasterConfig.getLocation(master.kdRanking);
            if (kdL != null) KDRankingEid = FloatingTextPacket.addFloatingText("§l§eKD 大神榜", text.toString(), kdL);
        } else if (!KDRankingPos.equals(master.kdRanking)) {
            FloatingTextPacket.removeFloatingText(KDRankingEid);
            KDRankingPos = master.kdRanking;
            StringBuilder text = new StringBuilder();
            for (String r : getRanking(1)) text.append(r).append("\n");
            Location kdL = MasterConfig.getLocation(master.kdRanking);
            if (kdL != null) KDRankingEid = FloatingTextPacket.addFloatingText("§l§eKD 大神榜", text.toString(), kdL);
        }
        if (KillRankingEid == -1) {
            StringBuilder text = new StringBuilder();
            for (String r : getRanking(2)) text.append(r).append("\n");
            Location killL = MasterConfig.getLocation(master.killRanking);
            if (killL != null)
                KillRankingEid = FloatingTextPacket.addFloatingText("§l§e击杀 大神榜", text.toString(), killL);
        } else if (!KillRankingPos.equals(master.killRanking)) {
            FloatingTextPacket.removeFloatingText(KillRankingEid);
            KillRankingPos = master.killRanking;
            StringBuilder text = new StringBuilder();
            for (String r : getRanking(2)) text.append(r).append("\n");
            Location kdL = MasterConfig.getLocation(master.killRanking);
            if (kdL != null) KDRankingEid = FloatingTextPacket.addFloatingText("§l§e击杀 大神榜", text.toString(), kdL);
        }
        if (BreakRankingEid == -1) {
            StringBuilder text = new StringBuilder();
            for (String r : getRanking(3)) text.append(r).append("\n");
            Location breakL = MasterConfig.getLocation(master.breakRanking);
            if (breakL != null)
                BreakRankingEid = FloatingTextPacket.addFloatingText("§l§e拆床 大神榜", text.toString(), breakL);
        } else if (!BreakRankingPos.equals(master.breakRanking)) {
            FloatingTextPacket.removeFloatingText(BreakRankingEid);
            BreakRankingPos = master.breakRanking;
            StringBuilder text = new StringBuilder();
            for (String r : getRanking(3)) text.append(r).append("\n");
            Location kdL = MasterConfig.getLocation(master.killRanking);
            if (kdL != null) KDRankingEid = FloatingTextPacket.addFloatingText("§l§e拆床 大神榜", text.toString(), kdL);
        }
        if (WinRankingEid == -1) {
            StringBuilder text = new StringBuilder();
            for (String r : getRanking(4)) text.append(r).append("\n");
            Location winL = MasterConfig.getLocation(master.winRanking);
            if (winL != null)
                WinRankingEid = FloatingTextPacket.addFloatingText("§l§e胜利 大神榜", text.toString(), winL);
        } else if (!WinRankingPos.equals(master.winRanking)) {
            FloatingTextPacket.removeFloatingText(WinRankingEid);
            WinRankingPos = master.winRanking;
            StringBuilder text = new StringBuilder();
            for (String r : getRanking(4)) text.append(r).append("\n");
            Location kdL = MasterConfig.getLocation(master.killRanking);
            if (kdL != null) KDRankingEid = FloatingTextPacket.addFloatingText("§l§e胜利 大神榜", text.toString(), kdL);
        }
    }

    public static void update(int type) {
        if (type == 1) {
            if (KDRankingEid == -1) return;
            StringBuilder text = new StringBuilder();
            for (String r : getRanking(1)) text.append(r).append("\n");
            FloatingTextPacket.setFloatingText(KDRankingEid, "§l§eKD 大神榜", text.toString());
            return;
        }
        if (type == 2) {
            //KILL
            if (KillRankingEid == -1) return;
            StringBuilder text = new StringBuilder();
            for (String r : getRanking(2)) text.append(r).append("\n");
            FloatingTextPacket.setFloatingText(KillRankingEid, "§l§e击杀 大神榜", text.toString());
            return;
        }
        if (type == 3) {
            //BREAK
            if (BreakRankingEid == -1) return;
            StringBuilder text = new StringBuilder();
            for (String r : getRanking(3)) text.append(r).append("\n");
            FloatingTextPacket.setFloatingText(BreakRankingEid, "§l§e拆床 大神榜", text.toString());
        }
        if (type == 3) {
            //WIN
            if (WinRankingEid == -1) return;
            StringBuilder text = new StringBuilder();
            for (String r : getRanking(4)) text.append(r).append("\n");
            FloatingTextPacket.setFloatingText(WinRankingEid, "§l§e胜利 大神榜", text.toString());
        }
    }

    private static String[] getRanking(int type) {
        if (type > 1) {
            List<Map.Entry<String, Integer>> ranking;
            ArrayList<String> array = new ArrayList<>();
            int tick = 1;
            switch (type) {
                case 2:
                    ranking = getDescendingOrderInt(rankingData.get("kill"));
                    if (ranking.isEmpty()) {
                        return new String[0];
                    }
                    for (Map.Entry<String, Integer> i : ranking) {
                        if (tick > 8) {
                            break;
                        }
                        array.add("§3■TOP" + tick + "■ §c击杀:§6" + i.getValue() + " §a玩家: §6" + i.getKey());
                        tick++;
                    }
                    break;
                case 3:
                    ranking = getDescendingOrderInt(rankingData.get("break"));
                    if (ranking.isEmpty()) {
                        return new String[0];
                    }
                    for (Map.Entry<String, Integer> i : ranking) {
                        if (tick > 8) {
                            break;
                        }
                        array.add("§3■TOP" + tick + "■ §c拆床:§6" + i.getValue() + " §a玩家: §6" + i.getKey());
                        tick++;
                    }
                    break;
                case 4:
                    ranking = getDescendingOrderInt(rankingData.get("win"));
                    if (ranking.isEmpty()) {
                        return new String[0];
                    }
                    for (Map.Entry<String, Integer> i : ranking) {
                        if (tick > 8) {
                            break;
                        }
                        array.add("§3■TOP" + tick + "■ §c胜利:§6" + i.getValue() + " §a玩家: §6" + i.getKey());
                        tick++;
                    }
                    break;
            }
            return array.toArray(new String[array.size()]);
        }
        List<Map.Entry<String, Double>> ranking;
        ranking = getDescendingOrderDouble(kdData);
        if (ranking.isEmpty()) {
            return new String[0];
        }
        int tick = 1;
        ArrayList<String> array = new ArrayList<>();
        for (Map.Entry<String, Double> i : ranking) {
            if (tick > 8) {
                break;
            }
            array.add("§3■TOP" + tick + "■ §cKD:§6" + i.getValue() + " §a玩家: §6" + i.getKey());
            tick++;
        }
        return array.toArray(new String[array.size()]);
    }

    private static List<Map.Entry<String, Integer>> getDescendingOrderInt(HashMap<String, Integer> data) {
        List<Map.Entry<String, Integer>> list_Data = new ArrayList<>(data.entrySet());
        list_Data.sort((o1, o2) -> o2.getValue().compareTo(o1.getValue()));
        return list_Data;
    }

    private static List<Map.Entry<String, Double>> getDescendingOrderDouble(HashMap<String, Double> data) {
        List<Map.Entry<String, Double>> list_Data = new ArrayList<>(data.entrySet());
        list_Data.sort((o1, o2) -> o2.getValue().compareTo(o1.getValue()));
        return list_Data;
    }
}
