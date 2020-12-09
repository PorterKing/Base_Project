package com.porterking.dblibrary.assit;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.porterking.dblibrary.utils.Checker;
import com.porterking.dblibrary.utils.ClassUtil;
import com.porterking.dblibrary.utils.DLog;
import com.porterking.dblibrary.utils.DataUtil;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * author: porter_king
 * 用于对sql语句封装并执行
 */
public class SQLStatement {

    public static final short NONE = -1;

    /**
     * sql语句
     */
    private String sql;

    /**
     * 相关参数
     */
    private Object[] args;

    /**
     * sql语句执行者
     */
    private SQLiteStatement mStatement;

    public SQLStatement(){}

    public SQLStatement(String sql, Object[] args){
        this.sql = sql;
        this.args = args;
    }


    public String getSql() {
        return sql;
    }

    public SQLStatement setSql(String sql) {
        this.sql = sql;
        return this;
    }

    public Object[] getArgs() {
        return args;
    }

    public SQLStatement setArgs(Object[] args) {
        this.args = args;
        return this;
    }

    /**
     * 执行create,drop table 等
     * @param db
     * @return
     */
    public boolean execute(SQLiteDatabase db) throws CustomException {
        DLog.i(sql);

        try {
            mStatement = db.compileStatement(sql);
            if (args != null) {
                for (int i = 0; i < args.length; i++) {
                    bind(i + 1, args[i]);
                }
            }
            mStatement.execute();
        } catch (Exception e) {
            throw new CustomException(CustomException.CODE_3005, e.getMessage());
        } finally {
            reset();
        }

        return true;
    }


    /**
     * 执行insert into
     * @param db
     * @return
     * @throws IOException
     */
    public long execInsert(SQLiteDatabase db) throws IOException, CustomException {
        DLog.i(sql);

        mStatement = db.compileStatement(sql);
        if (!Checker.isEmpty(args)) {
            for (int i = 0; i < args.length; i++) {
                bind(i + 1, args[i]);
            }
        }

        long rowID = NONE;
        try {
            rowID = mStatement.executeInsert();
        } catch (Exception e) {
            throw new CustomException(CustomException.CODE_3006, e.getMessage());
        }
        finally {
            reset();
        }

        return rowID;
    }

    /**
     * 执行update/delete
     * @param db
     * @return
     * @throws IOException
     */
    public long execUpdateOrDelete(SQLiteDatabase db) throws IOException, CustomException {
        DLog.i(sql);

        mStatement = db.compileStatement(sql);
        if (!Checker.isEmpty(args)) {
            for (int i = 0; i < args.length; i++) {
                bind(i + 1, args[i]);
            }
        }

        long rowID = NONE;
        try {
            rowID = mStatement.executeUpdateDelete();
        } catch (Exception e) {
            throw new CustomException(CustomException.CODE_3007, e.getMessage());
        }
        finally {
            reset();
        }

        return rowID;
    }

    /**
     * 执行select
     * @param db
     * @param cls
     * @param callBack
     * @throws Exception
     */
    public void execQuery(SQLiteDatabase db, Class<?> cls, ResultCallBack callBack) throws Exception {
        DLog.i(sql);

        Cursor cursor = null;
        try {
            cursor = db.rawQuery(sql, null);
        }catch (Exception e) {
            throw new CustomException(CustomException.CODE_3008, e.getMessage());
        }
        finally {
            reset();
        }

        callBack.callback(db, cls, cursor);
        cursor.close();
    }

    /**
     * 查询前N条数据
     * @param db
     * @return
     */
    public long execQueryAmount(SQLiteDatabase db) throws CustomException {
        DLog.i(sql);

        Cursor cursor = null;
        long count = 0;
        try {
            cursor = db.rawQuery(sql, null);
            if (cursor.moveToFirst()){
                count = cursor.getInt(0);
            }

        } catch (Exception e) {
            throw new CustomException(CustomException.CODE_3008, e.getMessage());
        }
        finally {
            reset();
            cursor.close();
        }

        return count;
    }

    /**
     * 执行select
     * @param db
     * @param cls
     * @param <T>
     * @return
     * @throws Exception
     */
    public <T> List<T> execQuery(SQLiteDatabase db, Class<T> cls) throws Exception {
        final List<T> listModel = new ArrayList<>();

        execQuery(db, cls, new ResultCallBack<T>() {
            @Override
            public void callback(SQLiteDatabase sqLiteDatabase, Class<T> cls, Cursor cursor) throws CustomException {
                if (cursor != null && cursor.moveToFirst()){
                    do {
                        try {
                            T object = ClassUtil.newInstance(cls);
                            DataUtil.injectDataToObject(cursor, object, TableManager.getTableInfo(cls));
                            listModel.add(object);
                        }catch (Exception e){
                            throw new CustomException(CustomException.CODE_3008, e.getMessage());
                        }
                    }while (cursor.moveToNext());
                }
            }
        });

        return listModel;
    }

    /**
     * 给sql语句的占位符(?)按序绑定值
     * @param i
     * @param o
     * @throws IOException
     */
    protected void bind(int i, Object o) throws IOException {
        if (o == null) {
            mStatement.bindNull(i);
        } else if (o instanceof CharSequence || o instanceof Boolean || o instanceof Character) {
            mStatement.bindString(i, String.valueOf(o));
        } else if (o instanceof Float || o instanceof Double) {
            mStatement.bindDouble(i, ((Number) o).doubleValue());
        } else if (o instanceof Number) {
            mStatement.bindLong(i, ((Number) o).longValue());
        } else if (o instanceof Date) {
            mStatement.bindLong(i, ((Date) o).getTime());
        } else if (o instanceof byte[]) {
            mStatement.bindBlob(i, (byte[]) o);
        } else if (o instanceof Serializable) {
            mStatement.bindBlob(i, DataUtil.objectToByte(o));
        } else {
            mStatement.bindNull(i);
        }
    }


    /**
     * 重置状态
     */
    private void reset() {
        if (mStatement != null) {
            mStatement.close();
        }
        sql = null;
        args = null;
        mStatement = null;
    }


    /**
     * 查询数据的回调接口类
     * @param <T>
     */
    public interface ResultCallBack<T>{

        /**
         * 查询数据的回调方法
         * @param sqLiteDatabase
         * @param cls
         * @param cursor
         * @throws Exception
         */
        void callback(SQLiteDatabase sqLiteDatabase, Class<T> cls, Cursor cursor) throws Exception;
    }
}
