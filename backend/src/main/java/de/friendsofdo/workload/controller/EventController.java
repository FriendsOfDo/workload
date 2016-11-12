package de.friendsofdo.workload.controller;

import de.friendsofdo.workload.datastore.EventService;
import de.friendsofdo.workload.domain.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/{userId}/events")
public class EventController {

    @Autowired
    private EventService eventService;

    @RequestMapping(consumes = "application/json", produces = "application/json", method = RequestMethod.POST)
    public Event save(@RequestBody Event event) {
        return eventService.save(event);
    }

    @RequestMapping(produces = "application/json", method = RequestMethod.GET)
    public List<Event> list(@PathVariable("userId") String userId) {
        return eventService.list(userId);
    }
}
