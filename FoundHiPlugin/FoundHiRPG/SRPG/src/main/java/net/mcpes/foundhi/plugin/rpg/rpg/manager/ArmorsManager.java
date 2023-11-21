package net.mcpes.foundhi.plugin.rpg.rpg.manager;

import cn.nukkit.item.*;

import java.util.Map;

/**
 * @author yui
 * @date 2017/10/14
 * @since RPG
 */
@SuppressWarnings("unchecked")
public class ArmorsManager extends Artifact {
    private Item item;
    private boolean notDamage;
    private int healthIncrease;//血量加成

    public ArmorsManager(String armorID, Map<String, Object> data) {
        super(armorID, data);
        Item armor = super.getItem();
        this.item = this.coloredLeather(armor, (Map<String, Object>) data.get("渲染"));
        this.healthIncrease = (int) data.get("血量加成");
        this.notDamage = (boolean) data.get("无限耐久");
    }

    @Override
    public Item getItem() {
        return this.item;
    }

    public boolean isNotDamage() {
        return notDamage;
    }

    public int getHealthIncrease() {
        return healthIncrease;
    }

    private Item coloredLeather(Item armor, Map<String, Object> rendering) {
        if ((boolean) rendering.get("开启")) {
            int r = (int) rendering.get("R");
            int g = (int) rendering.get("G");
            int b = (int) rendering.get("B");
            if (armor instanceof ItemLeggingsLeather) {
                ((ItemLeggingsLeather) armor).setColor(r, g, b);
            } else if (armor instanceof ItemBootsLeather) {
                ((ItemBootsLeather) armor).setColor(r, g, b);
            } else if (armor instanceof ItemChestplateLeather) {
                ((ItemChestplateLeather) armor).setColor(r, g, b);
            } else if (armor instanceof ItemHelmetLeather) {
                ((ItemHelmetLeather) armor).setColor(r, g, b);
            }
        }
        return armor;
    }
}
