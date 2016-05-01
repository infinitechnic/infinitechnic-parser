package com.infinitechnic.parser.csv.mapping.validation;


import com.infinitechnic.parser.csv.CsvDocument;
import com.infinitechnic.parser.csv.exception.RuleValidationException;
import com.infinitechnic.parser.csv.mapping.CsvCell;
import com.infinitechnic.parser.csv.mapping.CsvMapping;
import com.infinitechnic.util.StringUtil;

import java.lang.reflect.Field;
import java.util.*;

/*
This rule should be run after CsvMappingRule & HasHeaderRule are passed
 */
public class FieldMappingRule extends BaseRule {
    public FieldMappingRule(CsvDocument csvDocument, Class<?> mappingClass) {
        super(csvDocument, mappingClass);
    }

    @Override
    public void validate(CsvDocument csvDocument, Class<?> mappingClass) throws RuleValidationException {
        CsvMapping csvMapping = mappingClass.getAnnotation(CsvMapping.class);
        boolean caseSensitive = csvMapping.caseSensitive();
        Set<String> csvFieldSet = getCsvFieldSet(csvDocument, caseSensitive);
        Set<String> classFieldSet = getClassFieldSet(mappingClass);
        Iterator<String> classFieldIt = classFieldSet.iterator();

        String fieldName = null;
        while (classFieldIt.hasNext()) {
            fieldName = caseSensitive?classFieldIt.next():classFieldIt.next().toLowerCase();
            if (csvFieldSet.contains(fieldName)) {
                csvFieldSet.remove(fieldName);
                classFieldIt.remove();
            }
        }
        log("CSV", csvFieldSet);
        log("Class", classFieldSet);
        if (csvMapping.identical() && (csvFieldSet.size() > 0 || classFieldSet.size() > 0)) {
            throw new RuleValidationException("CSV & Class field mappings are not identical");
        }
    }

    private Set<String> getCsvFieldSet(CsvDocument csvDocument, boolean caseSensitive) {
        Set<String> fieldSet = new HashSet<>();
        csvDocument.getHeaders().stream().forEach(h -> {
            //TODO: throw exception if exist already
            fieldSet.add(caseSensitive?h:h.toLowerCase());
        });
        return fieldSet;
    }

    private Set<String> getClassFieldSet(Class<?> mappingClass) {
        Set<String> fieldSet = new HashSet<>();
        List<Field> fields = getAllFields(mappingClass);
        getAllFields(mappingClass).stream().forEach(f -> {
            CsvCell csvCell = null;
            if ((csvCell = f.getAnnotation(CsvCell.class)) != null) {
                fieldSet.add(StringUtil.isEmpty(csvCell.name()) ? f.getName() : csvCell.name());
            }
        });
        return fieldSet;
    }

    //TODO: should be declared in Util class
    private List<Field> getAllFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();
        if (clazz != null && clazz.getSuperclass() != null) {
            fields.addAll(getAllFields(clazz.getSuperclass()));
        }
        fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
        return fields;
    }

    private void log(String type, Set<String> fields) {
        int fn = 0;
        if (fields != null && (fn = fields.size()) > 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("Field mappings for ").append(type).append(" are missing. [");
            String[] fieldAry = fields.toArray(new String[]{});
            for (int i=0; i<fn; i++) {
                if (i>0) {
                    sb.append(", ");
                }
                sb.append(fieldAry[i]);
            }
            sb.append("]");
            //TODO: use logger
            System.out.println(sb.toString());
        }
    }
}
