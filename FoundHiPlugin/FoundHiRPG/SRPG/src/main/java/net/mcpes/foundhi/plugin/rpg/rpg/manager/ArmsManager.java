package net.mcpes.foundhi.plugin.rpg.rpg.manager;

import cn.nukkit.Server;
import net.mcpes.foundhi.plugin.rpg.rpg.manager.armsTypes.ArmsType;
import net.mcpes.foundhi.plugin.rpg.rpg.manager.armsTypes.LongRage;
import net.mcpes.foundhi.plugin.rpg.rpg.manager.armsTypes.SALRage;
import net.mcpes.foundhi.plugin.rpg.rpg.manager.armsTypes.ShortRange;

import java.util.Map;

/**
 * @author yui
 * @date 2017/10/12
 * @since RPG
 */

public class ArmsManager extends Artifact {
    private int PVPDamage;//PVP伤害
    private int PVEDamage;//PVE伤害
    private int KnockBack;//击退范围
    private int attackTeXiao;//攻击特效
    private int attackRange;//PVE攻击范围
    private ArmsType type;

    public ArmsManager(String armsID, Map<String, Object> data) {
        super(armsID, data);
        this.PVEDamage = (int) data.get("PVE伤害");
        this.PVPDamage = (int) data.get("PVP伤害");
        this.KnockBack = (int) data.get("击退范围");
        int type = (int) data.get("攻击模式");
        if (type == ArmsType.LONG_RAGE || type == ArmsType.SHORT_AND_LONG_RAGE) {
            int bulletTeXiao = (int) data.get("子弹特效");
            int bulletType = (int) data.get("子弹类型");
            int bulletSpeed = (int) data.get("子弹速度");
            int cd = (int) data.get("cd时间");
            if (type == ArmsType.LONG_RAGE) {
                this.type = new LongRage(bulletType, bulletSpeed, bulletTeXiao, cd);
            } else {
                this.type = new SALRage(bulletType, bulletSpeed, bulletTeXiao, cd);
            }
        } else if (type == ArmsType.SHORT_RAGE) {
            this.type = new ShortRange();
        } else {
            Server.getInstance().getLogger().warning("§4武器ID为" + this.getId() + "未定义武器类型,请确认并修改");
        }
        this.attackTeXiao = (int) data.get("攻击特效");
        this.attackRange = (int) data.get("PVE攻击范围");
    }

    public ArmsType getType() {
        return this.type;
    }

    public int getAttackTeXiao() {
        return attackTeXiao;
    }

    public int getPVEDamage() {
        return PVEDamage;
    }

    public void setPVEDamage(int PVEDamage) {
        this.PVEDamage = PVEDamage;
    }

    public int getPVPDamage() {
        return PVPDamage;
    }

    public void setPVPDamage(int PVPDamage) {
        this.PVPDamage = PVPDamage;
    }

    public int getKnockBack() {
        return KnockBack;
    }

    public int getAttackRange() {
        return attackRange;
    }
}
