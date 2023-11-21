package net.mcpes.foundhi.plugin.rpg.rpg.manager.attributes;

import cn.nukkit.entity.Entity;
import cn.nukkit.event.entity.EntityDamageEvent;
import net.mcpes.foundhi.plugin.rpg.rpg.Utils;
import net.mcpes.foundhi.plugin.rpg.rpg.manager.BuildManager;

import java.util.Map;

/**
 * RPG
 *
 * @author yui Copyright (c) 2018/7/5
 * version 1.0
 */
@SuppressWarnings("unchecked")
public class ReturnDamage extends Attribute {
    private int returnDamage;
    private int returnDamagePR;

    public ReturnDamage(int returnDamage, int returnDamagePR) {
        this.returnDamage = returnDamage;
        this.returnDamagePR = returnDamagePR;
    }

    @Override
    public float doSkill(Entity attack, Entity entity, float damage) {
        int PRAdd = 0;
        int damageAdd = 0;
        if (BuildManager.containPlayerBuild(entity.getName())) {
            if (BuildManager.containAdd(entity.getName(), "反伤")) {
                Map<String, Object> content = (Map<String, Object>) BuildManager.getContent(entity.getName(), "反伤");
                PRAdd = (int) content.get("机率");
                damageAdd = (int) content.get("百分比");
            }
        }
        if (Utils.random(returnDamagePR + PRAdd)) {
            attack.attack(new EntityDamageEvent(entity, EntityDamageEvent.DamageCause.ENTITY_ATTACK, damage * (returnDamage + damageAdd)));
        }
        return 0f;
    }
}
