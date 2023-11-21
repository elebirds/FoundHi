package net.mcpes.foundhi.plugin.rpg.rpg.task;

import cn.nukkit.entity.Entity;
import cn.nukkit.level.Level;
import cn.nukkit.level.particle.DustParticle;
import cn.nukkit.level.particle.Particle;
import cn.nukkit.scheduler.PluginTask;
import net.mcpes.foundhi.plugin.rpg.rpg.RPGMain;

/**
 * @author yui
 * @date 2018/1/14
 * @since Yui-RPG
 */

public class ParticleTask extends PluginTask<RPGMain> {
    private Entity entity;
    private String pos;

    public ParticleTask(RPGMain owner, long entity, Level level) {
        super(owner);
        this.entity = level.getEntity(entity);
    }

    @Override
    public void onRun(int i) {
        int tid = entity.namedTag.getInt("texiao");
        Particle particle = new DustParticle(entity.getPosition(), 245, 245, 245);
        switch (tid) {
            case 0:
                particle = null;
                break;
            case 1:
                particle = new DustParticle(entity.getPosition(), 245, 245, 245);
                break;
            case 2:
                particle = new DustParticle(entity.getPosition(), 100, 149, 237);
                break;
            case 3:
                particle = new DustParticle(entity.getPosition(), 255, 255, 0);
                break;
            case 4:
                particle = new DustParticle(entity.getPosition(), 124, 252, 0);
                break;
            case 5:
                particle = new DustParticle(entity.getPosition(), 255, 0, 0);
                break;
        }
        if (particle != null) {
            entity.getLevel().addParticle(particle);
        } else {
            this.getHandler().cancel();
        }
        if (entity.getPosition().toString().equals(this.pos)) {
            this.getHandler().cancel();
        }
        this.pos = entity.getPosition().toString();
        if (!entity.isAlive()) {
            this.getHandler().cancel();
        }
    }
}
