package net.mcpes.foundhi.plugin.rpg.rpg;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.data.EntityMetadata;
import cn.nukkit.entity.item.EntityItem;
import cn.nukkit.entity.projectile.*;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemArmor;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.DoubleTag;
import cn.nukkit.nbt.tag.FloatTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.network.protocol.AddEntityPacket;
import net.mcpes.foundhi.plugin.rpg.rpg.manager.Artifact;
import net.mcpes.foundhi.plugin.rpg.rpg.manager.armsTypes.LongRage;
import net.mcpes.foundhi.plugin.rpg.rpg.task.DeleteTextTask;
import net.mcpes.foundhi.plugin.rpg.rpg.task.ParticleTask;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * @author yui
 * @date 2017/10/13
 * @since RPG
 */

public class Utils {
    private static Random random = new Random();

    public static boolean isArtifactArmor(Item armor) {
        if (!isArtifact(armor) || !(armor instanceof ItemArmor)) return false;
        return RPGMain.getDataManager().existsArmors(getID(armor));
    }

    public static boolean isArtifactArms(Item arms) {
        if (!isArtifact(arms)) return false;
        return RPGMain.getDataManager().existsArms(getID(arms));
    }

    public static boolean isArtifact(Item item) {
        if (!item.hasCustomBlockData()) return false;
        if (item.getCustomBlockData() != null) {
            CompoundTag itemCustomBlockData = item.getCustomBlockData();
            return itemCustomBlockData.contains("ID");
        }
        return false;
    }

    private static String getID(Item item) {
        CompoundTag itemCustomBlockData = item.getCustomBlockData();
        return itemCustomBlockData.getString("ID");
    }

    public static boolean hasArmsInHand(Player player) {
        Item item = player.getInventory().getItemInHand();
        return isArtifactArms(item);
    }

    public static Artifact getItemArtifact(Item item) {
        String aID = item.getCustomBlockData().getString("ID");
        return DataManager.INSTANCE.getArtifact(aID);
    }

    public static List<Item> isInArmors(Entity entity) {
        Item[] items;
        items = ((Player) entity).getInventory().getArmorContents();
        if (items != null) {
            List<Item> armors = new ArrayList<>();
            for (int i = 0; i < 4; i++) {
                if (isArtifactArmor(items[i])) {
                    armors.add(items[i]);
                }
            }
            return armors;
        }
        return null;
    }

    public static boolean random(int pr) {
        int result = random.nextInt(100);
        return result < pr;
    }

    public static void addText(Player player, String text, double x, double y, double z) {
        long eid = Entity.entityCount++;
        Random random = new Random();
        float b = (random.nextInt(10) - 5) / 3;
        float d = (random.nextInt(10) - 5) / 3;
        AddEntityPacket pk = new AddEntityPacket();
        pk.entityUniqueId = eid;
        pk.entityRuntimeId = eid;
        pk.type = EntityItem.NETWORK_ID;
        pk.x = (float) x + b;
        pk.y = (float) y;
        pk.z = (float) z + d;
        pk.speedX = 0;
        pk.speedY = 0;
        pk.speedZ = 0;
        pk.yaw = 0;
        pk.pitch = 0;
        long flags = (
                (1L << Entity.DATA_FLAG_CAN_SHOW_NAMETAG) |
                        (1L << Entity.DATA_FLAG_ALWAYS_SHOW_NAMETAG) |
                        (1L << Entity.DATA_FLAG_IMMOBILE)
        );
        pk.metadata = new EntityMetadata()
                .putLong(Entity.DATA_FLAGS, flags)
                .putString(Entity.DATA_NAMETAG, text);
        player.dataPacket(pk);
        Server.getInstance().getScheduler().scheduleDelayedTask(new DeleteTextTask(RPGMain.getInstance(), player, eid), 10);
    }

