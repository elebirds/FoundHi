package net.mcpes.foundhipro.core

/**
 * FoundHiPro
 *
 * @author hhm Copyright (c) 2018/9/5 下午1:28
 * version 1.0
 */
object ServerType {
    lateinit var type: ServerType

    fun init(type: ServerType) {
        this.type = type
    }

    enum class ServerType(val id: Int) {
        DETECT(-1), API(0), HUB(2)
    }
}