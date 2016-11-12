package de.friendsofdo.workload.prepare;

import de.friendsofdo.workload.domain.Event;

import java.util.*;

public class EventGenerator {

    private static final String USER_ID_A = "user1";
    private static final String USER_ID_B = "user2";

    public static List<Event> generateEvents() {
        List<Event> events = new ArrayList<>();

        events.add(Event.newBuilder()
                .id(1)
                .userId(USER_ID_A)
                .date(dateOf(2016, 11, 7, 7, 50))
                .type(Event.Type.IN)
                .build());

        events.add(Event.newBuilder()
                .id(2)
                .userId(USER_ID_A)
                .date(dateOf(2016, 11, 7, 12, 5))
                .type(Event.Type.OUT)
                .build());

        events.add(Event.newBuilder()
                .id(3)
                .userId(USER_ID_A)
                .date(dateOf(2016, 11, 7, 13, 0))
                .type(Event.Type.IN)
                .build());

        events.add(Event.newBuilder()
                .id(4)
                .userId(USER_ID_A)
                .date(dateOf(2016, 11, 7, 18, 10))
                .type(Event.Type.OUT)
                .build());

        events.add(Event.newBuilder()
                .id(5)
                .userId(USER_ID_A)
                .date(dateOf(2016, 11, 8, 8, 50))
                .type(Event.Type.IN)
                .build());

        events.add(Event.newBuilder()
                .id(6)
                .userId(USER_ID_A)
                .date(dateOf(2016, 11, 8, 12, 15))
                .type(Event.Type.OUT)
                .build());

        events.add(Event.newBuilder()
                .id(7)
                .userId(USER_ID_A)
                .date(dateOf(2016, 11, 8, 13, 20))
                .type(Event.Type.IN)
                .build());

        events.add(Event.newBuilder()
                .id(8)
                .userId(USER_ID_A)
                .date(dateOf(2016, 11, 8, 18, 0))
                .type(Event.Type.OUT)
                .build());

        events.add(Event.newBuilder()
                .id(9)
                .userId(USER_ID_A)
                .date(dateOf(2016, 11, 9, 9, 5))
                .type(Event.Type.IN)
                .build());

        events.add(Event.newBuilder()
                .id(10)
                .userId(USER_ID_A)
                .date(dateOf(2016, 11, 9, 13, 10))
                .type(Event.Type.OUT)
                .build());

        events.add(Event.newBuilder()
                .id(11)
                .userId(USER_ID_A)
                .date(dateOf(2016, 11, 9, 13, 20))
                .type(Event.Type.IN)
                .build());

        events.add(Event.newBuilder()
                .id(12)
                .userId(USER_ID_A)
                .date(dateOf(2016, 11, 9, 19, 5))
                .type(Event.Type.OUT)
                .build());

        return events;
    }

    private static Date dateOf(int year, int month, int day, int hour, int minute) {
        Calendar cal = new GregorianCalendar(year, month - 1, day, hour, minute);
        return cal.getTime();
    }
}
