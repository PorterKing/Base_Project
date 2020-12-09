package com.porterking.dblibrary.assit;

import com.porterking.dblibrary.frame.PrecConstructor;
import com.porterking.dblibrary.frame.Precondition;

/**
 * author: porter_king
 * 提供sql操作的前提条件
 */
public class SQLPrecondition implements Precondition {

    private String whereSql;

    public SQLPrecondition(String sql){
        this.whereSql = sql;
    }

    public String getWhereSql() {
        return whereSql;
    }

    @Override
    public Precondition obtain(PrecConstructor constructor) {
        return null;
    }
}
