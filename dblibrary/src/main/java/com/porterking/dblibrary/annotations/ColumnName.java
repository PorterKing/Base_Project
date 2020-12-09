package com.porterking.dblibrary.annotations;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * author: porter_king
 * 标识数据库表中某一列的名称,如果没有设置默认取字段名称
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ColumnName {

    /**
     * 列名
     * @return
     */
    String value();
}
