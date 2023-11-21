package net.mcpes.foundhi.plugin.game.bedwars.listener;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.CommandSender;
import cn.nukkit.entity.passive.EntityVillager;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.player.PlayerFormRespondedEvent;
import cn.nukkit.form.response.FormResponseCustom;
import cn.nukkit.form.response.FormResponseSimple;
import net.mcpes.foundhi.plugin.game.bedwars.SBedWars;
import net.mcpes.foundhi.plugin.game.bedwars.gui.GUIShopBase;

import static net.mcpes.foundhi.plugin.game.bedwars.SBedWars.games;
import static net.mcpes.foundhi.plugin.game.bedwars.gui.GUIShopBase.*;

public class GUIListener implements Listener {
    @EventHandler
    public void onResult(PlayerFormRespondedEvent event) {
        String name = event.getPlayer().getName();
        if (!see.contains(name)) return;
        if (event.wasClosed()) {
            typeThree.remove(name);
            inLevel.remove(name);
            inType.remove(name);
            see.remove(name);
            return;
        }
        if (inLevel.containsKey(name)) {
            switch (inLevel.get(name)) {
                case 1:
                    GUIShopBase.handle(event.getPlayer(), (FormResponseSimple) event.getResponse());
                    break;
                case 2:
                    GUIShopBase.second.get(inType.get(name)).handle(event.getPlayer(), (FormResponseSimple) event.getResponse());
                    break;
                case 3:
                    GUIShopBase.second.get(inType.get(name)).getThird().get(typeThree.get(name)).handle(event.getPlayer(), (FormResponseCustom) event.getResponse());
                    break;
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onOpen(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof EntityVillager) {
            int r = SBedWars.getInstance().isPlayerGaming(event.getDamager().getName());
            if (r != -1 && event.getEntity().namedTag.contains("bedwars") && event.getEntity().namedTag.getInt("bedwars") == r) {
                if (games.get(r).getAlivePlayers().contains(event.getDamager())) {
                    event.setDamage(0);
                    event.setCancelled(true);
                    Server.getInstance().dispatchCommand((CommandSender) event.getDamager(), "bw gui");
                }
                event.setCancelled(true);
            }
        }
    }
}
