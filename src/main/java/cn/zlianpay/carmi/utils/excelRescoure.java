package cn.zlianpay.carmi.utils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * excel 注解
 * Created by Panyoujie on 2021-10-23 03:01:15
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface excelRescoure {
    String value() default "";
}
