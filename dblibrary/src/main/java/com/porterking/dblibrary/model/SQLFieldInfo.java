package com.porterking.dblibrary.model;


import com.porterking.dblibrary.annotations.ColumnName;
import com.porterking.dblibrary.annotations.PrimaryKey;

/**
 * author: porter_king
 * 数据库中表各个字段的信息
 */
public class SQLFieldInfo {

    @PrimaryKey
    public long id;
    @ColumnName("cid")
    public long cid;
    @ColumnName("name")
    public String name;
    @ColumnName("type")
    public String type;
    @ColumnName("notnull")
    public short notnull;
    @ColumnName("dflt_value")
    public String dflt_value;
    @ColumnName("pk")
    public short pk;

    public String getMessage() {
        return "Column [cid=" + cid + ", name=" + name + ", type=" + type + ", notnull=" + notnull + ", dflt_value="
                + dflt_value + ", pk=" + pk + "]";
    }
}
