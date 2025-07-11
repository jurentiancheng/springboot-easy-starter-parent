package com.easyapi.annotations;

import cn.hutool.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SystemLog {

    @AliasFor(attribute = "value")
    String title() default "";

    @AliasFor(attribute = "title")
    String value() default "";

    /**
     * hide result
     * @return
     */
    boolean hideResult() default false;


}
