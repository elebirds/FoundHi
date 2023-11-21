package net.mcpes.foundhipro.utils.font

/**
 * FoundHiPro
 *
 * @author hhm Copyright (c) 2018/8/31 下午6:20
 * version 1.0
 */
enum class FontAttribute(code: Char, intCode: Int, ansi: String) : FontEffect {
    /**
     * 混乱
     */
    OBFUSCATED('k', 0x10, "§"),
    /**
     * 加粗
     */
    BOLD('l', 0x11, "\u001b[21m"),
    /**
     * 删除线
     */
    STRIKETHROUGH('m', 0x12, "\u001b[09m"),
    /**
     * 下划线
     */
    UNDERLINE('n', 0x13, "\u001b[4m"),
    /**
     * 斜体
     */
    ITALIC('o', 0x14, "\u001b[3m"),
    /**
     * 重置
     */
    RESET('r', 0x15, "\u001b[m");

    private val codeI: Char = code
    private val intCodeI: Int = intCode
    private val ansiString: String = ansi

    override fun getCode(): Char {
        return codeI
    }

    override fun getANSIString(): String {
        return ansiString
    }

    override fun getIntCode(): Int {
        return intCodeI
    }

    override fun toString(): String {
        return "${ColorFormat.ESCAPE}$codeI"
    }
}