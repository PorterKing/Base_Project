package com.porterking.dblibrary.db;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.porterking.dblibrary.assit.CustomException;
import com.porterking.dblibrary.assit.DBConfig;
import com.porterking.dblibrary.assit.SQLBuilder;
import com.porterking.dblibrary.assit.SQLPrecConstructor;
import com.porterking.dblibrary.assit.TableManager;
import com.porterking.dblibrary.frame.AddCallBack;
import com.porterking.dblibrary.frame.DBCallBack;
import com.porterking.dblibrary.frame.IDataBase;
import com.porterking.dblibrary.frame.ModifyCallBack;
import com.porterking.dblibrary.frame.Precondition;
import com.porterking.dblibrary.frame.QueryAmountCallBack;
import com.porterking.dblibrary.frame.QueryCallBack;
import com.porterking.dblibrary.utils.DLog;

import java.io.IOException;
import java.util.List;

/**
 * author: porter_king
 * 具体实现的数据库操作类
 */
public class DBOperation implements IDataBase {

    private static final int NONE = -1;

    private Context context;

    /**
     * 数据库名称
     */
    private String name;

    /**
     * 数据库版本号
     */
    private int version;

    /**
     * 数据库操作
     */
    private WCDBOpenHelper openHelper;

    /**
     * sql条件构造
     */
    private SQLPrecConstructor constructor;

    public DBOperation(DBConfig config){
        this.context = config.getContext();
        this.name = config.getName();
        this.version = config.getVersion();

        openHelper = new WCDBOpenHelper(context);
        constructor = new SQLPrecConstructor();
        TableManager.initAllTablesFromSQLite(openHelper.getWritableDatabase());
    }

//-------------------------------------------------------------------------------------------------

    private class WCDBOpenHelper extends SQLiteOpenHelper {

        public WCDBOpenHelper(Context context) {
            super(context, name, null, version);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {

        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        }
    }

//-------------------------------------------------------------------------------------------------

    @Override
    public void insert(Object object, AddCallBack callBack) {
        DLog.i("插入数据----------------------->");
        if (!TableManager.init && callBack != null){
            DLog.e("数据库初始化失败,无法继续操作------------->" + TableManager.msg);
            callBack.callback(NONE, CustomException.CODE_3004, TableManager.msg);
            return;
        }

        long count = NONE;
        int code = DBCallBack.SUCCESS_CODE;
        String msg = DBCallBack.SUCCESS_MSG;
        try {
            DLog.i("检查插入数据状态----------->");
            TableManager.checkOrCreateTable(openHelper.getWritableDatabase(), object.getClass());
            DLog.i("开始执行插入操作----------->");
            count = SQLBuilder.buildInsertSql(object).execInsert(openHelper.getWritableDatabase());
        } catch (CustomException e) {
            code = e.getCode();
            msg = e.getMsg();
            DLog.e("插入失败-------->" + msg);
        } catch (IOException e) {
            code = CustomException.CODE_3000;
            msg = e.getMessage();
            DLog.e("插入失败-------->" + msg);
        }
        if (callBack != null){
            callBack.callback(count, code, msg);
        }

    }

    @Override
    public void update(Object object, List<String> columns, Precondition precondition, ModifyCallBack callBack) {
        DLog.i("更新数据----------------------->");
        if (!TableManager.init && callBack != null){
            DLog.e("数据库初始化失败,无法继续操作------------->" + TableManager.msg);
            callBack.callback(NONE, CustomException.CODE_3004, TableManager.msg);
            return;
        }

        Precondition condition = null;
        if (precondition != null){
            condition = precondition.obtain(constructor);
        }

        long count = NONE;
        int code = DBCallBack.SUCCESS_CODE;
        String msg = DBCallBack.SUCCESS_MSG;
        try {
            DLog.i("检查更新数据状态----------->");
            TableManager.checkOrCreateTable(openHelper.getWritableDatabase(), object.getClass());
            DLog.i("开始执行更新操作----------->");
            count = SQLBuilder.buildUpdateSql(object, columns, condition).execUpdateOrDelete(openHelper.getWritableDatabase());
        }catch (CustomException e){
            code = e.getCode();
            msg = e.getMsg();
            DLog.e("更新失败-------->" + msg);
        } catch (IOException e) {
            code = CustomException.CODE_3000;
            msg = e.getMessage();
            DLog.e("更新失败-------->" + msg);
        }

        if (callBack != null){
            callBack.callback(count, code, msg);
        }

    }

