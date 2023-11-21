package net.mcpes.foundhi.plugin.game.bedwars.utils;

import net.mcpes.foundhi.plugin.game.bedwars.config.PlayerConfig;
import net.mcpes.foundhi.plugin.game.bedwars.config.RankingConfig;

import static net.mcpes.foundhi.plugin.game.bedwars.SBedWars.players;

/**
 * @author hhm @ SBedWars Project
 **/
public class PlayerObject {
    private final String name;
    private int win;
    private int lose;
    private int draw;
    private int kill;
    private int death;
    private int core;
    private RankingConfig rc;


    public PlayerObject(String name) {
        this.name = name;
        rc = new RankingConfig(name);
        PlayerConfig pc = new PlayerConfig(name);
        this.win = pc.getConfig().getInt("win");
        this.lose = pc.getConfig().getInt("lose");
        this.draw = pc.getConfig().getInt("draw");
        this.kill = pc.getConfig().getInt("kill");
        this.death = pc.getConfig().getInt("death");
        this.core = pc.getConfig().getInt("core");
    }

    public String getName() {
        return name;
    }

    public void save() {
        PlayerConfig pc = new PlayerConfig(name);
        pc.getConfig().set("win", win);
        pc.getConfig().set("lose", lose);
        pc.getConfig().set("draw", draw);
        pc.getConfig().set("kill", kill);
        pc.getConfig().set("death", death);
        pc.getConfig().set("core", core);
        pc.getConfig().save();
        players.remove(name);
    }

    public int getWin() {
        return win;
    }

    public void setWin(int win) {
        this.win = win;
    }

    public int getLose() {
        return lose;
    }

    public void setLose(int lose) {
        this.lose = lose;
    }

    public int getDraw() {
        return draw;
    }

    public void setDraw(int draw) {
        this.draw = draw;
    }

    public int getKill() {
        return kill;
    }

    public void setKill(int kill) {
        this.kill = kill;
    }

    public int getDeath() {
        return death;
    }

    public void setDeath(int death) {
        this.death = death;
    }

    public int getCore() {
        return core;
    }

    public void setCore(int core) {
        this.core = core;
    }

    public void addWin() {
        this.win++;
        rc.addWin();
    }

    public void addLose() {
        this.lose++;
    }

    public void addDraw() {
        this.draw++;
    }

    public void addKill() {
        this.kill++;
        rc.addKill();
    }

    public void addDeath() {
        this.death++;
        rc.addDeath();
    }

    public void addCore() {
        this.core++;
        rc.addBreak();
    }
}
