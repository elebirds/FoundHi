package net.mcpes.foundhipro.core.network.protocol.packet

import com.google.gson.Gson
import net.mcpes.foundhipro.core.network.protocol.ProtocolInfo

abstract class DataPacket : Cloneable {
    abstract fun pid(): ProtocolInfo

    fun toJson(): String {
        return Gson().toJson(this)
    }

    public override fun clone(): DataPacket {
        return super.clone() as DataPacket
    }
}