package net.mcpes.foundhi.plugin.survival.land.utils

import cn.nukkit.Player
import cn.nukkit.Server
import cn.nukkit.block.Block
import cn.nukkit.level.Location
import cn.nukkit.level.generator.Generator
import cn.nukkit.math.Vector3
import cn.nukkit.scheduler.AsyncTask
import net.mcpes.foundhi.core.event.Event
import net.mcpes.foundhi.core.network.protocol.command.CommandPacket
import net.mcpes.foundhi.core.plugin.FoundHiPlugin
import net.mcpes.foundhi.plugin.survival.land.SLand
import net.mcpes.foundhi.plugin.survival.land.SLand.Companion.landWorldCorresponding
import net.mcpes.foundhi.plugin.survival.land.SLand.Companion.landWorlds
import net.mcpes.foundhi.plugin.survival.land.SLand.Companion.lands
import net.mcpes.foundhi.plugin.survival.land.SLand.Companion.pgLands
import net.mcpes.foundhi.plugin.survival.land.SLand.Companion.phLands
import net.mcpes.foundhi.plugin.survival.land.generate.ContractibleLand
import net.mcpes.foundhi.plugin.survival.land.generate.ExpandLand
import net.mcpes.foundhi.plugin.survival.land.handle.LandBuyHandle
import net.mcpes.foundhi.plugin.survival.land.handle.LandSellHandle
import net.mcpes.foundhi.plugin.survival.land.land.Land
import net.mcpes.foundhi.plugin.survival.land.land.LandWorld

/**
 * SLand
 *
 * @author hhm Copyright (c) 2018/5/2
 * version 1.0
 */
object LandUtils {
    fun isLandBlock(vector3: Vector3, type: Int): Boolean {
        val x = vector3.floorX
        val z = vector3.floorZ
        val f: Double
        val n: Double
        if (type == 1) {
            f = (z.toDouble() + 44) / 48
            n = (x.toDouble() + 44) / 48
        } else {
            f = (z.toDouble() + 60) / 64
            n = (x.toDouble() + 60) / 64
        }
        val q = n.toInt()
        val w = f.toInt()
        return n == q.toDouble() && w.toDouble() == f
    }

    fun getEmptyPos(pos: Location, type: Int): Vector3 {
        val z = pos.z.toInt()
        val x = pos.x.toInt()
        var o = 5
        while (true) {
            var x2 = x - o
            while (x2 <= x + o) {
                var z2 = z - o
                while (z2 <= z + o) {
                    if (isLandBlock(Vector3(x2.toDouble(), 10.0, z2.toDouble()), type)) {
                        if (!lands.containsKey("$x2-$z2-${pos.level.folderName}")) {
                            return Vector3((x2 + 2).toDouble(), 10.0, (z2 + 2).toDouble())
                        }
                    }
                    z2 += 1
                }
                x2 += 1
            }
            o += 5
        }
    }

    fun cleanLand(id: String, level: String) {
        val levels = Server.getInstance().getLevelByName(level)
        val landWorld = landWorlds[landWorldCorresponding[level]!!]!!
        val land: Land = landWorld.lands[id]!!
        val block: Block = if (landWorld.type == 1) {
            val args = ContractibleLand.generate.LAND_BLOCK.split(":")
            Block.get(args[0].toInt(), args[1].toInt())
        } else {
            val args = ExpandLand.generate.LAND_BLOCK.split(":")
            Block.get(args[0].toInt(), args[1].toInt())
        }
        for (xs in land.x1 + 1 until land.x2) {
            for (zx in land.z1 + 1 until land.z2) {
                for (ys in 0..99) {
                    if (ys in 2..9) {
                        levels.setBlock(Vector3(xs.toDouble(), ys.toDouble(), zx.toDouble()), block.clone())
                    } else {
                        levels.setBlockIdAt(xs, ys, zx, Block.AIR)
                    }
                }
            }
        }
        levels.saveChunks()
        levels.save()
    }

