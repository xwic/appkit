package de.xwic.appkit.webbase.toolkit.app;

import de.jwic.base.ControlContainer;
import de.jwic.base.IControlContainer;
import de.jwic.base.IOuterLayout;

public class PanelBodyContainer extends ControlContainer implements IOuterLayout {

	/**
	 * @param container
	 */
	public PanelBodyContainer(IControlContainer container) {
		super(container);
	}

	/**
	 * @param container
	 * @param name
	 */
	public PanelBodyContainer(IControlContainer container, String name) {
		super(container, name);
	}

	/* (non-Javadoc)
	 * @see de.jwic.base.IOuterLayout#getOuterTemplateName()
	 */
	@Override
	public String getOuterTemplateName() {
		return this.getClass().getName()+"_outerLayout";
	}

}
