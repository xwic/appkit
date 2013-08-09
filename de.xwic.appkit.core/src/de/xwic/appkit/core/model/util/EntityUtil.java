/**
 *
 */
package de.xwic.appkit.core.model.util;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.util.CollectionUtil;
import de.xwic.appkit.core.util.IEvaluator;

/**
 * @author Alexandru Bledea
 * @since Jul 1, 2013
 */
public class EntityUtil {

	public final static IEvaluator<IEntity, Integer> ENTITY_ID_EVALUATOR = new IEvaluator<IEntity, Integer>() {

		@Override
		public Integer evaluate(IEntity obj) {
			return obj.getId();
		}

	};

	/**
	 * @param collection
	 * @return
	 */
	public static Set<Integer> getIds(Collection<? extends IEntity> collection) {
		return CollectionUtil.createCollection(collection, ENTITY_ID_EVALUATOR, new HashSet<Integer>());

	}
}
