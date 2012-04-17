/**
 * 
 */
package de.xwic.appkit.webbase.toolkit.table.renderer;

import de.jwic.ecolib.tableviewer.TableColumn;

/**
 * @author Oleksiy Samokhvalov
 *
 */
public class DefaultHeaderContentRendererProvider implements IHtmlRendererProvider{

	/* (non-Javadoc)
	 * @see de.pol.netapp.amis.planning.forecasting.table.IHtmlRendererProvider#getRenderer(java.lang.Object)
	 */
	public IHtmlContentRenderer getRenderer(Object object) {
		final TableColumn column = (TableColumn)object;
		return new SimpleHeaderContentRenderer(column);
	}

}
