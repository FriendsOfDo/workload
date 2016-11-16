package de.friendsofdo.workload.datastore;

import com.google.cloud.datastore.*;
import de.friendsofdo.workload.domain.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class EventService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventService.class);

    private static final String KIND = "Event";
    public static final String NAMESPACE = "Workload";

    @Autowired
    private Datastore datastore;

    private KeyFactory keyFactory;

    @PostConstruct
    public void init() {
        keyFactory = datastore.newKeyFactory().setKind(KIND).setNamespace(NAMESPACE);
    }

    public Event save(Event event) {
        IncompleteKey incKey = keyFactory.newKey();
        Key key = datastore.allocateId(incKey);

        Entity entity = constructEntity(key, event);

        LOGGER.debug("Save entity: {}", entity.toString());

        Entity added = datastore.add(entity);
        event.setId(added.getKey().getId());

        return event;
    }

    public List<Event> list(String userId) {
        return list(userId, Integer.MAX_VALUE);
    }

    public List<Event> list(String userId, int limit) {
        LOGGER.debug("List events for user {} with limit {}", userId, limit);

        EntityQuery query = Query.newEntityQueryBuilder()
                .setNamespace(NAMESPACE)
                .setKind(KIND)
                .setFilter(StructuredQuery.PropertyFilter.eq("userId", userId))
                .setOrderBy(StructuredQuery.OrderBy.desc("date"))
                .setLimit(limit)
                .build();

        QueryResults<Entity> result = datastore.run(query);
        return transformResult(result);
    }

    public List<Event> list(String userId, Date from, Date to) {
        LOGGER.debug("List events for user {} between from {} and {}", userId, from, to);

        EntityQuery query = Query.newEntityQueryBuilder()
                .setNamespace(NAMESPACE)
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

    private Entity constructEntity(Key key, Event event) {
        return Entity.newBuilder(key)
                .set("userId", event.getUserId())
                .set("date", DateTime.copyFrom(event.getDate()))
                .set("type", event.getType().name())
                .set("lat", event.getLat())
                .set("lon", event.getLon())
                .build();
    }

    private Event constructEvent(Entity entity) {
        return Event.newBuilder()
                .type(Event.Type.valueOf(entity.getString("type")))
                .date(entity.getDateTime("date").toDate())
                .userId(entity.getString("userId"))
                .lat(entity.getDouble("lat"))
                .lon(entity.getDouble("lon"))
                .id(entity.getKey().getId())
                .build();
    }

    private List<Event> transformResult(QueryResults<Entity> result) {
        final List<Event> events = new ArrayList<>();

        result.forEachRemaining(entity -> events.add(constructEvent(entity)));

        return events;
    }
}
