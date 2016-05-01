package com.infinitechnic.parser.csv.mapping;

import java.lang.annotation.*;

@Inherited
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface CsvMapping {
    boolean caseSensitive() default true;  // Specify whether case sensitive or not when performing mapping
    boolean identical() default false;   // Specify whether the attributes in CSV & Class should be identical
}
