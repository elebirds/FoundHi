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
public class Crit extends Attribute {
    private int critPR;//暴击几率
    private int critDamage;//暴击伤害

    public Crit(int critPR, int critDamage) {
        this.critPR = critPR;
        this.critDamage = critDamage;
    }

    @Override
    public float doSkill(Entity attack, Entity entity) {
        float damage = 0f;
        int PRAdd = 0;
        int damageAdd = 0;
        if (BuildManager.containPlayerBuild(attack.getName())) {
            if (BuildManager.containAdd(attack.getName(), "暴击")) {
                Map<String, Object> content = (Map<String, Object>) BuildManager.getContent(attack.getName(), "暴击");
                PRAdd = (int) content.get("机率");
                damageAdd = (int) content.get("伤害");
            }
        }
        if (Utils.random(critPR + PRAdd)) {
            damage = critDamage + damageAdd;
            if (attack instanceof Player) {
                Utils.addText((Player) attack, "§l§6暴击！", entity.getX(), entity.getY() + entity.getEyeHeight(), entity.getZ());
            }
        }
        return damage;
    }
}
