package com.porterking.dblibrary.assit;


import com.porterking.dblibrary.frame.Precondition;
import com.porterking.dblibrary.model.FieldInfo;
import com.porterking.dblibrary.model.TableInfo;
import com.porterking.dblibrary.utils.Checker;
import com.porterking.dblibrary.utils.FieldUtil;
import com.porterking.dblibrary.utils.TypeCastUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * author: porter_king
 * 用于sql构建
 */
public class SQLBuilder {

    private static final String QUERY_TABLE_ALL     = "select * from sqlite_master where type='table' order by name";
    private static final String QUERY_COLUMN_ALL    = "pragma table_info(";
    private static final String CREATE              = "create table ";
    private static final String ALTER               = "alter table ";
    private static final String ALTER_ADD           = "add ";
    private static final String INSERT              = "insert into ";
    private static final String UPDATE              = "update ";
    private static final String SET                 = "set ";
    private static final String SELECT              = "select * from ";
    private static final String SELECT_COUNT        = "select count(*) from ";
    private static final String DELETE              = "delete from ";
    private static final String WHERE               = "where ";
    private static final String LEFT_PART           = " ( ";
    private static final String RIGHT_PART          = " ) ";
    private static final String COMMA               = ", ";
    private static final String SPACE               = " ";
    private static final String HOLDER              = "?";
    private static final String COMMA_HOLDER        = ",?";
    private static final String PRIMARY_TYPE        = " integer primary key autoincrement ";
    private static final String VALUES              = "values ";
    private static final String LIMIT               = "limit ";
    private static final String EQUALS              = " = ";
    private static final String NOR_EQUALS          = " != ";
    private static final String GREATER             = " > ";
    private static final String LESS                = " < ";
    private static final String IN                  = " in ";
    private static final String LIKE                = " like ";
    private static final String QUOTES              = "\'";
    private static final String PERCENT             = "%";
    private static final String ZERO                = "0";
    private static final String DEFAULT             = "default";
    private static final String AND                 = " and ";
    private static final String OR                  = " or ";
    private static final String ORDER               = " order by ";
    private static final String ASCSTR              = " asc ";
    private static final String DESCSTR             = " desc ";

    public static final int ASC                     = 0;
    public static final int DESC                    = 1;


    /**
     * 构建create
     * @param cls
     * @return
     */
    public static SQLStatement buildCreateTable(Class<?> cls) throws CustomException{
        TableInfo tableInfo = TableManager.getTableInfo(cls);

        StringBuffer stringBuffer = new StringBuffer();
        Map<String, FieldInfo> fieldMap = tableInfo.getFieldInfoMap();

        stringBuffer.append(CREATE).append(tableInfo.getTableName())
                .append(LEFT_PART).append(tableInfo.getPrimaryKey()).append(PRIMARY_TYPE);

        for (Map.Entry<String, FieldInfo> entry : fieldMap.entrySet()){
            if (entry.getKey().equals(tableInfo.getPrimaryKey())){//去除主键
                continue;
            }
            stringBuffer.append(COMMA).append(entry.getKey())
                    .append(SPACE)
                    .append(TypeCastUtil.getSQLDataType(entry.getValue().getClassType()));
            if (entry.getValue().getDefaultValue() != null){
                stringBuffer.append(DEFAULT)
                        .append(SPACE)
                        .append(LEFT_PART)
                        .append(QUOTES)
                        .append(entry.getValue().getDefaultValue())
                        .append(QUOTES)
                        .append(RIGHT_PART);
            }
        }
        stringBuffer.append(RIGHT_PART);

        return new SQLStatement(stringBuffer.toString(), null);
    }

    /**
     * 构建alter
     * @param cls
     * @param fieldInfo
     * @return
     */
    public static SQLStatement buildAlterTable(Class<?> cls, FieldInfo fieldInfo) throws CustomException{
        TableInfo tableInfo = TableManager.getTableInfo(cls);

        StringBuffer sqlBuffer = new StringBuffer();
        sqlBuffer.append(ALTER)
                .append(tableInfo.getTableName())
                .append(SPACE)
                .append(ALTER_ADD)
                .append(fieldInfo.getName())
                .append(SPACE)
                .append(TypeCastUtil.getSQLDataType(fieldInfo.getClassType()));

        return new SQLStatement(sqlBuffer.toString(), null);
    }


