package net.mcpes.foundhi.plugin.hub.music.task;

import cn.nukkit.scheduler.AsyncTask;

import static net.mcpes.foundhi.plugin.hub.music.SMusic.*;

public class PlayerTick extends AsyncTask {
    @Override
    public void onRun() {
        while (true) {
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (playing) {
                if (songPlayers.size() != 0) {
                    mp.broadcast();
                }
            }
        }
    }
}
