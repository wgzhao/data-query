package com.github.wgzhao.dbquery.util

import java.io.ByteArrayOutputStream
import java.security.*
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.*
import javax.crypto.Cipher

object RSAUtil {
    /**
     * RSA最大加密明文大小
     */
    private const val MAX_ENCRYPT_BLOCK = 117

    /**
     * RSA最大解密密文大小
     */
    private const val MAX_DECRYPT_BLOCK = 128

    /**
     * 编码
     */
    private const val charset = "utf-8"

    @get:Throws(Exception::class)
    val keyPair: KeyPair
        /**
         * 获取密钥对
         *
         * @return 密钥对
         */
        get() {
            val generator = KeyPairGenerator.getInstance("RSA")
            generator.initialize(512)
            return generator.generateKeyPair()
        }

    /**
     * 获取私钥
     *
     * @param privateKey 私钥字符串
     * @return [PrivateKey]
     */
    @Throws(Exception::class)
    fun getPrivateKey(privateKey: String?): PrivateKey? {
        val keyFactory = KeyFactory.getInstance("RSA")
        val decodedKey = Base64.getDecoder().decode(privateKey)
        val keySpec = PKCS8EncodedKeySpec(decodedKey)
        return keyFactory.generatePrivate(keySpec)
    }

    /**
     * 获取公钥
     *
     * @param publicKey 公钥字符串
     * @return [PublicKey]
     */
    @Throws(Exception::class)
    fun getPublicKey(publicKey: String?): PublicKey? {
        val keyFactory = KeyFactory.getInstance("RSA")
        //        byte[] decodedKey = Base64.decodeBase64(publicKey.getBytes(charset));
        val decodedKey = Base64.getDecoder().decode(publicKey)
        val keySpec = X509EncodedKeySpec(decodedKey)
        return keyFactory.generatePublic(keySpec)
    }

    /**
     * RSA加密
     *
     * @param data 待加密数据
     * @param publicKey 公钥
     * @return the encrypted string
     */
    @Throws(Exception::class)
    fun encrypt(data: String, publicKey: PublicKey?): String? {
        val cipher = Cipher.getInstance("RSA")
        cipher.init(Cipher.ENCRYPT_MODE, publicKey)
        val inputLen = data.toByteArray(charset(charset)).size
        val out = ByteArrayOutputStream()
        var offset = 0
        var cache: ByteArray
        var i = 0
        // 对数据分段加密
        while (inputLen - offset > 0) {
            if (inputLen - offset > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(data.toByteArray(charset(charset)), offset, MAX_ENCRYPT_BLOCK)
            } else {
                cache = cipher.doFinal(data.toByteArray(charset(charset)), offset, inputLen - offset)
            }
            out.write(cache, 0, cache.size)
            i++
            offset = i * MAX_ENCRYPT_BLOCK
        }
        val encryptedData = out.toByteArray()
        out.close()
        // 获取加密内容使用base64进行编码,并以UTF-8为标准转化成字符串
        // 加密后的字符串
        return Base64.getEncoder().encodeToString(encryptedData)
        //        return java.util.Base64.getEncoder().encode(encryptedData).toString();
    }

    /**
     * RSA解密
     *
     * @param data 待解密数据
     * @param privateKey 私钥
     * @return the decrypted string
     */
    @Throws(Exception::class)
    fun decrypt(data: String?, privateKey: PrivateKey?): String? {
        val cipher = Cipher.getInstance("RSA")
        cipher.init(Cipher.DECRYPT_MODE, privateKey)

        val dataBytes = Base64.getDecoder().decode(data)
        val inputLen = dataBytes.size
        val out = ByteArrayOutputStream()
        var offset = 0
        var cache: ByteArray
        var i = 0
        // 对数据分段解密
        while (inputLen - offset > 0) {
            if (inputLen - offset > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(dataBytes, offset, MAX_DECRYPT_BLOCK)
            } else {
                cache = cipher.doFinal(dataBytes, offset, inputLen - offset)
            }
            out.write(cache, 0, cache.size)
            i++
            offset = i * MAX_DECRYPT_BLOCK
        }
        out.close()
        // 解密后的内容
        return out.toString("UTF-8")
    }

