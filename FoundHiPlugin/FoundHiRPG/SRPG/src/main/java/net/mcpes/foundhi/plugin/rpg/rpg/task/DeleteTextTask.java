package net.mcpes.foundhi.plugin.rpg.rpg.task;

import cn.nukkit.Player;
import cn.nukkit.network.protocol.RemoveEntityPacket;
import cn.nukkit.scheduler.PluginTask;
import net.mcpes.foundhi.plugin.rpg.rpg.RPGMain;

/**
 * @author yui
 * @date 2017/10/13
 * @since RPG
 */

public class DeleteTextTask extends PluginTask<RPGMain> {
    private Player player;
    private long eid;

    public DeleteTextTask(RPGMain owner, Player player, long eid) {
        super(owner);
        this.player = player;
        this.eid = eid;
    }

    @Override
    public void onRun(int i) {
        RemoveEntityPacket pk = new RemoveEntityPacket();
        pk.eid = this.eid;
        player.dataPacket(pk);
    }
}
