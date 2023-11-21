package net.mcpes.foundhi.core.team

import net.mcpes.foundhi.core.app.APIFunction
import net.mcpes.foundhi.core.app.data.team.Team
import net.mcpes.foundhi.core.handle.HandleResults

/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/7/18
 * version 1.0
 */
class TeamSystem {
    init {
        instance = this
    }

    private var teamID: Int = 0
    private val teams: HashMap<Int, Team> = HashMap()
    private val abandonedTeamIDs: ArrayList<Int> = arrayListOf()

    fun addTeam(leader: String): Int {
        val player = APIFunction.instance.getPlayer(leader)!!
        if (player.inTeam != -1) return HandleResults.HAS_JOINED_TEAM
        val team = Team(this.getTeamID(), leader)
        player.inTeam = team.id
        teams[team.id] = team
        return team.id
    }

    fun getTeam(id: Int) = this.teams[id]

    fun delTeam(id: Int) {
        this.teams.remove(id)
        this.abandonedTeamIDs.add(id)
    }

    private fun getTeamID(): Int {
        if (abandonedTeamIDs.isEmpty()) {
            return this.teamID++
        }
        synchronized(abandonedTeamIDs) {
            return abandonedTeamIDs.removeAt(0)
        }
    }

    companion object {
        lateinit var instance: TeamSystem
    }
}