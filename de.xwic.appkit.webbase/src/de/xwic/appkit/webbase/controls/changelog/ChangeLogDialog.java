/**
 * 
 */
package de.xwic.appkit.webbase.controls.changelog;

import de.jwic.base.Page;
import de.jwic.controls.ScrollableContainer;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.webbase.dialog.AbstractDialogWindow;
import de.xwic.appkit.webbase.dialog.DialogContent;
import de.xwic.appkit.webbase.toolkit.app.Site;


/**
 * This dialog displays the change log of the specified entity.
 * @author lippisch
 */
public class ChangeLogDialog extends AbstractDialogWindow {

	private IEntity entity;

	/**
	 * @param site
	 */
	public ChangeLogDialog(final Site site, final IEntity entity) {
		super(site);
		this.entity = entity;

		setWidth(800);

		int height = Page.findPage(site).getPageSize().height - 150;
		if (height < 400) {
			height = 400;
		}
		setHeight(height);

		setTitle("Change Log");
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.dialog.AbstractDialogWindow#createContent(de.xwic.appkit.webbase.dialog.DialogContent)
	 */
	@Override
	protected void createContent(final DialogContent content) {

		ScrollableContainer sc = new ScrollableContainer(content);
		sc.setHeight((getHeight() - 70) + "px");
		ChangeLogViewer clv = new ChangeLogViewer(sc, "clViewer", entity);
		setTitle("Change Log (" + clv.getObjectTitle() + ")");

	}

}
