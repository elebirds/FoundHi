package net.mcpes.foundhi.core.network


import cn.nukkit.Server
import net.mcpes.foundhi.core.ServerType
import net.mcpes.foundhi.core.app.APIFunction
import net.mcpes.foundhi.core.network.plugin.listener.PluginListener
import net.mcpes.foundhi.core.network.protocol.DataPacket
import net.mcpes.foundhi.core.network.protocol.ProtocolInfo
import net.mcpes.foundhi.core.network.protocol.banned.AddPlayerBannedPacket
import net.mcpes.foundhi.core.network.protocol.banned.RemovePlayerBannedPacket
import net.mcpes.foundhi.core.network.protocol.banned.UnblockPlayerBannedPacket
import net.mcpes.foundhi.core.network.protocol.chat.EnableUniteChatPacket
import net.mcpes.foundhi.core.network.protocol.chat.PlayerChatPacket
import net.mcpes.foundhi.core.network.protocol.command.CommandPacket
import net.mcpes.foundhi.core.network.protocol.guild.RemoveGuildPacket
import net.mcpes.foundhi.core.network.protocol.guild.SendGuildPacket
import net.mcpes.foundhi.core.network.protocol.guild.UpdateGuildInformationPacket
import net.mcpes.foundhi.core.network.protocol.handle.RunHandlePacket
import net.mcpes.foundhi.core.network.protocol.player.*
import net.mcpes.foundhi.core.network.protocol.server.*
import net.mcpes.foundhi.core.network.server.listener.ServerListener
import net.mcpes.foundhi.core.plugin.FoundHiPlugin
import net.mcpes.foundhi.core.scheduler.AsyncTask

/**
 * @author hhm @ SGameCore Project
 */

class Network {
    var packetPool: Array<Class<out DataPacket>?> = arrayOfNulls(256)
        private set
    lateinit var serverListener: ServerListener
        private set
    lateinit var pluginListener: PluginListener
        private set

    init {
        try {
            instance = this
            this.registerPackets()
            if (ServerType.getType() == ServerType.API) {
                serverListener = ServerListener(this)
                APIFunction.instance.scheduler.scheduleAsyncTask(object : AsyncTask() {
                    override fun onRun() {
                        try {
                            serverListener.start()
                        } catch (e: Exception) {
                            APIFunction.instance.logger.critical("监听时遇到错误:")
                            e.printStackTrace()
                        }

                    }
                })
            } else {
                pluginListener = PluginListener(this)
                Server.getInstance().scheduler.scheduleAsyncTask(FoundHiPlugin.instance, object : cn.nukkit.scheduler.AsyncTask() {
                    override fun onRun() {
                        try {
                            pluginListener.start()
                        } catch (e: Exception) {
                            Server.getInstance().logger.critical("监听时遇到错误:")
                            e.printStackTrace()
                        }

                    }
                })
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun registerPackets() {
        this.packetPool[ProtocolInfo.SERVER_CONNECT_PACKET] = ServerConnectPacket::class.java
        this.packetPool[ProtocolInfo.SERVER_DISCONNECT_PACKET] = ServerDisconnectPacket::class.java
        this.packetPool[ProtocolInfo.UPDATE_PLAYER_PERFORMANCE_DATA_PACKET] = UpdateServerPerformanceData::class.java
        this.packetPool[ProtocolInfo.SERVER_COMMAND_PACKET] = ServerCommandPacket::class.java
        this.packetPool[ProtocolInfo.SERVER_BROADCAST_PACKET] = ServerBroadcastPacket::class.java
        this.packetPool[ProtocolInfo.BEST_HALL_PACKET] = BestHallPacket::class.java
        this.packetPool[ProtocolInfo.SEND_TOP_PACKET] = SendTopPacket::class.java
        this.packetPool[ProtocolInfo.PLAYER_JOIN_PACKET] = PlayerJoinPacket::class.java
        this.packetPool[ProtocolInfo.PLAYER_QUIT_PACKET] = PlayerQuitPacket::class.java
        this.packetPool[ProtocolInfo.PLAYER_SEND_INFORMATION_PACKET] = SendPlayerInformationPacket::class.java
        this.packetPool[ProtocolInfo.PLAYER_TRANSFER_PACKET] = PlayerTransferPacket::class.java
        this.packetPool[ProtocolInfo.PLAYER_SEND_MESSAGE_PACKET] = PlayerSendMessagePacket::class.java
        this.packetPool[ProtocolInfo.PLAYER_UPDATE_INFORMATION_PACKET] = UpdatePlayerInformationPacket::class.java
        this.packetPool[ProtocolInfo.GUILD_SEND_PACKET] = SendGuildPacket::class.java
        this.packetPool[ProtocolInfo.GUILD_UPDATE_INFORMATION_PACKET] = UpdateGuildInformationPacket::class.java
        this.packetPool[ProtocolInfo.GUILD_REMOVE_PACKET] = RemoveGuildPacket::class.java
        this.packetPool[ProtocolInfo.COMMAND_EXECUTE_PACKET] = CommandPacket::class.java
        this.packetPool[ProtocolInfo.HANDLE_RUN_PACKET] = RunHandlePacket::class.java
        this.packetPool[ProtocolInfo.PLAYER_SEND_BAG_PACKET] = SendBagPacket::class.java
        this.packetPool[ProtocolInfo.ENABLE_UNITE_CHAT_PACKET] = EnableUniteChatPacket::class.java
        this.packetPool[ProtocolInfo.PLAYER_CHAT_PACKET] = PlayerChatPacket::class.java
        this.packetPool[ProtocolInfo.BANNED_PLAYER_ADD_PACKET] = AddPlayerBannedPacket::class.java
        this.packetPool[ProtocolInfo.BANNED_PLAYER_DEL_PACKET] = RemovePlayerBannedPacket::class.java
        this.packetPool[ProtocolInfo.BANNED_PLAYER_UNBLOCK_PACKET] = UnblockPlayerBannedPacket::class.java
    }

    fun registerPacket(id: Int, pc: Class<out DataPacket>) {
        this.packetPool[id] = pc
    }

    fun getPacket(id: Int): DataPacket? {
        val clazz = this.packetPool[id and 0xff] ?: return null
        try {
            return clazz.newInstance()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    companion object {
        lateinit var instance: Network
            private set
    }
}
