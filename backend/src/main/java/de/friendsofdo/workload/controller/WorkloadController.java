package de.friendsofdo.workload.controller;

import de.friendsofdo.workload.datastore.EventService;
import de.friendsofdo.workload.domain.Event;
import de.friendsofdo.workload.domain.WorkWeek;
import de.friendsofdo.workload.util.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/{userId}/workloads")
public class WorkloadController {

    private static final Logger LOGGER = LoggerFactory.getLogger(WorkloadController.class);
    private static final Locale DEFAULT_LOCALE = Locale.GERMANY;

    @Autowired
    private EventService eventService;

    @RequestMapping(path = "/{year}/{weekOfYear}", produces = "application/json", method = RequestMethod.GET)
    public WorkWeek get(@PathVariable("userId") String userId, @PathVariable("year") int year, @PathVariable("weekOfYear") int weekOfYear) {
        WeekFields weekFields = WeekFields.of(DEFAULT_LOCALE);
        LocalDate fromDate = LocalDate.now().withYear(year).with(weekFields.weekOfYear(), weekOfYear).with(weekFields.dayOfWeek(), 1);
        LocalDate toDate = LocalDate.now().withYear(year).with(weekFields.weekOfYear(), weekOfYear).with(weekFields.dayOfWeek(), 7);

        LOGGER.info("Return events between {} and {}",
                fromDate.format(DateTimeFormatter.ISO_LOCAL_DATE),
                toDate.format(DateTimeFormatter.ISO_LOCAL_DATE));

        Date from = transformFrom(fromDate);
        Date to = transformTo(toDate);

        List<Event> events = eventService.list(userId, from, to);
        WorkWeek.Builder builder = new WorkWeek.Builder(year, weekOfYear);

        events.forEach(builder::add);

        WorkWeek week = builder.build();
        return week;
    }

    private Date transformFrom(LocalDate fromDate) {
        Date from = DateUtils.toDate(fromDate);
        from.setHours(00);
        from.setMinutes(00);
        return from;
    }

    private Date transformTo(LocalDate toDate) {
        Date to = DateUtils.toDate(toDate);
        to.setHours(23);
        to.setMinutes(59);
        return to;
    }
}
