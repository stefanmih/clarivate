package com.clarivate.scrapper;

import com.clarivate.collection.Tuple;

import java.util.List;

public interface WebScrapper {
    void parseDataFromSource(String source);
    List<Tuple> getDataFromSource() throws Exception;
    void insertDataIntoDB();
    List<?> getDataFromDB();
}
