package de.friendsofdo.workload.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import de.friendsofdo.workload.util.DateUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class WorkDay {

    public static class Builder {

        private LocalDate date;
        private List<Event> events;
        private int workingTime;
        private int pauseTime;

        public Builder(LocalDate date) {
            this.date = date;
            this.workingTime = 0;
            this.pauseTime = 0;
            this.events = new ArrayList<>();
        }

        public Builder add(Event event) {
            LocalDate eventDate = DateUtils.toLocalDate(event.getDate());
            if (eventDate.isEqual(date)) {
                events.add(event);
            }
            return this;
        }

        public WorkDay build() {
            events.sort((one, two) -> one.getDate().compareTo(two.getDate()));

            for (int idx = 1; idx < events.size(); idx++) {
                Event previous = events.get(idx - 1);
                Event current = events.get(idx);

                if (previous.getType() == Event.Type.IN && current.getType() == Event.Type.OUT) {
                    LocalDateTime start = DateUtils.toLocalDateTime(previous.getDate());
                    LocalDateTime end = DateUtils.toLocalDateTime(current.getDate());

                    workingTime += ChronoUnit.MINUTES.between(start, end);
                } else if (previous.getType() == Event.Type.OUT && current.getType() == Event.Type.IN) {
                    LocalDateTime start = DateUtils.toLocalDateTime(previous.getDate());
                    LocalDateTime end = DateUtils.toLocalDateTime(current.getDate());

                    pauseTime += ChronoUnit.MINUTES.between(start, end);
                }
            }

            WorkDay workDay = new WorkDay();
            workDay.date = date;
            workDay.workingTime = workingTime;
            workDay.pauseTime = pauseTime;
            workDay.events = new ArrayList<>(events);

            return workDay;
        }
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDate date;
    private int workingTime;
    private int pauseTime;
    private List<Event> events;

    private WorkDay() {
    }

    public LocalDate getDate() {
        return date;
    }

    public int getWorkingTime() {
        return workingTime;
    }

    public int getPauseTime() {
        return pauseTime;
    }

    public List<Event> getEvents() {
        return events;
    }

    @Override
    public String toString() {
        return "WorkDay{" +
                "date=" + date +
                ", workingTime=" + workingTime +
                ", pauseTime=" + pauseTime +
                ", events=" + events +
                '}';
    }

    public static Builder newBuilder(LocalDate date) {
        return new Builder(date);
    }
}
