package net.mcpes.foundhi.plugin.game.has.data

import cn.nukkit.Server
import cn.nukkit.level.Level
import cn.nukkit.level.Location
import net.mcpes.foundhi.plugin.game.has.utils.Utils

class RoomData(val id: Int) {
    var min: Int = 4//最小游戏人数
    var max: Int = 20//最大游戏人数
    var disName: String = ""//房间显示名称
    var waitTime: Int = 30//游戏等待时间
    var gameTime: Int = 600//最大游戏时间
    var startX: Double = 0.0 //游戏区域开始X
    var startZ: Double = 0.0 //游戏区域开始Z
    var endX: Double = 0.0 //游戏区域结束X
    var endZ: Double = 0.0 //游戏区域结束Z
    var animalPlayerProportion: Double = 3.0 // 猎人1 : 动物玩家x
    var animalRobotProportion: Double = 3.0 // 动物玩家1 : 动物机器人x
    var stayTime: Int = 20
    var waitPos: String = ""//等待游戏地点-String
    private var waitLocation: Location? = null//等待游戏地点-Location
    var stopPos: String = ""//结束游戏地点-String
    private var stopLocation: Location? = null//结束游戏地点-Location
    var gameWorld: String = ""//游戏世界名称
    private var gameLevel: Level? = null//游戏世界
    var hunterStayPos: String = ""//猎人等待地点-String
    private var hunterStayLocation: Location? = null//猎人等待地点-Location
    var hunterPos: String = ""//猎人重生地点-String
    private var hunterLocation: Location? = null//猎人重生地点-Location
    var animalType: Int = -1 //动物类型,-1为随机
    var animalPos: ArrayList<String> = ArrayList()
    private var animalLocation: ArrayList<Location> = ArrayList()

    fun setWaitLocation() {
        this.waitLocation = Utils.stringToLocation(this.waitPos)
    }

    fun getWaitLocation(): Location {
        return waitLocation!!
    }

    fun setStopLocation() {
        this.stopLocation = Utils.stringToLocation(this.stopPos)
    }

    fun getStopLocation(): Location {
        return stopLocation!!
    }

    fun setGameLevel() {
        this.gameLevel = Server.getInstance().getLevelByName(this.gameWorld)
    }

    fun getGameLevel(): Level {
        return gameLevel!!
    }

    fun setHunterLocation() {
        this.hunterLocation = Utils.stringToLocation(this.hunterPos)
    }

    fun getHunterLocation(): Location {
        return hunterLocation!!
    }

    fun setHunterStayLocation() {
        this.hunterStayLocation = Utils.stringToLocation(this.hunterStayPos)
    }

    fun getHunterStayLocation(): Location {
        return hunterStayLocation!!
    }

    fun setAnimalLocation() {
        for (pos: String in animalPos) {
            this.animalLocation.add(Utils.stringToLocation(pos)!!)
        }
    }

    fun getAnimalLocation(): ArrayList<Location> {
        return animalLocation
    }

    override fun equals(other: Any?): Boolean {
        return this.id == (other!! as RoomData).id
    }

    override fun hashCode(): Int {
        return id
    }
}