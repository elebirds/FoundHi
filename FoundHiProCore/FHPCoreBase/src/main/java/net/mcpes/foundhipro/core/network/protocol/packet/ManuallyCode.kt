package net.mcpes.foundhipro.core.network.protocol.packet

/**
 * FoundHiPro
 *
 * @author hhm Copyright (c) 2018/9/2 下午7:28
 * version 1.0
 */
interface ManuallyCode {
    fun encode(): String

    fun decode(data: MutableMap<*, *>): DataPacket
}