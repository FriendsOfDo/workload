package de.friendsofdo.workload.controller;

import de.friendsofdo.workload.datastore.EventService;
import de.friendsofdo.workload.domain.Event;
import de.friendsofdo.workload.domain.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/{userId}/status")
public class StatusController {

    @Autowired
    private EventService eventService;

    @RequestMapping(produces = "application/json", method = RequestMethod.GET)
    public Status getStatus(@PathVariable("userId") String userId) {
        List<Event> lastEvent = eventService.list(userId, 1);

        if (lastEvent == null || lastEvent.isEmpty()) {
            return Status.newBuilder()
                    .atWork(false)
                    .build();
        } else if (lastEvent.get(0).getType() == Event.Type.OUT) {
            return Status.newBuilder()
                    .atWork(false)
                    .build();
        } else {
            return Status.newBuilder()
                    .atWork(true)
                    .build();
        }
    }
}
