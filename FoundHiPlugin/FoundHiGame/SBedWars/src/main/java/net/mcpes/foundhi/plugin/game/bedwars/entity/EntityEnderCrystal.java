package net.mcpes.foundhi.plugin.game.bedwars.entity;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.level.Location;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.DoubleTag;
import cn.nukkit.nbt.tag.FloatTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.network.protocol.AddEntityPacket;
import cn.nukkit.network.protocol.RemoveEntityPacket;

public class EntityEnderCrystal extends Entity {
    public int health = 100;
    private long eid = -1;

    public EntityEnderCrystal(Location pos) {
        super(pos.level.getChunk((int) pos.x >> 4, (int) pos.z >> 4, true), new CompoundTag().putList(new ListTag<DoubleTag>("Pos").add(new DoubleTag("", pos.x)).add(new DoubleTag("", pos.y)).add(new DoubleTag("", pos.z))).putList(new ListTag<DoubleTag>("Motion").add(new DoubleTag("", 0)).add(new DoubleTag("", 0)).add(new DoubleTag("", 0))).putList(new ListTag<FloatTag>("Rotation").add(new FloatTag("", 0)).add(new FloatTag("", 0))));
        if (eid == -1) this.eid = entityCount++;
    }

    @Override
    public int getNetworkId() {
        return 71;
    }

    public long getEid() {
        return eid;
    }

    @Override
    public void spawnTo(Player player) {
        this.getServer().getLogger().info("Spawn Ender Crystal (eid:" + eid + ") to" + player.getName());
        AddEntityPacket pk = new AddEntityPacket();
        pk.entityRuntimeId = this.eid;
        pk.entityUniqueId = this.eid;
        pk.x = (float) this.x;
        pk.y = (float) this.y;
        pk.z = (float) this.z;
        pk.yaw = (float) this.yaw;
        pk.pitch = (float) this.pitch;
        pk.type = this.getNetworkId();
        pk.metadata = this.dataProperties;
        player.dataPacket(pk);
    }

    @Override
    public void despawnFrom(Player player) {

    }

    @Override
    public void despawnFromAll() {

    }

    public void despawn(Player player) {
        this.getServer().getLogger().info("Despawn Ender Crystal (eid:" + eid + ") to" + player.getName());
        RemoveEntityPacket pk = new RemoveEntityPacket();
        pk.eid = eid;
        player.dataPacket(pk);
    }
}
