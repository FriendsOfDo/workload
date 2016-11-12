package de.friendsofdo.workload.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtils {

    public static Date toDate(LocalDate localDate) {
        Calendar cal = new GregorianCalendar(
                localDate.getYear(),
                localDate.getMonthValue() - 1, // Gregorian calendar's month is 0 based
                localDate.getDayOfMonth());

        return cal.getTime();
    }

    public static LocalDate toLocalDate(Date date) {
        return LocalDate.of(
                date.getYear(),
                date.getMonth(),
                date.getDay());
    }

    public static LocalDateTime toLocalDateTime(Date date) {
        return LocalDateTime.of(
                date.getYear(),
                date.getMonth(),
                date.getDay(),
                date.getHours(),
                date.getMinutes());
    }
}
