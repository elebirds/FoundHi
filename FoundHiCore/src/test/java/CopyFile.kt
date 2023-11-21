import net.mcpes.foundhi.core.utils.FileUtils
import java.io.File

/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/7/29
 * version 1.0
 */

fun main(strings: Array<String>) {
    val path = File("E:\\Coding\\Java\\FoundHiProject\\FoundHiCore\\build\\FoundHiCore-1.0.0-alpha.jar")
    FileUtils.copyFile(path, File("E:\\Minecraft\\Server\\Test\\Nukkit\\plugins\\FoundHiCore-1.0.0-alpha.jar"))
    FileUtils.copyFile(path, File("E:\\Minecraft\\Server\\Test\\Nukkit - 副本\\plugins\\FoundHiCore-1.0.0-alpha.jar"))
    FileUtils.copyFile(path, File("E:\\Minecraft\\Server\\Test\\Nukkit - 副本 - 副本\\plugins\\FoundHiCore-1.0.0-alpha.jar"))
    FileUtils.copyFile(path, File("E:\\Minecraft\\Server\\Test\\Nukkit - 副本 - 副本 (2)\\plugins\\FoundHiCore-1.0.0-alpha.jar"))
}