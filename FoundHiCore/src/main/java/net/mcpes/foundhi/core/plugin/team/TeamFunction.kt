package net.mcpes.foundhi.core.plugin.team

import net.mcpes.foundhi.core.app.data.team.Team
import net.mcpes.foundhi.core.plugin.FoundHiPlugin
import net.mcpes.foundhi.core.plugin.data.NukkitPlayer
import net.mcpes.foundhi.core.plugin.gui.window.lambda.defaults.LambdaVariableModalGUI
import java.util.function.BiConsumer
import java.util.function.Consumer

/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/8/14
 * version 1.0
 */
class TeamFunction {
    init {
        val invite = LambdaVariableModalGUI("team_function_invite", Team.TEAM_TITLE + " 入队邀请", "§6§l%0邀请你加入他的小队")
        invite.setTrueButtonText("§l§6加入")
        invite.setFalseButtonText("§l§c拒绝")
        invite.closedClickedListener = Consumer {
            //拒绝
            if (it !is NukkitPlayer) return@Consumer
            this.refuseInvite(it.name, it.guiParams[invite.id]!![0].toString())
        }
        invite.buttonClickedListener = BiConsumer { t, u ->
            if (u !is NukkitPlayer) return@BiConsumer
            if (t) {
                //同意
                this.acceptInvite(u.name, u.guiParams[invite.id]!![0].toString())
            } else {
                //拒绝
                this.refuseInvite(u.name, u.guiParams[invite.id]!![0].toString())
            }
        }
        FoundHiPlugin.instance.addCommonGUI("team_function_invite", invite)
    }

    fun acceptInvite(invitee: String, leader: String) {

    }

    fun refuseInvite(invitee: String, leader: String) {

    }
}