package de.friendsofdo.workload.datastore;

import com.google.cloud.datastore.*;
import de.friendsofdo.workload.domain.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class EventService extends AbstractDatastoreService<Event> {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventService.class);

    private static final String NAMESPACE = "Workload";
    private static final String KIND = "Event";

    public EventService() {
        super(KIND, NAMESPACE);
    }

    @Override
    protected Entity constructEntity(Key key, Event event) {
        return Entity.newBuilder(key)
                .set("userId", event.getUserId())
                .set("date", DateTime.copyFrom(event.getDate()))
                .set("type", event.getType().name())
                .set("lat", event.getLat())
                .set("lon", event.getLon())
                .build();
    }

    @Override
    protected Event constructItem(Entity entity) {
        return Event.newBuilder()
                .id(entity.getKey().getId())
                .userId(entity.getString("userId"))
                .type(Event.Type.valueOf(entity.getString("type")))
                .date(entity.getDateTime("date").toDate())
                .lat(entity.getDouble("lat"))
                .lon(entity.getDouble("lon"))
                .build();
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
}
