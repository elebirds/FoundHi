package net.mcpes.foundhi.plugin.rpg.rpg.manager.attributes;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import net.mcpes.foundhi.plugin.rpg.rpg.Utils;
import net.mcpes.foundhi.plugin.rpg.rpg.manager.BuildManager;

import java.util.Map;

/**
 * FoundHi-RPG
 *
 * @author yui Copyright (c) 2018/6/6
 * version 1.0
 */
@SuppressWarnings("unchecked")
public class Healing extends Attribute {
    private int healing;//治疗倍数
    private int healingRange;//治疗范围
    private int healingPR;//治疗机率

    public Healing(int healingPR, int healingRange, int healing) {
        this.healingPR = healingPR;
        this.healing = healing;
        this.healingRange = healingRange;
    }

    @Override
    public boolean needDamage() {
        return true;
    }

    @Override
    public float doSkill(Entity attack, Entity entity, float damage) {
        int PRAdd = 0;
        int healingAdd = 0;
        int rangeAdd = 0;
        if (BuildManager.containPlayerBuild(attack.getName())) {
            if (BuildManager.containAdd(attack.getName(), "治疗")) {
                Map<String, Object> content = (Map<String, Object>) BuildManager.getContent(attack.getName(), "治疗");
                PRAdd = (int) content.get("机率");
                healingAdd = (int) content.get("百分比");
                rangeAdd = ((Double) content.get("范围")).intValue();
            }
        }
        if (Utils.random(healingPR + PRAdd)) {
            Map<Long, Player> players = attack.getLevel().getPlayers();
            for (Player player : players.values()) {
                int px = player.getFloorX();
                int pz = player.getFloorZ();
                int ax = attack.getFloorX();
                int az = attack.getFloorZ();
                if (Math.abs(px - ax) <= (healingRange + rangeAdd) && Math.abs(pz - az) <= (healingRange + rangeAdd)) {
                    player.setHealth(player.getHealth() + damage * ((healing + healingAdd) / 100));
                    if (attack instanceof Player) {
                        Utils.addText((Player) attack, "§l§4治疗！", player.getX(), player.getY() + player.getEyeHeight(), player.getZ());
                    }
                }
            }
        }
        return 0f;
    }
}
