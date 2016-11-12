package de.friendsofdo.workload.datastore;

import com.google.cloud.datastore.*;
import de.friendsofdo.workload.domain.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Date;
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

    public List<Event> list(String userId) {
        return list(userId, Integer.MAX_VALUE);
    }

    public List<Event> list(String userId, int limit) {
        EntityQuery query = Query.newEntityQueryBuilder()
                .setKind(KIND)
                .setFilter(StructuredQuery.PropertyFilter.eq("userId", userId))
                .setOrderBy(StructuredQuery.OrderBy.desc("date"))
                .setLimit(limit)
                .build();

        QueryResults<Entity> result = datastore.run(query);
        return transformResult(result);
    }

    public List<Event> list(String userId, Date from, Date to) {
        EntityQuery query = Query.newEntityQueryBuilder()
                .setKind(KIND)
                .setFilter(StructuredQuery.CompositeFilter.and(
                        StructuredQuery.PropertyFilter.eq("userId", userId),
                        StructuredQuery.PropertyFilter.ge("date", DateTime.copyFrom(from)),
                        StructuredQuery.PropertyFilter.le("date", DateTime.copyFrom(to))
                ))
                .setOrderBy(StructuredQuery.OrderBy.desc("date"))
                .build();

        QueryResults<Entity> result = datastore.run(query);
        return transformResult(result);
    }

    private List<Event> transformResult(QueryResults<Entity> result) {
        final List<Event> events = new ArrayList<>();

        result.forEachRemaining(entity -> events.add(Event.newBuilder()
                .type(Event.Type.valueOf(entity.getString("type")))
                .date(entity.getDateTime("date").toDate())
                .userId(entity.getString("userId"))
                .id(entity.getKey().getId())
                .build()));

        return events;
    }
}