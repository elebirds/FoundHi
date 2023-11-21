package net.mcpes.foundhi.core.network.protocol

/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/5/1
 * version 1.0
 */

interface ProtocolInfo {
    companion object {
        //Server
        const val SERVER_CONNECT_PACKET: Int = 0x01//服务器连接Packet
        const val SERVER_DISCONNECT_PACKET: Int = 0x02//服务器断开连接
        const val UPDATE_PLAYER_PERFORMANCE_DATA_PACKET: Int = 0x03//更新服务器人数
        const val BEST_HALL_PACKET: Int = 0x04//服务器获取最佳大厅并向最佳大厅发送PlayerTransferPacket 并转移玩家到最佳大厅
        const val SERVER_COMMAND_PACKET: Int = 0x05//服务器执行指令
        const val SERVER_BROADCAST_PACKET: Int = 0x06//广播消息
        const val SERVER_MESSAGE_PACKET: Int = 0x06//服务器后台输出info
        const val SEND_TOP_PACKET: Int = 0x07//排行榜
        const val SERVER_NAME_PACKET: Int = 0x08//服务器名字

        //Player
        const val PLAYER_JOIN_PACKET: Int = 0x0a//玩家进入
        const val PLAYER_QUIT_PACKET: Int = 0x0b//玩家退出
        const val PLAYER_TRANSFER_PACKET: Int = 0x0c//玩家转移
        const val PLAYER_SEND_MESSAGE_PACKET: Int = 0x0d//给玩家发送消息
        const val PLAYER_SEND_INFORMATION_PACKET: Int = 0x0e//发送玩家数据
        const val PLAYER_UPDATE_INFORMATION_PACKET: Int = 0x0f//跨服玩家数据
        const val PLAYER_SEND_BAG_PACKET: Int = 0x11//背包数据

        //Guild
        const val GUILD_SEND_PACKET: Int = 0x1a
        const val GUILD_UPDATE_INFORMATION_PACKET: Int = 0x1b//更新公会数据
        const val GUILD_REMOVE_PACKET: Int = 0x1c//删除公会信息

        //Command
        const val COMMAND_EXECUTE_PACKET: Int = 0x20//执行命令

        //Handle
        const val HANDLE_RUN_PACKET: Int = 0x22//执行操作

        //Chat
        const val ENABLE_UNITE_CHAT_PACKET: Int = 0x24
        const val PLAYER_CHAT_PACKET: Int = 0x25

        //Ban
        const val BANNED_PLAYER_ADD_PACKET: Int = 0x26
        const val BANNED_PLAYER_DEL_PACKET: Int = 0x27
        const val BANNED_PLAYER_UNBLOCK_PACKET: Int = 0x28

        //Stage
        const val STAGE_SERVER_ADD_STAGE_PACKET: Int = 0x29
        const val STAGE_CLIENT_STATE_PACKET: Int = 0x2a

        //Stage
        const val TEAM_INVITE_PACKET: Int = 0x30
    }
}
