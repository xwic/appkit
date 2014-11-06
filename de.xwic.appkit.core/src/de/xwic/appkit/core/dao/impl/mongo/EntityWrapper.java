package de.xwic.appkit.core.dao.impl.mongo;

import de.xwic.appkit.core.dao.Entity;
import de.xwic.appkit.core.dao.IEntity;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.PrePersist;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;

/**
 * @author Vitaliy Zhovtyuk
 * @version 31.10.2014
 */
public class EntityWrapper<T extends IEntity> extends Entity implements IEntityWrapper<T> {
    @Id
    private int id;

    @Embedded
    private T entity;

    @Override
    public int getId() {
        return id;
    }

    public T getEntity() {
        return entity;
    }

    public void setEntity(T entity) {
        this.entity = entity;
    }

    public void setId(int id) {
        this.id = id;
    }

    public EntityWrapper(Datastore ds) {
        this.ds = ds;
    }

    @Override
    public String toString() {
        return "EntityWrapper{" +
                "id=" + id +
                ", entity=" + entity +
                '}';
    }

    protected transient final Datastore ds;

    @PrePersist
    void prePersist(){
        if (id == 0) {
            String collName = ds.getCollection(entity.getClass()).getName();
            Query<StoredId> q = ds.find(StoredId.class, "_id", collName);
            UpdateOperations<StoredId> uOps = ds.createUpdateOperations(StoredId.class).inc("value");
            StoredId newId = ds.findAndModify(q, uOps);
            if (newId == null) {
                newId = new StoredId(collName);
                ds.save(newId);
            }

            id = newId.getValue();
            entity.setId(id);
        }
    }

    @org.mongodb.morphia.annotations.Entity(value="ids", noClassnameStored=true)
    public static class StoredId {
        final @Id String className;
        protected Integer value = 1;

        public StoredId(String name) {
            className = name;
        }

        protected StoredId(){
            className = "";
        }

        public Integer getValue() {
            return value;
        }
    }
}
