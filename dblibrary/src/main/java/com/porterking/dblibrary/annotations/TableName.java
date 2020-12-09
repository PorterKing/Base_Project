package com.porterking.dblibrary.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * author: porter_king
 * 标识数据库表名,如果没有设置默认取对象类的类名作为表名
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface TableName {

    /**
     * 表名
     * @return
     */
    String value();
}
