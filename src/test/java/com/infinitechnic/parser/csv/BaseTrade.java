package com.infinitechnic.parser.csv;

import com.infinitechnic.parser.csv.mapping.CsvCell;
import com.infinitechnic.parser.csv.mapping.CsvMapping;

@CsvMapping
public abstract class BaseTrade {
    @CsvCell(name="code")
    protected int code1;

    public Integer getCode() {
        return code1;
    }

    public void setCode(Integer code) {
        this.code1 = code;
    }
}
