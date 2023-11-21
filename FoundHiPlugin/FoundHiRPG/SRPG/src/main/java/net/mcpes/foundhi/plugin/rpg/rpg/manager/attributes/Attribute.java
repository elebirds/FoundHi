package net.mcpes.foundhi.plugin.rpg.rpg.manager.attributes;

import cn.nukkit.entity.Entity;


/**
 * FoundHi-RPG
 *
 * @author yui Copyright (c) 2018/6/6
 * version 1.0
 */
public abstract class Attribute {

    /*Do Skill by {attack} and {entity}*/
    public float doSkill(Entity attack, Entity entity) {
        return 0f;
    }

    /*Do Skill by {attack} , {entity} and {damage}*/
    public float doSkill(Entity attack, Entity entity, float damage) {
        return 0f;
    }

    public boolean needDamage() {
        return false;
    }
}
