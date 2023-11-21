package net.mcpes.foundhi.plugin.hub.music;

import cn.nukkit.Player;
import cn.nukkit.plugin.PluginBase;
import com.xxmicloxx.NoteBlockAPI.Song;
import net.mcpes.foundhi.plugin.hub.music.command.MusicCommand;
import net.mcpes.foundhi.plugin.hub.music.config.MasterConfig;
import net.mcpes.foundhi.plugin.hub.music.config.MusicTitleConfig;
import net.mcpes.foundhi.plugin.hub.music.listener.FloatingListener;
import net.mcpes.foundhi.plugin.hub.music.listener.PlayerListener;
import net.mcpes.foundhi.plugin.hub.music.player.MusicPlayer;
import net.mcpes.foundhi.plugin.hub.music.task.FloatingTask;
import net.mcpes.foundhi.plugin.hub.music.task.PlayerTick;

import java.io.File;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author hhm
 */

public class SMusic extends PluginBase {
    public static final String PluginName = "§l§7|§bS§a音§c乐§7|";
    public static final byte LIST_LOOP_PLAY = 1;
    public static final byte SINGLE_SONG_LOOP_PLAY = 2;
    public static final byte SEQUENTIAL_PLAY = 3;
    public static final byte RANDOM_PLAY = 4;
    public static CopyOnWriteArrayList<Song> songs;
    public static CopyOnWriteArrayList<Player> songPlayers;
    public static byte playMode;
    public static boolean playing;
    public static MusicPlayer mp;
    public static CopyOnWriteArrayList<Long> floatingEID = new CopyOnWriteArrayList<>();
    public static CopyOnWriteArrayList<String> floatingPos = new CopyOnWriteArrayList<>();
    private static SMusic instance;
    private static SMusicAPI api;
    private static MasterConfig mc;

    public static SMusic getInstance() {
        return instance;
    }

    public static SMusicAPI getAPI() {
        return api;
    }

    @Override
    public void onLoad() {
        playing = true;
        instance = this;
        mc = new MasterConfig();
        mc.load();
        mc.save();
        api = new SMusicAPI();
        songs = new CopyOnWriteArrayList<>();
        songPlayers = new CopyOnWriteArrayList<>();
        playMode = (byte) (mc.mode < 5 && mc.mode > 0 ? mc.mode : LIST_LOOP_PLAY);
        MusicTitleConfig.init();
        MusicTitleConfig.config.save();
        mp = new MusicPlayer();
        this.getServer().getLogger().info(PluginName + "§6加载成功");
    }

    @Override
    public void onEnable() {
        new File(this.getDataFolder() + "/songs/").mkdirs();
        api.loadAllSong();
        if (songs.size() > 0) {
            this.getServer().getScheduler().scheduleAsyncTask(this, new PlayerTick());
            this.getServer().getScheduler().scheduleAsyncTask(this, new FloatingTask());
            this.getServer().getCommandMap().register("music", new MusicCommand());
            this.getServer().getPluginManager().registerEvents(new PlayerListener(), this);
            this.getServer().getPluginManager().registerEvents(new FloatingListener(), this);
        }
        this.getServer().getLogger().info(PluginName + "§6开启成功");
    }

    @Override
    public void onDisable() {
        this.getServer().getLogger().info(PluginName + "§6关闭成功");
    }
}

