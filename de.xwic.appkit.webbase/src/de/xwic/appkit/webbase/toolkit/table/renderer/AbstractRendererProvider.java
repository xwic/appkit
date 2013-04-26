/**
 * 
 */
package de.xwic.appkit.webbase.toolkit.table.renderer;

import de.jwic.controls.tableviewer.RowContext;
import de.jwic.controls.tableviewer.TableColumn;
import de.jwic.controls.tableviewer.TableViewer;

/**
 * @author Oleksiy Samokhvalov
 * 
 */
public abstract class AbstractRendererProvider implements ITableRendererProvider {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2678887276225536711L;

	protected TableViewer viewer;
	protected IHtmlRendererProvider headerContentRendererProvider;

	/**
	 * @param viewer
	 * @param headerContentRendererProvider
	 */
	public AbstractRendererProvider(TableViewer viewer, IHtmlRendererProvider headerContentRendererProvider) {
		this.viewer = viewer;
		this.headerContentRendererProvider = headerContentRendererProvider;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.pol.netapp.amis.planning.forecasting.table.ITableRendererProvider#
	 * getCellRenderer(java.lang.Object, de.jwic.ecolib.tableviewer.TableColumn,
	 * de.jwic.ecolib.tableviewer.RowContext)
	 */
	public IHtmlContentRenderer getCellRenderer(Object row, TableColumn column, RowContext rowContext) {
		TableCellInfo info = new TableCellInfo(row, column, getCellLabel(row, column, rowContext), viewer, rowContext
				.getLevel());
		return new DefaultCellRenderer(info);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.pol.netapp.amis.planning.forecasting.table.ITableRendererProvider#
	 * getHeaderRenderer()
	 */
	public IHtmlContentRenderer getHeaderRenderer(TableColumn column) {
		return new DefaultHeaderRenderer(viewer, column, headerContentRendererProvider);
	}

}
