package net.mcpes.foundhi.plugin.currency.mw.task;

import cn.nukkit.Server;
import cn.nukkit.level.Level;
import cn.nukkit.plugin.Plugin;
import cn.nukkit.scheduler.PluginTask;
import net.mcpes.foundhi.plugin.currency.mw.utils.World;

import static net.mcpes.foundhi.plugin.currency.mw.SMultiWorld.worlds;

public class ClearTask extends PluginTask {

    public ClearTask(Plugin owner) {
        super(owner);
    }

    @Override
    public void onRun(int i) {
        for (World world : worlds.values()) {
            switch (world.getWeather()) {
                case 0:
                    Level level =
                            Server.getInstance().getLevelByName(world.getName());
                    level.setRaining(false);
                    level.setThundering(false);
                    level.setRainTime(Integer.MAX_VALUE);
                    level.setThunderTime(Integer.MAX_VALUE);
                    break;
            }
        }
    }
}