    public static EntityProjectile doLauch(Player player, LongRage type) {
        CompoundTag nbt = new CompoundTag()
                .putList(new ListTag<DoubleTag>("Pos")
                        .add(new DoubleTag("", player.getX()))
                        .add(new DoubleTag("", player.getY() + player.getEyeHeight()))
                        .add(new DoubleTag("", player.getZ())))
                .putList(new ListTag<DoubleTag>("Motion")
                        .add(new DoubleTag("", -Math.sin(player.getYaw() / 180 * Math.PI) * Math.cos(player.getPitch() / 180 * Math.PI)))
                        .add(new DoubleTag("", -Math.sin(player.getPitch() / 180 * Math.PI)))
                        .add(new DoubleTag("", Math.cos(player.getYaw() / 180 * Math.PI) * Math.cos(player.getPitch() / 180 * Math.PI))))
                .putList(new ListTag<FloatTag>("Rotation")
                        .add(new FloatTag("", (float) player.getYaw()))
                        .add(new FloatTag("", (float) player.getPitch())))
                .putInt("texiao", type.getBulletTeXiao())
                .putBoolean("rpg", true)
                .putString("mdzz", player.getName());
        EntityProjectile bullet;
        switch (type.getBulletType()) {
            case LongRage.BUTTLE_SNOW_BALL:
                bullet = new EntitySnowball(player.chunk, nbt, player);
                break;
            case LongRage.BUTTLE_ARROW:
                bullet = new EntityArrow(player.chunk, nbt, player);
                break;
            case LongRage.BUTTLE_EGG:
                bullet = new EntityEgg(player.chunk, nbt, player);
                break;
            default:
                bullet = new EntityEnderPearl(player.chunk, nbt, player);
                break;
        }
        bullet.setMotion(bullet.getMotion().multiply(type.getBulletSpeed()));
        bullet.setOnFire(10);
        RPGMain.getInstance().getServer().getScheduler().scheduleRepeatingTask(new ParticleTask(RPGMain.getInstance(), bullet.getId(), bullet.getLevel()), 1);
        return bullet;
    }

    static String[] getStringArray(String string, String cut) {
        return string.split(cut);
    }

    public static Item getItemByString(String string) {
        String[] item = getStringArray(string, ":");
        return Item.get(Integer.valueOf(item[0]), Integer.valueOf(item[1]));
    }

    public static boolean hasEnoughItem(Player player, int id, int meta, int amount, int type) {
        int l = 0;
        for (int m : player.getInventory().getContents().keySet()) {
            Item i = player.getInventory().getContents().get(m);
            if (i.getId() == id && i.getDamage() == meta && i.getCustomBlockData() != null && i.getCustomBlockData().contains("piece") && i.getCustomBlockData().getInt("piece") == type) {
                l += i.getCount();
            }
        }
        return l >= amount;
    }

    public static void removeItem(Player player, int id, int damage, int count, int type) {
        HashMap<Item, Integer> removeing = new HashMap<>();
        int now = count;
        for (Item item : player.getInventory().getContents().values()) {
            if (now <= 0) {
                break;
            }
            if (item.getId() == id && item.getDamage() == damage && item.getCustomBlockData() != null && item.getCustomBlockData().contains("piece") && item.getCustomBlockData().getInt("piece") == type) {
                if (item.getCount() <= now) {
                    removeing.put(item, item.getCount());
                    now -= item.getCount();
                } else {
                    removeing.put(item, now);
                    now = 0;
                }
            }
        }
        if (now <= 0) {
            for (Item item : removeing.keySet()) {
                Item i = item.clone();
                i.setCount(removeing.get(item));
                player.getInventory().removeItem(i);
            }
        }
    }

    static ArrayList<String> getFiles(String path) {
        ArrayList<String> files = new ArrayList<>();
        File file = new File(path);
        File[] tempList = file.listFiles();

        assert tempList != null;
        for (File aTempList : tempList) {
            if (aTempList.isFile()) {
                files.add(aTempList.toString());
            }
        }
        return files;
    }
}