package com.porterking.dblibrary.assit;

import android.text.TextUtils;

import com.porterking.dblibrary.frame.PrecConstructor;
import com.porterking.dblibrary.frame.Precondition;

import java.util.List;

/**
 * author: porter_king
 * 具体实现的操作条件构造类
 */
public class SQLPrecConstructor implements PrecConstructor {

    private StringBuffer sqlBuffer;

    private boolean isContinue = true;

    @Override
    public PrecConstructor buildWhereEquals(String column, Number value) {
        initSqlBuffer();
        if (isContinue)
            sqlBuffer.append(SQLBuilder.buildWhereEquals(column, value));
        return this;
    }

    @Override
    public PrecConstructor buildWhereNorEquals(String column, Number value) {
        initSqlBuffer();
        if (isContinue)
            sqlBuffer.append(SQLBuilder.buildWhereNorEquals(column, value));
        return this;
    }


    @Override
    public PrecConstructor buildWhereEquals(String column, String value) {
        initSqlBuffer();
        if (isContinue)
            sqlBuffer.append(SQLBuilder.buildWhereEquals(column, value));
        return this;
    }

    @Override
    public PrecConstructor buildWhereNorEquals(String column, String value) {
        initSqlBuffer();
        if (isContinue)
            sqlBuffer.append(SQLBuilder.buildWhereNorEquals(column, value));
        return this;
    }

    @Override
    public PrecConstructor buildWhereGreater(String column, Number value) {
        initSqlBuffer();
        if (isContinue)
            sqlBuffer.append(SQLBuilder.buildWhereGreater(column, value));
        return this;
    }

    @Override
    public PrecConstructor buildWhereLess(String column, Number value) {
        initSqlBuffer();
        if (isContinue)
            sqlBuffer.append(SQLBuilder.buildWhereLess(column, value));
        return this;
    }

    @Override
    public PrecConstructor buildWhereIn(String column, List value) {
        initSqlBuffer();
        if (isContinue)
            sqlBuffer.append(SQLBuilder.buildWhereIn(column, value));
        return this;
    }

    @Override
    public PrecConstructor buildWhereFuzzy(String column, String value) {
        initSqlBuffer();
        if (isContinue)
            sqlBuffer.append(SQLBuilder.buildWhereFuzzy(column, value));
        return this;
    }

    @Override
    public PrecConstructor buildWhereAmount(long amount) {
        if (sqlBuffer == null) {
            sqlBuffer = new StringBuffer();
        }

        if (isContinue) {
            sqlBuffer.append(SQLBuilder.buildWhereAmount(amount));
            isContinue = false;
        }
        return this;
    }

    @Override
    public PrecConstructor and() {
        if (checkSqlOrAndBuffer() && isContinue) {
            sqlBuffer.append(SQLBuilder.and());
        }
        return this;
    }

    @Override
    public PrecConstructor or() {
        if (checkSqlOrAndBuffer() && isContinue) {
            sqlBuffer.append(SQLBuilder.or());
        }
        return this;
    }

    @Override
    public PrecConstructor orderBy(String column, int by) {
        if (checkSqlOrderdBuffer() && isContinue) {
            sqlBuffer.append(SQLBuilder.buildOrder(column, by));
        }
        return this;
    }

    @Override
    public Precondition precondition() {
        if (sqlBuffer != null) {
            SQLPrecondition sqlPrecondition = new SQLPrecondition(sqlBuffer.toString());
            sqlBuffer = null;
            return sqlPrecondition;
        }

        return new SQLPrecondition("");
    }

    private void initSqlBuffer() {
        if (sqlBuffer == null) {
            sqlBuffer = new StringBuffer();
            sqlBuffer.append(SQLBuilder.where());
        }
    }

    /**
     * 检查是否符合操作and or的条件
     *
     * @return
     */
    private boolean checkSqlOrAndBuffer() {
        if (sqlBuffer == null) {
            return false;
        }

        String sql = sqlBuffer.toString();
        if (TextUtils.isEmpty(sql)) {
            return false;
        }

        String checkStr = sql.substring(sql.length() - 7, sql.length());
        if (checkStr.contains(SQLBuilder.and()) || checkStr.contains(SQLBuilder.or())) {
            return false;
        }

        return true;
    }


    /**
     * 检查order 操作条件
     * @return
     */
    private boolean checkSqlOrderdBuffer() {
        if (sqlBuffer == null) {
            return false;
        }

        String sql = sqlBuffer.toString();
        if (TextUtils.isEmpty(sql)) {
            return false;
        }
        if(sql.contains(SQLBuilder.order())){
            return  false;
        }

        return true;
    }
}
