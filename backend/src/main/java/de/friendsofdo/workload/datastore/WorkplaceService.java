package de.friendsofdo.workload.datastore;

import com.google.cloud.datastore.*;
import de.friendsofdo.workload.domain.Workplace;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WorkplaceService extends AbstractDatastoreService<Workplace> {

    private static final Logger LOGGER = LoggerFactory.getLogger(WorkplaceService.class);

    private static final String NAMESPACE = "Workload";
    private static final String KIND = "Workplace";

    public WorkplaceService() {
        super(KIND, NAMESPACE);
    }

    @Override
    protected Entity constructEntity(Key key, Workplace item) {
        return Entity.newBuilder(key)
                .set("userId", item.getUserId())
                .set("name", item.getName())
                .set("street", item.getStreet())
                .set("city", item.getCity())
                .set("postcode", item.getPostcode())
                .set("lat", item.getLat())
                .set("lon", item.getLon())
                .build();
    }

    @Override
    protected Workplace constructItem(Entity entity) {
        return Workplace.newBuilder()
                .id(entity.getKey().getId())
                .userId(entity.getString("userId"))
                .name(entity.getString("name"))
                .street(entity.getString("street"))
                .city(entity.getString("city"))
                .postcode(entity.getString("postcode"))
                .lat(entity.getDouble("lat"))
                .lon(entity.getDouble("lon"))
                .build();
    }

    public List<Workplace> list(String userId) {
        LOGGER.debug("List workplaces for user: {}", userId);

        EntityQuery query = Query.newEntityQueryBuilder()
                .setNamespace(NAMESPACE)
                .setKind(KIND)
                .setFilter(StructuredQuery.PropertyFilter.eq("userId", userId))
                .build();

        QueryResults<Entity> result = datastore.run(query);
        return transformResult(result);
    }
}
