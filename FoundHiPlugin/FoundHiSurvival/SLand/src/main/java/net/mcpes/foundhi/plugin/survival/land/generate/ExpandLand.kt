package net.mcpes.foundhi.plugin.survival.land.generate

import cn.nukkit.level.ChunkManager
import cn.nukkit.level.format.generic.BaseFullChunk
import cn.nukkit.level.generator.Generator
import cn.nukkit.level.generator.`object`.ore.OreType
import cn.nukkit.level.generator.populator.impl.PopulatorOre
import cn.nukkit.level.generator.populator.type.Populator
import cn.nukkit.math.NukkitRandom
import cn.nukkit.math.Vector3
import java.util.*

/**
 * SLand
 *
 * @author hhm Copyright (c) 2018/5/2
 * version 1.0
 */
open class ExpandLand @JvmOverloads constructor(private val options: Map<String, Any> = HashMap()) : Generator() {
    private var level: ChunkManager? = null
    private var chunk: BaseFullChunk? = null
    private val chunk1: BaseFullChunk? = null
    private var chunk2: BaseFullChunk? = null
    private var chunk3: BaseFullChunk? = null
    private var chunk4: BaseFullChunk? = null
    private var chunk5: BaseFullChunk? = null
    private var chunk6: BaseFullChunk? = null
    private var chunk7: BaseFullChunk? = null
    private var chunk8: BaseFullChunk? = null
    private var chunk9: BaseFullChunk? = null
    private var chunk10: BaseFullChunk? = null
    private var chunk11: BaseFullChunk? = null
    private var chunk12: BaseFullChunk? = null
    private var chunk13: BaseFullChunk? = null
    private val populators = ArrayList<Populator>()

    companion object {
        lateinit var generate: LandGenerate
        fun init(BLOCK: HashMap<String, String>) {
            generate = LandGenerate()
            generate.init(BLOCK)
        }
    }

    init {
        this.chunk = null

        if (this.options.containsKey("decoration")) {
            val ores = PopulatorOre()
            ores.setOreTypes(arrayOf<OreType>())
            this.populators.add(ores)
        }
    }

    override fun getId(): Int {
        return 5
    }

    override fun getChunkManager(): ChunkManager? {
        return level
    }

    override fun getSettings(): Map<String, Any> {
        return this.options
    }

    override fun getName(): String {
        return "land"
    }

    override fun getSpawn(): Vector3 {
        return Vector3(0.0, 10.0, 0.0)
    }

    override fun init(level: ChunkManager, random: NukkitRandom) {
        this.level = level
    }

    override fun populateChunk(chunkX: Int, chunkZ: Int) {

    }

