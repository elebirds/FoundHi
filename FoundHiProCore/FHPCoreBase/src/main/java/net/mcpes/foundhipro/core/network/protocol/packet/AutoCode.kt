package net.mcpes.foundhipro.core.network.protocol.packet

import com.google.gson.Gson

/**
 * FoundHiPro
 *
 * @author hhm Copyright (c) 2018/9/2 下午7:26
 * version 1.0
 */
interface AutoCode {
    fun encode() = Gson().toJson(this)!!
}