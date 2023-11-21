package net.mcpes.foundhi.plugin.rpg.rpg.listener;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.item.*;
import cn.nukkit.level.Location;
import cn.nukkit.level.particle.DestroyBlockParticle;
import net.mcpes.foundhi.plugin.rpg.rpg.DataManager;
import net.mcpes.foundhi.plugin.rpg.rpg.RPGMain;
import net.mcpes.foundhi.plugin.rpg.rpg.Utils;
import net.mcpes.foundhi.plugin.rpg.rpg.manager.ArmorsManager;
import net.mcpes.foundhi.plugin.rpg.rpg.manager.ArmsManager;
import net.mcpes.foundhi.plugin.rpg.rpg.manager.BuildManager;
import net.mcpes.foundhi.plugin.rpg.rpg.manager.PlayerManager;
import net.mcpes.foundhi.plugin.rpg.rpg.manager.armsTypes.LongRage;
import net.mcpes.foundhi.plugin.rpg.rpg.manager.armsTypes.SALRage;
import net.mcpes.foundhi.plugin.rpg.rpg.manager.armsTypes.ShortRange;
import net.mcpes.foundhi.plugin.rpg.rpg.manager.attributes.Attribute;

import java.util.List;

/**
 * @author yui
 * @date 2017/10/13
 * @since RPG
 */

public class AttackListener implements Listener {
    private DataManager dataManager = RPGMain.getDataManager();

    private void setNoDamage(Player player) {
        for (int i = 0; i < 4; i++) {
            Item item = player.getInventory().getArmorContents()[i];
            if (Utils.isArtifactArmor(item)) {
                String aID = item.getCustomBlockData().getString("ID");
                ArmorsManager armor = dataManager.getArmorsManager(aID);
                if (armor.isNotDamage()) {
                    item.setDamage(0);
                }
            } else {
                continue;
            }
            player.getInventory().setArmorItem(i, item);
        }
    }

