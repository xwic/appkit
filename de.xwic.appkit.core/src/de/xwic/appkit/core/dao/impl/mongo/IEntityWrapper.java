package de.xwic.appkit.core.dao.impl.mongo;

import de.xwic.appkit.core.dao.IEntity;

/**
 * @author Vitaliy Zhovtyuk
 * @version 31.10.2014
 */
public interface IEntityWrapper<T extends IEntity> extends IEntity {
    T getEntity();
}
