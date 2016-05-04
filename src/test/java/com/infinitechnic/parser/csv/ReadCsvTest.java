package com.infinitechnic.parser.csv;

import com.infinitechnic.util.StringUtil;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.Iterator;

public class ReadCsvTest {
    @Test
    public void readInputStream() {
        try {
//            URL resource = Thread.currentThread().getContextClassLoader().getResource("Sample.csv");
            URL resource = this.getClass().getClassLoader().getResource("Sample.csv");
            File file = new File(resource.toURI());
            FileInputStream input = new FileInputStream(file);
            CsvReader csvReader = new CsvReader("Sample.csv", input, true, ",", FutureTrade.class);
            CsvDocument csvDocument = csvReader.read();
            Assert.assertEquals(csvDocument.getNoOfLines(), 50);
            Iterator<FutureTrade> it = csvDocument.iterator();
            while (it.hasNext()) {
                FutureTrade ftr = it.next();
                System.out.println(
                StringUtil.concat(ftr.getCode(), "\t", ftr.getTradeDate(), "\t", ftr.getPositionTypeCode(), "\t",
                        ftr.getQuantity(), "\t", ftr.getLotSize(), "\t", ftr.getContractDeliveryDate(), "\t",
                        ftr.getPrice(), "\t", ftr.getUnitCode(), "\t", ftr.getCurrencyCode(), "\t",
                        ftr.getTraderCode(), "\t", ftr.getExchangeCode(), "\t", ftr.getProductCode(), "\t",
                        ftr.getSettlementDate(), "\t", ftr.getCompanyCode())
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
