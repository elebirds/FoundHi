package net.mcpes.foundhi.core.utils

import net.mcpes.foundhi.core.handle.HandleSystem
import net.mcpes.foundhi.core.handle.prefix.PrefixAddHandle
import net.mcpes.foundhi.core.handle.prefix.PrefixChooseHandle
import net.mcpes.foundhi.core.network.protocol.command.CommandPacket
import net.mcpes.foundhi.core.plugin.FoundHiPlugin

/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/7/28
 * version 1.0
 */
object PrefixUtils {
    @JvmStatic
    fun choose(id: String, chose: Int) {
        val cp = CommandPacket()
        val evid = HandleSystem.eventID++
        cp.eventID = evid.toString() + FoundHiPlugin.instance.id
        cp.event = true
        cp.sid = FoundHiPlugin.instance.id
        cp.cmd = "prefix choose $id $chose "
        FoundHiPlugin.instance.addHandle(evid, PrefixChooseHandle(id, chose))
        FoundHiPlugin.instance.dataPacket(cp)
    }

    @JvmStatic
    fun add(cause: String, target: String, prefix: String) {
        val cp = CommandPacket()
        val evid = HandleSystem.eventID++
        cp.eventID = evid.toString() + FoundHiPlugin.instance.id
        cp.event = true
        cp.sid = FoundHiPlugin.instance.id
        cp.cmd = "prefix add $cause $target $prefix "
        FoundHiPlugin.instance.addHandle(evid, PrefixAddHandle(cause, target, prefix))
        FoundHiPlugin.instance.dataPacket(cp)
    }
}