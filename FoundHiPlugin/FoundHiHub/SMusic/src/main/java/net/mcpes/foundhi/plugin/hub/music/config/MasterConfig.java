package net.mcpes.foundhi.plugin.hub.music.config;

import cn.nukkit.utils.SimpleConfig;
import net.mcpes.foundhi.plugin.hub.music.SMusic;

import static net.mcpes.foundhi.plugin.hub.music.SMusic.LIST_LOOP_PLAY;

/**
 * @author hhm
 */

public class MasterConfig extends SimpleConfig {
    @Path(value = "播放模式")
    public int mode = LIST_LOOP_PLAY;

    public MasterConfig() {
        super(SMusic.getInstance(), "config.yml");
    }
}