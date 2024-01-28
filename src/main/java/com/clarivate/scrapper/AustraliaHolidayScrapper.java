package com.clarivate.scrapper;

import com.clarivate.collection.Tuple;
import com.clarivate.entity.Record;
import com.clarivate.util.HibernateUtil;
import com.clarivate.util.StringUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AustraliaHolidayScrapper {
    private final List<Integer> years = new ArrayList<>();
    private final List<String> dates = new ArrayList<>();
    private final List<String> holidays = new ArrayList<>();

    public AustraliaHolidayScrapper(String source) {
        getHolidayDates(source);
        getHolidayNames(source);
        getYearsFromHeader(source);
    }

    private Matcher getHeaderContent(String source){
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
                    years.add(Integer.parseInt(matcher.group(2)));
                }
            }
        }
    }

    private void getHolidayNames(String source) {
        Pattern pattern = Pattern.compile("<th (.*?)><strong>(.*?)</strong></th>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(source);
        while (matcher.find()) {
            holidays.add(matcher.group(2));
        }
    }

    private void getHolidayDates(String source) {
        Pattern pattern = Pattern.compile("<td>([.\\s\\S]*?)</td>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(source);
        while (matcher.find()) {
            dates.add(matcher.group(1).replaceAll("(<br(\\s*)/>[\\n\\t]*&amp;<br(\\s*)/>[\\n\\t]*)"," & ").replace("*",""));
        }
    }

    public List<Tuple> getDataFromSource() throws Exception {
        int i = 0;
        List<Tuple> finalData = new ArrayList<>();
        for(String holiday : holidays){
            for(Integer year : years){
                finalData.add(Tuple.tupleOf(String.class, String.class).createTuple(holiday, dates.get(i) + " " + year));
                i++;
            }
        }
        return finalData;
    }

    public void insertDataIntoDB(){
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            for(Tuple tuple : getDataFromSource()){
                Record record = new Record((String)tuple.getElement(0), (String)tuple.getElement(1));
                session.persist(record);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public List<Tuple> getDataFromDB(){
        Transaction transaction = null;
        List<Tuple> records = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            records = session.createQuery("from Record", Record.class)
                    .stream()
                    .map(e-> {
                        try {
                            return Tuple.tupleOf(String.class, String.class).createTuple(e.getHoliday(), e.getDate());
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                    }).toList();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return records;
    }

}
