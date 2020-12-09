package com.porterking.dblibrary.assit;


import android.content.Context;

import com.porterking.dblibrary.utils.DLog;


/**
 * author: porter_king
 * 数据库相关信息
 */
public class DBConfig {
    public static final String DB_DEFAULT_NAME = "base_project.db";
    public static final int DB_DEFAULT_VERSION = 1;

    private Context context;

    /**
     * 数据库名称
     */
    private String name = DB_DEFAULT_NAME;

    /**
     * 版本号
     */
    private int version = DB_DEFAULT_VERSION;

    /**
     * 是否debug模式
     */
    private boolean isDebug = false;

    public DBConfig(Context context, String name, int version, boolean isDebug){
        this.context = context;
        this.name = name;
        this.version = version;
        this.isDebug = isDebug;

        DLog.IS_ON = isDebug;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public boolean isDebug() {
        return isDebug;
    }

    public void setDebug(boolean debug) {
        isDebug = debug;
    }
}
