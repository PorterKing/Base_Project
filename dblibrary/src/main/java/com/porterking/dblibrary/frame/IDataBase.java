package com.porterking.dblibrary.frame;

import android.database.Cursor;

import java.util.List;

/**
 * author: porter_king
 * 数据库操作统一接口
 */
public interface IDataBase {

    /**
     * 插入一条数据
     * @param object        插入的对象
     * @param callBack      回调信息
     */
    void insert(Object object, AddCallBack callBack);

    /**
     * 更新数据
     * @param object        更新的信息
     * @param columns       需要更新的列
     * @param precondition  操作条件
     * @param callBack      回调信息
     */
    void update(Object object, List<String> columns, Precondition precondition, ModifyCallBack callBack);

    /**
     * 删除数据
     * @param cls           需要删除的表
     * @param precondition  操作条件
     * @param callBack      回调信息
     * @return              返回删除的条数
     */
    void delete(Class<?> cls, Precondition precondition, ModifyCallBack callBack);

    /**
     * 查询数据库表数据
     * @param cls           需要查询的库对应的类信息
     * @param precondition  操作条件
     * @param callBack      回调信息
     * @param <T>
     */
    <T> void query(Class<T> cls, Precondition precondition, QueryCallBack<T> callBack);

    /**
     * 查询数据库表中数据数量
     * @param cls           需要查询的表
     * @param precondition  操作的前提条件
     */
    void queryAmount(Class<?> cls, Precondition precondition, QueryAmountCallBack callBack);

    /**
     * 通过sql语句查询
     * @param sql           sql语句
     * @param selectionArgs
     * @return
     */
    Cursor query(String sql, String[] selectionArgs);

    /**
     * 关闭数据库
     */
    void close();
}
