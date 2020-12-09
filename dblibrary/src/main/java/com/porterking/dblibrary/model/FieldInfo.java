package com.porterking.dblibrary.model;


import com.porterking.dblibrary.utils.TypeCastUtil;

import java.lang.reflect.Field;

/**
 * author: porter_king
 * 表示数据库表字段的信息
 */
public class FieldInfo {

    /**
     *  名称(该字段名称)
     */
    private String name;

    private Field field;

    /**
     *  字段默认值
     */
    private String defaultValue;

    private int classType = TypeCastUtil.CLASS_TYPE_NONE;

    public FieldInfo(String name, Field field, String defaultValue){
        this.name = name;
        this.field =field;

        if (classType <= 0){
            classType = TypeCastUtil.getFieldClassType(field);
            if (TypeCastUtil.getSQLDataType(classType).equals(TypeCastUtil.INTEGER)
                    || TypeCastUtil.getSQLDataType(classType).equals(TypeCastUtil.REAL)){
                defaultValue = "0";
            }
        }

        if (defaultValue != null){
            this.defaultValue = defaultValue;
        }
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public int getClassType() {
        return classType;
    }

    public void setClassType(int classType) {
        this.classType = classType;
    }

}
