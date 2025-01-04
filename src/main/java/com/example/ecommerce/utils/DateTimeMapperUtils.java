package com.example.ecommerce.utils;

import org.springframework.stereotype.Component;

import java.time.*;
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

    public String formatFacebookTime(LocalDateTime createdAt) {
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(createdAt, now);
        if (duration.isNegative()) {
            return "Thời gian không hợp lệ";
        }
        long seconds = duration.getSeconds();
        long minutes = duration.toMinutes();
        long hours = duration.toHours();
        long days = duration.toDays();
        if (seconds < 60) {
            return "Vừa xong";
        } else if (minutes < 60) {
            return minutes + " phút trước";
        } else if (hours < 24) {
            return hours + " giờ trước";
        } else if (days < 3) {
            return days + " ngày trước";
        } else if (days < 365) {
            DateTimeFormatter monthDayFormatter = DateTimeFormatter.ofPattern("d 'tháng' M");
            return createdAt.format(monthDayFormatter) + " lúc " + createdAt.format(DateTimeFormatter.ofPattern("HH:mm"));
        } else {
            DateTimeFormatter yearFormatter = DateTimeFormatter.ofPattern("'tháng' M 'năm' yyyy");
            return createdAt.format(yearFormatter);
        }
    }
}
