package net.mcpes.foundhi.core.utils.encryption

import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/5/29
 * version 1.0
 */
object MD5 : Encryption() {
    /**
     * 获取MD5-32位加密<br></br>
     *
     * @param s String 内容<br></br>
     * @return String md5
     */
    @JvmStatic
    fun encrypt(s: String): String {
        try {
            val md = MessageDigest.getInstance("MD5")
            md.update(s.toByteArray())
            val b = md.digest()
            var i: Int
            val sb = StringBuilder("")
            for (aB in b) {
                i = aB.toInt()
                if (i < 0)
                    i += 256
                if (i < 16)
                    sb.append("0")
                sb.append(Integer.toHexString(i))
            }
            return sb.toString()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
            return ""
        }
    }
}