    @Override
    public void delete(Class<?> cls, Precondition precondition, ModifyCallBack callBack) {
        DLog.i("删除数据----------------------->");
        if (!TableManager.init && callBack != null){
            DLog.e("数据库初始化失败,无法继续操作------------->" + TableManager.msg);
            callBack.callback(NONE, CustomException.CODE_3004, TableManager.msg);
        }

        Precondition condition = null;
        if (precondition != null){
            condition = precondition.obtain(constructor);
        }

        long count = NONE;
        int code = DBCallBack.SUCCESS_CODE;
        String msg = DBCallBack.SUCCESS_MSG;
        try {
            DLog.i("检查删除数据状态----------->");
            TableManager.checkOrCreateTable(openHelper.getWritableDatabase(), cls);
            DLog.i("开始执行删除操作----------->");
            count = SQLBuilder.buildDeleteSql(cls, condition).execUpdateOrDelete(openHelper.getWritableDatabase());
        } catch (CustomException e){
            code = e.getCode();
            msg = e.getMsg();
            DLog.e("删除失败-------->" + msg);
        } catch (IOException e) {
            code = CustomException.CODE_3000;
            msg = e.getMessage();
            DLog.e("删除失败-------->" + msg);
        }

        if (callBack != null){
            callBack.callback(count, code, msg);
        }

    }

    @Override
    public <T> void query(Class<T> cls, Precondition precondition, QueryCallBack<T> callBack) {
        DLog.i("查询数据----------------------->");
        if (!TableManager.init && callBack != null){
            DLog.e("数据库初始化失败,无法继续操作------------->" + TableManager.msg);
            callBack.callback(null, CustomException.CODE_3004, TableManager.msg);
        }

        List<T> result = null;
        Precondition condition = null;
        if (precondition != null){
            condition = precondition.obtain(constructor);
        }

        int code = DBCallBack.SUCCESS_CODE;
        String msg = DBCallBack.SUCCESS_MSG;
        try {
            DLog.i("检查查询数据状态----------->");
            TableManager.checkOrCreateTable(openHelper.getWritableDatabase(), cls);
            DLog.i("开始执行查询操作----------->");
            result = SQLBuilder.buildSelectSql(cls, condition).execQuery(openHelper.getWritableDatabase(), cls);
        } catch (CustomException e){
            code = e.getCode();
            msg = e.getMsg();
            DLog.e("查询失败-------->" + msg);
        } catch (Exception e) {
            code = CustomException.CODE_3000;
            msg = e.getMessage();
            DLog.e("查询失败-------->" + msg);
        }

        if (callBack != null){
            callBack.callback(result, code, msg);
        }

    }

    @Override
    public void queryAmount(Class<?> cls, Precondition precondition, QueryAmountCallBack callBack) {
        DLog.i("查询数据----------------------->");
        if (!TableManager.init && callBack != null){
            DLog.e("数据库初始化失败,无法继续操作------------->" + TableManager.msg);
            callBack.callback(NONE, CustomException.CODE_3004, TableManager.msg);
        }

        long count = NONE;
        int code = DBCallBack.SUCCESS_CODE;
        String msg = DBCallBack.SUCCESS_MSG;

        Precondition condition = null;
        if (precondition != null){
            condition = precondition.obtain(constructor);
        }
        try {
            DLog.i("检查查询数据状态----------->");
            TableManager.checkOrCreateTable(openHelper.getWritableDatabase(), cls);
            DLog.i("开始执行查询操作----------->");
            count = SQLBuilder.buildSelectAmountSql(cls, condition).execQueryAmount(openHelper.getWritableDatabase());
        } catch (CustomException e){
            code = e.getCode();
            msg = e.getMsg();
            DLog.e("查询失败-------->" + msg);
        } catch (Exception e) {
            code = CustomException.CODE_3000;
            msg = e.getMessage();
            DLog.e("查询失败-------->" + msg);
        }

        if (callBack != null){
            callBack.callback(count, code, msg);
        }

    }

    @Override
    public Cursor query(String sql, String[] selectionArgs) {
        return openHelper.getWritableDatabase().rawQuery(sql, selectionArgs);
    }

    @Override
    public void close() {
        DLog.i("--------------关闭数据库------------");
        openHelper.close();
    }
}
