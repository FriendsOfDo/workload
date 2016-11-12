package de.friendsofdo.workload.controller;

import de.friendsofdo.workload.datastore.EventService;
import de.friendsofdo.workload.domain.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/events")
public class EventController {

    @Autowired
    private EventService eventService;

    @RequestMapping(consumes = "application/json", produces = "application/json", method = RequestMethod.POST)
    public Event save(@RequestBody Event event) {
        return eventService.save(event);
    }

    @RequestMapping(produces = "application/json", method = RequestMethod.GET)
    public List<Event> list() {
        return eventService.list();
    }
}
