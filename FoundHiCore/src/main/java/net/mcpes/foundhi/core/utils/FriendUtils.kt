package net.mcpes.foundhi.core.utils

import net.mcpes.foundhi.core.handle.HandleSystem
import net.mcpes.foundhi.core.handle.friend.FriendAcceptHandle
import net.mcpes.foundhi.core.handle.friend.FriendApplyHandle
import net.mcpes.foundhi.core.handle.friend.FriendDeleteHandle
import net.mcpes.foundhi.core.handle.friend.FriendRefuseHandle
import net.mcpes.foundhi.core.network.protocol.command.CommandPacket
import net.mcpes.foundhi.core.plugin.FoundHiPlugin
import net.mcpes.foundhi.core.plugin.data.PlayerCache

/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/7/7
 * version 1.0
 */
object FriendUtils {
    @JvmStatic
    fun apply(from: String, to: String): Int {
        val cache: PlayerCache = FoundHiPlugin.instance.getPlayer(from)!!
        if (cache.friends.containsKey(to)) return -1
        val pk = CommandPacket()
        pk.event = true
        val evid = HandleSystem.eventID++
        pk.eventID = "$evid${FoundHiPlugin.instance.id}"
        pk.sid = FoundHiPlugin.instance.id
        pk.cmd = "friend apply $from $to"
        FoundHiPlugin.instance.dataPacket(pk)
        FoundHiPlugin.instance.addHandle(evid, FriendApplyHandle(from, to))
        return 0
    }

    @JvmStatic
    fun delete(from: String, to: String): Int {
        val cache: PlayerCache = FoundHiPlugin.instance.getPlayer(from)!!
        if (!cache.friends.containsKey(to)) return -1
        val pk = CommandPacket()
        pk.event = true
        val evid = HandleSystem.eventID++
        pk.eventID = "$evid${FoundHiPlugin.instance.id}"
        pk.sid = FoundHiPlugin.instance.id
        pk.cmd = "friend delete $from $to"
        FoundHiPlugin.instance.dataPacket(pk)
        FoundHiPlugin.instance.addHandle(evid, FriendDeleteHandle(from, to))
        return 0
    }

    @JvmStatic
    fun accept(from: String, to: String): Int {
        val cache: PlayerCache = FoundHiPlugin.instance.getPlayer(from)!!
        if (cache.friends.containsKey(to)) return -1
        val pk = CommandPacket()
        pk.event = true
        val evid = HandleSystem.eventID++
        pk.eventID = "$evid${FoundHiPlugin.instance.id}"
        pk.sid = FoundHiPlugin.instance.id
        pk.cmd = "friend accept $from $to"
        FoundHiPlugin.instance.dataPacket(pk)
        FoundHiPlugin.instance.addHandle(evid, FriendAcceptHandle(from, to))
        return 0
    }

    @JvmStatic
    fun refuse(from: String, to: String) {
        val pk = CommandPacket()
        pk.event = true
        val evid = HandleSystem.eventID++
        pk.eventID = "$evid${FoundHiPlugin.instance.id}"
        pk.sid = FoundHiPlugin.instance.id
        pk.cmd = "friend refuse $from $to"
        FoundHiPlugin.instance.dataPacket(pk)
        FoundHiPlugin.instance.addHandle(evid, FriendRefuseHandle(from, to))
    }
}