package net.mcpes.foundhi.plugin.rpg.rpg.manager;

import cn.nukkit.Server;
import cn.nukkit.item.Item;
import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.nbt.tag.CompoundTag;
import net.mcpes.foundhi.plugin.rpg.rpg.RPGMain;
import net.mcpes.foundhi.plugin.rpg.rpg.manager.attributes.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author yui
 * @date 2017/10/21
 * @since RPG
 */

@SuppressWarnings("unchecked")
public abstract class Artifact {
    protected ArrayList<Attribute> attributes = new ArrayList<>();
    private String id;
    private Item item;
    private int quality;//品质
    private int needPieces;//所需碎片
    private int levelLimit;//等级限制
    private int expAdd;//经验增加比例
    private String buildMode;//强化模式
    private String displayName;//显示名称
    private List<String> bindingRole;//绑定职业

    Artifact(String ID, Map<String, Object> data) {
        this.id = ID;
        this.quality = (int) data.get("品质");
        this.needPieces = (int) data.get("所需碎片");
        this.displayName = (String) data.get("显示名称");
        this.buildMode = String.valueOf(data.get("强化模式"));
        this.setItemFunction(data);
        this.addArtifactEnchant(data);
        this.setRoleFunction(data);
        this.initAttribute((Map<String, Object>) data.get("属性"));
    }

    private void setItemFunction(Map<String, Object> data) {
        String[] itemId = String.valueOf(data.get("物品ID")).split(":");
        this.item = Item.get(Integer.valueOf(itemId[0]), Integer.valueOf(itemId[1]), 1);
        this.item.setCustomBlockData(new CompoundTag().putString("ID", this.id)
                .putInt("BuildLevel", 0));
        this.item.setCustomName(getDisplayName());
    }

    private void setRoleFunction(Map<String, Object> data) {
        this.expAdd = (int) data.get("经验加成");
        if (data.containsKey("绑定职业")) {
            this.bindingRole = (List<String>) data.get("绑定职业");
            for (String role : bindingRole) {
                if (!RPGMain.getDataManager().existsRole(role)) {
                    Server.getInstance().getLogger().debug("§4无ID为" + role + "的职业！");
                    Server.getInstance().shutdown();
                }
            }
        } else {
            this.bindingRole = null;
        }
        if (data.containsKey("等级限制")) {
            this.levelLimit = (int) data.get("等级限制");
        } else {
            this.levelLimit = 0;
        }
    }

    private void addArtifactEnchant(Map<String, Object> data) {
        if (data.containsKey("附魔特效")) {
            List<String> enchantList = (ArrayList<String>) data.get("附魔特效");
            Enchantment[] enchantments = new Enchantment[enchantList.size()];
            for (int i = 0; i < enchantList.size(); i++) {
                String[] e = enchantList.get(i).split(":");
                int id = Integer.valueOf(e[0]);
                //int level = Integer.valueOf(e[1]);
                enchantments[i] = Enchantment.getEnchantment(id);
                //enchantments[i].setLevel(level);
            }
            this.item.addEnchantment(enchantments);
        }
    }

    private void initAttribute(Map<String, Object> attributes) {
        if (attributes.containsKey("燃烧")) {
            Map<String, Integer> content = (Map<String, Integer>) attributes.get("燃烧");
            int pr = content.get("机率");
            int time = content.get("时间");
            this.attributes.add(new Burning(pr, time));
        }
        if (attributes.containsKey("秒杀")) {
            Map<String, Integer> content = (Map<String, Integer>) attributes.get("秒杀");
            int pr = content.get("机率");
            this.attributes.add(new Seckill(pr));
        }
        if (attributes.containsKey("暴击")) {
            Map<String, Integer> content = (Map<String, Integer>) attributes.get("暴击");
            int pr = content.get("机率");
            int damage = content.get("伤害");
            this.attributes.add(new Crit(pr, damage));
        }
        if (attributes.containsKey("吸血")) {
            Map<String, Integer> content = (Map<String, Integer>) attributes.get("吸血");
            int pr = content.get("机率");
            int vampire = content.get("百分比");
            this.attributes.add(new Vampire(pr, vampire));
        }
        if (attributes.containsKey("治疗")) {
            Map<String, Integer> content = (Map<String, Integer>) attributes.get("治疗");
            int pr = content.get("机率");
            int vampire = content.get("百分比");
            int range = content.get("范围");
            this.attributes.add(new Healing(pr, range, vampire));
        }
        if (attributes.containsKey("雷击")) {
            Map<String, Integer> content = (Map<String, Integer>) attributes.get("雷击");
            int pr = content.get("机率");
            int damage = content.get("伤害");
            this.attributes.add(new Lightning(pr, damage));
        }
        if (attributes.containsKey("防御")) {
            this.attributes.add(new Defend((int) attributes.get("防御")));
        }
        if (attributes.containsKey("减伤")) {
            this.attributes.add(new DamageReduce(Float.valueOf(attributes.get("减伤").toString())));
        }
        if (attributes.containsKey("反伤")) {
            Map<String, Integer> content = (Map<String, Integer>) attributes.get("反伤");
            int pr = content.get("机率");
            int damage = content.get("百分比");
            this.attributes.add(new ReturnDamage(damage, pr));
        }
        if (attributes.containsKey("药水")) {
            Map<String, String> content = (Map<String, String>) attributes.get("药水");
            String[] myEffects = new String[4];
            String[] giveEffects = new String[4];
            if (content.containsKey("自给")) {
                myEffects = content.get("自给").split(",");
            }
            if (content.containsKey("给予")) {
                giveEffects = content.get("给予").split(",");
            }
            this.attributes.add(new Effects(myEffects, giveEffects));
        }
    }

    public String getId() {
        return id;
    }

    public int getExpAdd() {
        return expAdd;
    }

    public Item getItem() {
        Item item = this.item.clone();
        item.setCustomBlockData(this.item.getCustomBlockData().clone());
        return item;
    }

    public int getQuality() {
        return quality;
    }

    public int getNeedPieces() {
        return needPieces;
    }

    public int getLevelLimit() {
        return levelLimit;
    }

    public String getBuildMode() {
        return buildMode;
    }

    private String getDisplayName() {
        return displayName;
    }

    public List<String> getBindingRole() {
        return bindingRole;
    }

    public boolean hasBingdingRole() {
        return bindingRole != null;
    }

    public ArrayList<Attribute> getAttributes() {
        return attributes;
    }

    public boolean hasLevelLimit() {
        return String.valueOf(levelLimit).equals("");
    }
}
