package com.porterking.dblibrary.assit;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.porterking.dblibrary.annotations.ColDefaultValue;
import com.porterking.dblibrary.annotations.ColumnName;
import com.porterking.dblibrary.annotations.PrimaryKey;
import com.porterking.dblibrary.annotations.TableName;
import com.porterking.dblibrary.model.FieldInfo;
import com.porterking.dblibrary.model.SQLFieldInfo;
import com.porterking.dblibrary.model.SQLTableInfo;
import com.porterking.dblibrary.model.TableInfo;
import com.porterking.dblibrary.utils.Checker;
import com.porterking.dblibrary.utils.DLog;
import com.porterking.dblibrary.utils.DataUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * author: porter_king
 * 用于数据库表管理与操作，保存缓存信息等
 */
public class TableManager {

    public static final String DEFAULT_PRIMARY_KEY = "id";

    public static boolean init  = false;
    public static String msg   = null;

    /**
     * 存放本地实体类的信息
     */
    private static final HashMap<String, TableInfo> mTableInfoMap = new HashMap<>();


    /**
     * 存放数据库中所有的表结构的信息
     */
    private static final HashMap<String, SQLTableInfo> mSQLTableInfoMap = new HashMap<>();


    /**
     * 初始化全部表及其列名,初始化失败，则无法进行下去
     * @param db
     */
    public static void initAllTablesFromSQLite(SQLiteDatabase db) {
        DLog.i("--------------------数据库框架初始化--------------------");

        synchronized (mSQLTableInfoMap) {
            if (Checker.isEmpty(mSQLTableInfoMap)) {
                try {
                    SQLBuilder.buildQueryTableAll().execQuery(db, SQLTableInfo.class, new SQLStatement.ResultCallBack() {
                        @Override
                        public void callback(SQLiteDatabase sqLiteDatabase, Class cls, Cursor cursor) throws Exception {
                            if (cursor != null && cursor.moveToFirst()){
                                do {
                                    SQLTableInfo sqlTableInfo = new SQLTableInfo();
                                    DLog.i("Table--->" + sqlTableInfo.getMessage());
                                    DataUtil.injectDataToObject(cursor, sqlTableInfo, TableManager.getTableInfo(cls));
                                    List<SQLFieldInfo> sqlFieldInfos =
                                            SQLBuilder.buildQueryColumnAll(sqlTableInfo.name).execQuery(sqLiteDatabase, SQLFieldInfo.class);

                                    List<String> columns = new ArrayList<>();
                                    for (SQLFieldInfo sqlFieldInfo : sqlFieldInfos){
                                        columns.add(sqlFieldInfo.name);
                                        DLog.i("Columns--->" + sqlFieldInfo.getMessage());
                                    }

                                    sqlTableInfo.columns = columns;
                                    mSQLTableInfoMap.put(sqlTableInfo.name, sqlTableInfo);
                                }while (cursor.moveToNext());
                            }
                            init = true;
                        }
                    });
                }catch (CustomException e){
                    msg = e.getMsg();
                    DLog.e("数据库初始化失败--->" + msg);
                }catch (Exception e){
                    msg = e.getMessage();
                    DLog.e("数据库初始化失败--->" + msg);
                }
            }
        }

        DLog.i("--------------------数据库框架初始化结束--------------------");
    }

