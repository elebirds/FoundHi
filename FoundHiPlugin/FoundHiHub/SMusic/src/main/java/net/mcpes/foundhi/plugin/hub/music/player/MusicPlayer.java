package net.mcpes.foundhi.plugin.hub.music.player;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.level.Level;
import cn.nukkit.level.Location;
import com.xxmicloxx.NoteBlockAPI.Song;
import net.mcpes.foundhi.plugin.hub.music.config.MusicTitleConfig;
import net.mcpes.foundhi.plugin.hub.music.utils.FloatingTextPacket;

import java.text.NumberFormat;
import java.util.ArrayList;

import static net.mcpes.foundhi.plugin.hub.music.SMusic.*;

/**
 * @author hhm
 */

public class MusicPlayer {
    private Song song = null;
    private short tick = -1;
    private long lastPlayed = 0;

    public void setTick(short tick) {
        this.tick = tick;
    }

    public Song getSong() {
        return song;
    }

    public void setSong(Song song) {
        this.song = song;
    }

    public void nextSong() {
        if (!songs.contains(song)) {
            this.song = songs.get(0);
            return;
        }
        if (songs.indexOf(song) >= songs.size() - 1) {
            this.song = songs.get(0);
            return;
        }
        this.song = songs.get(songs.indexOf(song) + 1);
    }

    public void lastSong() {
        if (!songs.contains(song)) {
            this.song = songs.get(0);
            return;
        }
        if (songs.indexOf(song) < 1) {
            this.song = songs.get(songs.size() - 1);
            return;
        }
        this.song = songs.get(songs.indexOf(song) - 1);
    }

    public void broadcast() {
        if (!playing) return;
        if (song == null) this.nextSong();
        if (System.currentTimeMillis() - lastPlayed < 50 * song.getDelay()) return;
        tick++;
        if (tick > song.getLength()) {
            getAPI().down();
        }
        for (Player player : songPlayers) {
            if (player == null) {
                songPlayers.remove(null);
            } else {
                if (!player.isOnline()) songPlayers.remove(player);
                getAPI().sendLevelSoundEventPacket(player, song, tick);
            }
        }
        lastPlayed = System.currentTimeMillis();
    }

    public void floatingTick() {
        if (MusicTitleConfig.getPos().size() == 0) return;
        String title = "§3正在播放: §2" + song.getTitle();
        if (floatingEID.size() == 0) {
            for (String pos : MusicTitleConfig.getPos()) {
                floatingPos.add(pos);
                String[] m = pos.split(":");
                Level level = Server.getInstance().getLevelByName(m[3]);
                if (level != null) {
                    Location location = new Location(Integer.valueOf(m[0]), Integer.valueOf(m[1]), Integer.valueOf(m[2]), level);
                    floatingEID.add(FloatingTextPacket.addFloatingText(title, getPace(), location));
                }
            }
            return;
        }
        ArrayList<String> need = null;
        for (String pos : MusicTitleConfig.getPos()) {
            need = new ArrayList<>();
            if (!floatingPos.contains(pos)) {
                need.add(pos);
            }
        }
        if (need != null) {
            for (String pos : need) {
                floatingPos.add(pos);
                String[] m = pos.split(":");
                Level level = Server.getInstance().getLevelByName(m[3]);
                if (level != null) {
                    Location location = new Location(Integer.valueOf(m[0]), Integer.valueOf(m[1]), Integer.valueOf(m[2]), level);
                    floatingEID.add(FloatingTextPacket.addFloatingText(title, getPace(), location));
                }
            }
        }
        for (long eid : floatingEID) {
            FloatingTextPacket.setFloatingText(eid, title, getPace());
        }
    }

    private String getPace() {
        if (song == null) return "§8|||||||||||||||||||||||||";
        NumberFormat format = NumberFormat.getInstance();
        format.setMaximumFractionDigits(0);
        int bfb = (int) Math.floor(((float) tick / ((float) song.getLength())) * 100);
        StringBuilder pace = new StringBuilder("§a");
        for (int i = 0; i <= 100; i += 4) {
            if (i <= bfb) {
                pace.append("|");
            } else if (i - 4 <= bfb) {
                pace.append("§8|");
            } else {
                pace.append("|");
            }
        }
        return pace.toString();
    }
}