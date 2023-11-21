package net.mcpes.foundhi.plugin.survival.land.generate

import cn.nukkit.block.Block
import cn.nukkit.level.ChunkManager
import cn.nukkit.level.format.generic.BaseFullChunk
import java.util.*

/**
 * SLand
 *
 * @author hhm Copyright (c) 2018/5/2
 * version 1.0
 */
class LandGenerate {
    private lateinit var AISLE_BLOCK: String
    lateinit var FRAME_TOP_BLOCK: String
    lateinit var FRAME_BLOCK: String
    lateinit var LAND_SHOP_BLOCK: String
    lateinit var LAND_BLOCK: String

    fun init(BLOCK: HashMap<String, String>) {
        AISLE_BLOCK = if (BLOCK.containsKey("过道方块") && BLOCK["过道方块"] != null) BLOCK["过道方块"]!! else Block.WOOD.toString() + ""
        FRAME_TOP_BLOCK = if (BLOCK.containsKey("边框顶层方块") && BLOCK["边框顶层方块"] != null) BLOCK["边框顶层方块"]!! else Block.SLAB.toString() + ""
        FRAME_BLOCK = if (BLOCK.containsKey("边框方块") && BLOCK["边框方块"] != null) BLOCK["边框方块"]!! else Block.DOUBLE_SLAB.toString() + ""
        LAND_SHOP_BLOCK = if (BLOCK.containsKey("领地石方块") && BLOCK["领地石方块"] != null) BLOCK["领地石方块"]!! else Block.NETHER_REACTOR.toString() + ""
        LAND_BLOCK = if (BLOCK.containsKey("地皮方块") && BLOCK["地皮方块"] != null) BLOCK["地皮方块"]!! else Block.GRASS.toString() + ""
    }

