package com.porterking.dblibrary.frame;


import java.util.List;


/**
 * author: porter_king
 * 数据库操作条件构造接口
 */
public interface PrecConstructor {

    /**
     * where 列 = 值
     * @param column        列
     * @param value         值
     * @return
     */
    PrecConstructor buildWhereEquals(String column, Number value);

    /**
     * where 列 != 值
     * @param column        列
     * @param value         值
     * @return
     */
    PrecConstructor buildWhereNorEquals(String column, Number value);

    /**
     * where 列 = 值
     * @param column        列
     * @param value         值
     * @return
     */
    PrecConstructor buildWhereEquals(String column, String value);

    /**
     * where 列 != 值
     * @param column        列
     * @param value         值
     * @return
     */
    PrecConstructor buildWhereNorEquals(String column, String value);

    /**
     * where 列 > 值
     * @param column        列
     * @param value         值
     * @return
     */
    PrecConstructor buildWhereGreater(String column, Number value);

    /**
     * where 列 < 值
     * @param column        列
     * @param value         值
     * @return
     */
    PrecConstructor buildWhereLess(String column, Number value);

    /**
     * where in     包含查询
     * @param column        列
     * @param value         集合
     * @return
     */
    PrecConstructor buildWhereIn(String column, List value);

    /**
     * where like   模糊查询
     * @param column        列
     * @param value         值
     * @return
     */
    PrecConstructor buildWhereFuzzy(String column, String value);

    /**
     * limit        查询前amount条数据
     * @param amount        数量
     * @return
     */
    PrecConstructor buildWhereAmount(long amount);

    /**
     * and          与(多重条件)
     * @return
     */
    PrecConstructor and();

    /**
     * or           或(多重条件)
     * @return
     */
    PrecConstructor or();

    /**
     * 倒序 或者 正序
     * @return
     */
    PrecConstructor orderBy(String column, int by);

    /**
     * 返回构造好的条件
     * @return
     */
    Precondition precondition();


}
