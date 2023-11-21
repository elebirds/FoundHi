package net.mcpes.foundhi.plugin.currency.mw.listener;

import cn.nukkit.Player;
import cn.nukkit.block.BlockLava;
import cn.nukkit.block.BlockWater;
import cn.nukkit.entity.item.EntityPrimedTNT;
import cn.nukkit.entity.mob.EntityCreeper;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockUpdateEvent;
import cn.nukkit.event.entity.EntityExplosionPrimeEvent;
import cn.nukkit.event.entity.EntityLevelChangeEvent;
import cn.nukkit.event.entity.ExplosionPrimeEvent;
import cn.nukkit.event.level.LevelLoadEvent;
import cn.nukkit.event.level.LevelUnloadEvent;
import cn.nukkit.level.Level;
import net.mcpes.foundhi.plugin.currency.mw.config.WorldConfig;
import net.mcpes.foundhi.plugin.currency.mw.utils.World;

import java.util.ArrayList;

import static net.mcpes.foundhi.plugin.currency.mw.SMultiWorld.DEFAULT_TITLE;
import static net.mcpes.foundhi.plugin.currency.mw.SMultiWorld.worlds;

/**
 * @author hhm
 * @date 2017/7/8
 * @since SMultiWorld
 */

public class WorldListener implements Listener {
    @EventHandler
    public void load(LevelLoadEvent event) {
        String name = event.getLevel().getName();
        if (!worlds.containsKey(name)) {
            WorldConfig wc = new WorldConfig(name);
            wc.init();
            worlds.put(name, World.fromMap(name, wc.getConfig().getAll()));
            switch (worlds.get(name).getWeather()) {
                case 0:
                    event.getLevel().setRaining(false);
                    event.getLevel().setThundering(false);
                    event.getLevel().setThunderTime(Integer.MAX_VALUE);
                    event.getLevel().setRainTime(Integer.MAX_VALUE);
                    break;
            }
        }
        if (!worlds.get(event.getLevel().getName()).isGrow()) {
            event.getLevel().setTickRate(0);
        }
    }

    @EventHandler
    public void update(BlockUpdateEvent event) {
        if (event.getBlock() instanceof BlockLava) {
            if (!worlds.get(event.getBlock().getLevel().getName()).isLava_flow()) event.setCancelled(true);
            return;
        }
        if (event.getBlock() instanceof BlockWater) {
            if (!worlds.get(event.getBlock().getLevel().getName()).isWater_flow()) event.setCancelled(true);
        }
    }

    @EventHandler
    public void boom_creeper(ExplosionPrimeEvent event) {
        if (event.getEntity() instanceof EntityCreeper) {
            event.setBlockBreaking(worlds.get(event.getEntity().getLevel().getName()).isCreeper_boom());
        }
    }

    @EventHandler
    public void boom_tnt(EntityExplosionPrimeEvent event) {
        if (event.getEntity() instanceof EntityPrimedTNT) {
            event.setBlockBreaking(worlds.get(event.getEntity().getLevel().getName()).isTnt_boom());
        }
    }

    @EventHandler
    public void unload(LevelUnloadEvent event) {
        worlds.remove(event.getLevel().getName());
    }

    @EventHandler
    public void join(EntityLevelChangeEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        Level level_new = event.getTarget();
        Player player = (Player) event.getEntity();
        if (event.getOrigin() != null) {
            Level level_old = event.getOrigin();
            World old = worlds.get(level_old.getName());
            if (!old.getQuit_message().equals("{%not}")) {
                player.sendMessage(DEFAULT_TITLE + old.getQuit_message().replace("{%worldName}", old.getName()).replace("{%player}", player.getName()));
            }
        }
        World world = worlds.get(level_new.getName());
        if (world.getMaxPlayer() == 0) {
            player.sendMessage(DEFAULT_TITLE + "此世界不允许进入!");
            event.setCancelled(true);

        }
        if (world.getMaxPlayer() > 0) {
            if (level_new.getPlayers().size() >= world.getMaxPlayer()) {
                player.sendMessage(DEFAULT_TITLE + "此世界已满人!");
                event.setCancelled(true);
                return;
            }
        }
        ArrayList<Integer> gameModes = world.getGameMode();
        if (!gameModes.contains(player.getGamemode())) {
            player.sendMessage(DEFAULT_TITLE + "此世界禁止使用你现在的游戏模式!已经自动切换!");
        }
        if (world.getJoin_message().equals("{%not}")) return;
        player.sendMessage(DEFAULT_TITLE + world.getJoin_message().replace("{%worldName}", world.getName()).replace("{%player}", player.getName()));
    }
}
