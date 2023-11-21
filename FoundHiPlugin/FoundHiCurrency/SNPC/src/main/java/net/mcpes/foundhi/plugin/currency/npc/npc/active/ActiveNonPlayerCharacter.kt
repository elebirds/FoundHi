package net.mcpes.foundhi.plugin.currency.npc.npc.active

import cn.nukkit.entity.Entity
import cn.nukkit.level.format.FullChunk
import cn.nukkit.nbt.tag.CompoundTag
import net.mcpes.foundhi.plugin.currency.npc.npc.NonPlayerCharacter

/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/7/9
 * version 1.0
 */
abstract class ActiveNonPlayerCharacter(chunk: FullChunk, nbt: CompoundTag) : Entity(chunk, nbt), NonPlayerCharacter