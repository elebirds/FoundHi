package net.mcpes.foundhi.plugin.rpg.rpg.manager.attributes;

import cn.nukkit.entity.Entity;
import net.mcpes.foundhi.plugin.rpg.rpg.manager.BuildManager;

/**
 * RPG
 *
 * @author yui Copyright (c) 2018/7/5
 * version 1.0
 */
public class Defend extends Attribute {
    private int defense;//防御

    public Defend(int defense) {
        this.defense = defense;
    }

    @Override
    public float doSkill(Entity attack, Entity entity, float damage) {
        int defenseAdd = 0;
        if (BuildManager.containPlayerBuild(entity.getName())) {
            if (BuildManager.containAdd(entity.getName(), "防御")) {
                defenseAdd = (int) BuildManager.getContent(entity.getName(), "防御");
            }
        }
        return -damage * (1 - (100 - (defense + defenseAdd)) / 100);
    }
}
