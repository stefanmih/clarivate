package com.clarivate.util;

import java.nio.charset.StandardCharsets;

public class StringUtil {
    public static boolean isEmpty(String original){
        if(original == null)
            return true;
        String str = original.replaceAll("(^\\h*)|(\\h*$)","");
        if(str.length() == 0)
            return true;
        for(char c : str.toCharArray()){
            if(c != ' ')
                return false;
        }
        return true;
    }

    public static boolean isAlphabetic(String str){
        return !isNumeric(str);
    }

    public static boolean isNumeric(String str){
        for(char c : str.toCharArray()){
            if(Character.isAlphabetic(c) || c == ' ')
                return false;
        }
        return true;
    }
}
