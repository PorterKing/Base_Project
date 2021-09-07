package com.porterking.commonlibrary.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Base64;

import com.porterking.commonlibrary.BCHelper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import static android.util.Base64.encode;

/**
 * Created by PorterKing on 2016/8/11.
 */
public class SPManager {

    private static final String spName = "Yin_Shi_SP";

    private static SharedPreferences sp;


    /**
     * 保存布尔值
     *
     * @param key
     * @param value
     */
    public static void saveBoolean(String key, boolean value) {
        saveBoolean(spName, key, value);
    }

    public static void saveBoolean(String spName, String key, boolean value) {
        if (sp == null)
            sp = BCHelper.getInstance().getContext().getSharedPreferences(spName, Context.MODE_PRIVATE);
        sp.edit().putBoolean(key, value).apply();
    }


    /**
     * 保存字符串
     *
     * @param key
     * @param value
     */
    public static void saveString(String key, String value) {
        saveString(spName, key, value);
    }

    public static void saveString(String spName, String key, String value) {
        if (sp == null)
            sp = BCHelper.getInstance().getContext().getSharedPreferences(spName, Context.MODE_PRIVATE);
        sp.edit().putString(key, value).apply();

    }

    public static void clear(Context context) {
        if (sp == null)
            sp = BCHelper.getInstance().getContext().getSharedPreferences(spName, Context.MODE_PRIVATE);
        sp.edit().clear().apply();
    }

    public static void clear(Context context, String spName) {
        if (sp == null)
            sp = BCHelper.getInstance().getContext().getSharedPreferences(spName, Context.MODE_PRIVATE);
        sp.edit().clear().apply();
    }

    /**
     * 保存long型
     *
     * @param key
     * @param value
     */
    public static void saveLong(String key, boolean value) {
        saveBoolean(spName, key, value);
    }

    public static void saveLong(String spName, String key, long value) {
        if (sp == null)
            sp = BCHelper.getInstance().getContext().getSharedPreferences(spName, Context.MODE_PRIVATE);
        sp.edit().putLong(key, value).apply();
    }

    /**
     * 保存int型
     *
     * @param key
     * @param value
     */

    public static void saveInt(String key, boolean value) {
        saveBoolean(spName, key, value);
    }

    public static void saveInt(String spName, String key, int value) {
        if (sp == null)
            sp = BCHelper.getInstance().getContext().getSharedPreferences(spName, Context.MODE_PRIVATE);
        sp.edit().putInt(key, value).apply();
    }

    /**
     * 保存float型
     *
     * @param key
     * @param value
     */

    public static void saveFloat(String key, boolean value) {
        saveBoolean(spName, key, value);
    }

    public static void saveFloat(String spName, String key, float value) {
        if (sp == null)
            sp = BCHelper.getInstance().getContext().getSharedPreferences(spName, Context.MODE_PRIVATE);
        sp.edit().putFloat(key, value).apply();
    }

    /**
     * 获取字符值
     *
     * @param key
     * @param defValue
     * @return
     */
    public static String getString(String key, String defValue) {
        return getString(spName, key, defValue);
    }

    public static String getString(String spName, String key, String defValue) {
        if (sp == null)
            sp = BCHelper.getInstance().getContext().getSharedPreferences(spName, Context.MODE_PRIVATE);
        return sp.getString(key, defValue);
    }

    /**
     * 获取int值
     *
     * @param key
     * @param defValue
     * @return
     */
    public static int getInt(String key, int defValue) {
        return getInt(spName, key, defValue);
    }

    public static int getInt(String spName, String key, int defValue) {
        if (sp == null)
            sp = BCHelper.getInstance().getContext().getSharedPreferences(spName, Context.MODE_PRIVATE);
        return sp.getInt(key, defValue);
    }

    /**
     * 删除
     *
     * @param key
     * @return
     */

    public static void remove(String key) {
        remove(spName, key);
    }

    public static void remove(String spName, String key) {
        if (sp == null)
            sp = BCHelper.getInstance().getContext().getSharedPreferences(spName, Context.MODE_PRIVATE);
        sp.edit().remove(key).apply();
    }

    /**
     * 获取long值
     *
     * @param key
     * @param defValue
     * @return
     */
    public static long getLong(String key, long defValue) {
        return getLong(spName, key, defValue);
    }

    public static long getLong(String spName, String key, long defValue) {
        if (sp == null)
            sp = BCHelper.getInstance().getContext().getSharedPreferences(spName, Context.MODE_PRIVATE);
        return sp.getLong(key, defValue);
    }

    /**
     * 获取float值
     *
     * @param key
     * @param defValue
     * @return
     */

    public static float getFloat(String key, float defValue) {
        return getFloat(spName, key, defValue);
    }

    public static float getFloat(String spName, String key, float defValue) {
        if (sp == null)
            sp = BCHelper.getInstance().getContext().getSharedPreferences(spName, Context.MODE_PRIVATE);
        return sp.getFloat(key, defValue);
    }

    /**
     * 获取布尔值
     *
     * @param key
     * @param defValue
     * @return
     */

    public static boolean getBoolean(String key, boolean defValue) {
        return getBoolean(spName, key, defValue);
    }

    public static boolean getBoolean(String spName, String key, boolean defValue) {
        if (sp == null)
            sp = BCHelper.getInstance().getContext().getSharedPreferences(spName, Context.MODE_PRIVATE);
        return sp.getBoolean(key, defValue);
    }

    /**
     * 将对象进行base64编码后保存到SharePref中
     *
     * @param key
     * @param object
     */
    public static void getBoolean(String key, Object object) {
        saveObj(spName, key, object);
    }

    public static void saveObj(String spName, String key, Object object) {
        if (sp == null)
            sp = BCHelper.getInstance().getContext().getSharedPreferences(spName, Context.MODE_PRIVATE);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            // 将对象的转为base64码
            String objBase64 = new String(encode(baos.toByteArray(), Base64.DEFAULT));

            sp.edit().putString(key, objBase64).apply();
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将SharePref中经过base64编码的对象读取出来
     *
     * @param key
     * @return
     */
    public static Object getObj(String key) {
        return getObj(spName, key);
    }

    public static Object getObj(String spName, String key) {
        if (sp == null)
            sp = BCHelper.getInstance().getContext().getSharedPreferences(spName, Context.MODE_PRIVATE);
        String objBase64 = sp.getString(key, null);
        if (TextUtils.isEmpty(objBase64))
            return null;

        // 对Base64格式的字符串进行解码
        byte[] base64Bytes = Base64.encode(objBase64.getBytes(), Base64.DEFAULT);

        ByteArrayInputStream bais = new ByteArrayInputStream(base64Bytes);

        ObjectInputStream ois;
        Object obj = null;
        try {
            ois = new ObjectInputStream(bais);
            obj = (Object) ois.readObject();
            ois.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }

}
