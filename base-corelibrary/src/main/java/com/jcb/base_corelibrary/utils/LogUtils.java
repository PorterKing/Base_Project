package com.jcb.base_corelibrary.utils;

import com.jcb.base_corelibrary.BCHelper;

/**
 * Created by Poterking on 19-7-12.
 */
public class LogUtils {
    private static final boolean LOG_ON = BCHelper.getInstance().isDebug(); //默认关闭

    private static final String TAG = "Log-Base";

    public static void i(String msg) {
        i(TAG, msg);
    }

    public static void i(String tag, String msg) {
        if (msg == null) {
            return;
        }
        if (LOG_ON) {
            android.util.Log.i(tag, msg);
        }
    }

    public static void d(String msg) {
        d(TAG, msg);
    }
    public static void d(String tag, String msg) {
        if (msg == null) {
            return;
        }
        if (LOG_ON) {
            android.util.Log.d(tag, msg);
        }
    }

    public static void e(String msg) {
        e(TAG, msg);
    }
    public static void e(String tag, String msg) {
        if (msg == null) {
            return;
        }
        if (LOG_ON) {
            android.util.Log.e(tag, msg);
        }
    }

    public static void v(String msg) {
        v(TAG, msg);
    }
    public static void v(String tag, String msg) {
        if (msg == null) {
            return;
        }
        if (LOG_ON) {
            android.util.Log.v(tag, msg);
        }
    }

    public static void w(String msg) {
        w(TAG, msg);
    }
    public static void w(String tag, String msg) {
        if (msg == null) {
            return;
        }
        if (LOG_ON) {
            android.util.Log.w(tag, msg);
        }
    }
}
