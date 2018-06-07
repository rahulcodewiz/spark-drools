package com.ts.blog.rules;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 */
public class KieFunctions {
    private static final Logger logger = LogManager.getLogger(KieFunctions.class);
    public static boolean dateCompare(Date dt, String year){
        try {
            Objects.requireNonNull(dt,"dt cant be null");
            Objects.requireNonNull(year,"dt cant be null");
            return new SimpleDateFormat("yyyy").parse(year).compareTo(dt)>0;
        } catch (Exception e) {
            return false;
        }
    }
}
