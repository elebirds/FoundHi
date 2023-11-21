package net.mcpes.foundhi.plugin.game.has.utils

import java.io.*

object FileUtils {
    @Throws(IOException::class)
    fun copy(`in`: String, out: String) {
        File(out).mkdirs()
        // 获取源文件夹当前下的文件或目录
        val file = File(`in`).listFiles()
        for (i in file!!.indices) {
            if (file[i].isFile) {
                // 复制文件
                copyFile(file[i], File(out + "/" + file[i].name))
            }
            if (file[i].isDirectory) {
                // 复制目录
                val sourceDir = `in` + File.separator + file[i].name
                val targetDir = out + File.separator + file[i].name
                copyDirectory(sourceDir, targetDir)
            }
        }
    }

    // 复制文件
    @Throws(IOException::class)
    fun copyFile(sourceFile: File, targetFile: File) {
        // 新建文件输入流并对它进行缓冲
        val input = FileInputStream(sourceFile)
        val inBuff = BufferedInputStream(input)
        // 新建文件输出流并对它进行缓冲
        val output = FileOutputStream(targetFile)
        val outBuff = BufferedOutputStream(output)
        // 缓冲数组
        val b = ByteArray(1024 * 5)
        var len: Int
        len = inBuff.read(b)
        while ((len) != -1) {
            outBuff.write(b, 0, len)
            len = inBuff.read(b)
        }
        // 刷新此缓冲的输出流
        outBuff.flush()
        //关闭流
        inBuff.close()
        outBuff.close()
        output.close()
        input.close()
    }

    // 复制文件夹
    @Throws(IOException::class)
    fun copyDirectory(sourceDir: String, targetDir: String) {
        // 新建目标目录
        File(targetDir).mkdirs()
        // 获取源文件夹当前下的文件或目录
        val file = File(sourceDir).listFiles()
        for (i in file!!.indices) {
            if (file[i].isFile) {
                // 源文件
                val sourceFile = file[i]
                // 目标文件
                val targetFile = File(File(targetDir).absolutePath
                        + File.separator + file[i].name)
                copyFile(sourceFile, targetFile)
            }
            if (file[i].isDirectory) {
                // 准备复制的源文件夹
                val dir1 = sourceDir + "/" + file[i].name
                // 准备复制的目标文件夹
                val dir2 = targetDir + "/" + file[i].name
                copyDirectory(dir1, dir2)
            }
        }
    }

    fun remove(file: File) {
        if (file.isDirectory) {
            removeFolder(file.absolutePath)
        } else
            file.delete()
    }

    fun removeFolder(folderPath: String) {
        try {
            removeAllFile(folderPath) //删除完里面所有内容
            val myFilePath = File(folderPath)
            myFilePath.delete() //删除空文件夹
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun removeAllFile(path: String): Boolean {
        val file = File(path)
        if (!file.exists()) {
            return false
        }
        if (!file.isDirectory) {
            return false
        }
        val tempList = file.list() ?: return false
        var temp: File
        for (i in tempList.indices) {
            if (path.endsWith(File.separator)) {
                temp = File(path + tempList[i])
            } else {
                temp = File(path + File.separator + tempList[i])
            }
            if (temp.isFile) {
                temp.delete()
            }
            if (temp.isDirectory) {
                removeAllFile(path + "/" + tempList[i])//先删除文件夹里面的文件
                removeFolder(path + "/" + tempList[i])//再删除空文件夹
                return true
            }
        }
        return false
    }
}