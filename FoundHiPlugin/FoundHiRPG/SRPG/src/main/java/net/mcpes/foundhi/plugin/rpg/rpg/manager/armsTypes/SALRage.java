package net.mcpes.foundhi.plugin.rpg.rpg.manager.armsTypes;

/**
 * RPG |Short and Long Rage attack mode
 *
 * @author yui Copyright (c) 2018/7/9
 * version 1.0
 */
public class SALRage extends LongRage {
    public SALRage(int bulletType, int bulletSpeed, int bulletTeXiao, int cd) {
        super(bulletType, bulletSpeed, bulletTeXiao, cd);
    }

    @Override
    public int getTypeID() {
        return SHORT_AND_LONG_RAGE;
    }
}
