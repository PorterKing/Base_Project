package com.jcb.base_corelibrary;

import android.content.Context;

/**
 * Created by jinchangbo on 20-11-26.
 */
public class BCHelper {

    private Context mContext;

    private boolean mIsDebug;

    private static final BCHelper ourInstance = new BCHelper();

    public static BCHelper getInstance() {
        return ourInstance;
    }

    private BCHelper() {
    }

    private void initialize(Context context, boolean isDebug) {
        mContext = context;
        mIsDebug = isDebug;
    }

    public Context getContext() {
        return mContext;
    }

    public boolean isDebug() {
        return mIsDebug;
    }
}
