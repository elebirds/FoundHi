package net.mcpes.foundhipro.utils

import net.mcpes.foundhipro.utils.exception.DateFormatException
import java.text.SimpleDateFormat
import java.util.*

/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/6/26 下午9:39
 * version 1.0
 */

val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

fun Long.toDate(): Date {
    return Date(this)
}

fun Date.format(): String {
    return format.format(this)
}

object DateUtils {
    @JvmStatic
    @Throws(DateFormatException::class)
    fun getTimeDifference(input: String): Long {
        var pos = 0
        var time = 0.toLong()
        try {
            input.toCharArray().forEach {
                if (it !in '0'..'9') {
                    if ((it != 'y' && it != 's' && it != 'm' && it != 'd' && it != 'h' && it != 'i') || pos == 0) {
                        throw DateFormatException("时间格式不正确!")
                    } else {
                        var string = ""
                        var i = pos
                        while (i > 0) {
                            val t = input[i - 1]
                            if (t in '0'..'9') {
                                string = t + string
                            } else {
                                break
                            }
                            i--
                        }
                        when (it) {
                            'y' -> time += (string.toInt() * 31536000000) // year
                            'm' -> time += (string.toInt() * 2592000000) // month
                            'd' -> time += (string.toInt() * 86400000) // day
                            'h' -> time += (string.toInt() * 3600000) // hour
                            'i' -> time += (string.toInt() * 60000) // min
                            's' -> time += (string.toInt() * 1000) // second
                        }
                    }
                }
                pos++
            }
            return time
        } catch (e: Exception) {
            if (e.message != null) throw DateFormatException(e.message!!)
            else throw DateFormatException()
        }
    }
}