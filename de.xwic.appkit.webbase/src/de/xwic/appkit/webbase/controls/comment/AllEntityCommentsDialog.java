/**
 * 
 */
package de.xwic.appkit.webbase.controls.comment;

import de.jwic.base.Control;
import de.jwic.base.Dimension;
import de.jwic.base.Page;
import de.jwic.controls.ScrollableContainer;
import de.xwic.appkit.webbase.dialog.AbstractDialogWindow;
import de.xwic.appkit.webbase.dialog.DialogContent;
import de.xwic.appkit.webbase.toolkit.app.Site;

/**
 * @author lippisch
 *
 */
@SuppressWarnings("serial")
public class AllEntityCommentsDialog extends AbstractDialogWindow {

	private String entityType;
	private long entityId;
	private String category;
	private boolean allowEdit;

	/**
	 * @param site
	 * @param entityId
	 * @param entityType
	 * @param category
	 */
	public AllEntityCommentsDialog(Site site, String entityType, long entityId, String category, boolean allowEdit) {
		super(site);
		this.entityType = entityType;
		this.entityId = entityId;
		this.category = category;
		this.allowEdit = allowEdit;

		setTitle("All Comments");

		Dimension pageSize = Page.findPage((Control) site.getContentContainer()).getPageSize();
		setWidth((int) (pageSize.width * 0.8d));
		setHeight(pageSize.height - 250);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.webbase.dialog.AbstractDialogWindow#createContent(de.xwic.appkit.webbase.dialog.DialogContent)
	 */
	@Override
	protected void createContent(DialogContent content) {

		ScrollableContainer sc = new ScrollableContainer(content, "dlgContent");
		sc.setHeight((getHeight() - 70) + "px");
		sc.setWidth((getWidth() - 10) + "px");

		new EntityCommentList(sc, "entityCommentList", entityType, entityId, category, true, allowEdit, 0);
	}

}
