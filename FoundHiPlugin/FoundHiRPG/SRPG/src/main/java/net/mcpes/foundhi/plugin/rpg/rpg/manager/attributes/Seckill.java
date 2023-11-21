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
public class Seckill extends Attribute {
    private int seckillPR;//秒杀几率

    public Seckill(int seckillPR) {
        this.seckillPR = seckillPR;
    }

    @Override
    public float doSkill(Entity attack, Entity entity) {
        int PRAdd = 0;
        if (BuildManager.containPlayerBuild(attack.getName())) {
            if (BuildManager.containAdd(attack.getName(), "秒杀")) {
                Map<String, Object> content = (Map<String, Object>) BuildManager.getContent(attack.getName(), "秒杀");
                PRAdd = (int) content.get("机率");
            }
        }
        if (Utils.random(seckillPR + PRAdd)) {
            entity.kill();
            entity.close();
            if (attack instanceof Player) {
                Utils.addText((Player) attack, "§l§e秒杀！", entity.getX(), entity.getY() + entity.getEyeHeight(), entity.getZ());
            }
        }
        return 0f;
    }
}
