package net.mcpes.foundhi.core.app.command.defaults

import net.mcpes.foundhi.core.app.APIFunction
import net.mcpes.foundhi.core.app.command.Command
import net.mcpes.foundhi.core.app.command.CommandSender
import net.mcpes.foundhi.core.network.protocol.server.ServerCommandPacket

/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/4/27
 * version 1.0
 */
class StopAllCommand(name: String) : Command(name, "关闭所有服务器") {
    override fun execute(sender: CommandSender, s: String, strings: Array<String>): Boolean {
        val pk = ServerCommandPacket()
        pk.command = "stop"
        for (server in APIFunction.instance.getServers()) {
            sender.sendMessage("§c正在关闭服务器:" + server.name + "!")
            server.dataPacket(pk)
            server.channel.close()
        }
        try {
            Thread.sleep(2000)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        APIFunction.instance.shutdown()
        return true
    }
}