/**
 *
 */
package de.xwic.appkit.webbase.toolkit.app.helper;

import de.jwic.util.IHTMLElement;
import de.xwic.appkit.webbase.toolkit.app.IToolkitControlHelper;

/**
 * @author Alexandru Bledea
 * @since Aug 14, 2013
 */
public abstract class AbstractToolkitHTMLElementControl<H extends IHTMLElement> implements IToolkitControlHelper<H> {

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.toolkit.app.IToolkitControlHelper#markField(de.jwic.base.IControl, java.lang.String)
	 */
	@Override
	public final void markField(H control, String cssClass) {
		control.setCssClass(cssClass);
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.toolkit.app.IToolkitControlHelper#getFieldMarkedCssClass(de.jwic.base.IControl)
	 */
	@Override
	public final String getFieldMarkedCssClass(H control) {
		return control.getCssClass();
	}

}
