package com.infinitechnic.parser.csv.mapping;

import java.lang.annotation.*;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CsvCell {
    String name();  // Specify the CSV header name
}
