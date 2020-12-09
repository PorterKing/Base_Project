package com.porterking.dblibrary.model;

import com.porterking.dblibrary.annotations.ColumnName;
import com.porterking.dblibrary.annotations.PrimaryKey;

import java.util.List;

/**
 * author: porter_king
 * 数据库中各个表对应的信息
 */
public class SQLTableInfo {

    @PrimaryKey
    public long id;

    @ColumnName("type")
    public String type;

    @ColumnName("name")
    public String name;

    @ColumnName("tbl_name")
    public String tbl_name;

    @ColumnName("rootpage")
    public long rootpage;

    @ColumnName("sql")
    public String sql;

    public List<String> columns;


    public String getMessage() {
        return "SQLiteTable{" +
                "type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", tbl_name='" + tbl_name + '\'' +
                ", rootpage=" + rootpage +
                ", sql='" + sql + '\'' +
                ", columns=" + columns +
                '}';
    }
}
