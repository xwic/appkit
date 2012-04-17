/**
 * 
 */
package de.xwic.appkit.webbase.entityviewer;

import de.jwic.base.IControlContainer;
import de.jwic.base.JavaScriptSupport;
import de.jwic.controls.Button;
import de.jwic.events.SelectionEvent;
import de.jwic.events.SelectionListener;
import de.xwic.appkit.webbase.table.Column;
import de.xwic.appkit.webbase.table.EntityTableAdapter;
import de.xwic.appkit.webbase.table.EntityTableEvent;
import de.xwic.appkit.webbase.table.EntityTableModel;
import de.xwic.appkit.webbase.toolkit.util.ImageLibrary;

/**
 * @author Adrian Ionescu
 */
@JavaScriptSupport
public class ClearFiltersButton extends Button {

	private final EntityTableModel model;

	/**
	 * @param container
	 * @param name
	 */
	public ClearFiltersButton(IControlContainer container, String name, EntityTableModel model) {
		super(container, name);
		
		this.model = model;
		
		setTemplateName(Button.class.getName());
		
		setTitle("");
		setIconEnabled(ImageLibrary.ICON_TBL_FILTER_CLEAR);
		
		model.addEntityTableListener(new EntityTableAdapter() {
			@Override
			public void columnFiltered(EntityTableEvent event) {
				updateFilterInfo();
			}
			@Override
			public void columnSorted(EntityTableEvent event) {
				updateFilterInfo();
			}
		});
		
		addSelectionListener(new SelectionListener() {			
			@Override
			public void objectSelected(SelectionEvent event) {
				ClearFiltersButton.this.model.clearFilters();
			}
		});
		
		updateFilterInfo();
	}

	/**
	 * Update the filter info.
	 */
	private void updateFilterInfo() {
		StringBuilder sb = new StringBuilder();

		for (Column col : model.getColumns()) {
			if (col.getFilter() != null) {
				if (sb.length() > 0) {
					sb.append(" ; ");
				}
				sb.append("'").append(col.getTitle()).append("'");
			}
		}
		
		setTooltip(sb.length() == 0 ? "There are no filters" : "Clear current filters: " + sb.toString());
		requireRedraw();
	}
	
}
