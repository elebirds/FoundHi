package net.mcpes.foundhi.plugin.rpg.rpg.listener;

import cn.nukkit.Player;
import cn.nukkit.entity.projectile.EntityProjectile;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.event.entity.EntityArmorChangeEvent;
import cn.nukkit.event.entity.EntityShootBowEvent;
import cn.nukkit.event.entity.ProjectileLaunchEvent;
import cn.nukkit.event.player.*;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBow;
import net.mcpes.foundhi.plugin.rpg.rpg.DataManager;
import net.mcpes.foundhi.plugin.rpg.rpg.RPGMain;
import net.mcpes.foundhi.plugin.rpg.rpg.Utils;
import net.mcpes.foundhi.plugin.rpg.rpg.manager.ArmorsManager;
import net.mcpes.foundhi.plugin.rpg.rpg.manager.ArmsManager;
import net.mcpes.foundhi.plugin.rpg.rpg.manager.BuildManager;
import net.mcpes.foundhi.plugin.rpg.rpg.manager.PlayerManager;
import net.mcpes.foundhi.plugin.rpg.rpg.manager.armsTypes.LongRage;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author yui
 * @date 2017/10/12
 * @since RPG
 */

public class PlayerListener implements Listener {
    private DataManager dataManager = RPGMain.getDataManager();
    private Map<String, Long> cd = new HashMap<>();

    @EventHandler
    private void onFoodChange(PlayerFoodLevelChangeEvent event) {
        event.setCancelled();
    }

    @EventHandler
    private void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (!dataManager.containPlayerManager(player.getName().toLowerCase())) {
            PlayerManager playerManager = new PlayerManager(player);
            dataManager.addPlayer(playerManager);
        }
        if (Objects.requireNonNull(Utils.isInArmors(player)).size() != 0 || Utils.isArtifactArms(player.getInventory().getItemInHand())) {
            BuildManager.refreshAttributesBuild(player);
        }
    }

    @EventHandler
    private void onQuit(PlayerQuitEvent event) {
        String name = event.getPlayer().getName().toLowerCase();
        PlayerManager playerManager = dataManager.getPlayerManager(name);
        playerManager.saveAll();
        dataManager.removePlayer(name);
        if (BuildManager.containPlayerBuild(event.getPlayer().getName())) {
            BuildManager.removePlayerBuild(event.getPlayer().getName());
        }
    }

    @EventHandler
    private void onHandItemChange(PlayerItemHeldEvent event) {
        PlayerManager playerManager = dataManager.getPlayerManager(event.getPlayer().getName().toLowerCase());
        if (playerManager != null) {
            playerManager.updateAddExp();
            boolean a = Utils.isArtifactArms(event.getItem());
            if (Utils.isArtifactArms(event.getItem())) {
                BuildManager.refreshAttributesBuild(event.getPlayer());
            }
        }
    }

    @EventHandler
    private void onLauch(EntityShootBowEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (Utils.hasArmsInHand(player)) {
                String aID = event.getBow().getCustomBlockData().getString("ID");
                ArmsManager armsManager = dataManager.getArmsManager(aID);
                if (!armsManager.getBindingRole().contains(dataManager.getPlayerManager(player.getName().toLowerCase()).getRoleID())) {
                    event.setCancelled();
                    return;
                }
                if (armsManager.getType() instanceof LongRage) {
                    LongRage longRage = (LongRage) armsManager.getType();
                    if (this.handleCD(player, longRage)) {
                        event.setProjectile(Utils.doLauch(player, longRage));
                    }
                }
            }
        }
    }

    @EventHandler
    private void onArmorsChange(EntityArmorChangeEvent event) {
        Item itemOld = event.getOldItem();
        Item itemNew = event.getNewItem();
        if (Utils.isArtifactArmor(itemOld) || Utils.isArtifactArmor(itemNew)) {
            BuildManager.refreshAttributesBuild(event.getEntity());
        }
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            String pn = player.getName().toLowerCase();
            if (!dataManager.containPlayerManager(pn)) {
                PlayerManager playerManager = new PlayerManager(player);
                dataManager.addPlayer(playerManager);
            }
            PlayerManager pm = dataManager.getPlayerManager(pn);
            if (Utils.isArtifactArmor(itemOld)) {
                itemOld.setDamage(0);
                String aID = itemOld.getCustomBlockData().getString("ID");
                ArmorsManager armor = dataManager.getArmorsManager(aID);
                if (armor.getBindingRole().contains(pm.getRoleID())) {
                    float healthCoefficient = player.getHealth() / player.getMaxHealth();
                    player.setMaxHealth(player.getMaxHealth() - armor.getHealthIncrease());
                    player.setHealth(player.getMaxHealth() * healthCoefficient);
                } else {
                    event.setCancelled();
                }
            }
            if (Utils.isArtifactArmor(itemNew)) {
                itemNew.setDamage(0);
                String aID = itemNew.getCustomBlockData().getString("ID");
                ArmorsManager armor = dataManager.getArmorsManager(aID);
                if (armor.getBindingRole().contains(pm.getRoleID())) {
                    float healthCoefficient = player.getHealth() / player.getMaxHealth();
                    player.setMaxHealth(player.getMaxHealth() + armor.getHealthIncrease());
                    player.setHealth(player.getMaxHealth() * healthCoefficient);
                } else {
                    event.setCancelled();
                }
            }
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Item item = event.getItem();
        if (item.getId() == 0) return;
        if (item.getCustomBlockData() == null) return;
        if (Utils.isArtifactArmor(item)) {
            item.setDamage(0);
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Item item = event.getItem();
        if (item.getId() == 0) return;
        if (item.getCustomBlockData() == null) return;
        if (event.getAction() == PlayerInteractEvent.Action.RIGHT_CLICK_AIR) {
            if (Utils.isArtifact(item)) {
                ArmsManager armsManager = (ArmsManager) Utils.getItemArtifact(item);
                if (armsManager.getType() instanceof LongRage) {
                    Player player = event.getPlayer();
                    LongRage type = (LongRage) armsManager.getType();
                    if (player.getInventory().getItemInHand() instanceof ItemBow) {
                        return;
                    }
                    if (this.handleCD(player, type)) {
                        EntityProjectile bullet = Utils.doLauch(player, type);
                        ProjectileLaunchEvent projectileLaunchEvent = new ProjectileLaunchEvent(bullet);
                        player.getServer().getPluginManager().callEvent(projectileLaunchEvent);
                        if (projectileLaunchEvent.isCancelled()) {
                            bullet.kill();
                        } else {
                            bullet.spawnToAll();
                        }
                    }
                }
            }
        }
    }

    private boolean handleCD(Player player, LongRage longRage) {
        long currentTime = System.currentTimeMillis();
        if (cd.containsKey(player.getName())) {
            long time = cd.get(player.getName());
            if (time <= currentTime) {
                cd.put(player.getName(), currentTime + longRage.getCD() * 1000);
            } else {
                player.sendTip(RPGMain.PluginName + "§4此武器远程攻击冷却时间未结束！");
                return false;
            }
        } else {
            cd.put(player.getName(), currentTime + longRage.getCD() * 1000);
        }
        return true;
    }

}
