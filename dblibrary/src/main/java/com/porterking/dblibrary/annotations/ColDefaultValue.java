package com.porterking.dblibrary.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * author: porter_king
 * 标识数据库表字段的默认值
 */

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ColDefaultValue {

    /**
     * 默认值
     * @return
     */
    String value();
}
