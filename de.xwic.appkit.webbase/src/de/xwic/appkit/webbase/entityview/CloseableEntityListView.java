package de.xwic.appkit.webbase.entityview;

import java.util.Collection;


import de.jwic.base.IControlContainer;
import de.jwic.controls.Button;
import de.jwic.controls.ToolBarGroup;
import de.jwic.events.ElementSelectedEvent;
import de.jwic.events.ElementSelectedListener;
import de.jwic.events.SelectionEvent;
import de.jwic.events.SelectionListener;
import de.xwic.appkit.core.config.ConfigurationException;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.webbase.entityselection.EntitySelectionModel;
import de.xwic.appkit.webbase.entityselection.IEntitySelectionContributor;
import de.xwic.appkit.webbase.entityviewer.EntityListView;
import de.xwic.appkit.webbase.entityviewer.EntityListViewConfiguration;
import de.xwic.appkit.webbase.toolkit.util.ImageLibrary;

/**
 * <a href="mailto:vzhovtiuk@gmail.com">Vitaliy Zhovtyuk</a>
 *
 */
public class CloseableEntityListView<I extends IEntity> extends EntityListView<IEntity> {

	private Button btSelect;
	private Button btAbort;

	private final EntitySelectionModel model;

	/**
	 * 
	 * @param container
	 * @param name
	 * @param configuration
	 * @param contributor
	 * @throws ConfigurationException
	 */
	public CloseableEntityListView(IControlContainer container, String name, EntityListViewConfiguration configuration,
			IEntitySelectionContributor contributor) throws ConfigurationException {
		super(container, name, configuration);
		this.model = contributor.getSelectionModel();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.webbase.entityviewer.EntityListView#addStandardActions()
	 */
	@Override
	protected void addStandardActions() {
		ToolBarGroup tg = toolbar.addGroup();

		btSelect = tg.addButton();
		btSelect.setTitle("Select");
		btSelect.addSelectionListener(new SelectionListener() {

			public void objectSelected(SelectionEvent event) {
				performSelect();
			}

		});
		btSelect.setIconEnabled(ImageLibrary.ICON_ACCEPT_ACTIVE);
		btSelect.setIconDisabled(ImageLibrary.ICON_ACCEPT_INACTIVE);

		btAbort = tg.addButton();
		btAbort.setTitle("Cancel");
		btAbort.addSelectionListener(new SelectionListener() {

			public void objectSelected(SelectionEvent event) {
				performAbort();
			}

		});
		btAbort.setIconEnabled(ImageLibrary.ICON_CLOSED);
		btAbort.setIconDisabled(ImageLibrary.ICON_CLOSED_INACTIVE);
		addElementSelectedListener(new ElementSelectedListener() {
			@Override
			public void elementSelected(ElementSelectedEvent event) {
				if (event.isDblClick()) {
					performSelect();
				}
			}
		});
	}

	/**
	 * 
	 */
	private void performSelect() {
		Collection<?> sel = entityTable.getTableViewer().getModel().getSelection();
		if (!sel.isEmpty()) {
			String key = (String) sel.iterator().next();
			long id = Long.parseLong(key);

			model.setSelectedEntityId(id);
		}

		model.closeEntitySelection();
	}

	/**
	 * 
	 */
	private void performAbort() {
		model.closeEntitySelection();
	}

}
