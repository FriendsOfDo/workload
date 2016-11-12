package de.friendsofdo.workload.domain;

public class Status {

    public static class Builder {

        private Status status;

        public Builder() {
            status = new Status();
        }

        public Builder atWork(boolean atWork) {
            status.setAtWork(atWork);
            return this;
        }

        public Status build() {
            return status;
        }
    }

    private boolean atWork;

    public boolean isAtWork() {
        return atWork;
    }

    public void setAtWork(boolean atWork) {
        this.atWork = atWork;
    }

    @Override
    public String toString() {
        return "Status{" +
                "atWork=" + atWork +
                '}';
    }

    public static Builder newBuilder() {
        return new Builder();
    }
}
