package net.mcpes.foundhi.core.utils

import net.mcpes.foundhi.core.handle.HandleSystem
import net.mcpes.foundhi.core.handle.marry.MarryAcceptHandle
import net.mcpes.foundhi.core.handle.marry.MarryApplyHandle
import net.mcpes.foundhi.core.handle.marry.MarryDivorceHandle
import net.mcpes.foundhi.core.handle.marry.MarryRejectHandle
import net.mcpes.foundhi.core.network.protocol.command.CommandPacket
import net.mcpes.foundhi.core.plugin.FoundHiPlugin
import net.mcpes.foundhi.core.plugin.data.PlayerCache

/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/5/1
 * version 1.0
 */
object MarryUtils {
    @JvmStatic
    fun marry(from: String, to: String): Int {
        val cache: PlayerCache = FoundHiPlugin.instance.getPlayer(from)!!
        if (cache.lover != "") return -4
        val pk = CommandPacket()
        pk.event = true
        val evid = HandleSystem.eventID++
        pk.eventID = "$evid${FoundHiPlugin.instance.id}"
        pk.sid = FoundHiPlugin.instance.id
        pk.cmd = "marry marry $from $to"
        FoundHiPlugin.instance.dataPacket(pk)
        FoundHiPlugin.instance.addHandle(evid, MarryApplyHandle(from, to))
        return 0
    }

    @JvmStatic
    fun reject(from: String, to: String) {
        val pk = CommandPacket()
        pk.event = true
        val evid = HandleSystem.eventID++
        pk.eventID = "$evid${FoundHiPlugin.instance.id}"
        pk.sid = FoundHiPlugin.instance.id
        pk.cmd = "marry reject $from $to"
        FoundHiPlugin.instance.dataPacket(pk)
        FoundHiPlugin.instance.addHandle(evid, MarryRejectHandle(from, to))
    }

    @JvmStatic
    fun accept(from: String, to: String) {
        val pk = CommandPacket()
        pk.event = true
        val evid = HandleSystem.eventID++
        pk.eventID = "$evid${FoundHiPlugin.instance.id}"
        pk.sid = FoundHiPlugin.instance.id
        pk.cmd = "marry accept $from $to"
        FoundHiPlugin.instance.dataPacket(pk)
        FoundHiPlugin.instance.addHandle(evid, MarryAcceptHandle(from, to))
    }

    @JvmStatic
    fun divorce(from: String) {
        val pk = CommandPacket()
        pk.event = true
        val evid = HandleSystem.eventID++
        pk.eventID = "$evid${FoundHiPlugin.instance.id}"
        pk.sid = FoundHiPlugin.instance.id
        pk.cmd = "marry divorce $from"
        FoundHiPlugin.instance.dataPacket(pk)
        FoundHiPlugin.instance.addHandle(evid, MarryDivorceHandle(from))
    }
}