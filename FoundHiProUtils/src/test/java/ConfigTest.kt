/**
 * FoundHiPro
 *
 * @author hhm Copyright (c) 2018/8/29
 * version 1.0
 */

fun main(args: Array<String>) {
    val s = "%fuck%,%emm%"
    val st = System.currentTimeMillis()
    val r = s.split("%".toRegex())
    val et = System.currentTimeMillis()
    println(et - st)
}