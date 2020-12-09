package com.porterking.dblibrary.utils;

import com.porterking.dblibrary.model.FieldInfo;

import java.lang.reflect.Field;

/**
 * author: porter_king
 * 字段解析帮助类
 */
public class FieldUtil {

    public static Object get(Field field, Object object) throws IllegalAccessException {
        field.setAccessible(true);
        return field.get(object);
    }

    public static void set(Field field, Object object, Object value) throws IllegalAccessException {
        field.setAccessible(true);
        field.set(object, value);
    }

    /**
     * 判断该字段是否为空（是否设值）
     * @param fieldInfo
     * @return
     */
    public static boolean isNULL(FieldInfo fieldInfo, Object object) throws IllegalAccessException {
        int classType = TypeCastUtil.getFieldClassType(fieldInfo.getField());
        Object value = get(fieldInfo.getField(), object);
        switch (classType){
            case TypeCastUtil.CLASS_TYPE_DOUBLE:
                return (double)value == 0.0d;
            case TypeCastUtil.CLASS_TYPE_FLOAT:
                return (float)value == 0.0f;
            case TypeCastUtil.CLASS_TYPE_INT:
                return (int)value == 0;
            case TypeCastUtil.CLASS_TYPE_LONG:
                return (long)value == 0l;
            case TypeCastUtil.CLASS_TYPE_SHORT:
                return (short)value == 0;
        }

        if (value == null){
            return true;
        }

        return false;
    }
}
