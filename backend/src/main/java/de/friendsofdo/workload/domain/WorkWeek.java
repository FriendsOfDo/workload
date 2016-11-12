package de.friendsofdo.workload.domain;

import de.friendsofdo.workload.util.DateUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorkWeek {

    public static class Builder {
        private int year;
        private int calendarWeek;
        private int workingTime;
        private int pauseTime;
        private List<WorkDay> workDays;

        private Map<LocalDate, WorkDay.Builder> workDayBuilders;

        public Builder(int year, int calendarWeek) {
            this.year = year;
            this.calendarWeek = calendarWeek;
            this.workingTime = 0;
            this.pauseTime = 0;
            this.workDayBuilders = new HashMap<>();
            this.workDays = new ArrayList<>(7);
        }

        public Builder add(Event event) {
            LocalDate localDate = DateUtils.toLocalDate(event.getDate());

            WorkDay.Builder workDayBuilder = this.workDayBuilders.get(localDate);
            if (workDayBuilder == null) {
                workDayBuilder = new WorkDay.Builder(localDate);
                workDayBuilders.put(localDate, workDayBuilder);
            }

            workDayBuilder.add(event);

            return this;
        }

        public WorkWeek build() {
            workDayBuilders.forEach((localDate, builder) -> {
                WorkDay workDay = builder.build();
                workingTime += workDay.getWorkingTime();
                pauseTime += workDay.getPauseTime();
                workDays.add(workDay);
            });

            WorkWeek week = new WorkWeek();
            week.year = year;
            week.calendarWeek = calendarWeek;
            week.workingTime = workingTime;
            week.pauseTime = pauseTime;
            week.workDays = workDays;

            return week;
        }
    }

    private int year;
    private int calendarWeek;
    private int workingTime;
    private int pauseTime;
    private List<WorkDay> workDays;

    private WorkWeek() {
    }

    public int getYear() {
        return year;
    }

    public int getCalendarWeek() {
        return calendarWeek;
    }

    public int getWorkingTime() {
        return workingTime;
    }

    public int getPauseTime() {
        return pauseTime;
    }

    public List<WorkDay> getWorkDays() {
        return workDays;
    }

    @Override
    public String toString() {
        return "WorkWeek{" +
                "calendarWeek=" + calendarWeek +
                ", workingTime=" + workingTime +
                ", pauseTime=" + pauseTime +
                ", workDays=" + workDays +
                '}';
    }
}
