package net.mcpes.foundhi.plugin.currency.team

import net.mcpes.foundhi.core.plugin.modal.FoundHiPluginBase

/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/7/18
 * version 1.0
 */
class STeam : FoundHiPluginBase() {
    init {
        init(this)
    }

    override fun onEnable() {

    }

    companion object {
        lateinit var instance: STeam

        fun init(instance: STeam) {
            STeam.instance = instance
        }
    }
}