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

    @RequestMapping(method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    public Event save(@PathVariable("userId") String userId, @RequestBody Event event) {
        event.setUserId(userId);
        return eventService.save(event);
    }

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public List<Event> list(@PathVariable("userId") String userId) {
        return eventService.list(userId);
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.GET, produces = "application/json", consumes = "application/json")
    public Event get(@PathVariable("userId") String userId, @PathVariable("id") long id) {
        return eventService.get(id);
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("userId") String userId, @PathVariable("id") long id) {
        eventService.delete(id);
    }
}
