package com.example.ecommerce.utils;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Component
public class DateTimeMapperUtils {

    public String localDateToString(LocalDate date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return date.format(formatter);
    }

    public LocalDate stringToLocalDate(String dateString){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(dateString, formatter);
    }

    public String localDateTimeToString(LocalDateTime dateTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return dateTime.format(formatter);
    }

    public LocalDateTime stringToLocalDateTime(String dateTimeString){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        return LocalDateTime.parse(dateTimeString, formatter);
    }

    public String localDateTimeToHouseString(LocalDateTime dateTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");
        return dateTime.format(formatter);
    }

    public LocalDateTime htmlValueToLocalDateTime(String time){
        return LocalDateTime.parse(time, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    public String timestampToDate(Long leadtime) {
        LocalDateTime leadtimeDate = LocalDateTime.ofInstant(Instant.ofEpochSecond(leadtime), ZoneId.systemDefault());
        leadtimeDate = leadtimeDate.plusDays(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return leadtimeDate.format(formatter);
    }

    public long timestampToDateDifference(Long leadtime) {
        LocalDateTime leadTimeDate = LocalDateTime.ofInstant(Instant.ofEpochSecond(leadtime), ZoneId.systemDefault());
        LocalDate currentDate = LocalDate.now();
        long daysBetween = leadTimeDate.toLocalDate().toEpochDay() - currentDate.toEpochDay();
        if (daysBetween < 1) {
            daysBetween = 2;
        } else {
            daysBetween = 1 + daysBetween;
        }
        return daysBetween;
    }

    public LocalDateTime timestampToLocalDateTime(Long leadtime) {
        LocalDateTime leadtimeDate = LocalDateTime.ofInstant(Instant.ofEpochSecond(leadtime), ZoneId.systemDefault());
        leadtimeDate = leadtimeDate.plusDays(1);
        return leadtimeDate;
    }
}
