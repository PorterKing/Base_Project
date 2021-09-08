package com.porterking.commonlibrary.utils.des

import android.text.TextUtils
import java.lang.Exception
import kotlin.reflect.KProperty

/**
 * Created by Poterking on 2021/9/7.
 */
open class DesCryptEngine {
    private val descCrypt :DesCrypt? by DesDelegate()

    fun encode(content:String?):String{
        var res = ""
        if(TextUtils.isEmpty(content)){
            return res
        }
        try {
            res = descCrypt!!.encrypt(content!!)
        }catch (e:Exception){
            e.printStackTrace()
        }
        return res
    }

    fun decode(content:String?):String{
        var res = ""
        if(TextUtils.isEmpty(content)){
            return res
        }
        try {
            res = descCrypt!!.decrypt(content!!)
        }catch (e:Exception){
            e.printStackTrace()
        }
        return res
    }
}



class DesDelegate {
    operator  fun getValue(thisRef:DesCryptEngine,property:KProperty<*>):DesCrypt{
        return DesCrypt("porterking")
    }
}