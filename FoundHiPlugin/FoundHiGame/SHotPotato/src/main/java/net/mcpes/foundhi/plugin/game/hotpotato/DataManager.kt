package net.mcpes.foundhi.plugin.game.hotpotato

import net.mcpes.foundhi.plugin.game.hotpotato.manager.RoomManager

object DataManager {
    var roomDatas = HashMap<String, RoomManager>()
        private set
    var gamePlayers = HashMap<String, String>()
        private set

    fun addRoom(roomName: String) {
        roomDatas[roomName] = RoomManager(roomName)
    }

    fun removeRoom(roomName: String) {
        roomDatas.remove(roomName)
    }

    fun containRoom(roomName: String): Boolean = roomDatas.containsKey(roomName)

    fun addGamePlayer(playerName: String, roomName: String) {
        gamePlayers[playerName] = roomName
    }

    fun removeGamePlayer(playerName: String) {
        gamePlayers.remove(playerName)
    }

    fun isInGame(playerName: String): Boolean = gamePlayers.containsKey(playerName)

    fun getPlayerRoomManager(playerName: String): RoomManager? {
        return roomDatas[gamePlayers[playerName]]
    }
}