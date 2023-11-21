package net.mcpes.foundhi.core.handle

import java.util.function.Consumer

/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/8/14
 * version 1.0
 */
class FastHandle(var handle: Consumer<Int>? = null) : Handle {
    override fun handle(result: Int) {
        handle?.accept(result)
    }
}