    fun isOwner(name: String, pos: Location): Boolean {
        if (!phLands.containsKey(name)) return false
        phLands[name]!!.forEach {
            if (it.level == pos.level.name) {
                if (pos.x > it.x1 && pos.x < it.x2 && pos.z > it.z1 && pos.z < it.z2) {
                    return true
                }
            }
        }
        return false
    }

    fun isGuest(name: String, pos: Location): Land? {
        if (!pgLands.containsKey(name)) return null
        pgLands[name]!!.forEach {
            if (it.level == pos.level.name) {
                if (pos.x > it.x1 && pos.x < it.x2 && pos.z > it.z1 && pos.z < it.z2) {
                    return it
                }
            }
        }
        return null
    }

    @Synchronized
    fun buy(player: Player, land: Land, landWorld: LandWorld) {
        if (land.buying) {
            player.sendMessage("§c请不要重复操作!")
            return
        }
        land.buying = true
        val pk = CommandPacket()
        val evid = Event.eventID++
        pk.cmd = "admin money del ${player.name} ${landWorld.moneyType} ${landWorld.money}"
        pk.eventID = "$evid${FoundHiPlugin.instance.id}"
        pk.event = true
        pk.sid = FoundHiPlugin.instance.id
        FoundHiPlugin.instance.addHandle(evid, LandBuyHandle(player, land, landWorld))
        FoundHiPlugin.instance.dataPacket(pk)
    }

    @Synchronized
    fun sell(player: Player, land: Land, landWorld: LandWorld) {
        if (land.selling) {
            player.sendMessage("§c请不要重复操作!")
            return
        }
        land.selling = true
        val pk = CommandPacket()
        val evid = Event.eventID++
        pk.cmd = "admin money add ${player.name} ${landWorld.moneyType} ${landWorld.money}"
        pk.eventID = "$evid${FoundHiPlugin.instance.id}"
        pk.event = true
        pk.sid = FoundHiPlugin.instance.id
        FoundHiPlugin.instance.addHandle(evid, LandSellHandle(player, land, landWorld))
        FoundHiPlugin.instance.dataPacket(pk)
        Server.getInstance().scheduler.scheduleAsyncTask(SLand.instance, object : AsyncTask() {
            override fun onRun() {
                Thread.sleep(5000)
                if (land.selling) land.selling = false
            }
        })
    }

    fun delete(land: Land, landWorld: LandWorld) {
        landWorld.lands.remove(land.id, land)
        this.delPhLand(land.owner, land)
        land.guests.forEach {
            this.delPgLand(it, land)
        }
        lands.remove("${land.id}-${landWorld.level}")
    }

    @Synchronized
    fun addPhLand(name: String, land: Land) {
        if (!phLands.containsKey(name)) {
            val lands = ArrayList<Land>()
            lands.add(land)
            phLands[name] = lands
        } else {
            phLands[name]!!.add(land)
        }
    }

    @Synchronized
    fun delPhLand(name: String, land: Land) {
        if (phLands.containsKey(name)) {
            phLands[name]!!.remove(land)
        }
    }

    @Synchronized
    fun addPgLand(name: String, land: Land) {
        if (!phLands.containsKey(name)) {
            val lands = ArrayList<Land>()
            lands.add(land)
            pgLands[name] = lands
        } else {
            pgLands[name]!!.add(land)
        }
    }

    @Synchronized
    fun delPgLand(name: String, land: Land) {
        if (phLands.containsKey(name)) {
            pgLands[name]!!.remove(land)
        }
    }

    fun createLandWorld(level: String, type: Int) {
        if (type == 1) {
            Server.getInstance().generateLevel(level, 23333, (ContractibleLand::class.java) as Class<out Generator>)
        } else {
            Server.getInstance().generateLevel(level, 23333, (ExpandLand::class.java) as Class<out Generator>)
        }
    }
}