package net.mcpes.foundhi.plugin.hub.music;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.network.protocol.LevelSoundEventPacket;
import com.xxmicloxx.NoteBlockAPI.Layer;
import com.xxmicloxx.NoteBlockAPI.NBSDecoder;
import com.xxmicloxx.NoteBlockAPI.Note;
import com.xxmicloxx.NoteBlockAPI.Song;

import java.io.File;
import java.util.Random;

import static net.mcpes.foundhi.plugin.hub.music.SMusic.*;

/**
 * @author hhm
 */

public class SMusicAPI {
    public void loadAllSong() {
        String f[] = new File(SMusic.getInstance().getDataFolder() + "/songs/").list();
        if (f == null) {
            return;
        }
        for (String name : f) {
            if (!name.endsWith(".nbs")) {
                return;
            } else {
                this.loadSong(name.replace(".nbs", ""));
            }
        }
    }

    public void loadSong(String name) {
        Song song = NBSDecoder.parse(new File(SMusic.getInstance().getDataFolder() + "/songs/" + name + ".nbs"), name);
        if (song == null) return;
        SMusic.songs.add(song);
        SMusic.getInstance().getServer().getLogger().info(PluginName + "§6成功加载音乐 " + name + "!");
    }

    public void sendLevelSoundEventPacket(Player player, Song song, int tick) {
        for (Layer l : song.getLayerHashMap().values()) {
            Note note = l.getNote(tick);
            if (note == null) continue;
            LevelSoundEventPacket pk = new LevelSoundEventPacket();
            pk.extraData = note.getInstrument();
            pk.isBabyMob = false;
            pk.isGlobal = false;
            pk.pitch = note.getKey() - 33;
            pk.x = (float) player.getPosition().x;
            pk.y = (float) player.getPosition().y;
            pk.z = (float) player.getPosition().z;
            pk.sound = LevelSoundEventPacket.SOUND_NOTE;
            player.dataPacket(pk);
        }
    }

    public void up() {
        switch (playMode) {
            case LIST_LOOP_PLAY://列表循环
                mp.lastSong();
                mp.setTick((short) -1);
                Server.getInstance().broadcastMessage(PluginName + "§6正在播放: " + mp.getSong().getTitle() + "");
                break;
            case SEQUENTIAL_PLAY://列表播放
                if (songs.get(0) == mp.getSong()) {
                    playing = false;
                    mp.setTick((short) -1);
                    Server.getInstance().getLogger().info(PluginName + "§6列表播放完毕,暂停播放");
                    return;
                }
                mp.setTick((short) -1);
                mp.lastSong();
                Server.getInstance().getLogger().info(PluginName + "§6正在播放: " + mp.getSong().getTitle() + "");
                break;
            case SINGLE_SONG_LOOP_PLAY://单曲循环
                mp.setTick((short) -1);
                Server.getInstance().getLogger().info(PluginName + "§6仍然播放: " + mp.getSong().getTitle() + "");
                break;
        }
    }

    public void down() {
        switch (playMode) {
            case RANDOM_PLAY://随机播放
                int random = new Random().nextInt(songs.size());
                while (songs.get(random) == mp.getSong()) {
                    random = new Random().nextInt(songs.size());
                }
                mp.setSong(songs.get(random));
                mp.setTick((short) -1);
                Server.getInstance().broadcastMessage(PluginName + "§6正在播放: " + mp.getSong().getTitle() + "");
                break;
            case LIST_LOOP_PLAY://列表循环
                mp.nextSong();
                mp.setTick((short) -1);
                Server.getInstance().broadcastMessage(PluginName + "§6正在播放: " + mp.getSong().getTitle() + "");
                break;
            case SEQUENTIAL_PLAY://列表播放
                if (songs.get(songs.size()) == mp.getSong()) {
                    playing = false;
                    mp.setTick((short) -1);
                    Server.getInstance().getLogger().info(PluginName + "§6列表播放完毕,暂停播放");
                    return;
                }
                mp.setTick((short) -1);
                mp.nextSong();
                Server.getInstance().getLogger().info(PluginName + "§6正在播放: " + mp.getSong().getTitle() + "");
                break;
            case SINGLE_SONG_LOOP_PLAY://单曲循环
                mp.setTick((short) -1);
                Server.getInstance().getLogger().info(PluginName + "§6仍然播放: " + mp.getSong().getTitle() + "");
                break;
        }
    }
}