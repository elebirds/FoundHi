package net.mcpes.foundhi.plugin.game.bedwars.listener;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.event.block.BlockPlaceEvent;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.inventory.CraftItemEvent;
import cn.nukkit.event.player.*;
import cn.nukkit.event.server.DataPacketReceiveEvent;
import cn.nukkit.inventory.transaction.data.UseItemOnEntityData;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemClay;
import cn.nukkit.level.Location;
import cn.nukkit.level.Sound;
import cn.nukkit.network.protocol.InventoryTransactionPacket;
import net.mcpes.foundhi.plugin.game.bedwars.SBedWarsAPI;
import net.mcpes.foundhi.plugin.game.bedwars.game.BedWars;

import java.util.ArrayList;

import static net.mcpes.foundhi.plugin.game.bedwars.SBedWars.*;

/**
 * @author hhm
 * @date 2017/7/13
 * @since SBedWars
 */

public class PlayerListener implements Listener {
    public ArrayList<String> locations = new ArrayList<>();

    @EventHandler(priority = EventPriority.HIGH)
    public void onQuit(PlayerQuitEvent event) {
        int id = SBedWarsAPI.getInstance().isPlayerGaming(event.getPlayer().getName());
        if (id != -1) {
            //TODO:bag
            games.get(id).onQuit(event.getPlayer(), false);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onJoin(PlayerJoinEvent event) {
        event.getPlayer().setCheckMovement(false);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onChat(PlayerChatEvent event) {
        int id = SBedWarsAPI.getInstance().isPlayerGaming(event.getPlayer().getName());
        if (id != -1) {
            event.setCancelled(true);
            if (games.get(id).getGameMode() == 3) {
                if (event.getMessage().substring(0, 1).equals("!")) {
                    SBedWarsAPI.getInstance().broadcastSpeak(games.get(id).getAllPlayers(), event.getPlayer().getName(), event.getMessage().substring(1, event.getMessage().length()));
                } else {
                    SBedWarsAPI.getInstance().broadcastTeamSpeak(games.get(id).getTeam(games.get(id).getTeam(event.getPlayer().getName())), event.getPlayer().getName(), event.getMessage());
                }
            } else {
                SBedWarsAPI.getInstance().broadcastSpeak(games.get(id).getAllPlayers(), event.getPlayer().getName(), event.getMessage());
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onBlockBreak(BlockBreakEvent event) {
        String n = event.getPlayer().getName();
        int i = SBedWarsAPI.getInstance().isPlayerGaming(n);
        if (i != -1) {
            if (games.get(i).getGameMode() != 3) {
                event.setCancelled(true);
                event.getPlayer().sendMessage(DEFAULT_TITLE + "§6非游戏开始时不允许破坏方块！");
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onImmuneDamage(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            if (event.getDamager().getLevel().getFolderName().equals("lobby")) {
                event.setCancelled();
                return;
            }
            Player entity = (Player) event.getEntity();//防御者
            Player damager = (Player) event.getDamager();
            int a = SBedWarsAPI.getInstance().isPlayerGaming(entity.getName());
            int b = SBedWarsAPI.getInstance().isPlayerGaming(damager.getName());
            if (a != -1 || b != -1) {
                if (games.get(a).getGameMode() != 3 || games.get(b).getGameMode() != 3) {
                    try {
                        event.setDamage(0);
                    } catch (NullPointerException e) {
                        //辣鸡bug你开心就好
                    }
                    event.setCancelled(true);
                    damager.sendMessage(DEFAULT_TITLE + "§6非开始时不准进行攻击！");
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onCommandPreprocess(PlayerCommandPreprocessEvent event) {
        if (SBedWarsAPI.getInstance().isPlayerGaming(event.getPlayer().getName()) != -1) {
            if (!event.getPlayer().isOp()) {
                if (event.getMessage().length() < 4 && (!event.getMessage().equals("/bw"))) {
                    event.setCancelled(true);
                    return;
                }
                if ((!event.getMessage().substring(0, 4).equals("/bw "))) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Item inHand = event.getPlayer().getInventory().getItemInHand();
        if (!(inHand instanceof ItemClay)) return;
        if (event.getAction() != PlayerInteractEvent.Action.RIGHT_CLICK_AIR) return;
        int room = SBedWarsAPI.getInstance().isPlayerGaming(event.getPlayer().getName());
        if (room <= 0) return;
        if (games.get(room).getGameMode() != 3) return;
        BedWars game = games.get(room);
        event.getPlayer().getInventory().removeItem(inHand);
        event.getPlayer().teleport((Location) rooms.get(room).getTeamData().get(game.getTeam(event.getPlayer().getName())).get("gameLocation"));
        event.getPlayer().sendMessage(DEFAULT_TITLE + "你成功使用了回城火药!");
    }

    @EventHandler
    public void onBreakCore(DataPacketReceiveEvent event) {
        String n = event.getPlayer().getName();
        int i = SBedWarsAPI.getInstance().isPlayerGaming(n);
        if (i == -1) return;
        if (games.get(i).getGameMode() != 3) return;
        if (!(event.getPacket() instanceof InventoryTransactionPacket)) return;
        InventoryTransactionPacket pk = (InventoryTransactionPacket) event.getPacket();
        if (pk.transactionType != InventoryTransactionPacket.TYPE_USE_ITEM_ON_ENTITY) return;
        UseItemOnEntityData data = (UseItemOnEntityData) pk.transactionData;
        if (data.actionType != 1) return;
        event.setCancelled(games.get(i).attackCrystal(event.getPlayer(), data.itemInHand, data.entityRuntimeId));
    }
    /*
    @EventHandler
    public void onBreakChest(BlockBreakEvent event) {
        int room = SBedWarsAPI.getInstance().isPlayerGaming(event.getPlayer().getName());
        if (room <= 0) return;
        if (games.get(room).getGameMode() != 3) return;
        if (!(event.getBlock() instanceof BlockTrappedChest)) return;
        BlockTrappedChest block = (BlockTrappedChest) event.getBlock();
        int playerTeam = games.get(room).getTeam(event.getPlayer().getName());
        String pos = block.getLocation().toString();
        if (!games.get(room).chestData.containsKey(pos)) return;
        int chestTeam = games.get(room).chestData.get(pos);
        if (playerTeam == chestTeam) return;
        SBedWarsAPI.getInstance().broadcastTeamSpeak(games.get(room).teams.get(chestTeam),"§c系统", "基地的陷阱箱被破坏了,是不是有人在偷城?请回去查看!");
    }*/

    @EventHandler(priority = EventPriority.HIGH)
    public void onCraft(CraftItemEvent event) {
        int room = SBedWarsAPI.getInstance().isPlayerGaming(event.getPlayer().getName());
        if (room <= 0) return;
        if (games.get(room).getGameMode() != 3) return;
        event.getPlayer().level.addSound(event.getPlayer().getLocation(), Sound.RANDOM_ANVIL_USE);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDie(PlayerDeathEvent event) {
        String name = event.getEntity().getName();
        int room = SBedWarsAPI.getInstance().isPlayerGaming(name);
        if (room <= 0) return;
        if (games.get(room).getGameMode() != 3) return;
        event.setCancelled(true);
        event.setCancelled(true);
        event.setCancelled(true);
        event.setCancelled(true);
        games.get(room).onDeath(event.getEntity(), event);
    }

    @EventHandler
    public void onAttack(EntityDamageByEntityEvent event) {
        String name = event.getEntity().getName();
        int room = SBedWarsAPI.getInstance().isPlayerGaming(name);
        if (room <= 0) return;
        if (games.get(room).getGameMode() != 3) return;
        if ((!(event.getEntity() instanceof Player)) || (!(event.getDamager() instanceof Player))) return;
        Item helmet = ((Player) event.getEntity()).getInventory().getHelmet();
        Item boots = ((Player) event.getEntity()).getInventory().getBoots();
        if (helmet == null || boots == null || helmet.getCustomBlockData() == null || boots.getCustomBlockData() == null)
            return;
        if (helmet.getCustomBlockData().contains("bedWars") && helmet.getCustomBlockData().getString("bedWars").equals("ironHelmet") && boots.getCustomBlockData().contains("bedWars") && boots.getCustomBlockData().getString("bedWars").equals("ironBoots")) {
            event.setKnockBack(0);
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        String name = event.getPlayer().getName();
        int room = SBedWarsAPI.getInstance().isPlayerGaming(name);
        if (room <= 0) return;
        if (games.get(room).getGameMode() != 3) return;
        locations.add(event.getBlock().getLocation().toString());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlock(BlockBreakEvent event) {
        String name = event.getPlayer().getName();
        int room = SBedWarsAPI.getInstance().isPlayerGaming(name);
        if (room <= 0) return;
        if (games.get(room).getGameMode() != 3) return;
        for (String pos : locations) {
            if (event.getBlock().getLocation().toString().equals(pos)) return;
        }
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onTouch(PlayerInteractEvent event) {
        String name = event.getPlayer().getName();
        int room = SBedWarsAPI.getInstance().isPlayerGaming(name);
        if (room <= 0) return;
        if (games.get(room).getGameMode() != 3) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            int room = SBedWarsAPI.getInstance().isPlayerGaming(event.getEntity().getName());
            if (room <= 0) return;
            if (games.get(room).getGameMode() != 3) {
                event.setDamage(0);
                event.setCancelled(true);
            }
        }
    }
}
