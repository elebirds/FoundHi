import net.mcpes.foundhi.core.utils.encryption.RSA
import net.sf.json.JSONObject

/**
 * FoundHi
 *
 * @author hhm Copyright (c) 2018/5/30
 * version 1.0
 */
fun main(a: Array<String>) {
    val key = RSA.createKeys(2048)
    val encoded = RSA.publicEncrypt("yuisb", RSA.getPublicKey(key[RSA.RSA_PUBLIC_KEY]!!))
    println("密钥:" + JSONObject.fromObject(key))
    println("加密：$encoded")
    //val decodedPU = RSA.publicDecrypt(encoded,RSA.getPublicKey(key[RSA.RSA_PUBLIC_KEY]!!))
    val decodedPR = RSA.privateDecrypt(encoded, RSA.getPrivateKey(key[RSA.RSA_PRIVATE_KEY]!!))
    //println("公钥解密：$decodedPU")
    println("私钥解密：$decodedPR")
}