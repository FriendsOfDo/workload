package de.friendsofdo.workload.domain;

import de.friendsofdo.workload.prepare.EventGenerator;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class WorkWeekBuilderTest {

    private List<Event> events;

    @Before
    public void setup() {
        events = EventGenerator.generateEvents();
    }

    @Test
    public void testBuild() {
        WorkWeek.Builder builder = WorkWeek.newBuilder(2016, 45);
        events.forEach(builder::add);

        WorkWeek workWeek = builder.build();

        assertEquals("WorkWeek's year is incorrect", 2016, workWeek.getYear());
        assertEquals("WorkWeek's weekOfYear is incorrect", 45, workWeek.getCalendarWeek());
        assertEquals("WorkWeek's number of WorkDays is incorrect", 3, workWeek.getWorkDays().size());
        assertEquals("WorkWeek's working time calculation is incorrect", 1640, workWeek.getWorkingTime());
        assertEquals("WorkWeek's pause time calculation is incorrect", 130, workWeek.getPauseTime());
    }
}