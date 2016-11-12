package de.friendsofdo.workload.domain;

import java.util.ArrayList;
import java.util.List;

public class WorkWeek {

    private int calendarWeek;
    private int workingTime;
    private int pauseTime;
    private List<WorkDay> workDays;

    public WorkWeek(int calendarWeek) {
        this.calendarWeek = calendarWeek;
        this.workDays = new ArrayList<>(5);
    }

    public int getCalendarWeek() {
        return calendarWeek;
    }

    public void setCalendarWeek(int calendarWeek) {
        this.calendarWeek = calendarWeek;
    }

    public int getWorkingTime() {
        return workingTime;
    }

    public void setWorkingTime(int workingTime) {
        this.workingTime = workingTime;
    }

    public int getPauseTime() {
        return pauseTime;
    }

    public void setPauseTime(int pauseTime) {
        this.pauseTime = pauseTime;
    }

    public List<WorkDay> getWorkDays() {
        return workDays;
    }

    public void addWorkDay(WorkDay workDay) {
        workingTime += workDay.getWorkingTime();
        pauseTime += workDay.getPauseTime();

        workDays.add(workDay);
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
