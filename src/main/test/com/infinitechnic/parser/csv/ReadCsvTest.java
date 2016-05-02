package com.infinitechnic.parser.csv;

import com.infinitechnic.parser.csv.CsvReader;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;

public class ReadCsvTest {
    @Test
    public void readInputStream() {
        try {
            URL resource = Thread.currentThread().getContextClassLoader().getResource("Sample.csv");
            File file = new File(resource.toURI());
            FileInputStream input = new FileInputStream(file);
            CsvReader csvReader = new CsvReader("Sample.csv", input, true, ",");
            CsvDocument csvDocument =csvReader.read();
            Assert.assertEquals(csvDocument.getNoOfLines(), 500);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
