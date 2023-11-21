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
public class Burning extends Attribute {
    private int burningPR;
    private int burningTime;

    public Burning(int burningPR, int burningTime) {
        this.burningPR = burningPR;
        this.burningTime = burningTime;
    }

    @Override
    public float doSkill(Entity attack, Entity entity) {
        int PRAdd = 0;
        int timeAdd = 0;
        if (BuildManager.containPlayerBuild(attack.getName())) {
            if (BuildManager.containAdd(attack.getName(), "燃烧")) {
                Map<String, Object> content = (Map<String, Object>) BuildManager.getContent(attack.getName(), "燃烧");
                PRAdd = (int) content.get("机率");
                timeAdd = (int) content.get("时间");
            }
        }
        if (Utils.random(this.burningPR + PRAdd)) {
            entity.setOnFire(this.burningTime + timeAdd);
            if (attack instanceof Player) {
                Utils.addText((Player) attack, "§l§c燃烧！", entity.getX(), entity.getY() + entity.getEyeHeight(), entity.getZ());
            }
        }
        return 0f;
    }
}
