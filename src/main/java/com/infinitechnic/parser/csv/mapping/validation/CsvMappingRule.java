package com.infinitechnic.parser.csv.mapping.validation;

import com.infinitechnic.parser.csv.CsvDocument;
import com.infinitechnic.parser.csv.exception.RuleValidationException;
import com.infinitechnic.parser.csv.mapping.CsvMapping;

public class CsvMappingRule extends BaseRule {
    public CsvMappingRule(CsvDocument csvDocument, Class<?> mappingClass) {
        super(csvDocument, mappingClass);
    }

    @Override
    public void validate(CsvDocument csvDocument, Class<?> mappingClass) throws RuleValidationException {
        if (mappingClass == null) {
            throw new RuleValidationException("Mapping Class is null");
        } else {
            if (mappingClass.getAnnotation(CsvMapping.class) == null) {
                throw new RuleValidationException("The Mapping Class doesn't annotated as CsvMapping");
            }
        }
    }
}
