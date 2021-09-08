package com.porterking.commonlibrary.manager

import android.os.Parcelable
import com.porterking.commonlibrary.utils.des.DesCryptEngine
import com.tencent.mmkv.MMKV

/**
 * Created by Poterking on 2021/9/7.
 */
object MMKVManager {

    var hashMap = HashMap<String, MMKVEntity>()

    lateinit var defaultMMKV: MMKVEntity

    fun getMMKV(name:String) : MMKVEntity? {
        if(hashMap.containsKey(name)){
            return hashMap[name]
        }
        var mmkv = MMKVEntity(name)
        hashMap[name] = mmkv
        return  mmkv

    }
    fun getDefault(): MMKVEntity {
        if (defaultMMKV == null) {
            defaultMMKV = MMKVEntity()
        }
        return defaultMMKV
    }
}

class MMKVEntity(private val spName: String = "mmkv_common") : DesCryptEngine() {

    private val mmkv: MMKV? by lazy {
        MMKV.mmkvWithID(spName)
    }

    fun seString(name: String, value: String) {
        mmkv?.encode(encode(name), encode(value))
    }

    fun getString(name: String, default: String): String {
        val res: String? = mmkv?.decodeString(encode(name), encode(default))
        return decode(res)
    }


    fun setInt(name: String, value: Int) {
        mmkv?.encode(encode(name), value)
    }

    fun getInt(name: String, default: Int): Int {
        return mmkv?.decodeInt(encode(name), default) ?: default
    }

    fun setBoolean(name: String, value: Boolean) {
        mmkv?.encode(encode(name), value)
    }

    fun getBoolean(name: String, default: Boolean): Boolean {
        return mmkv?.decodeBool(encode(name), default) ?: default
    }

    fun setLong(name: String, value: Long) {
        mmkv?.encode(encode(name), value)
    }

    fun getLong(name: String, default: Long): Long {
        return mmkv?.decodeLong(encode(name), default) ?: default
    }


    fun setFloat(name: String, value: Float) {
        mmkv?.encode(encode(name), value)
    }

    fun getFloat(name: String, default: Float): Float {
        return mmkv?.decodeFloat(encode(name), default) ?: default
    }

    fun <T : Parcelable> setParcelable(name: String, value: T) {
        mmkv?.encode(encode(name), value)
    }


    fun <T : Parcelable> getParcelable(name: String, default: Class<T>): T? {
        return mmkv?.decodeParcelable(encode(name), default)
    }

    fun clearPreference() {
        mmkv?.clearAll()
    }

    fun clearPreference(key: String) {
        mmkv?.remove(encode(key))
    }

    fun contains(key: String): Boolean {
        return mmkv?.contains(encode(key)) ?: false
    }


}