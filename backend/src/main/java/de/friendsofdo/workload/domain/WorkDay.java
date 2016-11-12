package de.friendsofdo.workload.domain;

import java.util.Date;

public class WorkDay {

    private Date date;
    private int workingTime;
    private int pauseTime;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
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

    @Override
    public String toString() {
        return "WorkDay{" +
                "date=" + date +
                ", workingTime=" + workingTime +
                ", pauseTime=" + pauseTime +
                '}';
    }
}