    fun generate(type: Int, sourceChunk: BaseFullChunk?, level: ChunkManager, chunkX: Int, chunkZ: Int): BaseFullChunk? {
        if (sourceChunk == null) {
            val chunk = level.getChunk(chunkX, chunkZ).clone()
            when (type) {
                1 -> {
                    var x = 0
                    var z: Int
                    var y: Int
                    while (x < 16) {
                        z = 0
                        while (z < 16) {
                            y = 0
                            while (y < 10) {
                                if (y == 0) {
                                    chunk.setBlockId(x, y, z, Block.BEDROCK)
                                    chunk.setBlockData(x, y, x, 0)
                                } else {
                                    if (x < 4 || z < 4) {
                                        chunk.setBlockId(x, y, z, getBlockInfo(AISLE_BLOCK, true))//羊毛
                                        chunk.setBlockData(x, y, x, getBlockInfo(AISLE_BLOCK, false))
                                    } else if (x > 4 && z > 4) {
                                        chunk.setBlockId(x, y, z, getBlockInfo(LAND_BLOCK, true))
                                        chunk.setBlockData(x, y, x, getBlockInfo(LAND_BLOCK, false))
                                        chunk.setBlockId(4, 11, 4, getBlockInfo(LAND_SHOP_BLOCK, true))
                                        chunk.setBlockData(4, 11, 4, getBlockInfo(LAND_SHOP_BLOCK, false))
                                    } else {
                                        chunk.setBlockId(x, y, z, getBlockInfo(FRAME_BLOCK, true))
                                        chunk.setBlockData(x, y, z, getBlockInfo(FRAME_BLOCK, false))
                                        if (y == 9) {
                                            chunk.setBlockId(x, y + 1, z, getBlockInfo(FRAME_TOP_BLOCK, true))
                                            chunk.setBlockData(x, y + 1, z, getBlockInfo(FRAME_TOP_BLOCK, false))
                                        }
                                    }
                                }
                                y++
                            }
                            z++
                        }
                        x++
                    }
                }
                2 -> {
                    var x = 0
                    var z: Int
                    var y: Int
                    while (x < 16) {
                        z = 0
                        while (z < 16) {
                            y = 0
                            while (y < 10) {
                                if (y == 0) {
                                    chunk.setBlockId(x, y, z, Block.BEDROCK)
                                    chunk.setBlockData(x, y, x, 0)
                                } else {
                                    if (x < 4) {
                                        chunk.setBlockId(x, y, z, getBlockInfo(AISLE_BLOCK, true))
                                        chunk.setBlockData(x, y, z, getBlockInfo(AISLE_BLOCK, false))
                                    } else if (x > 4) {
                                        chunk.setBlockId(x, y, z, getBlockInfo(LAND_BLOCK, true))
                                        chunk.setBlockData(x, y, x, getBlockInfo(LAND_BLOCK, false))
                                    } else {
                                        chunk.setBlockId(x, y, z, getBlockInfo(FRAME_BLOCK, true))
                                        chunk.setBlockData(x, y, z, getBlockInfo(FRAME_BLOCK, false))
                                        if (y == 9) {
                                            chunk.setBlockId(x, y + 1, z, getBlockInfo(FRAME_TOP_BLOCK, true))
                                            chunk.setBlockData(x, y + 1, z, getBlockInfo(FRAME_TOP_BLOCK, false))
                                        }
                                    }
                                }
                                y++
                            }
                            z++
                        }
                        x++
                    }
                }
                3 -> {
                    var x = 0
                    var z: Int
                    var y: Int
                    while (x < 16) {
                        z = 0
                        while (z < 16) {
                            y = 0
                            while (y < 10) {
                                if (y == 0) {
                                    chunk.setBlockId(x, y, z, Block.BEDROCK)
                                    chunk.setBlockData(x, y, x, 0)
                                } else {
                                    if (x < 4 || z > 11) {
                                        chunk.setBlockId(x, y, z, getBlockInfo(AISLE_BLOCK, true))
                                        chunk.setBlockData(x, y, z, getBlockInfo(AISLE_BLOCK, false))
                                    } else if (x > 4 && z < 11) {
                                        chunk.setBlockId(x, y, z, getBlockInfo(LAND_BLOCK, true))
                                        chunk.setBlockData(x, y, x, getBlockInfo(LAND_BLOCK, false))
                                    } else {
                                        chunk.setBlockId(x, y, z, getBlockInfo(FRAME_BLOCK, true))
                                        chunk.setBlockData(x, y, z, getBlockInfo(FRAME_BLOCK, false))
                                        if (y == 9) {
                                            chunk.setBlockId(x, y + 1, z, getBlockInfo(FRAME_TOP_BLOCK, true))
                                            chunk.setBlockData(x, y + 1, z, getBlockInfo(FRAME_TOP_BLOCK, false))
                                        }
                                    }
                                }
                                y++
                            }
                            z++
                        }
                        x++
                    }
                }
                4 -> {
                    var x = 0
                    var z: Int
                    var y: Int
                    while (x < 16) {
                        z = 0
                        while (z < 16) {
                            y = 0
                            while (y < 10) {
                                if (y == 0) {
                                    chunk.setBlockId(x, y, z, Block.BEDROCK)
                                    chunk.setBlockData(x, y, x, 0)
                                } else {
                                    if (z < 4) {
                                        chunk.setBlockId(x, y, z, getBlockInfo(AISLE_BLOCK, true))
                                        chunk.setBlockData(x, y, z, getBlockInfo(AISLE_BLOCK, false))
                                    } else if (z > 4) {
                                        chunk.setBlockId(x, y, z, getBlockInfo(LAND_BLOCK, true))
                                        chunk.setBlockData(x, y, x, getBlockInfo(LAND_BLOCK, false))
                                    } else {
                                        chunk.setBlockId(x, y, z, getBlockInfo(FRAME_BLOCK, true))
                                        chunk.setBlockData(x, y, z, getBlockInfo(FRAME_BLOCK, false))
                                        if (y == 9) {
                                            chunk.setBlockId(x, y + 1, z, getBlockInfo(FRAME_TOP_BLOCK, true))
                                            chunk.setBlockData(x, y + 1, z, getBlockInfo(FRAME_TOP_BLOCK, false))
                                        }
                                    }
                                }
                                y++
                            }
                            z++
                        }
                        x++
                    }
                }
                5 -> {
                    var x = 0
                    var z: Int
                    var y: Int
                    while (x < 16) {
                        z = 0
                        while (z < 16) {
                            y = 0
                            while (y < 10) {
                                if (y == 0) {
                                    chunk.setBlockId(x, y, z, Block.BEDROCK)
                                    chunk.setBlockData(x, y, x, 0)
                                } else {
                                    if (x > 11 || z < 4) {
                                        chunk.setBlockId(x, y, z, getBlockInfo(AISLE_BLOCK, true))
                                        chunk.setBlockData(x, y, z, getBlockInfo(AISLE_BLOCK, false))
                                    } else if (x < 11 && z > 4) {
                                        chunk.setBlockId(x, y, z, getBlockInfo(LAND_BLOCK, true))
                                        chunk.setBlockData(x, y, x, getBlockInfo(LAND_BLOCK, false))
                                    } else {
                                        chunk.setBlockId(x, y, z, getBlockInfo(FRAME_BLOCK, true))
                                        chunk.setBlockData(x, y, z, getBlockInfo(FRAME_BLOCK, false))
                                        if (y == 9) {
                                            chunk.setBlockId(x, y + 1, z, getBlockInfo(FRAME_TOP_BLOCK, true))
                                            chunk.setBlockData(x, y + 1, z, getBlockInfo(FRAME_TOP_BLOCK, false))
                                        }
                                    }
                                }
                                y++
                            }
                            z++
                        }
                        x++
                    }
                }
                6 -> {
                    var x = 0
                    var z: Int
                    var y: Int
                    while (x < 16) {
                        z = 0
                        while (z < 16) {
                            y = 0
                            while (y < 10) {
                                if (y == 0) {
                                    chunk.setBlockId(x, y, z, Block.BEDROCK)
                                    chunk.setBlockData(x, y, x, 0)
                                } else {
                                    if (x > 11) {
                                        chunk.setBlockId(x, y, z, getBlockInfo(AISLE_BLOCK, true))
                                        chunk.setBlockData(x, y, z, getBlockInfo(AISLE_BLOCK, false))
                                    } else if (x < 11) {
                                        chunk.setBlockId(x, y, z, getBlockInfo(LAND_BLOCK, true))
                                        chunk.setBlockData(x, y, x, getBlockInfo(LAND_BLOCK, false))
                                    } else {
                                        chunk.setBlockId(x, y, z, getBlockInfo(FRAME_BLOCK, true))
                                        chunk.setBlockData(x, y, z, getBlockInfo(FRAME_BLOCK, false))
                                        if (y == 9) {
                                            chunk.setBlockId(x, y + 1, z, getBlockInfo(FRAME_TOP_BLOCK, true))
                                            chunk.setBlockData(x, y + 1, z, getBlockInfo(FRAME_TOP_BLOCK, false))
                                        }
                                    }
                                }
                                y++
                            }
                            z++
                        }
                        x++
                    }
                }
                7 -> {
                    var x = 0
                    var z: Int
                    var y: Int
                    while (x < 16) {
                        z = 0
                        while (z < 16) {
                            y = 0
                            while (y < 10) {
                                if (y == 0) {
                                    chunk.setBlockId(x, y, z, Block.BEDROCK)
                                    chunk.setBlockData(x, y, x, 0)
                                } else {
                                    if (x > 11 || z > 11) {
                                        chunk.setBlockId(x, y, z, getBlockInfo(AISLE_BLOCK, true))
                                        chunk.setBlockData(x, y, z, getBlockInfo(AISLE_BLOCK, false))
                                    } else if (x < 11 && z < 11) {
                                        chunk.setBlockId(x, y, z, getBlockInfo(LAND_BLOCK, true))
                                        chunk.setBlockData(x, y, x, getBlockInfo(LAND_BLOCK, false))
                                    } else {
                                        chunk.setBlockId(x, y, z, getBlockInfo(FRAME_BLOCK, true))
                                        chunk.setBlockData(x, y, z, getBlockInfo(FRAME_BLOCK, false))
                                        if (y == 9) {
                                            chunk.setBlockId(x, y + 1, z, getBlockInfo(FRAME_TOP_BLOCK, true))
                                            chunk.setBlockData(x, y + 1, z, getBlockInfo(FRAME_TOP_BLOCK, false))
                                        }
                                    }
                                }
                                y++
                            }
                            z++
                        }
                        x++
                    }
                }
                8 -> {
                    var x = 0
                    var z: Int
                    var y: Int
                    while (x < 16) {
                        z = 0
                        while (z < 16) {
                            y = 0
                            while (y < 10) {
                                if (y == 0) {
                                    chunk.setBlockId(x, y, z, Block.BEDROCK)
                                    chunk.setBlockData(x, y, x, 0)
                                } else {
                                    if (z > 11) {
                                        chunk.setBlockId(x, y, z, getBlockInfo(AISLE_BLOCK, true))
                                        chunk.setBlockData(x, y, z, getBlockInfo(AISLE_BLOCK, false))
                                    } else if (z < 11) {
                                        chunk.setBlockId(x, y, z, getBlockInfo(LAND_BLOCK, true))
                                        chunk.setBlockData(x, y, x, getBlockInfo(LAND_BLOCK, false))
                                    } else {
                                        chunk.setBlockId(x, y, z, getBlockInfo(FRAME_BLOCK, true))
                                        chunk.setBlockData(x, y, z, getBlockInfo(FRAME_BLOCK, false))
                                        if (y == 9) {
                                            chunk.setBlockId(x, y + 1, z, getBlockInfo(FRAME_TOP_BLOCK, true))
                                            chunk.setBlockData(x, y + 1, z, getBlockInfo(FRAME_TOP_BLOCK, false))
                                        }
                                    }
                                }
                                y++
                            }
                            z++
                        }
                        x++
                    }
                }
                0 -> {
                    var x = 0
                    var y: Int
                    var z: Int
                    while (x < 16) {
                        z = 0
                        while (z < 16) {
                            y = 0
                            while (y < 10) {
                                if (y == 0) {
                                    chunk.setBlockId(x, y, z, Block.BEDROCK)
                                    chunk.setBlockData(x, y, x, 0)
                                } else {
                                    chunk.setBlockId(x, y, z, getBlockInfo(LAND_BLOCK, true))
                                    chunk.setBlockData(x, y, x, getBlockInfo(LAND_BLOCK, false))
                                }
                                y++
                            }
                            z++
                        }
                        x++
                    }
                }
            }
            return chunk
        }
        return null
    }

    private fun getBlockInfo(block: String, type: Boolean): Int {
        val d = block.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        if (type) {
            return Integer.valueOf(d[0])
        }
        return if (d.size > 1) Integer.valueOf(d[1]) else 0
    }
}