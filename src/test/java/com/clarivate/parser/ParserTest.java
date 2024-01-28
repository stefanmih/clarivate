package com.clarivate.parser;

import com.clarivate.scrapper.impl.AustraliaHolidayScrapper;
import com.clarivate.util.StringUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ParserTest {

    @Test
    public void parserTest() {
        String source = "<p><strong>Public holidays in Western Australia - 2024 to 2026</strong></p>\n" +
                "</caption>\n" +
                "<thead><tr><th scope=\"row\"> </th>\n" +
                "<th scope=\"col\">1452</th>\n" +
                "<th style=\"background-color:red\" scope=\"col\">1944</th>\n" +
                "<th scope=\"col\">wrong year</th>\n" +
                "</tr></thead><tbody><tr><th scope=\"row\"><strong>New Year's Day</strong></th>\n" +
                "<td>Monday 1 January</td>\n" +
                "<td>Wednesday 1 January</td>\n" +
                "<td>Thursday 1 January</td>\n" +
                "</tr><tr><th scope=\"row\"><strong>Australia Day</strong></th>\n" +
                "<td>Friday 26 January</td>\n" +
                "<td>Monday 26 January</td>\n" +
                "<td>Saturday 26 January</td>\n" +
                "</tr><tr><th scope=\"row\"><strong>Labour Day</strong></th>\n" +
                "<td>Monday 4 March</td>\n" +
                "<td>Monday 3 March</td>\n" +
                "<td>Monday 2 March</td>\n" +
                "</tr><tr><th scope=\"row\"><strong>Good Friday</strong></th>\n" +
                "<td>Friday 29 March</td>\n" +
                "<td>Friday 18 April</td>\n" +
                "<td>Friday 3 April</td>\n";
        AustraliaHolidayScrapper scrapper = new AustraliaHolidayScrapper();
        scrapper.parseDataFromSource(source);
        try {
            System.out.println(scrapper.getDataFromSource());
        } catch (Exception e) {
            Assertions.fail(e);
        }
        scrapper.insertDataIntoDB();
        System.out.println(StringUtil.printRecords(scrapper.getDataFromDB()));
    }
}
