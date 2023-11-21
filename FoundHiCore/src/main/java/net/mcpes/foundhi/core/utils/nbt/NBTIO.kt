package net.mcpes.foundhi.core.utils.nbt

import cn.nukkit.utils.ThreadCache
import net.mcpes.foundhi.core.utils.nbt.stream.NBTInputStream
import net.mcpes.foundhi.core.utils.nbt.stream.NBTOutputStream
import net.mcpes.foundhi.core.utils.nbt.stream.PGZIPOutputStream
import net.mcpes.foundhi.core.utils.nbt.tag.CompoundTag
import net.mcpes.foundhi.core.utils.nbt.tag.Tag
import java.io.*
import java.nio.ByteOrder
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import java.util.zip.Deflater
import java.util.zip.DeflaterOutputStream
import java.util.zip.GZIPInputStream

/**
 * FoundHi
 *
 * @author yui Copyright (c) 2018/6/5
 * version 1.0
 */
object NBTIO {
    @Throws(IOException::class)
    fun read(file: File): CompoundTag? {
        return read(file, ByteOrder.BIG_ENDIAN)
    }

    @Throws(IOException::class)
    fun read(file: File, endianness: ByteOrder): CompoundTag? {
        return if (!file.exists()) null else read(FileInputStream(file), endianness)
    }

    @Throws(IOException::class)
    fun read(inputStream: InputStream): CompoundTag {
        return read(inputStream, ByteOrder.BIG_ENDIAN)
    }

    @Throws(IOException::class)
    fun read(inputStream: InputStream, endianness: ByteOrder): CompoundTag {
        return read(inputStream, endianness, false)
    }

    @Throws(IOException::class)
    fun read(inputStream: InputStream, endianness: ByteOrder, network: Boolean): CompoundTag {
        val stream = NBTInputStream(inputStream, endianness, network)
        val tag = Tag.readNamedTag(stream)
        if (tag is CompoundTag) {
            return tag
        }
        throw IOException("Root tag must be a named compound tag")
    }

    @Throws(IOException::class)
    fun read(data: ByteArray): CompoundTag {
        return read(data, ByteOrder.BIG_ENDIAN)
    }

    @Throws(IOException::class)
    fun read(data: ByteArray, endianness: ByteOrder): CompoundTag {
        return read(ByteArrayInputStream(data), endianness)
    }

    @Throws(IOException::class)
    fun read(data: ByteArray, endianness: ByteOrder, network: Boolean): CompoundTag {
        return read(ByteArrayInputStream(data), endianness, network)
    }

    @Throws(IOException::class)
    fun readCompressed(inputStream: InputStream): CompoundTag {
        return readCompressed(inputStream, ByteOrder.BIG_ENDIAN)
    }

    @Throws(IOException::class)
    fun readCompressed(inputStream: InputStream, endianness: ByteOrder): CompoundTag {
        return read(BufferedInputStream(GZIPInputStream(inputStream)), endianness)
    }

    @Throws(IOException::class)
    fun readCompressed(data: ByteArray): CompoundTag {
        return readCompressed(data, ByteOrder.BIG_ENDIAN)
    }

    @Throws(IOException::class)
    fun readCompressed(data: ByteArray, endianness: ByteOrder): CompoundTag {
        return read(BufferedInputStream(GZIPInputStream(ByteArrayInputStream(data))), endianness, true)
    }

    @Throws(IOException::class)
    fun readNetworkCompressed(inputStream: InputStream): CompoundTag {
        return readNetworkCompressed(inputStream, ByteOrder.BIG_ENDIAN)
    }

    @Throws(IOException::class)
    fun readNetworkCompressed(inputStream: InputStream, endianness: ByteOrder): CompoundTag {
        return read(BufferedInputStream(GZIPInputStream(inputStream)), endianness)
    }

    @Throws(IOException::class)
    fun readNetworkCompressed(data: ByteArray): CompoundTag {
        return readNetworkCompressed(data, ByteOrder.BIG_ENDIAN)
    }

    @Throws(IOException::class)
    fun readNetworkCompressed(data: ByteArray, endianness: ByteOrder): CompoundTag {
        return read(BufferedInputStream(GZIPInputStream(ByteArrayInputStream(data))), endianness, true)
    }

    @Throws(IOException::class)
    fun write(tag: CompoundTag): ByteArray {
        return write(tag, ByteOrder.BIG_ENDIAN)
    }

    @Throws(IOException::class)
    fun write(tag: CompoundTag, endianness: ByteOrder): ByteArray {
        return write(tag, endianness, false)
    }

    @Throws(IOException::class)
    fun write(tag: CompoundTag, endianness: ByteOrder, network: Boolean): ByteArray {
        val baos = ThreadCache.fbaos.get().reset()
        NBTOutputStream(baos, endianness, network).run {
            Tag.writeNamedTag(tag, this)
            return baos.toByteArray()
        }
    }

