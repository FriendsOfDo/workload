package de.friendsofdo.workload.controller;

import de.friendsofdo.workload.domain.WorkWeek;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/{userId}/workloads")
public class WorkloadController {

    private static final Logger LOGGER = LoggerFactory.getLogger(WorkloadController.class);

    @RequestMapping(produces = "application/json", method = RequestMethod.GET)
    public List<WorkWeek> list() {
        // TODO

        return new ArrayList<>();
    }
}
