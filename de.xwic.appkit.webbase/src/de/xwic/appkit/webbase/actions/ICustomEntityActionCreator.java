package de.xwic.appkit.webbase.actions;

import de.xwic.appkit.webbase.toolkit.app.Site;

/**
 * @author <a href="mailto:vzhovtiuk@gmail.com">Vitaliy Zhovtyuk</a>
 */
public interface ICustomEntityActionCreator {

   IEntityAction createAction(final Site site, String entityType, String id);
}
