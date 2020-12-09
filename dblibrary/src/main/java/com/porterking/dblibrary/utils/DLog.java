package com.porterking.dblibrary.utils;

/**
 * author: porter_king
 * 日志工具
 */

public final class DLog {

    private static final String DEFAULT_TAG = "DLog";

    //日志输出开关
    public static boolean           IS_ON = false;

//------------------------------------------------------------

    public static int v(String msg) {
        return v(DEFAULT_TAG, msg);
    }

    public static int d(String msg) {
        return d(DEFAULT_TAG, msg);
    }

    public static int i(String msg) {
        return i(DEFAULT_TAG, msg);
    }

    public static int w(String msg) {
        return w(DEFAULT_TAG, msg);
    }

    public static int e(String msg) {
        return e(DEFAULT_TAG, msg);
    }

//--------------------------------------------------------------------------

    public static int v(String tag, String msg) {
        return IS_ON && msg != null ? android.util.Log.v(tag, msg) : -1;
    }

    public static int d(String tag, String msg) {
        return IS_ON && msg != null ? android.util.Log.d(tag, msg) : -1;
    }

    public static int i(String tag, String msg) {
        return IS_ON && msg != null ? android.util.Log.i(tag, msg) : -1;
    }

    public static int w(String tag, String msg) {
        return IS_ON && msg != null ? android.util.Log.w(tag, msg) : -1;
    }

    public static int e(String tag, String msg) {
        return IS_ON && msg != null ? android.util.Log.e(tag, msg) : -1;
    }
}
