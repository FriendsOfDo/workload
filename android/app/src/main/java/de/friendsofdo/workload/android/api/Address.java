package de.friendsofdo.workload.android.api;

public class Address {

    private String street;
    private String number;
    private String postcode;
    private String city;

    private Address(Builder builder) {
        setStreet(builder.street);
        setNumber(builder.number);
        setPostcode(builder.postcode);
        setCity(builder.city);
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }


    public static final class Builder {
        private String street;
        private String number;
        private String postcode;
        private String city;

        public Builder() {
        }

        public Builder street(String val) {
            street = val;
            return this;
        }

        public Builder number(String val) {
            number = val;
            return this;
        }

        public Builder postcode(String val) {
            postcode = val;
            return this;
        }

        public Builder city(String val) {
            city = val;
            return this;
        }

        public Address build() {
            return new Address(this);
        }
    }
}
