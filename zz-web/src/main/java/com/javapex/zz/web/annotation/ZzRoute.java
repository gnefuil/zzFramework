package com.javapex.zz.web.annotation;
import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ZzRoute {
    String value() default "" ;
}
