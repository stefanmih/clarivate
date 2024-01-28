package com.clarivate.main;

import com.clarivate.collection.Tuple;
import com.clarivate.scrapper.AustraliaHolidayScrapper;
import com.clarivate.util.HttpUtil;

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
        System.out.println(scrapper.getDataFromDB());
    }


}
