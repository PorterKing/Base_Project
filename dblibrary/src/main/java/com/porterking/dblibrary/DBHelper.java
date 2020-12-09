package com.porterking.dblibrary;


import android.content.Context;
import android.database.Cursor;

import com.porterking.dblibrary.assit.DBConfig;
import com.porterking.dblibrary.db.DBOperation;
import com.porterking.dblibrary.frame.AddCallBack;
import com.porterking.dblibrary.frame.IDataBase;
import com.porterking.dblibrary.frame.ModifyCallBack;
import com.porterking.dblibrary.frame.Precondition;
import com.porterking.dblibrary.frame.QueryAmountCallBack;
import com.porterking.dblibrary.frame.QueryCallBack;

import java.util.List;

/**
 * author: porter_king
 * 外部调用统一接口
 */
public class DBHelper implements IDataBase {

    /**
     * 数据库相关信息
     */
    private volatile static DBConfig config;

    private static DBHelper instance;
    private IDataBase operation;

    private DBHelper(DBConfig config){
        operation = new DBOperation(config);
    }

//---------------------------------------------------------

    public static void initialize(Context context){
        initialize(context, false);
    }

    public static void initialize(Context context, boolean isDebug){
        initialize(context, isDebug, DBConfig.DB_DEFAULT_NAME);
    }

    public static void initialize(Context context, boolean isDebug, String dbName){
        initialize(context, isDebug, dbName, DBConfig.DB_DEFAULT_VERSION);
    }

    public static void initialize(Context context, boolean isDebug, String dbName, int version){
        config = new DBConfig(context, dbName, version, isDebug);
    }


    public static DBHelper getInstance(){
        if (instance == null){
            synchronized (DBHelper.class){
                if (instance == null){
                    instance = new DBHelper(config);
                }
            }
        }

        return instance;
    }

//--------------------------------------------------------------


    @Override
    public void insert(Object object, AddCallBack callBack) {
        operation.insert(object, callBack);
    }

    @Override
    public void update(Object object, List<String> columns, Precondition precondition, ModifyCallBack callBack) {
        operation.update(object, columns, precondition, callBack);
    }

    @Override
    public void delete(Class<?> cls, Precondition precondition, ModifyCallBack callBack) {
        operation.delete(cls, precondition, callBack);
    }

    @Override
    public <T> void query(Class<T> cls, Precondition precondition, QueryCallBack<T> callBack) {
        operation.query(cls, precondition, callBack);
    }

    @Override
    public void queryAmount(Class<?> cls, Precondition precondition, QueryAmountCallBack callBack) {
        operation.queryAmount(cls, precondition, callBack);
    }

    @Override
    public Cursor query(String sql, String[] selectionArgs) {
        return operation.query(sql, selectionArgs);
    }

    @Override
    public void close() {
        operation.close();
    }
}
