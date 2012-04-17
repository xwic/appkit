/**
 * 
 */
package de.xwic.appkit.webbase.toolkit.util;

import de.xwic.appkit.core.dao.DAO;
import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.model.entities.IPicklistEntry;

/**
 * Helper for velocity.
 * 
 * @author Ronny Pfretzschner
 *
 */
public class VtlTool {
	
	/**
	 * Get the text of the picklistentry.
	 * 
	 * @param entry
	 * @param langId
	 * @return
	 */
    public String getText(IPicklistEntry entry, String langId) {
        return entry.getBezeichnung(langId);
    }

    /**
     * Returns the title, specified in the configuration, of the
     * given entity.
     * 
     * @param entity
     * @return
     */
    public String getTitle(IEntity entity) {
        DAO dao = DAOSystem.findDAOforEntity(entity.type());
        if (dao != null) {
            return dao.buildTitle(entity);
        }
        return "[NO DAO for " + entity.type() + "]";
    }

}
