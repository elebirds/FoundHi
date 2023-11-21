package net.mcpes.foundhi.plugin.rpg.rpg.manager.attributes;

import cn.nukkit.entity.Entity;
import net.mcpes.foundhi.plugin.rpg.rpg.manager.BuildManager;

/**
 * RPG
 *
 * @author yui Copyright (c) 2018/7/5
 * version 1.0
 */
public class DamageReduce extends Attribute {
    private float reduction;

    public DamageReduce(float reduction) {
        this.reduction = reduction;
    }

    @Override
    public float doSkill(Entity attack, Entity entity) {
        int reductionAdd = 0;
        if (BuildManager.containPlayerBuild(entity.getName())) {
            if (BuildManager.containAdd(entity.getName(), "减伤")) {
                reductionAdd = (int) BuildManager.getContent(entity.getName(), "减伤");
            }
        }
        return -reduction - reductionAdd;
    }
}
