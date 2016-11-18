package de.friendsofdo.workload.controller;

import de.friendsofdo.workload.datastore.WorkplaceService;
import de.friendsofdo.workload.domain.Workplace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/{userId}/workplaces")
public class WorkplaceController {

    @Autowired
    private WorkplaceService workplaceService;

    @RequestMapping(method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    public Workplace save(@PathVariable("userId") String userId, @RequestBody Workplace workplace) {
        workplace.setUserId(userId);
        return workplaceService.save(workplace);
    }

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public List<Workplace> list(@PathVariable("userId") String userId) {
        return workplaceService.list(userId);
    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/{id}")
    public void delete(@PathVariable("userId") String userId, @PathVariable("id") long id) {
        workplaceService.delete(id);
    }
}
