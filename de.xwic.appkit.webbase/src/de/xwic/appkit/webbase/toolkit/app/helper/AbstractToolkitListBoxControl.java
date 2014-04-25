/**
 *
 */
package de.xwic.appkit.webbase.toolkit.app.helper;

import de.jwic.util.IHTMLElement;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.webbase.toolkit.components.IEntityListBoxControl;

/**
 * @author Alexandru Bledea
 * @since Aug 14, 2013
 */
public abstract class AbstractToolkitListBoxControl<E extends IEntity, A extends IEntityListBoxControl<E> & IHTMLElement> extends
	AbstractToolkitHTMLElementControl<A> {

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.toolkit.app.IToolkitControlHelper#loadContent(de.jwic.base.IControl, java.lang.Object)
	 */
	@Override
	public void loadContent(A control, Object obj) {
		control.selectEntry((E) obj);
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.toolkit.app.IToolkitControlHelper#getContent(de.jwic.base.IControl)
	 */
	@Override
	public Object getContent(A control) {
		return control.getSelectedEntry();
	}

}
