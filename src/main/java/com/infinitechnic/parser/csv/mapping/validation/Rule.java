package com.infinitechnic.parser.csv.mapping.validation;


import com.infinitechnic.parser.csv.CsvDocument;
import com.infinitechnic.parser.csv.exception.RuleValidationException;

public interface Rule {
    void validate(CsvDocument csvDocument, Class<?> mappingClass) throws RuleValidationException;
}