    /**
     * 签名
     *
     * @param data 待签名数据
     * @param privateKey 私钥
     * @return 签名
     */
    @Throws(Exception::class)
    fun sign(data: String, privateKey: PrivateKey): String {
        val keyBytes = privateKey.getEncoded()
        val keySpec = PKCS8EncodedKeySpec(keyBytes)
        val keyFactory = KeyFactory.getInstance("RSA")
        val key = keyFactory.generatePrivate(keySpec)
        val signature = Signature.getInstance("MD5withRSA")
        signature.initSign(key)
        signature.update(data.toByteArray(charset(charset)))
        return String(Base64.getEncoder().encode(signature.sign()), charset(charset))
    }

    /**
     * 验签
     *
     * @param srcData 原始字符串
     * @param publicKey 公钥
     * @param sign 签名
     * @return 是否验签通过
     */
    @Throws(Exception::class)
    fun verify(srcData: String, publicKey: PublicKey, sign: String): Boolean {
        val keyBytes = publicKey.getEncoded()
        val keySpec = X509EncodedKeySpec(keyBytes)
        val keyFactory = KeyFactory.getInstance("RSA")
        val key = keyFactory.generatePublic(keySpec)
        val signature = Signature.getInstance("MD5withRSA")
        signature.initVerify(key)
        signature.update(srcData.toByteArray(charset(charset)))
        return signature.verify(Base64.getDecoder().decode(sign.toByteArray(charset(charset))))
    }

    @JvmStatic
    fun main(args: Array<String>) {
        try {
            // 生成密钥对
            val keyPair: KeyPair = keyPair
            val privateKey = Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded())
            val publicKey = Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded())
            println("私钥:" + privateKey)
            println("公钥:" + publicKey)
            // RSA加密
//            String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCIDV8I1zpoazcFmv3VNtG/E9/QC14gDhBoW9Yq6o9UNLaOZC41yoGa7hjHqjuPOcmPJ61Wmv7i5UbB5BceGRl2i0pSyOzeAeYpoY5cNRStfQlXFlwV1Ig1P081rxBcCgkWZvhodsWp9yRdKOTTHUCj0FpgD94/2QhvqkxOaW9vAwIDAQAB";
//            String privateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAIgNXwjXOmhrNwWa/dU20b8T39ALXiAOEGhb1irqj1Q0to5kLjXKgZruGMeqO485yY8nrVaa/uLlRsHkFx4ZGXaLSlLI7N4B5imhjlw1FK19CVcWXBXUiDU/TzWvEFwKCRZm+Gh2xan3JF0o5NMdQKPQWmAP3j/ZCG+qTE5pb28DAgMBAAECgYBihmxgFp0xqRL7eDaCBWT3nwjhvJm5VPYE3RzHj32kWVgq3dmpErGw5OQFE/51xj908CLTKQOUhL0tBGTJYxvQci8y9c0Ajt+epQt8wfe1pGJ/ORFP8AAFttMUYRqvjWX+kE+nmnM9jYe2Zqnj7PeUbNFCjwdUEhEeRDflYubzQQJBAMgsI6mWFJ4X7uS+hIemme5hgPQDg8aeoubdSOFYb34Em3wZO7lPHa/Y0UNFsmgE2NAfVuoWx7TNg/A2EbPhx6sCQQCt/zHV6nZNFcR5vfI60L7vd3cNAR4IY55C/n9TXZpihLT3WjOyo1yGyWDiD1o8Y4HKik8/JHeNK3Lv176EbD4JAkEAhYLrRnGTzt6nuGpaex/kC9t850Rw4Elu3g06TxNtSeBI1Lz/2NmsM12qNfSGylpxQl+k2P3Ytf9dwRpPNGujgQJAThhXXuMQXALkH5xQp3Nf741YQt74gt1rgDhIH7vIemWD7+1tfMVz1w91y6EGaEplS+oOLZIJkrQor1vPKBKJOQJAFRSzcW4F+GznqXVZ43o9UnyyqnZbEnDX+lssZdg3q5bJhvk1vjUTRq+uBLfNZX/x9kfVXAm6zn1YNMFXwEsRSg==";
//            String data = "12312aawevrdgr312332312312312";
//            String encryptData = encrypt(data, getPublicKey(publicKey));
//            System.out.println("加密后内容:" + encryptData);
//            // RSA解密
//            String decryptData = decrypt(encryptData, getPrivateKey(privateKey));
//            System.out.println("解密后内容:" + decryptData);
//
//            // RSA签名
//            String sign = sign(data, getPrivateKey(privateKey));
//            // RSA验签
//            boolean result = verify(data, getPublicKey(publicKey), sign);
//            System.out.print("验签结果:" + result);
        } catch (e: Exception) {
            e.printStackTrace()
            print("加解密异常")
        }
    }
}