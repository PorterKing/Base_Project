package com.porterking.commonlibrary.manager

import com.tencent.mmkv.MMKV

/**
 * Created by Poterking on 2021/9/7.
 */
object MMKVManager {

    var hashMap = HashMap<String, MMKV>()

    lateinit var defaultMMKV: MMKV

    fun getDefault ():MMKV{
        if(defaultMMKV == null){
            defaultMMKV = MMKV()
        }
        return defaultMMKV
    }
}

class MMKVEntity(val spName:String ="porterking_common"){

}