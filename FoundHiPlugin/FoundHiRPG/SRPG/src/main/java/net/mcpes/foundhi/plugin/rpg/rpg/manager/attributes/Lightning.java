package net.mcpes.foundhi.plugin.rpg.rpg.manager.attributes;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.weather.EntityLightning;
import cn.nukkit.network.protocol.AddEntityPacket;
import net.mcpes.foundhi.plugin.rpg.rpg.RPGMain;
import net.mcpes.foundhi.plugin.rpg.rpg.Utils;
import net.mcpes.foundhi.plugin.rpg.rpg.manager.BuildManager;

import java.util.Map;

/**
 * FoundHi-RPG
 *
 * @author yui Copyright (c) 2018/6/6
 * version 1.0
 */
@SuppressWarnings("unchecked")
public class Lightning extends Attribute {
    private int lightningPR;
    private int lightningDamage;

    public Lightning(int lightningPR, int lightningDamage) {
        this.lightningDamage = lightningDamage;
        this.lightningPR = lightningPR;
    }

    @Override
    public float doSkill(Entity attack, Entity entity) {
        float damage = 0f;
        int PRAdd = 0;
        int damageAdd = 0;
        if (BuildManager.containPlayerBuild(attack.getName())) {
            if (BuildManager.containAdd(attack.getName(), "雷击")) {
                Map<String, Object> content = (Map<String, Object>) BuildManager.getContent(attack.getName(), "雷击");
                PRAdd = (int) content.get("机率");
                damageAdd = (int) content.get("伤害");
            }
        }
        if (Utils.random(lightningPR + PRAdd)) {
            damage = lightningDamage + damageAdd;
            this.callLightning(entity);
            if (attack instanceof Player) {
                Utils.addText((Player) attack, "§l§b雷击！", entity.getX(), entity.getY() + entity.getEyeHeight(), entity.getZ());
            }
        }
        return damage;
    }

    private void callLightning(Entity entity) {
        AddEntityPacket pk = new AddEntityPacket();
        pk.type = EntityLightning.NETWORK_ID;
        pk.x = (float) entity.x;
        pk.y = (float) entity.y;
        pk.z = (float) entity.z;
        pk.speedX = 0;
        pk.speedY = 0;
        pk.speedZ = 0;
        pk.yaw = (float) entity.yaw;
        pk.pitch = (float) entity.pitch;
        Server server = RPGMain.getInstance().getServer();
        server.batchPackets(server.getOnlinePlayers().values().toArray(new Player[server.getOnlinePlayers().size()]), new AddEntityPacket[]{pk});
    }
}
