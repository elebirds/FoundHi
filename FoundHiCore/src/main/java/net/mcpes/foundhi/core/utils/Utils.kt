package net.mcpes.foundhi.core.utils

import cn.nukkit.Server
import cn.nukkit.level.Location
import java.io.PrintWriter
import java.io.StringWriter
import java.lang.management.ManagementFactory
import java.text.DecimalFormat
import java.util.*
import java.util.regex.Pattern


/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/7/28
 * version 1.0
 */

var decimalFormat = DecimalFormat("0.00")

@Suppress("UNCHECKED_CAST")
fun <T> Any?.cast(): T {
    return this as T
}

fun IntArray.bubbleSort() {
    for (i in 0 until this.size - 1) {
        for (j in 0 until this.size - 1 - i) {
            if (this[j] > this[j + 1]) {
                val temp = this[j]
                this[j] = this[j + 1]
                this[j + 1] = temp
            }
        }
    }
}

fun FloatArray.bubbleSort() {
    for (i in 0 until this.size - 1) {
        for (j in 0 until this.size - 1 - i) {
            if (this[j] > this[j + 1]) {
                val temp = this[j]
                this[j] = this[j + 1]
                this[j + 1] = temp
            }
        }
    }
}

fun DoubleArray.bubbleSort() {
    for (i in 0 until this.size - 1) {
        for (j in 0 until this.size - 1 - i) {
            if (this[j] > this[j + 1]) {
                val temp = this[j]
                this[j] = this[j + 1]
                this[j + 1] = temp
            }
        }
    }
}

fun HashMap<*, Int>.bubbleAscendingSort(): ArrayList<Map.Entry<*, Int>> {
    val list = ArrayList<Map.Entry<*, Int>>(this.entries)
    list.sortWith(Comparator { o1, o2 ->
        //根据value排序
        val result = o2.value - o1.value
        when {
            result > 0 -> -1
            result == 0 -> 0
            else -> 1
        }
    })
    return list
}

fun HashMap<*, Int>.bubbleDescendingSort(): ArrayList<Map.Entry<*, Int>> {
    val list = ArrayList<Map.Entry<*, Int>>(this.entries)
    list.sortWith(Comparator { o1, o2 ->
        //根据value排序
        val result = o2.value - o1.value
        when {
            result > 0 -> 1
            result == 0 -> 0
            else -> -1
        }
    })
    return list
}

fun Collection<*>.isIdentical(collection: Collection<*>) = this.containsAll(collection) && collection.containsAll(this)

fun String.toLocation(): Location? {
    val b = this.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
    if (b.size != 4) return null
    return if (!Server.getInstance().isLevelLoaded(b[3])) {
        if (Server.getInstance().loadLevel(b[3])) {
            Location(Integer.valueOf(b[0]).toDouble(), Integer.valueOf(b[1]).toDouble(), Integer.valueOf(b[2]).toDouble(), Server.getInstance().getLevelByName(b[3]))
        } else {
            null
        }
    } else Location(Integer.valueOf(b[0]).toDouble(), Integer.valueOf(b[1]).toDouble(), Integer.valueOf(b[2]).toDouble(), Server.getInstance().getLevelByName(b[3]))
}

fun Double.moneyFormat() = decimalFormat.format(this)!!

fun String.removeLastChar() = this.substring(0, this.length - 1)

object Utils {
    @JvmStatic
    fun getAllThreadDumps(): String {
        val threads = ManagementFactory.getThreadMXBean().dumpAllThreads(true, true)
        val builder = StringBuilder()
        for (info in threads) {
            builder.append('\n').append(info)
        }
        return builder.toString()
    }

    @JvmStatic
    fun getExceptionMessage(e: Throwable): String {
        val stringWriter = StringWriter()
        val printWriter = PrintWriter(stringWriter)
        e.printStackTrace(printWriter)
        return stringWriter.toString()
    }

    @JvmStatic
    fun isInteger(str: String): Boolean {
        val pattern = Pattern.compile("^[-\\+]?[\\d]*$")
        return pattern.matcher(str).matches()
    }
}