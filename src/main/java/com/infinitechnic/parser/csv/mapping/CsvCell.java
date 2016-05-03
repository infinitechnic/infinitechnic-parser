package com.infinitechnic.parser.csv.mapping;

import java.lang.annotation.*;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CsvCell {
    String name() default "";  // Specify the CSV header name
    String format() default "";    // Handle special format (e.g. Date format: yyyyMMdd)
}
