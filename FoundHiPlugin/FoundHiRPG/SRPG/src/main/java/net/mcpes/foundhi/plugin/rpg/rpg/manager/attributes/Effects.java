package net.mcpes.foundhi.plugin.rpg.rpg.manager.attributes;

import cn.nukkit.entity.Entity;
import cn.nukkit.potion.Effect;
import net.mcpes.foundhi.plugin.rpg.rpg.Utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * RPG
 *
 * @author yui Copyright (c) 2018/6/7
 * version 1.0
 */
public class Effects extends Attribute {

    private Map<Effect, Integer> myEffects;//自给药水
    private Map<Effect, Integer> giveEffects;//给予药水

    public Effects(String[] myEffects, String[] giveEffects) {
        this.myEffects = this.getEffects(myEffects);
        this.giveEffects = this.getEffects(giveEffects);
    }

    @Override
    public float doSkill(Entity attack, Entity entity) {

        if (!myEffects.isEmpty()) {
            this.giveEffects(myEffects, attack);
        }
        if (!giveEffects.isEmpty()) {
            this.giveEffects(giveEffects, entity);
        }
        return 0f;
    }

    private void giveEffects(Map<Effect, Integer> effects, Entity entity) {
        Set entries = effects.entrySet();
        for (Object entry1 : entries) {
            Map.Entry entry = (Map.Entry) entry1;
            Effect effect = (Effect) entry.getKey();
            int pr = (int) entry.getValue();
            if (Utils.random(pr)) {
                entity.addEffect(effect);
            }
        }
    }

    private Map<Effect, Integer> getEffects(String[] stringEffects) {
        Map<Effect, Integer> effects = new HashMap<>();
        for (String stringEffect : stringEffects) {
            String[] arrayEffects = stringEffect.split("：");
            int id = Integer.valueOf(arrayEffects[0]);
            int duration = Integer.valueOf(arrayEffects[2]) * 20;
            boolean amplifier = Boolean.valueOf(arrayEffects[1]);
            int probability = Integer.valueOf(arrayEffects[3]);
            Effect effect = Effect.getEffect(id);
            effect.setDuration(duration);
            effect.setAmbient(amplifier);
            effects.put(effect, probability);
        }
        return effects;
    }
}
