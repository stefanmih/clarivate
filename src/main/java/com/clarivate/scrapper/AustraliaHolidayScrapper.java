package com.clarivate.scrapper;

import com.clarivate.collection.Tuple;
import com.clarivate.util.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AustraliaHolidayScrapper {
    private Tuple year;
    private Tuple date;
    private Tuple holiday;

    public AustraliaHolidayScrapper(String source) throws Exception {
        getYearsFromHeader(source);
    }

    private void getYearsFromHeader(String source) throws Exception {
        Pattern pattern = Pattern.compile("<th (.*?)>(.*?)</th>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(source);
        List<Integer> years = new ArrayList<>();
        while(matcher.find()) {
            System.out.println(matcher.group(2));
            if(!StringUtil.isEmpty(matcher.group(2)) && StringUtil.isNumeric(matcher.group(2))) {
                years.add(Integer.parseInt(matcher.group(2)));
            }
        }
        year = Tuple.tupleOf(Integer.class, Integer.class, Integer.class).createTuple(years.toArray());
    }

    public Tuple getYear() {
        return year;
    }

    public Tuple getDate() {
        return date;
    }

    public Tuple getHoliday() {
        return holiday;
    }
}
