package net.mcpes.foundhi.plugin.game.bedwars.listener;

import cn.nukkit.Server;
import cn.nukkit.blockentity.BlockEntitySign;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import net.mcpes.foundhi.plugin.game.bedwars.SBedWars;
import net.mcpes.foundhi.plugin.game.bedwars.data.RoomData;
import net.mcpes.foundhi.plugin.game.bedwars.utils.PlayerObject;

import java.util.Objects;

import static net.mcpes.foundhi.plugin.game.bedwars.SBedWars.*;

/**
 * @author hhm @ SBedWars Project
 **/
public class NormalListener implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        String name = event.getPlayer().getName();
        players.put(name, new PlayerObject(name));
        event.getPlayer().setSpawn(Server.getInstance().getDefaultLevel().getSafeSpawn());
        for (RoomData data : SBedWars.rooms.values()) {
            if (event.getPlayer().level.getName().equals(data.getGameWorld())) {
                event.getPlayer().teleport(Server.getInstance().getDefaultLevel().getSpawnLocation());
                break;
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        String name = event.getPlayer().getName();
        players.get(name).save();
    }

    @EventHandler
    public void onTouch(PlayerInteractEvent event) {
        if (dataSignSetting.contains(event.getPlayer().getName())) {
            if (event.getBlock().getId() == 323 || event.getBlock().getId() == 63 || event.getBlock().getId() == 68) {
                master.dataSign = ((int) event.getBlock().getLocation().x) + ":" + (int) event.getBlock().getLocation().y + ":" + (int) event.getBlock().getLocation().z + event.getBlock().getLevel().getName();
                event.getPlayer().sendMessage(DEFAULT_TITLE + "数据查看牌设置成功!");
                BlockEntitySign sign = (BlockEntitySign) event.getBlock().getLevel().getBlockEntity(event.getBlock().getLocation());
                sign.setText(DEFAULT_TITLE, "", "§5======§bMy §cINFO§5======");
                dataSignSetting.remove(event.getPlayer().getName());
            }
        } else if (Objects.equals(master.dataSign, ((int) event.getBlock().getLocation().x) + ":" + (int) event.getBlock().getLocation().y + ":" + (int) event.getBlock().getLocation().z + event.getBlock().getLevel().getName())) {
            Server.getInstance().dispatchCommand(event.getPlayer(), "bw my");
        }
    }
}
