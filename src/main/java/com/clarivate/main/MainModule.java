package com.clarivate.main;

import com.clarivate.scrapper.impl.AustraliaHolidayScrapper;
import com.clarivate.util.HttpUtil;
import com.clarivate.util.StringUtil;

import java.io.IOException;


public class MainModule {
    public static AustraliaHolidayScrapper scrapper;

    static {
        try {
            scrapper = new AustraliaHolidayScrapper(HttpUtil.getSiteSource("https://www.commerce.wa.gov.au/labour-relations/public-holidays-western-australia"));
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args){
        scrapper.insertDataIntoDB();
        System.out.println(StringUtil.printRecords(scrapper.getDataFromDB()));
    }


}
