package de.friendsofdo.workload.domain;

import de.friendsofdo.workload.prepare.EventGenerator;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.*;

import static junit.framework.Assert.assertEquals;

public class WorkDayBuilderTest {

    private List<Event> events = new ArrayList<>();

    @Before
    public void setup() {
        events = EventGenerator.generateEvents();
    }

    @Test
    public void testBuild() {
        LocalDate localDate = LocalDate.of(2016, 11, 7);
        WorkDay.Builder builder = WorkDay.newBuilder(localDate);
        events.forEach(builder::add);

        WorkDay workDay = builder.build();

        assertEquals("WorkDay's date is incorrect", localDate, workDay.getDate());
        assertEquals("WorkDay's number of events is incorrect", 4, workDay.getEvents().size());
        assertEquals("WorkDay's working time calculation is incorrect", 565, workDay.getWorkingTime());
        assertEquals("WorkDay's pause time calculation is incorrect", 55, workDay.getPauseTime());
    }

    private Date dateOf(int year, int month, int day, int hour, int minute) {
        Calendar cal = new GregorianCalendar(year, month - 1, day, hour, minute);
        return cal.getTime();
    }
}