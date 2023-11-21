import net.mcpes.foundhi.core.utils.DateUtils
import java.util.*
import kotlin.concurrent.thread

/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/6/25 下午1:11
 * version 1.0
 */
fun main(arr: Array<String>) {
    thread(start = true) {
        StringThreadTest.send("Alen")
    }
    thread(start = true) {
        StringThreadTest.send("Alen")
    }
    thread(start = true) {
        StringThreadTest.send("Kotlin")
    }
    val date = Date()
    val s = "3d2s"
    val newDate = Date(date.time + DateUtils.getTimeDifference(s))
    println(date)
    println(newDate)
}

object StringThreadTest {
    @JvmStatic
    fun send(name: String) {
        synchronized(name) {
            println("$name:{${name.hashCode()}}")
        }
    }
}