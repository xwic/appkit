/**
 *
 */
package de.xwic.appkit.webbase.toolkit.components;

import java.util.Set;

import de.xwic.appkit.core.dao.IEntity;

/**
 * @author Alexandru Bledea
 * @since Aug 16, 2013
 * @param <E>
 */
public interface IEntityListBoxMultiControl<E extends IEntity> {

	/**
	 * select the item corresponding to the given entity
	 *
	 * @param entity
	 */
	public void selectEntries(Set<E> entities);

	/**
	 * @return the Entity of the selected entry, can be null if nothing is selected
	 */
	public Set<E> getSelectedEntries();

}
