package com.porterking.dblibrary.utils;


import android.database.Cursor;

import com.porterking.dblibrary.model.FieldInfo;
import com.porterking.dblibrary.model.TableInfo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.util.Date;

/**
 * author: porter_king
 * 数据相关操作帮助类
 */
public class DataUtil {


    /**
     * 将Cursor的数据注入模型
     * 支持11种基本类型
     * 同时支持序列化对象
     * @param c
     * @param object
     * @param table
     * @throws Exception
     */
    public static void injectDataToObject(Cursor c, Object object, TableInfo table) throws Exception {
        Field f;
        FieldInfo fi;
        for (int i = 0; i < c.getColumnCount(); i++) {

            String col = c.getColumnName(i);
            fi = null;
            if (!Checker.isEmpty(table.getFieldInfoMap())) {
                fi = table.getFieldInfoMap().get(col);
            }
            if (fi == null) {
                continue;
            }
            f = fi.getField();
            f.setAccessible(true);
            switch (fi.getClassType()) {
                case TypeCastUtil.CLASS_TYPE_STRING:
                    FieldUtil.set(f, object, c.getString(i));
                    break;
                case TypeCastUtil.CLASS_TYPE_BOOLEAN:
                    FieldUtil.set(f, object, Boolean.parseBoolean(c.getString(i)));
                    break;
                case TypeCastUtil.CLASS_TYPE_LONG:
                    FieldUtil.set(f, object, c.getLong(i));
                    break;
                case TypeCastUtil.CLASS_TYPE_INT:
                    FieldUtil.set(f, object, c.getInt(i));
                    break;
                case TypeCastUtil.CLASS_TYPE_DOUBLE:
                    FieldUtil.set(f, object, c.getDouble(i));
                    break;
                case TypeCastUtil.CLASS_TYPE_FLOAT:
                    FieldUtil.set(f, object, c.getFloat(i));
                    break;
                case TypeCastUtil.CLASS_TYPE_SHORT:
                    FieldUtil.set(f, object, c.getShort(i));
                    break;
                case TypeCastUtil.CLASS_TYPE_BYTE:
                    if (c.getString(i) != null) {
                        FieldUtil.set(f, object, Byte.parseByte(c.getString(i)));
                    }
                    break;
                case TypeCastUtil.CLASS_TYPE_BYTE_ARRAY:
                    FieldUtil.set(f, object, c.getBlob(i));
                    break;
                case TypeCastUtil.CLASS_TYPE_CHAR:
                    String value = c.getString(i);
                    if (!Checker.isEmpty(value)) {
                        FieldUtil.set(f, object, value.charAt(0));
                    }
                    break;
                case TypeCastUtil.CLASS_TYPE_DATE:
                    Long time = c.getLong(i);
                    if (time != null) {
                        FieldUtil.set(f, object, new Date(time));
                    }
                    break;
                case TypeCastUtil.CLASS_TYPE_SERIALIZABLE:
                    byte[] bytes = c.getBlob(i);
                    if (bytes != null) {
                        FieldUtil.set(f, object, byteToObject(bytes));
                    }
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * byte[] 转为 对象
     */
    public static Object byteToObject(byte[] bytes) throws Exception {
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
            return ois.readObject();
        } finally {
            if (ois != null)
                ois.close();
        }
    }


    /**
     * 对象 转为 byte[]
     */
    public static byte[] objectToByte(Object obj) throws IOException {
        ObjectOutputStream oos = null;
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            return bos.toByteArray();
        } finally {
            if (oos != null)
                oos.close();
        }
    }
}
