package net.mcpes.foundhi.core.network.protocol

import net.sf.json.JSONObject

/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/6/26 下午9:25
 * version 1.0
 */

abstract class DataPacket {
    abstract fun pid(): Int

    abstract fun decode(json: JSONObject): DataPacket

    abstract fun encode(): String

    abstract fun handle()

    override fun toString(): String {
        return this.encode()
    }
}
