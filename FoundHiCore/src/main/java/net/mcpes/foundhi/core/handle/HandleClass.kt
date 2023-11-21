package net.mcpes.foundhi.core.handle

/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/8/13
 * version 1.0
 */
abstract class HandleClass : Handle {
    @JvmField
    protected var result: Int = 0

    abstract fun handle()

    final override fun handle(result: Int) {
        this.result = result
        this.handle()
    }
}