    override fun generateChunk(chunkX: Int, chunkZ: Int) {
        val cx = if (chunkX % 4 < 0) chunkX % 4 + 4 else chunkX % 4
        val cz = if (chunkZ % 4 < 0) chunkZ % 4 + 4 else chunkZ % 4
        val asd = cx.toString() + ":" + cz
        when (asd) {
            "0:0" -> {
                /**
                 * 16 # # # # @ & & & & & & & & & & &
                 * 15 # # # # @ & & & & & & & & & & &
                 * 14 # # # # @ & & & & & & & & & & &
                 * 13 # # # # @ & & & & & & & & & & &
                 * 12 # # # # @ & & & & & & & & & & &
                 * 11 # # # # @ & & & & & & & & & & &
                 * 10 # # # # @ & & & & & & & & & & &
                 *  9 # # # # @ & & & & & & & & & & &
                 *  8 # # # # @ & & & & & & & & & & &
                 *  7 # # # # @ & & & & & & & & & & &
                 *  6 # # # # @ S & & & & & & & & & &
                 *  5 # # # # @ @ @ @ @ @ @ @ @ @ @ @
                 *  4 # # # # # # # # # # # # # # # #
                 *  3 # # # # # # # # # # # # # # # #
                 *  2 # # # # # # # # # # # # # # # #
                 *  1 # # # # # # # # # # # # # # # #
                 *  0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 */
                val generateChunk = generate.generate(1, chunk1, this.level!!, chunkX, chunkZ)
                if (generateChunk != null) {
                    chunk = generateChunk.clone()
                    chunk!!.x = chunkX
                    chunk!!.z = chunkZ
                    this.level!!.setChunk(chunkX, chunkZ, chunk)
                }
            }
            "0:1" -> {
                /**
                 * 16 # # # # @ & & & & & & & & & & &
                 * 15 # # # # @ & & & & & & & & & & &
                 * 14 # # # # @ & & & & & & & & & & &
                 * 13 # # # # @ & & & & & & & & & & &
                 * 12 # # # # @ & & & & & & & & & & &
                 * 11 # # # # @ & & & & & & & & & & &
                 * 10 # # # # @ & & & & & & & & & & &
                 *  9 # # # # @ & & & & & & & & & & &
                 *  8 # # # # @ & & & & & & & & & & &
                 *  7 # # # # @ & & & & & & & & & & &
                 *  6 # # # # @ & & & & & & & & & & &
                 *  5 # # # # @ & & & & & & & & & & &
                 *  4 # # # # @ & & & & & & & & & & &
                 *  3 # # # # @ & & & & & & & & & & &
                 *  2 # # # # @ & & & & & & & & & & &
                 *  1 # # # # @ & & & & & & & & & & &
                 *  0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6
                 */
                val generateChunk = generate.generate(2, chunk2, this.level!!, chunkX, chunkZ)
                if (generateChunk != null) {
                    chunk = generateChunk.clone()
                    chunk!!.x = chunkX
                    chunk!!.z = chunkZ
                    this.level!!.setChunk(chunkX, chunkZ, chunk)
                }
            }
            "0:2" -> {
                /**
                 * 16 # # # # @ & & & & & & & & & & &
                 * 15 # # # # @ & & & & & & & & & & &
                 * 14 # # # # @ & & & & & & & & & & &
                 * 13 # # # # @ & & & & & & & & & & &
                 * 12 # # # # @ & & & & & & & & & & &
                 * 11 # # # # @ & & & & & & & & & & &
                 * 10 # # # # @ & & & & & & & & & & &
                 *  9 # # # # @ & & & & & & & & & & &
                 *  8 # # # # @ & & & & & & & & & & &
                 *  7 # # # # @ & & & & & & & & & & &
                 *  6 # # # # @ & & & & & & & & & & &
                 *  5 # # # # @ & & & & & & & & & & &
                 *  4 # # # # @ & & & & & & & & & & &
                 *  3 # # # # @ & & & & & & & & & & &
                 *  2 # # # # @ & & & & & & & & & & &
                 *  1 # # # # @ & & & & & & & & & & &
                 *  0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6
                 */
                val generateChunk = generate.generate(2, chunk13, this.level!!, chunkX, chunkZ)
                if (generateChunk != null) {
                    chunk = generateChunk.clone()
                    chunk!!.x = chunkX
                    chunk!!.z = chunkZ
                    this.level!!.setChunk(chunkX, chunkZ, chunk)
                }
            }
            "0:3" -> {
                /**
                 * 16 # # # # # # # # # # # # # # # #
                 * 15 # # # # # # # # # # # # # # # #
                 * 14 # # # # # # # # # # # # # # # #
                 * 13 # # # # # # # # # # # # # # # #
                 * 12 # # # # @ @ @ @ @ @ @ @ @ @ @ @
                 * 11 # # # # @ # # # # # # # # # # #
                 * 10 # # # # @ # # # # # # # # # # #
                 *  9 # # # # @ # # # # # # # # # # #
                 *  8 # # # # @ # # # # # # # # # # #
                 *  7 # # # # @ # # # # # # # # # # #
                 *  6 # # # # @ # # # # # # # # # # #
                 *  5 # # # # @ # # # # # # # # # # #
                 *  4 # # # # @ # # # # # # # # # # #
                 *  3 # # # # @ # # # # # # # # # # #
                 *  2 # # # # @ # # # # # # # # # # #
                 *  1 # # # # @ # # # # # # # # # # #
                 *  0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6
                 */
                val generateChunk = generate.generate(3, chunk3, this.level!!, chunkX, chunkZ)
                if (generateChunk != null) {
                    chunk = generateChunk.clone()
                    chunk!!.x = chunkX
                    chunk!!.z = chunkZ
                    this.level!!.setChunk(chunkX, chunkZ, chunk)
                }
            }
            "1:0" -> {
                /**
                 * 16 & & & & & & & & & & & & & & & &
                 * 15 & & & & & & & & & & & & & & & &
                 * 14 & & & & & & & & & & & & & & & &
                 * 13 & & & & & & & & & & & & & & & &
                 * 12 & & & & & & & & & & & & & & & &
                 * 11 & & & & & & & & & & & & & & & &
                 * 10 & & & & & & & & & & & & & & & &
                 *  9 & & & & & & & & & & & & & & & &
                 *  8 & & & & & & & & & & & & & & & &
                 *  7 & & & & & & & & & & & & & & & &
                 *  6 & & & & & & & & & & & & & & & &
                 *  5 @ @ @ @ @ @ @ @ @ @ @ @ @ @ @ @
                 *  4 # # # # # # # # # # # # # # # #
                 *  3 # # # # # # # # # # # # # # # #
                 *  2 # # # # # # # # # # # # # # # #
                 *  1 # # # # # # # # # # # # # # # #
                 *  0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6
                 */
                val generateChunk = generate.generate(4, chunk4, this.level!!, chunkX, chunkZ)
                if (generateChunk != null) {
                    chunk = generateChunk.clone()
                    chunk!!.x = chunkX
                    chunk!!.z = chunkZ
                    this.level!!.setChunk(chunkX, chunkZ, chunk)
                }
            }
            "2:0" -> {
                /**
                 * 16 & & & & & & & & & & & & & & & &
                 * 15 & & & & & & & & & & & & & & & &
                 * 14 & & & & & & & & & & & & & & & &
                 * 13 & & & & & & & & & & & & & & & &
                 * 12 & & & & & & & & & & & & & & & &
                 * 11 & & & & & & & & & & & & & & & &
                 * 10 & & & & & & & & & & & & & & & &
                 *  9 & & & & & & & & & & & & & & & &
                 *  8 & & & & & & & & & & & & & & & &
                 *  7 & & & & & & & & & & & & & & & &
                 *  6 & & & & & & & & & & & & & & & &
                 *  5 @ @ @ @ @ @ @ @ @ @ @ @ @ @ @ @
                 *  4 # # # # # # # # # # # # # # # #
                 *  3 # # # # # # # # # # # # # # # #
                 *  2 # # # # # # # # # # # # # # # #
                 *  1 # # # # # # # # # # # # # # # #
                 *  0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6
                 */
                val generateChunk = generate.generate(4, chunk12, this.level!!, chunkX, chunkZ)
                if (generateChunk != null) {
                    chunk = generateChunk.clone()
                    chunk!!.x = chunkX
                    chunk!!.z = chunkZ
                    this.level!!.setChunk(chunkX, chunkZ, chunk)
                }
            }
            "3:0" -> {
                /**
                 * 16 & & & & & & & & & & & @ # # # #
                 * 15 & & & & & & & & & & & @ # # # #
                 * 14 & & & & & & & & & & & @ # # # #
                 * 13 & & & & & & & & & & & @ # # # #
                 * 12 & & & & & & & & & & & @ # # # #
                 * 11 & & & & & & & & & & & @ # # # #
                 * 10 & & & & & & & & & & & @ # # # #
                 *  9 & & & & & & & & & & & @ # # # #
                 *  8 & & & & & & & & & & & @ # # # #
                 *  7 & & & & & & & & & & & @ # # # #
                 *  6 & & & & & & & & & & & @ # # # #
                 *  5 @ @ @ @ @ @ @ @ @ @ @ @ # # # #
                 *  4 # # # # # # # # # # # # # # # #
                 *  3 # # # # # # # # # # # # # # # #
                 *  2 # # # # # # # # # # # # # # # #
                 *  1 # # # # # # # # # # # # # # # #
                 *  0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6
                 */
                val generateChunk = generate.generate(5, chunk5, this.level!!, chunkX, chunkZ)
                if (generateChunk != null) {
                    chunk = generateChunk.clone()
                    chunk!!.x = chunkX
                    chunk!!.z = chunkZ
                    this.level!!.setChunk(chunkX, chunkZ, chunk)
                }
            }
            "3:1" -> {
                /**
                 * 16 & & & & & & & & & & & @ # # # #
                 * 15 & & & & & & & & & & & @ # # # #
                 * 14 & & & & & & & & & & & @ # # # #
                 * 13 & & & & & & & & & & & @ # # # #
                 * 12 & & & & & & & & & & & @ # # # #
                 * 11 & & & & & & & & & & & @ # # # #
                 * 10 & & & & & & & & & & & @ # # # #
                 *  9 & & & & & & & & & & & @ # # # #
                 *  8 & & & & & & & & & & & @ # # # #
                 *  7 & & & & & & & & & & & @ # # # #
                 *  6 & & & & & & & & & & & @ # # # #
                 *  5 & & & & & & & & & & & @ # # # #
                 *  4 & & & & & & & & & & & @ # # # #
                 *  3 & & & & & & & & & & & @ # # # #
                 *  2 & & & & & & & & & & & @ # # # #
                 *  1 & & & & & & & & & & & @ # # # #
                 *  0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6
                 */
                val generateChunk = generate.generate(6, chunk6, this.level!!, chunkX, chunkZ)
                if (generateChunk != null) {
                    chunk = generateChunk.clone()
                    chunk!!.x = chunkX
                    chunk!!.z = chunkZ
                    this.level!!.setChunk(chunkX, chunkZ, chunk)
                }
            }
            "3:2" -> {
                /**
                 * 16 & & & & & & & & & & & @ # # # #
                 * 15 & & & & & & & & & & & @ # # # #
                 * 14 & & & & & & & & & & & @ # # # #
                 * 13 & & & & & & & & & & & @ # # # #
                 * 12 & & & & & & & & & & & @ # # # #
                 * 11 & & & & & & & & & & & @ # # # #
                 * 10 & & & & & & & & & & & @ # # # #
                 *  9 & & & & & & & & & & & @ # # # #
                 *  8 & & & & & & & & & & & @ # # # #
                 *  7 & & & & & & & & & & & @ # # # #
                 *  6 & & & & & & & & & & & @ # # # #
                 *  5 & & & & & & & & & & & @ # # # #
                 *  4 & & & & & & & & & & & @ # # # #
                 *  3 & & & & & & & & & & & @ # # # #
                 *  2 & & & & & & & & & & & @ # # # #
                 *  1 & & & & & & & & & & & @ # # # #
                 *  0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6
                 */
                val generateChunk = generate.generate(6, chunk11, this.level!!, chunkX, chunkZ)
                if (generateChunk != null) {
                    chunk = generateChunk.clone()
                    chunk!!.x = chunkX
                    chunk!!.z = chunkZ
                    this.level!!.setChunk(chunkX, chunkZ, chunk)
                }
            }
            "3:3" -> {
                /**
                 * 16 # # # # # # # # # # # # # # # #
                 * 15 # # # # # # # # # # # # # # # #
                 * 14 # # # # # # # # # # # # # # # #
                 * 13 # # # # # # # # # # # # # # # #
                 * 12 @ @ @ @ @ @ @ @ @ @ @ @ # # # #
                 * 11 & & & & & & & & & & & @ # # # #
                 * 10 & & & & & & & & & & & @ # # # #
                 *  9 & & & & & & & & & & & @ # # # #
                 *  8 & & & & & & & & & & & @ # # # #
                 *  7 & & & & & & & & & & & @ # # # #
                 *  6 & & & & & & & & & & & @ # # # #
                 *  5 & & & & & & & & & & & @ # # # #
                 *  4 & & & & & & & & & & & @ # # # #
                 *  3 & & & & & & & & & & & @ # # # #
                 *  2 & & & & & & & & & & & @ # # # #
                 *  1 & & & & & & & & & & & @ # # # #
                 *  0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6
                 */
                val generateChunk = generate.generate(7, chunk7, this.level!!, chunkX, chunkZ)
                if (generateChunk != null) {
                    chunk = generateChunk.clone()
                    chunk!!.x = chunkX
                    chunk!!.z = chunkZ
                    this.level!!.setChunk(chunkX, chunkZ, chunk)
                }
            }
            "1:3" -> {
                /**
                 * 16 # # # # # # # # # # # # # # # #
                 * 15 # # # # # # # # # # # # # # # #
                 * 14 # # # # # # # # # # # # # # # #
                 * 13 # # # # # # # # # # # # # # # #
                 * 12 @ @ @ @ @ @ @ @ @ @ @ @ @ @ @ @
                 * 11 & & & & & & & & & & & & & & & &
                 * 10 & & & & & & & & & & & & & & & &
                 *  9 & & & & & & & & & & & & & & & &
                 *  8 & & & & & & & & & & & & & & & &
                 *  7 & & & & & & & & & & & & & & & &
                 *  6 & & & & & & & & & & & & & & & &
                 *  5 & & & & & & & & & & & & & & & &
                 *  4 & & & & & & & & & & & & & & & &
                 *  3 & & & & & & & & & & & & & & & &
                 *  2 & & & & & & & & & & & & & & & &
                 *  1 & & & & & & & & & & & & & & & &
                 *  0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6
                 */
                val generateChunk = generate.generate(8, chunk8, this.level!!, chunkX, chunkZ)
                if (generateChunk != null) {
                    chunk = generateChunk.clone()
                    chunk = generateChunk.clone()
                    chunk!!.x = chunkX
                    chunk!!.z = chunkZ
                    this.level!!.setChunk(chunkX, chunkZ, chunk)
                }
            }
            "2:3" -> {
                /**
                 * 16 # # # # # # # # # # # # # # # #
                 * 15 # # # # # # # # # # # # # # # #
                 * 14 # # # # # # # # # # # # # # # #
                 * 13 # # # # # # # # # # # # # # # #
                 * 12 @ @ @ @ @ @ @ @ @ @ @ @ @ @ @ @
                 * 11 & & & & & & & & & & & & & & & &
                 * 10 & & & & & & & & & & & & & & & &
                 *  9 & & & & & & & & & & & & & & & &
                 *  8 & & & & & & & & & & & & & & & &
                 *  7 & & & & & & & & & & & & & & & &
                 *  6 & & & & & & & & & & & & & & & &
                 *  5 & & & & & & & & & & & & & & & &
                 *  4 & & & & & & & & & & & & & & & &
                 *  3 & & & & & & & & & & & & & & & &
                 *  2 & & & & & & & & & & & & & & & &
                 *  1 & & & & & & & & & & & & & & & &
                 *  0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6
                 */
                val generateChunk = generate.generate(8, chunk10, this.level!!, chunkX, chunkZ)
                if (generateChunk != null) {
                    chunk = generateChunk.clone()
                    chunk!!.x = chunkX
                    chunk!!.z = chunkZ
                    this.level!!.setChunk(chunkX, chunkZ, chunk)
                }
            }
            else -> {
                /**
                 * 16 & & & & & & & & & & & & & & & &
                 * 15 & & & & & & & & & & & & & & & &
                 * 14 & & & & & & & & & & & & & & & &
                 * 13 & & & & & & & & & & & & & & & &
                 * 12 & & & & & & & & & & & & & & & &
                 * 11 & & & & & & & & & & & & & & & &
                 * 10 & & & & & & & & & & & & & & & &
                 *  9 & & & & & & & & & & & & & & & &
                 *  8 & & & & & & & & & & & & & & & &
                 *  7 & & & & & & & & & & & & & & & &
                 *  6 & & & & & & & & & & & & & & & &
                 *  5 & & & & & & & & & & & & & & & &
                 *  4 & & & & & & & & & & & & & & & &
                 *  3 & & & & & & & & & & & & & & & &
                 *  2 & & & & & & & & & & & & & & & &
                 *  1 & & & & & & & & & & & & & & & &
                 *  0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6
                 */
                val generateChunk = generate.generate(0, chunk9, this.level!!, chunkX, chunkZ)
                if (generateChunk != null) {
                    chunk = generateChunk.clone()
                    chunk!!.x = chunkX
                    chunk!!.z = chunkZ
                    this.level!!.setChunk(chunkX, chunkZ, chunk)
                }
            }
        }
    }
}