import redis.clients.jedis.Jedis

/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/6/10
 * version 1.0
 */

fun main(array: Array<String>) {
    val jedis = Jedis("127.0.0.1", 6379)

    println(jedis.hgetAll("hhm").size)
}