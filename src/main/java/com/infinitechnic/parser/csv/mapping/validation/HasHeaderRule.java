package com.infinitechnic.parser.csv.mapping.validation;


import com.infinitechnic.parser.csv.CsvDocument;
import com.infinitechnic.parser.csv.exception.RuleValidationException;

/*
This rule should be run after CsvMappingRule is passed
*/
public class HasHeaderRule extends BaseRule {
    public HasHeaderRule(CsvDocument csvDocument, Class<?> mappingClass) {
        super(csvDocument, mappingClass);
    }

    @Override
    public void validate(CsvDocument csvDocument, Class<?> mappingClass) throws RuleValidationException {
        if (!csvDocument.isHasHeader()) {
            throw new RuleValidationException("No header in CSV");
        }
    }
}
