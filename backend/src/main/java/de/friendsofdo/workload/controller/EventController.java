package de.friendsofdo.workload.controller;

import de.friendsofdo.workload.domain.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/events")
public class EventController {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventController.class);

    @RequestMapping(consumes = "application/json", produces = "application/json", method = RequestMethod.POST)
    public Event save(@RequestBody Event event) {
        LOGGER.info("POST /events  {}", event);

        // TODO persist

        return event;
    }

    @RequestMapping(produces = "application/json", method = RequestMethod.GET)
    public List<Event> list() {
        LOGGER.info("GET /events");

        // TODO

        return new ArrayList<>();
    }
}
