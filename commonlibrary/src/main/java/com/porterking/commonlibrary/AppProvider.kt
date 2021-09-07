package com.porterking.commonlibrary

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.startup.Initializer
import java.lang.ref.WeakReference

/**
 * Created by Poterking on 2021/9/7.
 */

class AppInitializer : Initializer<AppProvider> {
    override fun create(context: Context): AppProvider {
        var application = context.applicationContext as Application
        application.unregisterActivityLifecycleCallbacks(AppLifecycleCallbacks)
        application.registerActivityLifecycleCallbacks(AppLifecycleCallbacks)
        AppProvider.context = application
        return  AppProvider
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
       return emptyList()
    }

}
object AppProvider {
    lateinit var context: Application
    lateinit var currentActivity: WeakReference<Activity>
}

object  AppLifecycleCallbacks:Application.ActivityLifecycleCallbacks {
    override fun onActivityPaused(activity: Activity) {
    }

    override fun onActivityStarted(activity: Activity) {
    }

    override fun onActivityDestroyed(activity: Activity) {
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
    }

    override fun onActivityStopped(activity: Activity) {
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
    }

    override fun onActivityResumed(activity: Activity) {
        AppProvider.currentActivity = WeakReference(activity)
    }

}