    @Throws(IOException::class)
    fun write(tags: Collection<CompoundTag>): ByteArray {
        return write(tags, ByteOrder.BIG_ENDIAN)
    }

    @Throws(IOException::class)
    fun write(tags: Collection<CompoundTag>, endianness: ByteOrder): ByteArray {
        return write(tags, endianness, false)
    }

    @Throws(IOException::class)
    fun write(tags: Collection<CompoundTag>, endianness: ByteOrder, network: Boolean): ByteArray {
        val baos = ThreadCache.fbaos.get().reset()
        NBTOutputStream(baos, endianness, network).run {
            for (tag in tags) {
                Tag.writeNamedTag(tag, this)
            }
            return baos.toByteArray()
        }
    }

    @Throws(IOException::class)
    fun write(tag: CompoundTag, file: File) {
        write(tag, file, ByteOrder.BIG_ENDIAN)
    }

    @Throws(IOException::class)
    fun write(tag: CompoundTag, file: File, endianness: ByteOrder) {
        write(tag, FileOutputStream(file), endianness)
    }

    @Throws(IOException::class)
    fun write(tag: CompoundTag, outputStream: OutputStream) {
        write(tag, outputStream, ByteOrder.BIG_ENDIAN)
    }

    @Throws(IOException::class)
    fun write(tag: CompoundTag, outputStream: OutputStream, endianness: ByteOrder) {
        write(tag, outputStream, endianness, false)
    }

    @Throws(IOException::class)
    fun write(tag: CompoundTag, outputStream: OutputStream, endianness: ByteOrder, network: Boolean) {
        NBTOutputStream(outputStream, endianness, network).run {
            Tag.writeNamedTag(tag, this)
        }
    }

    @Throws(IOException::class)
    fun writeGZIPCompressed(tag: CompoundTag): ByteArray {
        return writeGZIPCompressed(tag, ByteOrder.BIG_ENDIAN)
    }

    @Throws(IOException::class)
    fun writeGZIPCompressed(tag: CompoundTag, endianness: ByteOrder): ByteArray {
        val baos = ThreadCache.fbaos.get().reset()
        writeGZIPCompressed(tag, baos, endianness)
        return baos.toByteArray()
    }

    @Throws(IOException::class)
    fun writeGZIPCompressed(tag: CompoundTag, outputStream: OutputStream) {
        writeGZIPCompressed(tag, outputStream, ByteOrder.BIG_ENDIAN)
    }

    @Throws(IOException::class)
    fun writeGZIPCompressed(tag: CompoundTag, outputStream: OutputStream, endianness: ByteOrder) {
        write(tag, PGZIPOutputStream(outputStream), endianness)
    }

    @Throws(IOException::class)
    fun writeNetworkGZIPCompressed(tag: CompoundTag): ByteArray {
        return writeNetworkGZIPCompressed(tag, ByteOrder.BIG_ENDIAN)
    }

    @Throws(IOException::class)
    fun writeNetworkGZIPCompressed(tag: CompoundTag, endianness: ByteOrder): ByteArray {
        val baos = ThreadCache.fbaos.get().reset()
        writeNetworkGZIPCompressed(tag, baos, endianness)
        return baos.toByteArray()
    }

    @Throws(IOException::class)
    fun writeNetworkGZIPCompressed(tag: CompoundTag, outputStream: OutputStream) {
        writeNetworkGZIPCompressed(tag, outputStream, ByteOrder.BIG_ENDIAN)
    }

    @Throws(IOException::class)
    fun writeNetworkGZIPCompressed(tag: CompoundTag, outputStream: OutputStream, endianness: ByteOrder) {
        write(tag, PGZIPOutputStream(outputStream), endianness, true)
    }

    @Throws(IOException::class)
    fun writeZLIBCompressed(tag: CompoundTag, outputStream: OutputStream) {
        writeZLIBCompressed(tag, outputStream, ByteOrder.BIG_ENDIAN)
    }

    @Throws(IOException::class)
    fun writeZLIBCompressed(tag: CompoundTag, outputStream: OutputStream, endianness: ByteOrder) {
        writeZLIBCompressed(tag, outputStream, Deflater.DEFAULT_COMPRESSION, endianness)
    }

    @Throws(IOException::class)
    fun writeZLIBCompressed(tag: CompoundTag, outputStream: OutputStream, level: Int) {
        writeZLIBCompressed(tag, outputStream, level, ByteOrder.BIG_ENDIAN)
    }

    @Throws(IOException::class)
    fun writeZLIBCompressed(tag: CompoundTag, outputStream: OutputStream, level: Int, endianness: ByteOrder) {
        write(tag, DeflaterOutputStream(outputStream, Deflater(level)), endianness)
    }

    @Throws(IOException::class)
    fun safeWrite(tag: CompoundTag, file: File) {
        val tmpFile = File(file.absolutePath + "_tmp")
        if (tmpFile.exists()) {
            tmpFile.delete()
        }
        write(tag, tmpFile)
        Files.move(tmpFile.toPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE)
    }
}