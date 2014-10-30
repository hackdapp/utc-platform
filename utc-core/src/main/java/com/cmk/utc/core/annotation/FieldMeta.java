/*
 * 文件名：SD.java 版权：Copyright by www.wmccn.com 描述： 修改人：LIFE2014 修改时间：2014-10-30 跟踪单号： 修改单号： 修改内容：
 */

package com.cmk.utc.core.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FieldMeta {
    public abstract String fieldName();

    public abstract boolean isPrimaryKey() default false;

    public abstract boolean isNullAble() default true;

    public abstract String defaultValue() default "";

    public abstract String comment() default "";

    public abstract int fieldLength();

    public abstract int decLength() default 0;
}