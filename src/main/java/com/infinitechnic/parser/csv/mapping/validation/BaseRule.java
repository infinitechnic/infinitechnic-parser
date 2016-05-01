package com.infinitechnic.parser.csv.mapping.validation;


import com.infinitechnic.parser.csv.CsvDocument;
import com.infinitechnic.parser.csv.exception.RuleValidationException;

public abstract class BaseRule implements Rule {
    protected BaseRule nextRule;
    protected CsvDocument csvDocument;
    protected Class<?> mappingClass;

    protected BaseRule(CsvDocument csvDocument, Class<?> mappingClass) {
        super();
        this.nextRule = null;
        this.csvDocument = csvDocument;
        this.mappingClass = mappingClass;
    }

    public BaseRule addChain(BaseRule rule) {
        this.nextRule = rule;
        return rule;
    }

    public void validate() throws RuleValidationException {
        validate(csvDocument, mappingClass);
        if (nextRule != null) {
            nextRule.validate();
        }
    }
}
