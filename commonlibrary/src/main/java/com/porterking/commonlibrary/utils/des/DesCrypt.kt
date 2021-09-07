package com.porterking.commonlibrary.utils.des

import android.util.Base64
import java.lang.Exception
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.DESKeySpec

/**
 * Created by Poterking on 2021/9/7.
 */
class DesCrypt(private var desKey: String) {

    var mDesKey: ByteArray = desKey.toByteArray()


    @Throws(Exception::class)
    fun desEncrypt(cleartext: ByteArray): ByteArray {
        var sr = SecureRandom()
        var rawKeyData = mDesKey
        var dks = DESKeySpec(rawKeyData)
        var keyFactory = SecretKeyFactory.getInstance("DES")
        var key = keyFactory.generateSecret(dks)
        var cipher = Cipher.getInstance("DES")
        cipher.init(Cipher.ENCRYPT_MODE, key, sr)
        var data = cleartext
        return cipher.doFinal(data)
    }

    @Throws(Exception::class)
    fun desDecrypt(cleartext: ByteArray): ByteArray {
        var sr = SecureRandom()
        var rawKeyData = mDesKey
        var dks = DESKeySpec(rawKeyData)
        var keyFactory = SecretKeyFactory.getInstance("DES")
        var key = keyFactory.generateSecret(dks)
        var cipher = Cipher.getInstance("DES")
        cipher.init(Cipher.DECRYPT_MODE, key, sr)
        var data = cleartext
        return cipher.doFinal(data)
    }

    @Throws(Exception::class)
    fun encrypt(input: String): String {
        return base64Encode(desEncrypt(input.toByteArray()))
    }
    @Throws(Exception::class)
    fun decrypt(input: String): String {
        var result = base64Decode(input)
        return String(desDecrypt(result))
    }


    fun base64Encode(s: ByteArray?): String {
        return Base64.encodeToString(s, Base64.DEFAULT)
    }

    fun base64Decode(s: String?): ByteArray {
        return Base64.decode(s, Base64.DEFAULT)
    }
}