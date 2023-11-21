package net.mcpes.foundhi.plugin.rpg.rpg.manager.armsTypes;

/**
 * RPG
 *
 * @author yui Copyright (c) 2018/7/6
 * version 1.0
 */
public class LongRage implements ArmsType {
    public static final int BUTTLE_SNOW_BALL = 0;
    public static final int BUTTLE_ARROW = 1;
    public static final int BUTTLE_EGG = 2;
    public static final int BUTTLE_ENDER_PEARL = 3;
    private int bulletType;//子弹类型
    private int bulletSpeed;//子弹速度
    private int bulletTeXiao;//子弹特效
    private int cd;//冷却时间

    public LongRage(int bulletType, int bulletSpeed, int bulletTeXiao, int cd) {
        this.bulletType = bulletType;
        this.bulletSpeed = bulletSpeed;
        this.bulletTeXiao = bulletTeXiao;
        this.cd = cd;
    }

    public int getBulletType() {
        return bulletType;
    }

    public int getBulletSpeed() {
        return bulletSpeed;
    }

    public int getBulletTeXiao() {
        return bulletTeXiao;
    }

    public int getCD() {
        return cd;
    }

    @Override
    public int getTypeID() {
        return LONG_RAGE;
    }
}
