package net.mcpes.foundhi.core.utils.encryption

import java.io.ByteArrayOutputStream
import java.security.KeyFactory
import java.security.KeyPairGenerator
import java.security.NoSuchAlgorithmException
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.security.spec.InvalidKeySpecException
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.*
import javax.crypto.Cipher

/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/5/30
 * version 1.0
 */
object RSA : Encryption() {
    const val ALGORITHM = "RSA"
    const val RSA_PUBLIC_KEY = "ruk"
    const val RSA_PRIVATE_KEY = "rrk"

    fun createKeys(keySize: Int): Map<String, String> {
        //为RSA算法创建一个KeyPairGenerator对象
        val kpg: KeyPairGenerator
        try {
            kpg = KeyPairGenerator.getInstance(ALGORITHM)
        } catch (e: NoSuchAlgorithmException) {
            throw IllegalArgumentException("No such algorithm-->[$ALGORITHM]")
        }
        //初始化KeyPairGenerator对象,密钥长度
        kpg.initialize(keySize)
        //生成密匙对
        val keyPair = kpg.generateKeyPair()
        //得到公钥
        val publicKey = keyPair.public
        val publicKeyStr = Base64.getEncoder().encodeToString(publicKey.encoded)
        //得到私钥
        val privateKey = keyPair.private
        val privateKeyStr = Base64.getEncoder().encodeToString(privateKey.encoded)
        val keyPairMap = HashMap<String, String>()
        keyPairMap[RSA_PUBLIC_KEY] = publicKeyStr
        keyPairMap[RSA_PRIVATE_KEY] = privateKeyStr
        return keyPairMap
    }

    /**
     * 得到公钥
     * @param publicKey 密钥字符串（经过base64编码）
     * @throws Exception
     */
    @Throws(NoSuchAlgorithmException::class, InvalidKeySpecException::class)
    fun getPublicKey(publicKey: String): RSAPublicKey {
        //通过X509编码的Key指令获得公钥对象
        val keyFactory = KeyFactory.getInstance(ALGORITHM)
        val x509KeySpec = X509EncodedKeySpec(Base64.getDecoder().decode(publicKey))
        return keyFactory.generatePublic(x509KeySpec) as RSAPublicKey
    }

    /**
     * 得到私钥
     * @param privateKey 密钥字符串（经过base64编码）
     * @throws Exception
     */
    @Throws(NoSuchAlgorithmException::class, InvalidKeySpecException::class)
    fun getPrivateKey(privateKey: String): RSAPrivateKey {
        //通过PKCS#8编码的Key指令获得私钥对象
        val keyFactory = KeyFactory.getInstance(ALGORITHM)
        val pkcs8KeySpec = PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKey))
        return keyFactory.generatePrivate(pkcs8KeySpec) as RSAPrivateKey
    }

    /**
     * 公钥加密
     * @param data
     * @param publicKey
     * @return
     */
    fun publicEncrypt(data: String, publicKey: RSAPublicKey): String {
        try {
            val cipher = Cipher.getInstance(ALGORITHM)
            cipher.init(Cipher.ENCRYPT_MODE, publicKey)
            return Base64.getEncoder().encodeToString(rsaSplitCodec(cipher, Cipher.ENCRYPT_MODE, data.toByteArray(CHARSET), publicKey.modulus.bitLength()))
        } catch (e: Exception) {
            throw RuntimeException("加密字符串[$data]时遇到异常", e)
        }

    }

    /**
     * 私钥解密
     * @param data
     * @param privateKey
     * @return
     */
    fun privateDecrypt(data: String, privateKey: RSAPrivateKey): String {
        try {
            val cipher = Cipher.getInstance(ALGORITHM)
            cipher.init(Cipher.DECRYPT_MODE, privateKey)
            return String(rsaSplitCodec(cipher, Cipher.DECRYPT_MODE, Base64.getDecoder().decode(data), privateKey.modulus.bitLength()), CHARSET)
        } catch (e: Exception) {
            throw RuntimeException("解密字符串[$data]时遇到异常", e)
        }

    }

    /**
     * 私钥解密
     * @param data
     * @param privateKey String
     * @return
     */
    fun privateDecrypt(data: String, privateKey: String): String {
        return this.privateDecrypt(data, this.getPrivateKey(privateKey))
    }

    /**
     * 私钥加密
     * @param data
     * @param privateKey RSAPrivateKey
     * @return
     */
    fun privateEncrypt(data: String, privateKey: RSAPrivateKey): String {
        try {
            val cipher = Cipher.getInstance(ALGORITHM)
            cipher.init(Cipher.ENCRYPT_MODE, privateKey)
            return Base64.getEncoder().encodeToString(rsaSplitCodec(cipher, Cipher.ENCRYPT_MODE, data.toByteArray(CHARSET), privateKey.modulus.bitLength()))
        } catch (e: Exception) {
            throw RuntimeException("加密字符串[$data]时遇到异常", e)
        }

    }

    /**
     * 公钥解密
     * @param data
     * @param publicKey
     * @return
     */
    fun publicDecrypt(data: String, publicKey: RSAPublicKey): String {
        try {
            val cipher = Cipher.getInstance(ALGORITHM)
            cipher.init(Cipher.DECRYPT_MODE, publicKey)
            return String(rsaSplitCodec(cipher, Cipher.DECRYPT_MODE, Base64.getDecoder().decode(data), publicKey.modulus.bitLength()), CHARSET)
        } catch (e: Exception) {
            throw RuntimeException("解密字符串[$data]时遇到异常", e)
        }

    }

    private fun rsaSplitCodec(cipher: Cipher, opmode: Int, datas: ByteArray, keySize: Int): ByteArray {
        val maxBlock: Int = if (opmode == Cipher.DECRYPT_MODE) {
            keySize / 8
        } else {
            keySize / 8 - 11
        }
        val out = ByteArrayOutputStream()
        var offSet = 0
        var buff: ByteArray
        var i = 0
        try {
            while (datas.size > offSet) {
                if (datas.size - offSet > maxBlock) {
                    buff = cipher.doFinal(datas, offSet, maxBlock)
                } else {
                    buff = cipher.doFinal(datas, offSet, datas.size - offSet)
                }
                out.write(buff, 0, buff.size)
                i++
                offSet = i * maxBlock
            }
        } catch (e: Exception) {
            throw RuntimeException("加解密阀值为[$maxBlock]的数据时发生异常", e)
        }
        return out.toByteArray()
    }
}