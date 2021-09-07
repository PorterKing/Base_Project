package com.porterking.commonlibrary.utils.rsa;


import android.util.Base64;
import android.util.Log;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * RSA加密工具
 */
public class RSAEngineer {

    private static final String TAG = "RSAEngineer";
    private PublicKey publicKey;
    public static int MAX_LENGTH = 117;

    public RSAEngineer() {
    }

    public RSAEngineer(String keyName) throws InvalidKeySpecException, NoSuchAlgorithmException, NoSuchProviderException, IOException {
        //生成公钥
        byte[] szBuf = FileConfig.getFileBuffer(keyName);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        publicKey = keyFactory.generatePublic(new X509EncodedKeySpec(Base64.decode(szBuf, Base64.DEFAULT)));
    }

    /**
     * 加密
     *
     * @param data
     * @return
     */
    public String encryptData(byte[] data) throws BadPaddingException, IllegalBlockSizeException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        Log.d(TAG, Base64.encodeToString(cipher.doFinal(data), Base64.DEFAULT));
        return Base64.encodeToString(cipher.doFinal(data), Base64.DEFAULT);
    }

    /**
     * 加密
     *
     * @param data   要加密的内容
     * @param rsaKey 公钥
     * @return
     * @throws InvalidKeySpecException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchProviderException
     * @throws IOException
     * @throws IllegalBlockSizeException
     * @throws InvalidKeyException
     * @throws NoSuchPaddingException
     * @throws BadPaddingException
     */
    public String encryptData(byte[] data, byte[] rsaKey) throws InvalidKeySpecException, NoSuchAlgorithmException, NoSuchProviderException, IOException, IllegalBlockSizeException, InvalidKeyException, NoSuchPaddingException, BadPaddingException {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        publicKey = keyFactory.generatePublic(new X509EncodedKeySpec(Base64.decode(rsaKey, Base64.DEFAULT)));
        return encryptData(data);
    }

    public Cipher getCipher() throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher;
    }



}
