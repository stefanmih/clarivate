package com.clarivate.scrapper.impl;

import com.clarivate.collection.Tuple;
import com.clarivate.entity.Date;
import com.clarivate.entity.Record;
import com.clarivate.scrapper.WebScrapper;
import com.clarivate.util.EntityIDGenerator;
import com.clarivate.util.HibernateUtil;
import com.clarivate.util.StringUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AustraliaHolidayScrapper implements WebScrapper {
    private final List<Integer> years = new ArrayList<>();
    private final List<String> dates = new ArrayList<>();
    private final List<String> holidays = new ArrayList<>();

    public AustraliaHolidayScrapper(String source) {
        parseDataFromSource(source);
    }

    public AustraliaHolidayScrapper() {
    }

    @Override
    public void parseDataFromSource(String source){
        getHolidayDates(source);
        getHolidayNames(source);
        getYearsFromHeader(source);
    }

    private Matcher getHeaderContent(String source) {
        Pattern pattern = Pattern.compile("<thead>([.\\s\\S ]*?)</thead>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
        return pattern.matcher(source);
    }

    private void getYearsFromHeader(String source) {
        Pattern pattern = Pattern.compile("<th (.*?)>(.*?)</th>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
        Matcher matcherThead = getHeaderContent(source);
        if (matcherThead.find()) {
            Matcher matcher = pattern.matcher(matcherThead.group(1));
            while (matcher.find()) {
                if (!StringUtil.isEmpty(matcher.group(2)) && StringUtil.isNumeric(matcher.group(2))) {
                    years.add(Integer.parseInt(matcher.group(2)
                            .replace("\u00a0", " ")));
                }
            }
        }
    }

    private void getHolidayNames(String source) {
        Pattern pattern = Pattern.compile("<th(.*?)><strong>(.*?)</strong></th>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(source);
        while (matcher.find()) {
            holidays.add(matcher.group(2)
                    .replace("\u00a0", " "));
        }
    }

    private void getHolidayDates(String source) {
        Pattern pattern = Pattern.compile("<td>([.\\s\\S]*?)</td>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(source);

        while (matcher.find()) {
            dates.add(matcher.group(1)
                    .replaceAll("(<br(\\s*)/>[\\n\\t]*&amp;<br(\\s*)/>[\\n\\t]*)", " & ")
                    .replace("\u00a0", " ")
                    .replace("*", ""));
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Tuple> getDataFromSource() throws Exception {
        int i = 0;
        List<Tuple> finalData = new ArrayList<>();
        for (String holiday : holidays) {
            Tuple tuple = Tuple.tupleOf(String.class, ArrayList.class).createTuple(holiday, new ArrayList<>());
            finalData.add(tuple);
            for (Integer year : years) {
                ((ArrayList<Date>)tuple.getElement(1)).addAll(parseDates(dates.get(i).trim() + " " + year));
                i++;
            }
        }
        return finalData;
    }

    private List<Date> parseDates(String date) throws Exception {
        List<Date> dates = new ArrayList<>();
        if (date.contains("&")) {
            int year = Integer.parseInt(date.substring(date.length() - 4));
            String date1 = date.split("&")[0].trim();
            String date2 = date.split("&")[1].trim();
            dates.add(new Date(parseDate(date1, year)));
            dates.add(new Date(parseDate(date2)));
        } else {
            dates.add(new Date(parseDate(date)));
        }
        return dates;
    }

    private java.util.Date parseDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.ENGLISH);
        LocalDate dateTime = LocalDate.parse(date.split(" ")[1] + " " + date.split(" ")[2] + " " + date.split(" ")[3], formatter);
        return java.util.Date.from(dateTime.atStartOfDay().toInstant(ZoneOffset.UTC));
    }

    private java.util.Date parseDate(String date, int year) {
        return parseDate(date.trim() + " " + year);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void insertDataIntoDB() {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            for (Tuple tuple : getDataFromSource()) {
                Record record = new Record((String) tuple.getElement(0), null);
                record.setId(EntityIDGenerator.getRecordId(session));
                session.persist(record);
                for(Date dt : (List<Date>) tuple.getElement(1)){
                    dt.setId(EntityIDGenerator.getDateId(session));
                    dt.setRecordId(record.getId());
                    session.persist(dt);
                    session.flush();
                }
            }
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }

        }
    }

    @Override
    public List<Record> getDataFromDB() {
        Transaction transaction = null;
        List<Record> records = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            records = session.createQuery("from Record", Record.class).stream().toList();
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }
        return records;
    }

}
