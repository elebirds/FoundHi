package net.mcpes.foundhi.plugin.hub.music.utils;

import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.data.EntityMetadata;
import cn.nukkit.item.Item;
import cn.nukkit.level.Position;
import cn.nukkit.network.protocol.AddPlayerPacket;
import cn.nukkit.network.protocol.RemoveEntityPacket;
import cn.nukkit.network.protocol.SetEntityDataPacket;

import java.util.HashMap;
import java.util.UUID;

/**
 * @author hhm
 * @date 2017/7/18
 * @since SMusic
 */

public class FloatingTextPacket {
    public static HashMap<Long, HashMap<String, Object>> datas = new HashMap<>();

    public static long addFloatingText(String title, String text, Position pos) {
        long eid = Entity.entityCount++;
        AddPlayerPacket pk = new AddPlayerPacket();
        pk.uuid = UUID.randomUUID();
        pk.username = "";
        pk.entityUniqueId = eid;
        pk.entityRuntimeId = eid;
        pk.x = (float) pos.x;
        pk.y = (float) (pos.y - 0.75);
        pk.z = (float) pos.z;
        pk.speedX = 0;
        pk.speedY = 0;
        pk.speedZ = 0;
        pk.yaw = 0;
        pk.pitch = 0;
        long flags = (
                (1L << Entity.DATA_FLAG_CAN_SHOW_NAMETAG) |
                        (1L << Entity.DATA_FLAG_ALWAYS_SHOW_NAMETAG) |
                        (1L << Entity.DATA_FLAG_IMMOBILE)
        );
        pk.metadata = new EntityMetadata()
                .putLong(Entity.DATA_FLAGS, flags)
                .putString(Entity.DATA_NAMETAG, title + "\n" + text)
                .putLong(Entity.DATA_LEAD_HOLDER_EID, -1)
                .putFloat(Entity.DATA_SCALE, 0f); //zero causes problems on debug builds?
        pk.item = Item.get(Item.AIR);
        Server.broadcastPacket(Server.getInstance().getOnlinePlayers().values(), pk);
        HashMap<String, Object> data = new HashMap<>();
        data.put("title", title);
        data.put("text", text);
        data.put("pos", pos);
        data.put("packet", pk);
        datas.put(eid, data);
        return eid;
    }

    public static void setFloatingText(long eid, String title, String text) {
        SetEntityDataPacket npk = new SetEntityDataPacket();
        long flags = 114688L;
        npk.metadata = (new EntityMetadata()).putLong(Entity.DATA_FLAGS, flags)
                .putString(Entity.DATA_NAMETAG, title + "\n" + text)
                .putLong(Entity.DATA_LEAD_HOLDER_EID, -1)
                .putFloat(Entity.DATA_SCALE, 0f);
        npk.eid = eid;
        Server.broadcastPacket(Server.getInstance().getOnlinePlayers().values(), npk);
        HashMap<String, Object> data = datas.get(eid);
        data.put("title", title);
        data.put("text", text);
        AddPlayerPacket pk = (AddPlayerPacket) data.get("packet");
        pk.metadata = (new EntityMetadata()).putLong(Entity.DATA_FLAGS, flags)
                .putString(Entity.DATA_NAMETAG, title + "\n" + text)
                .putLong(Entity.DATA_LEAD_HOLDER_EID, -1)
                .putFloat(Entity.DATA_SCALE, 0.0f);
        data.put("packet", pk);
        datas.put(eid, data);
    }

    public static boolean removeFloatingText(long eid) {
        RemoveEntityPacket pk = new RemoveEntityPacket();
        pk.eid = eid;
        Server.broadcastPacket(Server.getInstance().getOnlinePlayers().values(), pk);
        datas.remove(eid);
        return true;
    }
}