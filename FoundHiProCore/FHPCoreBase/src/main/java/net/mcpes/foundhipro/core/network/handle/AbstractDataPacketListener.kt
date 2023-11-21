package net.mcpes.foundhipro.core.network.handle

import io.netty.channel.Channel
import net.mcpes.foundhipro.core.network.protocol.packet.DataPacket
import java.util.function.BiConsumer


/**
 * FoundHiPro
 *
 * @author WetABQ Copyright (c) 2018.09
 * @version 1.0
 */
open class AbstractDataPacketListener<in T: DataPacket>(val packetClass: Class<out DataPacket>) : DataPacketListener<T>{

    private var receiveListener: BiConsumer<Channel,DataPacket> = BiConsumer { _,_ ->  }
    private var sendListener: BiConsumer<Channel,DataPacket> = BiConsumer { _,_ ->  }

    override fun receivePacket(channel: Channel, dataPacket: T) {
        receiveListener.accept(channel,dataPacket)
    }

    override fun sendPacket(channel: Channel, dataPacket: T) {
        sendListener.accept(channel,dataPacket)
    }

    fun onReceive(listener: BiConsumer<Channel,DataPacket>): AbstractDataPacketListener<T> {
        this.receiveListener = listener
        return this
    }

    fun onSend(listener: BiConsumer<Channel,DataPacket>): AbstractDataPacketListener<T> {
        this.sendListener = listener
        return this
    }




}