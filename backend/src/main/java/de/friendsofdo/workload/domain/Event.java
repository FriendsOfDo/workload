package de.friendsofdo.workload.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class Event {

    public enum Type {
        IN,
        OUT
    }

    public static class Builder {

        private Event event;

        public Builder() {
            this.event = new Event();
        }

        public Builder type(Type type) {
            event.setType(type);
            return this;
        }

        public Builder userId(String userId) {
            event.setUserId(userId);
            return this;
        }

        public Builder id(long id) {
            event.setId(id);
            return this;
        }

        public Builder date(Date date) {
            event.setDate(date);
            return this;
        }

        public Builder lat(double lat) {
            event.setLat(lat);
            return this;
        }

        public Builder lon(double lon) {
            event.setLon(lon);
            return this;
        }

        public Event build() {
            return event;
        }
    }

    private long id;
    private String userId;
    private Type type;
    private double lat;
    private double lon;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date date;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", type=" + type +
                ", lat=" + lat +
                ", lon=" + lon +
                ", date=" + date +
                '}';
    }

    public static Builder newBuilder() {
        return new Builder();
    }
}
