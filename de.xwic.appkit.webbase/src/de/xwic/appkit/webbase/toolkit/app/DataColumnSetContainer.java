package de.xwic.appkit.webbase.toolkit.app;

import de.jwic.base.ControlContainer;
import de.jwic.base.IControlContainer;
import de.jwic.base.IOuterLayout;

/**
 * Container for data blocks
 * @author dotto
 *
 */
public class DataColumnSetContainer extends ControlContainer implements IOuterLayout{

	/**
	 * @param container
	 */
	public DataColumnSetContainer(IControlContainer container) {
		super(container);
	}

	/**
	 * @param container
	 * @param name
	 */
	public DataColumnSetContainer(IControlContainer container, String name) {
		super(container, name);
	}

	/* (non-Javadoc)
	 * @see de.jwic.base.IOuterLayout#getOuterTemplateName()
	 */
	@Override
	public String getOuterTemplateName() {
		return this.getClass().getName() + "_outerLayout";
	}

}
