package com.clarivate.main;

import com.clarivate.scrapper.impl.AustraliaHolidayScrapper;
import com.clarivate.util.HttpUtil;
import com.clarivate.util.StringUtil;

import java.io.IOException;


public class MainModule {
    public static void main(String[] args) throws IOException, InterruptedException {
        String pageSource = HttpUtil.getSiteSource("https://www.commerce.wa.gov.au/labour-relations/public-holidays-western-australia");
        AustraliaHolidayScrapper scrapper = new AustraliaHolidayScrapper(pageSource);
        scrapper.insertDataIntoDB();
        System.out.println(StringUtil.printRecords(scrapper.getDataFromDB()));
    }


}
