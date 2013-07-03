/**
 *
 */
package de.xwic.appkit.core.model.util;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import de.xwic.appkit.core.dao.IEntity;

/**
 * @author Alexandru Bledea
 * @since Jul 1, 2013
 */
public class EntityUtil {

	/**
	 * @param collection
	 * @return
	 */
	public static Set<Integer> getIds(Collection<? extends IEntity> collection) {
		Set<Integer> ids = new HashSet<Integer>();
		for (IEntity iEntity : collection) {
			ids.add(iEntity.getId());
		}
		return ids;
	}
}
