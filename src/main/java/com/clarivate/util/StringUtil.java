package com.clarivate.util;

import com.clarivate.entity.Date;
import com.clarivate.entity.Record;
import java.text.SimpleDateFormat;
import java.util.List;

public class StringUtil {
    public static boolean isEmpty(String original) {
        if (original == null)
            return true;
        String str = original.replaceAll("(^\\h*)|(\\h*$)", "").replace("\u00a0", " ");
        if (str.length() == 0)
            return true;
        for (char c : str.toCharArray()) {
            if (c != ' ')
                return false;
        }
        return true;
    }

    public static boolean isNumeric(String str) {
        for (char c : str.toCharArray()) {
            if (Character.isAlphabetic(c) || c == ' ')
                return false;
        }
        return true;
    }

    public static String printRecords(List<Record> records) {
        StringBuilder sb = new StringBuilder();
        for (Record record : records) {
            sb.append(String.format("%-30s|   ", record.getHoliday()));
            for (Date date : record.getDate()) {
                SimpleDateFormat formatter = new SimpleDateFormat("EEE, dd-MM-yyyy");
                String format = formatter.format(date.getDate());
                sb.append(String.format("%-20s", format));
            }
            sb.append("\n");
        }
        String dash = String.format("%20s%60s", "Holiday", "Dates (for years 2024, 2025, 2026)") + "\n" + "_".repeat(115) + "\n";
        return dash + sb;
    }
}
