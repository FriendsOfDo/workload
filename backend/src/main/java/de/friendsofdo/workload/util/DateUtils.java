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
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return LocalDate.of(
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH)+1,
                cal.get(Calendar.DAY_OF_MONTH));
    }

    public static LocalDateTime toLocalDateTime(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return LocalDateTime.of(
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH)+1,
                cal.get(Calendar.DAY_OF_MONTH),
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE));
    }
}
