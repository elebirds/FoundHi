package net.mcpes.foundhi.core.utils

import net.mcpes.foundhi.core.app.data.player.Player
import net.mcpes.foundhi.core.handle.HandleSystem
import net.mcpes.foundhi.core.handle.permission.PermissionSetHandle
import net.mcpes.foundhi.core.network.protocol.command.CommandPacket
import net.mcpes.foundhi.core.plugin.FoundHiPlugin

/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/4/14
 * version 1.0
 */
object PermissionUtils {
    @JvmStatic
    operator fun set(cause: String, target: String, permission: Player.Permission, send: Boolean) {
        val cp = CommandPacket()
        val evid = HandleSystem.eventID++
        cp.eventID = evid.toString() + FoundHiPlugin.instance.id
        cp.event = true
        cp.sid = FoundHiPlugin.instance.id
        cp.cmd = "admin permission " + target + " " + permission.level
        if (send) FoundHiPlugin.instance.addHandle(evid, PermissionSetHandle(cause, target, permission.level))
        FoundHiPlugin.instance.dataPacket(cp)
    }
}
