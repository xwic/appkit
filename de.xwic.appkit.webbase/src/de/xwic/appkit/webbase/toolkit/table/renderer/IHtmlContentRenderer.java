/**
 * 
 */
package de.xwic.appkit.webbase.toolkit.table.renderer;

import java.io.PrintWriter;

/**
 * It is general interface for some renderer
 * 
 * @author Oleksiy Samokhvalov
 *
 */
public interface IHtmlContentRenderer {
	/**
	 * Renders some content into the writer.
	 * 
	 * @param writer to write the content into.
	 */
	void render(PrintWriter writer);
}