    @EventHandler
    public void onAttack(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            this.setNoDamage(player);
        }
        if (event.getDamager() instanceof Player) {
            Player attack = (Player) event.getDamager();
            if (!dataManager.getPlayerManager(attack.getName().toLowerCase()).haveRole()) {
                event.setCancelled();
                return;
            }
            Entity entity = event.getEntity();
            if (!Utils.hasArmsInHand(attack)) return;
            Item item = attack.getInventory().getItemInHand();
            String aID = item.getCustomBlockData().getString("ID");
            ArmsManager arms = dataManager.getArmsManager(aID);
            PlayerManager playerManager = dataManager.getPlayerManager(attack.getName().toLowerCase());
            if (arms.hasBingdingRole()) {
                if (!arms.getBindingRole().contains(playerManager.getRoleID())) {
                    attack.sendMessage(RPGMain.PluginName + "你无法使用此武器，已绑定职业");
                    event.setCancelled();
                    return;
                }
            }
            if (arms.getLevelLimit() > playerManager.getLevel()) {
                attack.sendMessage(RPGMain.PluginName + "你无法使用此武器，等级限制");
                event.setCancelled();
                return;
            }
            item.setDamage(0);
            attack.getInventory().setItemInHand(item);
            if (event.getCause() == EntityDamageEvent.DamageCause.PROJECTILE && item instanceof ItemSnowball || item instanceof ItemArrow || item instanceof ItemEgg || item instanceof ItemEnderPearl) {
                if (!(arms.getType() instanceof LongRage)) return;
                if (arms.getAttackRange() == 0) {
                    this.handleParticle(arms, entity);
                    float damage = this.doSkills(entity, attack, arms);
                    event.setDamage(damage);
                    event.setDamage(this.doDefense(entity, attack, event.getDamage()));
                    // event.setKnockBack(arms.getKnockBack());
                    Utils.addText(attack, "§l§b-" + damage, entity.getX(), entity.getY() + entity.getEyeHeight(), entity.getZ());
                }
                return;
            }
            if (!(arms.getType() instanceof ShortRange) && !(arms.getType() instanceof SALRage)) {
                if (event.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK)) {
                    event.setCancelled();
                    return;
                }
            }
            this.handleParticle(arms, entity);
            float damage = this.doSkills(entity, attack, arms);
            event.setDamage(damage);
            if (event.getEntity() instanceof Player) {
                event.setDamage(this.doDefense(entity, attack, event.getDamage()));
            }
            // event.setKnockBack(arms.getKnockBack());
            Utils.addText(attack, "§l§b-" + damage, entity.getX(), entity.getY() + entity.getEyeHeight(), entity.getZ());
        } /*else{
            if(event.getEntity() instanceof Player){
                Player entity = (Player)event.getEntity();
                if (event.getDamager() instanceof Entity){
                   Entity attack = (Entity)event.getDamager();
                    if (attack.hasHandItem()) {
                        Item handItem = attack.getHandItem();
                        if (Utils.isArtifactArms(handItem)) {
                            String aID = handItem.getCustomBlockData().getString("ID");
                            ArmsManager arms = dataManager.getArmsManager(aID);
                            Block block = Block.get(152);
                            switch (arms.getAttackTeXiao()) {
                                case 2:
                                    block = Block.get(10);
                                    break;
                                case 3:
                                    block = Block.get(8);
                                    break;
                            }
                            entity.getLevel().addParticle(new DestroyBlockParticle(new Location(entity.getX(), entity.getY() + entity.getEyeHeight(), entity.getZ(), entity.getLevel()), block));
                            float damage = Utils.doSkills(entity, attack, arms);
                            event.setDamage(damage);
                        }
                    }
                }
                event.setDamage(Utils.doDefense(entity,event.getDamager(),event.getDamage()));
            }
        }*/
        if (event.getDamager() instanceof Entity) {
            if (event.getEntity() instanceof Player) {
                Player player = (Player) event.getEntity();
                event.setDamage(this.doDefense(player, event.getDamager(), event.getDamage()));
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onAfterDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            if (event.getEntity().getHealth() - event.getDamage() <= 0) {
                if (dataManager.getMobExp().containsKey(event.getEntity().getMaxHealth())) {
                    Player damager = (Player) event.getDamager();
                    PlayerManager playerManager = dataManager.getPlayerManager(damager.getName().toLowerCase());
                    playerManager.addExp(dataManager.getMobExp().get(event.getEntity().getMaxHealth()));
                }
            }
        }
    }

    private float doSkills(Entity entity, Entity attack, ArmsManager arms) {
        float damage;
        String buildMode = arms.getBuildMode();
        int buildLevel = BuildManager.getBuildLevel(arms.getItem());
        BuildManager buildManager = DataManager.INSTANCE.getBuildManager(buildMode);
        if (entity instanceof Player) {
            damage = arms.getPVPDamage() + buildManager.getPVPDamage(buildLevel);
        } else {
            damage = arms.getPVEDamage() + buildManager.getPVEDamage(buildLevel);
        }
        List<Attribute> add = arms.getAttributes();
        for (Attribute attribute : arms.getAttributes()) {
            if (attribute.needDamage()) {
                damage += attribute.doSkill(attack, entity, damage);
            } else {
                damage += attribute.doSkill(attack, entity);
            }
        }
        if (entity instanceof Player) {
            damage = doDefense(entity, attack, damage);
        }
        return damage;
    }

    private float doDefense(Entity entity, Entity damager, float damage) {
        List<Item> items = Utils.isInArmors(entity);
        if (items != null) {
            for (Item item : items) {
                if (item.getId() != 0) {
                    String aID = item.getCustomBlockData().getString("ID");
                    ArmorsManager armor = RPGMain.getDataManager().getArmorsManager(aID);
                    if (entity instanceof Player) {
                        PlayerManager playerManager = RPGMain.getDataManager().getPlayerManager(entity.getName().toLowerCase());
                        if (armor.hasBingdingRole()) {
                            if (!armor.getBindingRole().contains(playerManager.getRoleID())) {
                                continue;
                            }
                        }
                        if (!playerManager.haveRole() || playerManager.getLevel() < armor.getLevelLimit()) {
                            continue;
                        }
                    }
                    for (Attribute attribute : armor.getAttributes()) {
                        if (attribute.needDamage()) {
                            damage += attribute.doSkill(damager, entity, damage);
                        } else {
                            damage += attribute.doSkill(damager, entity);
                        }
                    }
                    if (damage < 1) {
                        damage = 1;
                    }
                }
            }
        }
        return damage;
    }

    private void handleParticle(ArmsManager arms, Entity entity) {
        Block block = Block.get(152);
        switch (arms.getAttackTeXiao()) {
            case 2:
                block = Block.get(10);
                break;
            case 3:
                block = Block.get(8);
                break;
        }
        entity.getLevel().addParticle(new DestroyBlockParticle(new Location(entity.getX(), entity.getY() + entity.getEyeHeight(), entity.getZ(), entity.getLevel()), block));
    }
}
