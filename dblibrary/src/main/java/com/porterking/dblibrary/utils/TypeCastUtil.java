package com.porterking.dblibrary.utils;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Date;

/**
 * author: porter_king
 * 用于类型之间转换的工具
 */
public class TypeCastUtil {

//------------------sql存储数据类型-----------------------------
    public static final String NULL = " null ";

    public static final String INTEGER = " integer ";

    public static final String REAL = " real ";

    public static final String TEXT = " text ";

    public static final String BLOB = " blob ";

//------------------------------------------------------------

    public static final int CLASS_TYPE_NONE = 0;

    public static final int CLASS_TYPE_STRING = 1;

    public static final int CLASS_TYPE_BOOLEAN = 2;

    public static final int CLASS_TYPE_DOUBLE = 3;

    public static final int CLASS_TYPE_FLOAT = 4;

    public static final int CLASS_TYPE_LONG = 5;

    public static final int CLASS_TYPE_INT = 6;

    public static final int CLASS_TYPE_SHORT = 7;

    public static final int CLASS_TYPE_BYTE = 8;

    public static final int CLASS_TYPE_BYTE_ARRAY = 9;

    public static final int CLASS_TYPE_CHAR = 10;

    public static final int CLASS_TYPE_DATE = 11;

    public static final int CLASS_TYPE_SERIALIZABLE = 12;

    public static final int CLASS_TYPE_UNKNOWN = 13;

    public static int getFieldClassType(Field f) {
        Class type = f.getType();
        if (CharSequence.class.isAssignableFrom(type)) {
            return CLASS_TYPE_STRING;
        } else if (boolean.class.isAssignableFrom(type) || Boolean.class.isAssignableFrom(type)) {
            return CLASS_TYPE_BOOLEAN;
        } else if (double.class.isAssignableFrom(type) || Double.class.isAssignableFrom(type)) {
            return CLASS_TYPE_DOUBLE;
        } else if (float.class.isAssignableFrom(type) || Float.class.isAssignableFrom(type)) {
            return CLASS_TYPE_FLOAT;
        } else if (long.class.isAssignableFrom(type) || Long.class.isAssignableFrom(type)) {
            return CLASS_TYPE_LONG;
        } else if (int.class.isAssignableFrom(type) || Integer.class.isAssignableFrom(type)) {
            return CLASS_TYPE_INT;
        } else if (short.class.isAssignableFrom(type) || Short.class.isAssignableFrom(type)) {
            return CLASS_TYPE_SHORT;
        } else if (byte.class.isAssignableFrom(type) || Byte.class.isAssignableFrom(type)) {
            return CLASS_TYPE_BYTE;
        } else if (byte[].class.isAssignableFrom(type) || Byte[].class.isAssignableFrom(type)) {
            return CLASS_TYPE_BYTE_ARRAY;
        } else if (char.class.isAssignableFrom(type) || Character.class.isAssignableFrom(type)) {
            return CLASS_TYPE_CHAR;
        } else if (Date.class.isAssignableFrom(type)) {
            return CLASS_TYPE_DATE;
        } else if (Serializable.class.isAssignableFrom(type)) {
            return CLASS_TYPE_SERIALIZABLE;
        }
        return CLASS_TYPE_UNKNOWN;
    }


    public static String getSQLDataType(int classType) {
        switch (classType) {
            case CLASS_TYPE_STRING:
            case CLASS_TYPE_BOOLEAN:
            case CLASS_TYPE_CHAR:
                return TEXT;
            case CLASS_TYPE_DOUBLE:
            case CLASS_TYPE_FLOAT:
                return REAL;
            case CLASS_TYPE_LONG:
            case CLASS_TYPE_INT:
            case CLASS_TYPE_SHORT:
            case CLASS_TYPE_BYTE:
            case CLASS_TYPE_DATE:
                return INTEGER;
            case CLASS_TYPE_BYTE_ARRAY:
            case CLASS_TYPE_SERIALIZABLE:
            default:
                return BLOB;
        }
    }
}
