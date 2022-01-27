package com.lab7.api.command.annotation;

import com.lab7.api.entity.Dragon;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface AttachedObj {
    Class<?> type() default Dragon.class;
}
