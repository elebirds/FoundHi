package net.mcpes.foundhi.core.app.data.team

import net.mcpes.foundhi.core.app.APIFunction

/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/7/18
 * version 1.0
 */
class Team(val id: Int, val leader: String) {
    val members: ArrayList<String> = ArrayList()
    val apply: HashMap<String, Int> = HashMap()

    fun dissolve(reason: String) {
        members.add(leader)
        this.members.forEach {
            val p = APIFunction.instance.getPlayer(it)
            if (p != null) {
                p.inTeam = -1
                p.sendMessage(TEAM_TITLE + "你的小队(id:$id)已经解散,原因:$reason!")
            }
        }
        APIFunction.instance.teamSystem.delTeam(this.id)
    }

    companion object {
        const val TEAM_TITLE = "§l§e[§6小§c队§e]"
    }
}