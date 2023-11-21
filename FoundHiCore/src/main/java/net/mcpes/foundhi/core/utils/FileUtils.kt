package net.mcpes.foundhi.core.utils

import com.google.common.base.Preconditions
import net.mcpes.foundhi.core.app.APIFunction
import net.mcpes.foundhi.core.app.FoundHiAPIMain
import java.io.*
import java.nio.charset.StandardCharsets
import java.util.*

/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/7/28
 * version 1.0
 */
object FileUtils {
    @JvmStatic
    @Throws(IOException::class)
    fun readProperties(file: File): Properties {
        val properties = Properties()
        properties.load(BufferedReader(FileReader(file)))
        return properties
    }

    @JvmStatic
    @Throws(IOException::class)
    fun writeFile(fileName: String, content: String) {
        writeFile(fileName, ByteArrayInputStream(content.toByteArray(StandardCharsets.UTF_8)))
    }

    @JvmStatic
    @Throws(IOException::class)
    fun writeFile(fileName: String, content: InputStream) {
        writeFile(File(fileName), content)
    }

    @JvmStatic
    @Throws(IOException::class)
    fun writeFile(file: File, content: String) {
        writeFile(file, ByteArrayInputStream(content.toByteArray(StandardCharsets.UTF_8)))
    }

    @JvmStatic
    @Throws(IOException::class)
    fun writeFile(file: File, content: InputStream?) {
        if (content == null) {
            throw IllegalArgumentException("content must not be null")
        }
        if (!file.exists()) {
            file.createNewFile()
        }
        val stream = FileOutputStream(file)
        val buffer = ByteArray(1024)
        var length: Int = content.read(buffer)
        while (length != -1) {
            stream.write(buffer, 0, length)
            length = content.read(buffer)
        }
        stream.close()
        content.close()
    }

    @JvmStatic
    @Throws(IOException::class)
    fun readFile(file: File): String {
        if (!file.exists() || file.isDirectory) {
            throw FileNotFoundException()
        }
        return readFile(FileInputStream(file))
    }

    @JvmStatic
    @Throws(IOException::class)
    fun readFile(filename: String): String {
        val file = File(filename)
        if (!file.exists() || file.isDirectory) {
            throw FileNotFoundException()
        }
        return readFile(FileInputStream(file))
    }

    @JvmStatic
    @Throws(IOException::class)
    fun readFile(inputStream: InputStream): String {
        return readFile(InputStreamReader(inputStream, StandardCharsets.UTF_8))
    }

    @JvmStatic
    @Throws(IOException::class)
    private fun readFile(reader: Reader): String {
        val br = BufferedReader(reader)
        var temp: String?
        val stringBuilder = StringBuilder()
        temp = br.readLine()
        while (temp != null) {
            if (stringBuilder.isNotEmpty()) {
                stringBuilder.append("\n")
            }
            stringBuilder.append(temp)
            temp = br.readLine()
        }
        br.close()
        reader.close()
        return stringBuilder.toString()
    }

    @JvmStatic
    fun saveResource(filename: String, outputName: String?, replace: Boolean): Boolean {
        Preconditions.checkArgument(outputName != null, "Filename can not be null!")
        Preconditions.checkArgument(filename.trim { it <= ' ' }.isNotEmpty() && outputName!!.trim { it <= ' ' }.isNotEmpty(), "Filename can not be empty!")
        val out = File(FoundHiAPIMain.DATA_PATH, outputName!!)
        if (!out.exists() || replace) {
            try {
                getResource(filename)!!.use { resource ->
                    val outFolder = out.parentFile
                    if (!outFolder.exists()) {
                        outFolder.mkdirs()
                    }
                    writeFile(out, resource)
                    return true
                }
            } catch (e: IOException) {
                APIFunction.instance.logger.logException(e)
            }

        }
        return false
    }

    @JvmStatic
    fun saveResource(filename: String, out: File, replace: Boolean): Boolean {
        if (!out.exists() || replace) {
            try {
                getResource(filename)!!.use { resource ->
                    val outFolder = out.parentFile
                    if (!outFolder.exists()) {
                        outFolder.mkdirs()
                    }
                    writeFile(out, resource)
                    return true
                }
            } catch (e: IOException) {
                APIFunction.instance.logger.logException(e)
            }

        }
        return false
    }

    @JvmStatic
    fun getResource(filename: String): InputStream? {
        return this::class.java.classLoader.getResourceAsStream(filename)
    }

    @JvmStatic
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
    @JvmStatic
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
        var len: Int = inBuff.read(b)
        while (len != -1) {
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
    @JvmStatic
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

    @JvmStatic
    fun remove(file: File) {
        if (file.isDirectory) {
            removeFolder(file.absolutePath)
        } else
            file.delete()
    }

    @JvmStatic
    fun removeFolder(folderPath: String) {
        try {
            removeAllFile(folderPath) //删除完里面所有内容
            val myFilePath = File(folderPath)
            myFilePath.delete() //删除空文件夹
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    @JvmStatic
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
            temp = if (path.endsWith(File.separator)) {
                File(path + tempList[i])
            } else {
                File(path + File.separator + tempList[i])
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