    /**
     * 构建Insert
     * @param tableModel
     * @return
     */
    public static SQLStatement buildInsertSql(Object tableModel) throws CustomException{
        SQLStatement statement = new SQLStatement();

        TableInfo tableInfo = TableManager.getTableInfo(tableModel);

        String tableName = tableInfo.getTableName();
        Map<String, FieldInfo> fieldInfoMap = tableInfo.getFieldInfoMap();

        if (fieldInfoMap.isEmpty()){
            return null;
        }

        StringBuffer sqlBuffer = new StringBuffer();
        sqlBuffer.append(INSERT).append(tableName).append(SPACE).append(LEFT_PART)
                .append(tableInfo.getPrimaryKey());
        StringBuffer valueBuffer = new StringBuffer();
        valueBuffer.append(LEFT_PART).append(HOLDER);

        ArrayList<Object> arrayList = new ArrayList<>();

        arrayList.add(null);
        for (Map.Entry<String, FieldInfo> entry : tableInfo.getFieldInfoMap().entrySet()){
            if (tableInfo.getPrimaryKey().equals(entry.getKey())){//去除主键
                continue;
            }

            try {
                if (FieldUtil.isNULL(entry.getValue(), tableModel)){
                    continue;
                }
                sqlBuffer.append(COMMA).append(entry.getKey());
                arrayList.add(FieldUtil.get(entry.getValue().getField(), tableModel));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            valueBuffer.append(COMMA_HOLDER);
        }
        Object[] args = new Object[arrayList.size()];
        arrayList.toArray(args);
        sqlBuffer.append(RIGHT_PART)
                .append(VALUES)
                .append(valueBuffer)
                .append(RIGHT_PART);

        return statement.setSql(sqlBuffer.toString()).setArgs(args);
    }

    /**
     * 构建update
     * @param tableModel
     * @param columns
     * @param precondition
     * @return
     */
    public static SQLStatement buildUpdateSql(Object tableModel, List<String> columns, Precondition precondition) throws CustomException{
        SQLStatement statement = new SQLStatement();

        TableInfo tableInfo = TableManager.getTableInfo(tableModel);
        if (tableInfo == null){
            return null;
        }

        String tableName = tableInfo.getTableName();
        Map<String, FieldInfo> fieldInfoMap = tableInfo.getFieldInfoMap();

        if (fieldInfoMap.isEmpty()){
            return null;
        }

        StringBuffer sqlBuffer = new StringBuffer();
        sqlBuffer.append(UPDATE).append(tableName).append(SPACE).append(SET);

        Object[] args = new Object[columns.size()];
        boolean flag = false;
        int i = 0;
        for (String column : columns){
            FieldInfo fieldInfo = fieldInfoMap.get(column);
            if (fieldInfo == null)
                continue;

            if (flag){
                sqlBuffer.append(COMMA).append(column).append(EQUALS).append(HOLDER);;
            }else {
                sqlBuffer.append(column).append(EQUALS).append(HOLDER);
                flag = true;
            }
            try {
                args[i++] = FieldUtil.get(fieldInfo.getField(), tableModel);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        if (precondition != null && precondition instanceof SQLPrecondition){
            String whereSql = ((SQLPrecondition) precondition).getWhereSql();
            sqlBuffer.append(SPACE).append(whereSql);
        }

        return new SQLStatement(sqlBuffer.toString(), args);
    }

    /**
     * 构建delete
     * @param cls
     * @param precondition
     * @return
     */
    public static SQLStatement buildDeleteSql(Class<?> cls, Precondition precondition) throws CustomException{
        SQLStatement statement = new SQLStatement();

        TableInfo tableInfo = TableManager.getTableInfo(cls);
        String tableName = tableInfo.getTableName();
        StringBuffer sqlBuffer = new StringBuffer();
        sqlBuffer.append(DELETE).append(tableName);

        if (precondition != null && precondition instanceof SQLPrecondition){
            String whereSql = ((SQLPrecondition) precondition).getWhereSql();
            sqlBuffer.append(SPACE).append(whereSql);
        }

        statement.setSql(sqlBuffer.toString());
        return statement;
    }

    /**
     * 构建select
     * @param cls           表
     * @param precondition  操作的前提条件
     * @return
     */
    public static SQLStatement buildSelectSql(Class<?> cls, Precondition precondition) throws CustomException{
        SQLStatement statement = new SQLStatement();

        TableInfo tableInfo = TableManager.getTableInfo(cls);
        String tableName = tableInfo.getTableName();
        StringBuffer sqlBuffer = new StringBuffer();
        sqlBuffer.append(SELECT).append(tableName);

        if (precondition != null && precondition instanceof SQLPrecondition){
            String whereSql = ((SQLPrecondition) precondition).getWhereSql();
            sqlBuffer.append(SPACE).append(whereSql);
        }
        statement.setSql(sqlBuffer.toString());
        return statement;
    }


    /**
     * 构建select count
     * @param cls
     * @param precondition  操作的前提条件
     * @return
     */
    public static SQLStatement buildSelectAmountSql(Class<?> cls, Precondition precondition) throws CustomException{
        TableInfo tableInfo = TableManager.getTableInfo(cls);
        String tableName = tableInfo.getTableName();
        StringBuffer sqlBuffer = new StringBuffer();
        sqlBuffer.append(SELECT_COUNT).append(tableName);

        if (precondition != null && precondition instanceof SQLPrecondition){
            String whereSql = ((SQLPrecondition) precondition).getWhereSql();
            sqlBuffer.append(SPACE).append(whereSql);
        }

        return new SQLStatement(sqlBuffer.toString(), null);
    }

    /**
     * 查询数据库中所有的表信息
     * @return
     */
    public static SQLStatement buildQueryTableAll(){
        return new SQLStatement(QUERY_TABLE_ALL, null);
    }

    /**
     * 查询表中所有的字段信息
     * @param name
     * @return
     */
    public static SQLStatement buildQueryColumnAll(String name){
        StringBuffer sqlBuffer = new StringBuffer();
        sqlBuffer.append(QUERY_COLUMN_ALL).append(name).append(RIGHT_PART);
        return new SQLStatement(sqlBuffer.toString(), null);
    }


    /**
     * where 列 = 值
     * @param column        列
     * @param value         值
     * @return
     */
    public static String buildWhereEquals(String column, Number value) {
        StringBuffer sqlBuffer = new StringBuffer();
        sqlBuffer.append(column).append(EQUALS).append(value);
        return sqlBuffer.toString();
    }

    /**
     * where 列 != 值
     * @param column        列
     * @param value         值
     * @return
     */
    public static String buildWhereNorEquals(String column, Number value) {
        StringBuffer sqlBuffer = new StringBuffer();
        sqlBuffer.append(column).append(NOR_EQUALS).append(value);
        return sqlBuffer.toString();
    }

    /**
     * where 列 = 值
     * @param column        列
     * @param value         值
     * @return
     */
    public static String buildWhereEquals(String column, String value) {
        StringBuffer sqlBuffer = new StringBuffer();
        sqlBuffer.append(column).append(EQUALS).append(QUOTES).append(value).append(QUOTES);
        return sqlBuffer.toString();
    }

    /**
     * where 列 != 值
     * @param column        列
     * @param value         值
     * @return
     */
    public static String buildWhereNorEquals(String column, String value) {
        StringBuffer sqlBuffer = new StringBuffer();
        sqlBuffer.append(column).append(NOR_EQUALS).append(QUOTES).append(value).append(QUOTES);
        return sqlBuffer.toString();
    }

    /**
     * where 列 > 值
     * @param column        列
     * @param value         值
     * @return
     */
    public static String buildWhereGreater(String column, Number value) {
        StringBuffer sqlBuffer = new StringBuffer();
        sqlBuffer.append(column).append(GREATER).append(value);
        return sqlBuffer.toString();
    }


    /**
     * where 列 < 值
     * @param column        列
     * @param value         值
     * @return
     */
    public static String buildWhereLess(String column, Number value) {
        StringBuffer sqlBuffer = new StringBuffer();
        sqlBuffer.append(column).append(LESS).append(value);
        return sqlBuffer.toString();
    }


    /**
     * where in     包含查询
     * @param column        列
     * @param value         集合
     * @return
     */
    public static String buildWhereIn(String column, List value) {

        StringBuffer valueBuffer = new StringBuffer();
        boolean flag = false;
        for (int i = 0; i < value.size(); i++){
            Object object = value.get(i);

            if (flag){
                valueBuffer.append(COMMA);
            }

            if (object instanceof CharSequence){
                valueBuffer.append(QUOTES).append((String)object).append(QUOTES);
                flag = true;
            }else if (object instanceof Number){
                valueBuffer.append((Number)object);
                flag = true;
            }
        }

        if (Checker.isEmpty(valueBuffer)){
            return null;
        }

        StringBuffer sqlBuffer = new StringBuffer();
        sqlBuffer.append(column).append(IN).append(LEFT_PART).append(valueBuffer).append(RIGHT_PART);
        return sqlBuffer.toString();
    }


    /**
     * where like   模糊查询
     * @param column        列
     * @param value         值
     * @return
     */
    public static String buildWhereFuzzy(String column, String value) {
        StringBuffer sqlBuffer = new StringBuffer();
        sqlBuffer.append(column).append(LIKE).
                append(QUOTES).append(PERCENT).
                append(value).append(PERCENT).
                append(QUOTES);
        return sqlBuffer.toString();
    }


    /**
     * limit        查询前amount条数据
     * @param amount        数量
     * @return
     */
    public static String buildWhereAmount(long amount){
        StringBuffer sqlBuffer = new StringBuffer();
        sqlBuffer.append(LIMIT)
                .append(ZERO)
                .append(COMMA)
                .append(amount);
        return sqlBuffer.toString();
    }

    /**
     * order 正序  倒序查询
     * @param column
     * @param by
     * @return
     */
    public static String buildOrder(String column, int by){
        StringBuffer sqlBuffer = new StringBuffer();
        sqlBuffer.append(ORDER)
                .append(column);
        if(ASC == by){
            sqlBuffer.append(ASCSTR);
        }else{
            sqlBuffer.append(DESCSTR);
        }
        return sqlBuffer.toString();
    }


    /**
     * where
     * @return
     */
    public static String where(){
        return WHERE;
    }

    /**
     * and
     * @return
     */
    public static String and(){
        return AND;
    }

    /**
     * or
     * @return
     */
    public static String or(){
        return OR;
    }

    /**
     * order
     * @return
     */
    public static String order(){
        return ORDER;
    }
}
