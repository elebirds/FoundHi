package net.mcpes.foundhi.plugin.rpg.rpg.manager;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.item.Item;
import cn.nukkit.nbt.tag.CompoundTag;
import net.mcpes.foundhi.plugin.rpg.rpg.DataManager;
import net.mcpes.foundhi.plugin.rpg.rpg.Utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * RPG
 *
 * @author yui Copyright (c) 2018/7/9
 * version 1.0
 */
@SuppressWarnings("unchecked")
public class BuildManager {
    private static Map<String, Object> attributesBuild = new HashMap<>();
    private String id;
    private Map<String, Object> data;

    public BuildManager(String id, Map<String, Object> data) {
        this.id = id;
        this.data = data;
    }

    private static Item setBuildLevel(Item item, int level) {
        String itemName = item.getCustomName();
        if (itemName.contains(String.valueOf(level - 1))) {
            itemName = itemName.replace(String.valueOf(level - 1), String.valueOf(level));
        } else {
            itemName = "§e+" + level + item.getCustomName();
        }
        item.setCustomName(itemName);
        item.setCustomBlockData(item.getCustomBlockData().putInt("BuildLevel", level));
        return item;
    }

    public static int getBuildLevel(Item item) {
        CompoundTag itemCustomBlockData = item.getCustomBlockData();
        return itemCustomBlockData.getInt("BuildLevel");
    }

    public static boolean isBuildArtifact(Item item) {
        return getBuildLevel(item) != 0;
    }

    public static Item addBuildLevel(Item item) {
        return setBuildLevel(item, getBuildLevel(item) + 1);
    }

    public static Item backBuildLevel(Item item) {
        return setBuildLevel(item, getBuildLevel(item) - 1);
    }

    //这个算法贼恶心，建议不要观看
    public static void refreshAttributesBuild(Entity entity) {
        if (entity instanceof Player) {
            Player player = (Player) entity;
            Item itemInHand = player.getInventory().getItemInHand();
            if (Utils.isArtifactArms(itemInHand) && isBuildArtifact(itemInHand)) {
                int buildLevel = getBuildLevel(itemInHand);
                BuildManager buildManager = DataManager.INSTANCE.getBuildManager(itemInHand);
                Map<String, Object> addAttributes = buildManager.getAddAttributes(buildLevel);
                attributesBuild.put(player.getName(), addAttributes);
            }
            List<Item> equipments = Utils.isInArmors(entity);
            assert equipments != null;
            for (Item armor : equipments) {
                int buildLevel = getBuildLevel(armor);
                if (buildLevel == 0) {
                    continue;
                }
                BuildManager buildManager = DataManager.INSTANCE.getBuildManager(armor);
                Map<String, Object> addAttributes = buildManager.getAddAttributes(buildLevel);
                if (!attributesBuild.containsKey(player.getName())) {
                    attributesBuild.put(player.getName(), addAttributes);
                    continue;
                }
                Map<String, Object> playerAdd = (Map<String, Object>) attributesBuild.get(player.getName());
                for (Map.Entry<String, Object> entry : addAttributes.entrySet()) {
                    String name = entry.getKey();
                    if (playerAdd.containsKey(name)) {
                        if (name.equals("秒杀")) {
                            playerAdd = doAdd(entry, playerAdd, name, "", "");
                            continue;
                        }
                        if (name.equals("燃烧")) {
                            playerAdd = doAdd(entry, playerAdd, name, "时间", "");
                            continue;
                        }
                        if (name.equals("治疗")) {
                            playerAdd = doAdd(entry, playerAdd, name, "百分比", "范围");
                            continue;
                        }
                        if (name.equals("反伤") || name.equals("吸血")) {
                            playerAdd = doAdd(entry, playerAdd, name, "百分比", "");
                            continue;
                        }
                        if (name.equals("防御") || name.equals("减伤")) {
                            int value = (int) entry.getValue();
                            int playerValue = (int) playerAdd.get(name);
                            playerAdd.put(name, playerValue + value);
                            continue;
                        }
                        if (name.equals("暴击") || name.equals("雷击")) {
                            playerAdd = doAdd(entry, playerAdd, name, "伤害", "");
                        }
                    } else {
                        playerAdd.put(name, entry.getValue());
                    }
                }
                attributesBuild.put(player.getName(), playerAdd);
            }
        }
    }

    private static Map<String, Object> doAdd(Map.Entry<String, Object> entry, Map<String, Object> playerAdd, String name, String part2, String part3) {
        Map<String, Integer> content = (Map<String, Integer>) entry.getValue();
        int pr = content.get("机率");
        Map<String, Integer> playerContent = (Map<String, Integer>) playerAdd.get(name);
        int playerPR = playerContent.get("机率");
        playerContent.put("机率", pr + playerPR);
        if (!part2.equals("")) {
            int damage = content.get(part2);
            int playerDamage = playerContent.get(part2);
            playerContent.put(part2, playerDamage + damage);
        }
        if (!part3.equals("")) {
            int damage = content.get(part3);
            int playerDamage = playerContent.get(part3);
            playerContent.put(part3, playerDamage + damage);
        }
        playerAdd.put(name, playerContent);
        return playerAdd;
    }

    public static Object getContent(String name, String key) {
        Map<String, Object> playerAdd = (Map<String, Object>) attributesBuild.get(name);
        return playerAdd.get(key);
    }

    public static boolean containAdd(String name, String attribute) {
        Map<String, Object> content = (Map<String, Object>) attributesBuild.get(name);
        return content.containsKey(attribute);
    }

    public static boolean containPlayerBuild(String name) {
        return attributesBuild.containsKey(name);
    }

    public static void removePlayerBuild(String name) {
        attributesBuild.remove(name);
    }

    public String getId() {
        return id;
    }

    public int getMaxBuildLevel() {
        return data.size();
    }

    private Map<String, Object> getLevelObject(int level) {
        return (Map<String, Object>) data.get(level);
    }

    public int getMoney(int level) {
        Map<String, Object> levelObject = this.getLevelObject(level);
        return (int) levelObject.get("费用");
    }

    public int getSucceedPR(int level) {
        return (int) this.getLevelObject(level).get("成功机率");
    }

    public float getPVPDamage(int level) {
        if (level == 0) return 0;
        return Float.valueOf(this.getLevelObject(level).get("PVP伤害").toString());
    }

    public float getPVEDamage(int level) {
        if (level == 0) return 0;
        return Float.valueOf(this.getLevelObject(level).get("PVE伤害").toString());
    }

    public boolean canBack(int level) {
        return (boolean) this.getLevelObject(level).get("失败是否退级");
    }

    private Map<String, Object> getAddAttributes(int level) {
        return (Map<String, Object>) this.getLevelObject(level).get("属性加成");
    }
}
