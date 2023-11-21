package net.mcpes.foundhi.core.plugin.entity.entity.creature

import cn.nukkit.entity.EntityLiving
import cn.nukkit.level.Location
import net.mcpes.foundhi.core.utils.EntityUtils

/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/7/20
 * version 1.0
 */
abstract class EntityCreature(location: Location) : EntityLiving(EntityUtils.getChunk(location), EntityUtils.getNBT(location)) {

}