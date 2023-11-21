package net.mcpes.foundhipro.utils.encryption

import net.mcpes.foundhipro.utils.Charset
import java.security.SecureRandom
import java.util.*
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.spec.SecretKeySpec

/**
 * FoundHiPro
 *
 * @author hhm Copyright (c) 2018/8/29
 * version 1.0
 */
object AES : Encryption() {
    private const val KEY_ALGORITHM = "AES"
    private const val DEFAULT_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding"//默认的加密算法
    private const val DIGIT: Int = 256

    /**
     * AES 加密操作
     *
     * @param content 待加密内容
     * @param password 加密密码
     * @return 返回Base64转码后的加密数据
     */
    @JvmStatic
    fun encrypt(content: String, password: String): String {
        val cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM)// 创建密码器
        val byteContent = content.toByteArray(Charset.defaultCharset)
        cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(password))// 初始化为加密模式的密码器
        val result = cipher.doFinal(byteContent)// 加密
        return Base64.getEncoder().encodeToString(result)//通过Base64转码返回
    }

    /**
     * 解密
     * @param content AES加密过过的内容
     * @param password 加密时的密码
     * @return 明文
     */
    @JvmStatic
    fun decrypt(content: String, password: String): String {
        //实例化
        val cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM)
        //使用密钥初始化，设置为解密模式
        cipher.init(Cipher.DECRYPT_MODE, getSecretKey(password))
        //执行操作
        val result = cipher.doFinal(Base64.getDecoder().decode(content))
        return String(result, Charset.defaultCharset)
    }

    /**
     * 生成加密秘钥
     *
     * @return
     */
    private fun getSecretKey(password: String): SecretKeySpec {
        val kg: KeyGenerator? = KeyGenerator.getInstance(KEY_ALGORITHM)
        kg!!.init(DIGIT, SecureRandom(password.toByteArray()))
        val secretKey = kg.generateKey()
        return SecretKeySpec(secretKey.encoded, KEY_ALGORITHM)// 转换为AES专用密钥
    }
}