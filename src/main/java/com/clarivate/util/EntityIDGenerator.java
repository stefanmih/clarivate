package com.clarivate.util;

import com.clarivate.entity.Date;
import com.clarivate.entity.Record;
import org.hibernate.Session;

public class EntityIDGenerator {

    public static Long getRecordId(Session session) {
        long records;
        try {
            records = session.createQuery("from Record", Record.class).stream().count() + 1;
            return records;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Long getDateId(Session session) {
        long records;
        try {
            records = session.createQuery("from Date", Date.class).stream().count() + 1;
            return records;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
