/**
 *
 */
package de.xwic.appkit.core.dao.util;

import de.xwic.appkit.core.dao.IEntity;

/**
 * @author Alexandru Bledea
 * @since Oct 22, 2014
 */
public final class Entities {

	public static final int NEW_ENTITY_ID = 0;
	public static final int LOWEST_POSSIBLE_ID = 1;

	/**
	 *
	 */
	private Entities() {
	}

	/**
	 * @param entity
	 * @return
	 */
	public static int getId(final IEntity entity) {
		if (entity != null) {
			return entity.getId();
		}
		return Entities.NEW_ENTITY_ID;
	}

	/**
	 * @param entity
	 * @return
	 */
	public static Integer getIdOrNull(final IEntity entity) {
		if (entity == null) {
			return null;
		}
		return entity.getId();
	}

}
