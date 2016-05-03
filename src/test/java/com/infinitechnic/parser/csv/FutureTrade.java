package com.infinitechnic.parser.csv;

import com.infinitechnic.parser.csv.mapping.CsvCell;
import com.infinitechnic.parser.csv.mapping.CsvMapping;

@CsvMapping
public class FutureTrade {
    @CsvCell(name = "code")
    private Integer code;

    public FutureTrade() {
        super();
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
