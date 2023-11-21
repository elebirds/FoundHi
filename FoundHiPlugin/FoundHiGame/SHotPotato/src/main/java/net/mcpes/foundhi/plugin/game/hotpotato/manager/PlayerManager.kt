package net.mcpes.foundhi.plugin.game.hotpotato.manager

import cn.nukkit.Player
import cn.nukkit.Server
import cn.nukkit.item.Item
import cn.nukkit.item.ItemHelmetLeather
import cn.nukkit.item.ItemPotato
import cn.nukkit.network.protocol.MobArmorEquipmentPacket

class PlayerManager(private val player: Player, private val roomManager: RoomManager) {
    val name = player.name
    val scale = player.scale
    val items = player.inventory.contents
    val equipments = player.inventory.armorContents
    val effects = player.effects

    fun getRoomName(): String {
        return roomManager.roomName
    }

    fun setPotato() {
        if (roomManager.hasPotatoPlayer()) {
            roomManager.potatoPlayer!!.inventory.remove(Item.get(393))
            roomManager.getPlayer(roomManager.potatoPlayer!!.name).setHelmet(Item.get(0))
        }
        val helmet: ItemHelmetLeather = Item.get(298) as ItemHelmetLeather
        helmet.setColor(0, 255, 0)
        val item: Item = player.inventory.itemInHand
        if (item !is ItemPotato) {
            player.inventory.addItem(item)
            player.inventory.itemInHand = Item.get(393)
        }
        roomManager.potatoPlayer = player
        this.setHelmet(helmet)
    }

    fun returnAll() {
        this.effects.forEach {
            player.addEffect(it.value)
        }
        player.setScale(this.scale)
        player.inventory.contents = this.items
        player.inventory.armorContents = this.equipments
    }

    fun isAlive(): Boolean {
        return roomManager.isAlive(name)
    }

    fun isPotatoPlayer(): Boolean {
        return roomManager.potatoPlayer != null && roomManager.potatoPlayer!!.name == player.name
    }

    fun setHelmet(item: Item) {
        val items: Array<Item> = arrayOf(item, Item.get(0), Item.get(0), Item.get(0))
        val pk = MobArmorEquipmentPacket()
        pk.eid = player.id
        pk.slots = items
        pk.encode()
        for (playerName: String in roomManager.players.keys) {
            val player: Player = Server.getInstance().getPlayer(playerName)
            player.dataPacket(pk)
        }
    }
}