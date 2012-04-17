/**
 * 
 */
package de.xwic.appkit.webbase.toolkit.table.renderer;

/**
 * @author Oleksiy Samokhvalov
 *
 */
public interface IHtmlRendererProvider {
	/**
	 * @param object an arbitrary object in order to help to choose the renderer.
	 * @return
	 */
	IHtmlContentRenderer getRenderer(Object object);
}
