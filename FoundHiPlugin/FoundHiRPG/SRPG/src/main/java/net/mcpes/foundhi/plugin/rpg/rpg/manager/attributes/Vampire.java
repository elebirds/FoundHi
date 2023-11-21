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
public class Vampire extends Attribute {
    private int vampirePR;//吸血几率
    private int vampire;//吸血倍数

    public Vampire(int vampirePR, int vampire) {
        this.vampire = vampire;
        this.vampirePR = vampirePR;
    }

    @Override
    public boolean needDamage() {
        return true;
    }

    @Override
    public float doSkill(Entity attack, Entity entity, float damage) {
        int PRAdd = 0;
        int vampireAdd = 0;
        if (BuildManager.containPlayerBuild(attack.getName())) {
            if (BuildManager.containAdd(attack.getName(), "吸血")) {
                Map<String, Object> content = (Map<String, Object>) BuildManager.getContent(attack.getName(), "吸血");
                PRAdd = (int) content.get("机率");
                vampireAdd = (int) content.get("百分比");
            }
        }
        if (Utils.random(vampirePR + PRAdd)) {
            float vampireHealth = damage * ((vampire + vampireAdd) / 100);
            attack.setHealth(attack.getHealth() + vampireHealth);
            if (attack instanceof Player) {
                Utils.addText((Player) attack, "§l§4吸血！", entity.getX(), entity.getY() + entity.getEyeHeight(), entity.getZ());
            }
        }
        return 0f;
    }
}
