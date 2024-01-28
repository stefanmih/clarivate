package com.clarivate.main;

import com.clarivate.scrapper.AustraliaHolidayScrapper;
import com.clarivate.util.HttpUtil;


public class MainModule {
    public static AustraliaHolidayScrapper scrapper;

    static {
        try {
            scrapper = new AustraliaHolidayScrapper(HttpUtil.getSiteSource("https://www.commerce.wa.gov.au/labour-relations/public-holidays-western-australia"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws Exception {
        System.out.println(scrapper.getYear());
    }


}
