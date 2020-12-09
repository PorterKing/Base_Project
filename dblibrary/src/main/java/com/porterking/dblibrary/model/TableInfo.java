package com.porterking.dblibrary.model;

import java.util.LinkedHashMap;

/**
 * author: porter_king
 * 将javabean映射成该对象以表示数据库表信息
 */
public class TableInfo {

    /**
     *  表名
     */
    private String tableName;

    /**
     * 主键
     */
    private String primaryKey;

    /**
     *  表字段信息
     */
    private LinkedHashMap<String, FieldInfo> fieldInfoMap;


    public TableInfo(String tableName){
        this.tableName = tableName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(String primaryKey) {
        this.primaryKey = primaryKey;
    }

    public LinkedHashMap<String, FieldInfo> getFieldInfoMap() {
        return fieldInfoMap;
    }

    public void setFieldInfoMap(LinkedHashMap<String, FieldInfo> fieldInfoMap) {
        this.fieldInfoMap = fieldInfoMap;
    }
}
