package net.mcpes.foundhipro.utils.font

/**
 * FoundHiPro
 *
 * @author hhm Copyright (c) 2018/8/31 下午6:18
 * version 1.0
 */
enum class FontColor(code: Char, intCode: Int, ansi: String) : FontEffect {
    /**
     * Represents black.
     */
    BLACK('0', 0x00, "\u001b[0;30;22m"),
    /**
     * Represents dark blue.
     */
    DARK_BLUE('1', 0x1, "\u001b[0;34;22m"),
    /**
     * Represents dark green.
     */
    DARK_GREEN('2', 0x2, "\u001b[0;32;22m"),
    /**
     * Represents dark blue (aqua).
     */
    DARK_AQUA('3', 0x3, "\u001b[0;36;22m"),
    /**
     * Represents dark red.
     */
    DARK_RED('4', 0x4, "\u001b[0;31;22m"),
    /**
     * Represents dark purple.
     */
    DARK_PURPLE('5', 0x5, "\u001b[0;35;22m"),
    /**
     * Represents gold.
     */
    GOLD('6', 0x6, "\u001b[0;33;22m"),
    /**
     * Represents gray.
     */
    GRAY('7', 0x7, "\u001b[0;37;22m"),
    /**
     * Represents dark gray.
     */
    DARK_GRAY('8', 0x8, "\u001b[0;30;1m"),
    /**
     * Represents blue.
     */
    BLUE('9', 0x9, "\u001b[0;34;1m"),
    /**
     * Represents green.
     */
    GREEN('a', 0xA, "\u001b[0;32;1m"),
    /**
     * Represents aqua.
     */
    AQUA('b', 0xB, "\u001b[0;36;1m"),
    /**
     * Represents red.
     */
    RED('c', 0xC, "\u001b[0;31;1m"),
    /**
     * Represents light purple.
     */
    LIGHT_PURPLE('d', 0xD, "\u001b[0;35;1m"),
    /**
     * Represents yellow.
     */
    YELLOW('e', 0xE, "\u001b[0;33;1m"),
    /**
     * Represents white.
     */
    WHITE('f', 0xF, "\u001b[0;37;1m");

    private val codeI: Char = code
    private val intCodeI: Int = intCode
    private val ansiString: String = ansi

    override fun getCode(): Char {
        return codeI
    }

    override fun getIntCode(): Int {
        return intCodeI
    }

    override fun getANSIString(): String {
        return ansiString
    }

    override fun toString(): String {
        return "${ColorFormat.ESCAPE}$codeI"
    }
}