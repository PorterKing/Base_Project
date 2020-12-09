package com.yinshifinance.ths.utils

import android.app.Activity
import android.app.Application

/**
 * Created by jinchangbo on 20-11-5.
 */
class ScreenAdaptUtil {
    companion object {
        fun setCustomDensity(activity: Activity, application: Application){
            val applicationMetrics = application.resources.displayMetrics
            var targetDensity:Float = (applicationMetrics.widthPixels/360).toFloat()
            var targetDensityDpi = 160*targetDensity

            applicationMetrics.density = targetDensity
            applicationMetrics.scaledDensity = targetDensity
            applicationMetrics.densityDpi = targetDensityDpi.toInt()

            val activityMetrics = activity.resources.displayMetrics
            activityMetrics.density = targetDensity
            activityMetrics.scaledDensity = targetDensity
            activityMetrics.densityDpi = targetDensityDpi.toInt()
        }
    }
}