    /**
     * 检查该表是否存在，不存在则创建表，存在则检查是否有新增字段
     * @param sqLiteDatabase
     * @param cls
     */
    public static void checkOrCreateTable(SQLiteDatabase sqLiteDatabase, Class<?> cls) throws CustomException{
        //1.获取表结构信息并存入缓存
        TableInfo tableInfo = getTableInfo(cls);

        SQLTableInfo sqlTableInfo = mSQLTableInfoMap.get(tableInfo.getTableName());
        //说明该表不存在，创建表
        if (sqlTableInfo == null){
            DLog.i(tableInfo.getTableName() + "不存在，创建中------->");
            SQLBuilder.buildCreateTable(cls).execute(sqLiteDatabase);
            sqlTableInfo = new SQLTableInfo();
            sqlTableInfo.name = tableInfo.getTableName();

            List<String> sqlFieldInfos = new ArrayList<>();
            for (Map.Entry<String, FieldInfo> entry : tableInfo.getFieldInfoMap().entrySet()){
                sqlFieldInfos.add(entry.getValue().getName());
            }

            sqlTableInfo.columns = sqlFieldInfos;
            mSQLTableInfoMap.put(sqlTableInfo.name, sqlTableInfo);
        }
        //该表已存在，检查是否有新增字段
        else {
            DLog.i(tableInfo.getTableName() + "已存在，检查是否有新增字段------->");
            for (Map.Entry<String, FieldInfo> entry : tableInfo.getFieldInfoMap().entrySet()){
                FieldInfo fieldInfo = entry.getValue();
                String fieldName = fieldInfo.getName();
                if (!sqlTableInfo.columns.contains(fieldName)){
                    DLog.i("新增字段--------->" + fieldName);
                    SQLBuilder.buildAlterTable(cls, fieldInfo).execute(sqLiteDatabase);
                    sqlTableInfo.columns.add(fieldName);
                    mSQLTableInfoMap.put(sqlTableInfo.name, sqlTableInfo);
                }
            }
        }
    }


    /**
     * 获取对应的数据库表结构信息
     * @param tableModel
     * @return
     */
    public static TableInfo getTableInfo(Object tableModel) throws CustomException{
        return getTableInfo(tableModel.getClass());
    }

    /**
     * 通过class对象获取对应的数据库表结构信息
     * @param cls
     * @return
     */
    public static TableInfo getTableInfo(Class<?> cls) throws CustomException{

        String tableName = cls.getSimpleName();

        boolean isExist = cls.isAnnotationPresent(TableName.class);
        if(isExist){
            TableName tableAnn = cls.getAnnotation(TableName.class);
            tableName = tableAnn.value();
        }

        TableInfo tableInfo = mTableInfoMap.get(tableName);
        if (tableInfo != null){
            return tableInfo;
        }

        tableInfo = new TableInfo(tableName);

        Field[] fields = cls.getDeclaredFields();
        if (Checker.isEmpty(fields)){
            throw new CustomException(CustomException.CODE_3001, "该表不符合规则，字段信息为空，无法继续操作");
        }

        boolean flag = true;
        LinkedHashMap<String, FieldInfo> maps = new LinkedHashMap<>();
        for (Field field : fields){
            String fieldName = field.getName();
            String defaultValue = null;

            isExist = field.isAnnotationPresent(ColumnName.class);
            if (isExist){
                ColumnName fieldAnn = field.getAnnotation(ColumnName.class);
                fieldName = fieldAnn.value();
            }

            isExist = field.isAnnotationPresent(ColDefaultValue.class);
            if (isExist){
                ColDefaultValue fieldAnn = field.getAnnotation(ColDefaultValue.class);
                defaultValue = fieldAnn.value();
            }

            isExist = field.isAnnotationPresent(PrimaryKey.class);
            if (isExist && flag){
                if (!(long.class.isAssignableFrom(field.getType())
                        || Long.class.isAssignableFrom(field.getType()))){
                    throw new CustomException(CustomException.CODE_3003, "主键必须是long类型的变量");
                }

                tableInfo.setPrimaryKey(fieldName);
                flag = false;
            }

            FieldInfo fieldInfo = new FieldInfo(fieldName, field, defaultValue);
            maps.put(fieldName, fieldInfo);
        }

        //如果未设置主键，默认取id字段作为主键
        if (tableInfo.getPrimaryKey() == null){
            FieldInfo primaryInfo = maps.get(DEFAULT_PRIMARY_KEY);
            if (primaryInfo != null){

                if (!(long.class.isAssignableFrom(primaryInfo.getField().getType())
                        || Long.class.isAssignableFrom(primaryInfo.getField().getType()))){
                    throw new CustomException(CustomException.CODE_3003, "主键必须是long类型的变量");
                }

                tableInfo.setPrimaryKey(DEFAULT_PRIMARY_KEY);
                flag = false;
            }
        }

        if (flag){
            throw new CustomException(CustomException.CODE_3002, "该表未设置主键，无法继续操作");
        }

        tableInfo.setFieldInfoMap(maps);
        mTableInfoMap.put(tableName ,tableInfo);
        return tableInfo;
    }

}
