package net.mcpes.foundhi.plugin.currency.shop.listener


import cn.nukkit.event.EventHandler
import cn.nukkit.event.Listener
import cn.nukkit.event.player.PlayerInteractEvent
import net.mcpes.foundhi.plugin.currency.shop.utils.Utils

class PlayerListener : Listener {
    @EventHandler
    fun onTouch(event: PlayerInteractEvent) {
        val posString = Utils.locationToString(event.block.location)
        //if (existShopByPos(posString)) {
        //    val shopBase = getShopByPos(posString)
        //    shopBase.open(event.player)
        //}
    }
}
