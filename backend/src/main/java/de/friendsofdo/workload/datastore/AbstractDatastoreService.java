package de.friendsofdo.workload.datastore;

import com.google.cloud.datastore.*;
import de.friendsofdo.workload.domain.DatastoreEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractDatastoreService<T extends DatastoreEntity> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractDatastoreService.class);

    protected abstract Entity constructEntity(Key key, T item);

    protected abstract T constructItem(Entity entity);

    @Autowired
    protected Datastore datastore;

    protected KeyFactory keyFactory;

    private final String kind;
    private final String namespace;

    public AbstractDatastoreService(String kind, String namespace) {
        this.kind = kind;
        this.namespace = namespace;
    }

    @PostConstruct
    public void init() {
        keyFactory = datastore.newKeyFactory().setKind(kind).setNamespace(namespace);
    }

    public T save(T item) {
        IncompleteKey incKey = keyFactory.newKey();
        Key key = datastore.allocateId(incKey);

        Entity entity = constructEntity(key, item);

        LOGGER.debug("Save entity: {}", entity.toString());

        Entity added = datastore.add(entity);
        item.setId(added.getKey().getId());

        return item;
    }

    public void delete(long id) {
        Key key = keyFactory.newKey(id);
        datastore.delete(key);
    }

    public T get(long id) {
        LOGGER.debug("Get item by id: {}", id);

        Key key = keyFactory.newKey(id);
        Entity entity = datastore.get(key);

        return constructItem(entity);
    }

    protected List<T> transformResult(QueryResults<Entity> result) {
        final List<T> items = new ArrayList<>();

        result.forEachRemaining(entity -> items.add(constructItem(entity)));

        return items;
    }
}
