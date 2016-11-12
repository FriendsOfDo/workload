package de.friendsofdo.workload.datastore;

import com.google.cloud.datastore.*;
import de.friendsofdo.workload.domain.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Component
public class EventService {

    private static final String KIND = "Event";

    @Autowired
    private Datastore datastore;

    private KeyFactory keyFactory;

    @PostConstruct
    public void init() {
        keyFactory = datastore.newKeyFactory().setKind(KIND);
    }

    public Event save(Event event) {
        IncompleteKey incKey = keyFactory.newKey();
        Key key = datastore.allocateId(incKey);

        Entity entity = Entity.newBuilder(key)
                .set("userId", event.getUserId())
                .set("date", DateTime.copyFrom(event.getDate()))
                .set("type", event.getType().name())
                .build();

        Entity added = datastore.add(entity);
        event.setId(added.getKey().getId());

        return event;
    }

    public List<Event> list() {
        List<Event> events = new ArrayList<>();

        EntityQuery query = Query.newEntityQueryBuilder()
                .setKind(KIND)
                .setOrderBy(StructuredQuery.OrderBy.asc("date"))
                .build();

        QueryResults<Entity> result = datastore.run(query);
        result.forEachRemaining(entity -> events.add(Event.newBuilder()
                .type(Event.Type.valueOf(entity.getString("type")))
                .date(entity.getDateTime("date").toDate())
                .userId(entity.getString("userId"))
                .id(entity.getKey().getId())
                .build()));

        return events;
    }
}
