/**
 * 
 */
package de.xwic.appkit.webbase.toolkit.components;

import de.jwic.controls.tableviewer.CellLabel;
import de.jwic.controls.tableviewer.RowContext;
import de.jwic.controls.tableviewer.TableColumn;
import de.xwic.appkit.core.config.model.Property;
import de.xwic.appkit.webbase.toolkit.util.ImageLibrary;
import de.xwic.appkit.webbase.viewer.base.PropertyLabelProvider;
import de.xwic.appkit.webbase.viewer.columns.TableColumnInfo;

/**
 * @author Ronny Pfretzschner
 *
 */
public class BaseLabelProvider extends PropertyLabelProvider {

	
	@Override
	public CellLabel getCellLabel(Object row, TableColumn column,
			RowContext rowContext) {
		CellLabel label = super.getCellLabel(row, column, rowContext);
		
		int index = column.getIndex();
		Object obj = columnInfoList.get(index);
		
		if (obj instanceof TableColumnInfo) {
			TableColumnInfo info = (TableColumnInfo) obj;
			Property prop = info.getColumn().getFinalProperty();

			if (prop.getDescriptor().getPropertyType().equals(boolean.class)) {
				Boolean val;
				try {
					val = (Boolean) info.getData(row);
				} catch (Exception e) {
					label.text = e.toString();
					return label;
				}
				
				if (val != null && val.booleanValue()) {
					label.image = ImageLibrary.ICON_CHECKED;
				} else {
					label.image = ImageLibrary.ICON_UNCHECKED;
				}
				label.text = "";
			}
		}
		
		return label;
	}
}
