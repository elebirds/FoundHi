package net.mcpes.foundhi.plugin.currency.mw.listener;

import cn.nukkit.Player;
import cn.nukkit.block.BlockAir;
import cn.nukkit.blockentity.BlockEntitySign;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.event.block.BlockPlaceEvent;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.player.*;
import cn.nukkit.level.Location;
import net.mcpes.foundhi.plugin.currency.mw.config.DropSignConfig;
import net.mcpes.foundhi.plugin.currency.mw.utils.World;

import java.util.ArrayList;

import static net.mcpes.foundhi.plugin.currency.mw.SMultiWorld.*;

/**
 * @author hhm * @date 2017/7/8 * @since SMultiWorld
 */
public class PlayerListener implements Listener {
    @EventHandler
    public void die(PlayerDeathEvent event) {
        if (event.getEntity() == null || (!(event.getEntity() instanceof Player))) return;
        event.setKeepInventory(!worlds.get(event.getEntity().getLevel().getName()).isDrop());
    }

    @EventHandler
    public void gmChange(PlayerGameModeChangeEvent event) {
        ArrayList<Integer> gameModes = worlds.get(event.getPlayer().getLevel().getName()).getGameMode();
        if (!gameModes.contains(event.getNewGamemode())) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(DEFAULT_TITLE + "此世界禁止使用这个游戏模式!");
        }
    }

    @EventHandler
    public void attack(EntityDamageEvent event) {
        if (!(event instanceof EntityDamageByEntityEvent)) return;
        if (!(((EntityDamageByEntityEvent) event).getDamager() instanceof Player)) return;
        if (event.getEntity() instanceof Player) {
            if (!worlds.get(event.getEntity().getLevel().getName()).isPvp()) {
                event.setCancelled(true);
            }
        } else {
            if (!worlds.get(event.getEntity().getLevel().getName()).isPve()) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void join(PlayerJoinEvent event) {
        event.getPlayer().setCheckMovement(worlds.get(event.getPlayer().getLevel().getName()).isMove_check());
    }

    @EventHandler
    public void blockBreak(BlockBreakEvent event) {
        World world = worlds.get(event.getPlayer().getLevel().getName());
        if (world.isDestroy()) return;
        if (world.getCan_destroyAndPlace().contains(event.getPlayer().getName())) return;
        event.getPlayer().sendMessage(DEFAULT_TITLE + "此世界禁止破坏方块!");
        event.setCancelled(true);
    }

    @EventHandler
    public void blockPlace(BlockPlaceEvent event) {
        World world = worlds.get(event.getPlayer().getLevel().getName());
        if (world.isPlace()) return;
        if (world.getCan_destroyAndPlace().contains(event.getPlayer().getName())) return;
        event.getPlayer().sendMessage(DEFAULT_TITLE + "此世界禁止放置方块!");
        event.setCancelled(true);
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        World world = worlds.get(event.getPlayer().getLevel().getName());
        if (world.isDropItem()) return;
        event.getPlayer().sendMessage(DEFAULT_TITLE + "此世界禁止丢弃物品!");
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void SignTouch(PlayerInteractEvent event) {
        if (event.getBlock().getId() == 323 || event.getBlock().getId() == 63 || event.getBlock().getId() == 68) {
            Location l = event.getBlock().getLocation();
            String pos = (int) l.getX() + ":" + (int) l.getY() + ":" + (int) l.getZ() + ":" + l.getLevel().getName();
            BlockEntitySign sign = (BlockEntitySign) event.getBlock().getLevel().getBlockEntity(event.getBlock().getLocation());
            event.setCancelled(true);
            if (DropSignConfig.getSign().contains(pos)) {
                if (clicked.getOrDefault(event.getPlayer().getName(), "").equals(pos)) {
                    clicked.remove(event.getPlayer().getName());
                    event.getPlayer().sendMessage(DEFAULT_TITLE + "扔除成功!");
                    event.getPlayer().getInventory().removeItem(event.getItem());
                } else {
                    event.getPlayer().sendMessage(DEFAULT_TITLE + "请再点击一次!");
                    clicked.put(event.getPlayer().getName(), pos);
                }
            } else {
                if (!event.getPlayer().isOp()) return;
                if (sign.getText()[0].equals("drop")) {
                    sign.namedTag.putString("Text1", DEFAULT_TITLE);
                    sign.namedTag.putString("Text2", "§2点我扔掉手中物品");
                    sign.namedTag.putString("Text3", "");
                    sign.namedTag.putString("Text4", "");
                    sign.spawnToAll();
                    if (sign.chunk != null) {
                        sign.chunk.setChanged();
                        //sign.level.clearChunkCache(sign.chunk.getX(), sign.chunk.getZ());
                    }
                    ArrayList<String> p = DropSignConfig.getSign();
                    p.add(pos);
                    DropSignConfig.getConfig().set("sign", p);
                    DropSignConfig.getConfig().save();
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void SignBreak(BlockBreakEvent event) {
        if (event.getBlock().getId() == 323 || event.getBlock().getId() == 63 || event.getBlock().getId() == 68) {
            Location l = event.getBlock().getLocation();
            String pos = (int) l.getX() + ":" + (int) l.getY() + ":" + (int) l.getZ() + ":" + l.getLevel().getName();
            if (DropSignConfig.getSign().contains(pos)) {
                event.setCancelled(true);
                if (event.getPlayer().isOp()) {
                    event.getBlock().getLevel().setBlock(l, new BlockAir());
                    event.getPlayer().sendMessage(DEFAULT_TITLE + "§6成功删除扔出木牌");
                }
            }
        }
    }
}