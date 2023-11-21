package net.mcpes.foundhipro.utils.font

/**
 * FoundHiPro
 *
 * @author hhm Copyright (c) 2018/8/31 下午6:18
 * version 1.0
 */

object ColorFormat {
    val replacements: HashMap<String, FontEffect> = hashMapOf()

    /**
     * The special character which prefixes all format codes. Use this if
     * you need to dynamically convert format codes from your custom format.
     */
    const val ESCAPE = '§'

    init {
        FontColor.values().forEach {
            replacements[it.getCode().toString()] = it
        }
        FontAttribute.values().forEach {
            replacements[it.getCode().toString()] = it
        }
    }


    fun clean(string: String): String {
        var str = string
        this.replacements.values.forEach {
            str = str.replace("§${it.getCode()}", "")
        }
        return str
    }
}