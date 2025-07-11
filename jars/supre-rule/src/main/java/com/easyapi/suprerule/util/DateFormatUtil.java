package com.easyapi.suprerule.util;

import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Component
public class DateFormatUtil {

    public String datetimeToDatetime(String fromDatetime, String fromDatetimePattern, String toDatetimePattern) {
       try {
           DateFormat fromDateFormat = new SimpleDateFormat(fromDatetimePattern);
           Date parseDate = fromDateFormat.parse(fromDatetime);
           DateFormat toDateFormat = new SimpleDateFormat(toDatetimePattern);
           String newDateString = toDateFormat.format(parseDate);
           return newDateString;
       }catch (Exception e) {
            e.printStackTrace();
            System.out.println();
       }
       return null;
    }

    public String timestampToDatetime(Long timestamp, String datetimePattern) {
        Long secondMillis = timestamp.toString().length() == 10 ? timestamp * 1000 : timestamp;
        LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(secondMillis), ZoneId.systemDefault());
        return dateTime.format(DateTimeFormatter.ofPattern(datetimePattern));
    }
}
