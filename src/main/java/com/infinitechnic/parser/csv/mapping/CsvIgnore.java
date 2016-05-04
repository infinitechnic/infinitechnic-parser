package com.infinitechnic.parser.csv.mapping;

import java.lang.annotation.*;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CsvIgnore {
}
