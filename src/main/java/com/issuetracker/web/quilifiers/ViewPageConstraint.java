package com.issuetracker.web.quilifiers;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;
 
@Retention(RUNTIME)
@Target(ElementType.CONSTRUCTOR)
public @interface ViewPageConstraint {
    String allowedAction() default "";
    String allowedRole() default "";
}
