/**
 * 
 */
package de.xwic.appkit.webbase.controls.changelog;

import org.apache.commons.lang.Validate;

import de.jwic.base.SessionContext;
import de.jwic.controls.Button;
import de.jwic.events.SelectionEvent;
import de.jwic.events.SelectionListener;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.webbase.toolkit.app.ExtendedApplication;
import de.xwic.appkit.webbase.toolkit.app.Site;

/**
 * @author Alexandru Bledea
 * @since Feb 26, 2014
 */
public class ChangeLogContainer {

	private final SessionContext sessionContext;

	/**
	 * @param button
	 * @param entityProvider
	 */
	public ChangeLogContainer(final Button button, final IChangeLogEntityProvider<?> entityProvider) {
		sessionContext = button.getSessionContext();
		button.setTitle("Change Log");
		button.addSelectionListener(new SelectionListener() {

			@Override
			public void objectSelected(final SelectionEvent event) {
				openForEntity(entityProvider.getEntityForLog());
			}
		});
	}

	/**
	 * @param entity
	 */
	private void openForEntity(final IEntity entity) {
		Validate.notNull(entity, "Cannot load the changelog");

		final Site site = ExtendedApplication.getSite(sessionContext);
		new ChangeLogDialog(site, entity).show();
	}

}
