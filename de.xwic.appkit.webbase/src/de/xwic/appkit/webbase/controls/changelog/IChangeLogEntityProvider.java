/**
 * 
 */
package de.xwic.appkit.webbase.controls.changelog;

import de.xwic.appkit.core.dao.IEntity;

/**
 * @author Alexandru Bledea
 * @since Feb 26, 2014
 */
public interface IChangeLogEntityProvider<I extends IEntity> {

	/**
	 * @return
	 */
	I getEntityForLog();
}
