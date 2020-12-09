package com.porterking.netlibrary;

import android.content.Context;

/**
 * Created by jinchangbo on 20-11-26.
 */
public class NetHelper {

    private Context mContext;

    private boolean mIsDebug;

    private static final NetHelper ourInstance = new NetHelper();

    public static NetHelper getInstance() {
        return ourInstance;
    }

    private NetHelper() {
    }

    private void initialize(Context context, boolean isDebug) {
        mContext = context;
        mIsDebug = isDebug;
    }

    public Context getContext() {
        return mContext;
    }

    public boolean isIsDebug() {
        return mIsDebug;
    }
}
