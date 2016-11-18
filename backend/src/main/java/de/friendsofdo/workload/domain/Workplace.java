package de.friendsofdo.workload.domain;

public class Workplace implements DatastoreEntity {

    private long id;
    private String userId;

    private String name;

    private String street;
    private String city;
    private String postcode;

    private double lat;
    private double lon;

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

    public String getName() {
        return name;
    }

    public String getStreet() {
        return street;
    }

    public String getCity() {
        return city;
    }

    public String getPostcode() {
        return postcode;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static final class Builder {
        private long id;
        private String userId;
        private String name;
        private String street;
        private String city;
        private String postcode;
        private double lat;
        private double lon;

        public Builder id(long id) {
            this.id = id;
            return this;
        }

        public Builder userId(String userId) {
            this.userId = userId;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder street(String street) {
            this.street = street;
            return this;
        }

        public Builder city(String city) {
            this.city = city;
            return this;
        }

        public Builder postcode(String postcode) {
            this.postcode = postcode;
            return this;
        }

        public Builder lat(double lat) {
            this.lat = lat;
            return this;
        }

        public Builder lon(double lon) {
            this.lon = lon;
            return this;
        }

        public Workplace build() {
            Workplace workplace = new Workplace();
            workplace.id = this.id;
            workplace.userId = this.userId;
            workplace.street = this.street;
            workplace.lat = this.lat;
            workplace.city = this.city;
            workplace.lon = this.lon;
            workplace.name = this.name;
            workplace.postcode = this.postcode;
            return workplace;
        }
    }
}
