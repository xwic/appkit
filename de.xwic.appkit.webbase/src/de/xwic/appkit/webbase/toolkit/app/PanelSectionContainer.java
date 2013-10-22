package de.xwic.appkit.webbase.toolkit.app;

import de.jwic.base.ControlContainer;
import de.jwic.base.IControlContainer;
import de.jwic.base.IOuterLayout;

/**
 * Section container.
 * @author dotto
 *
 */
public class PanelSectionContainer extends ControlContainer implements IOuterLayout{

	/**
	 * @param container
	 * @param name
	 */
	public PanelSectionContainer(IControlContainer container, String name) {
		super(container, name);
		setRendererId(DEFAULT_OUTER_RENDERER);
	}

	/**
	 * @param container
	 */
	public PanelSectionContainer(IControlContainer container) {
		super(container);
		setRendererId(DEFAULT_OUTER_RENDERER);
	}

	/* (non-Javadoc)
	 * @see de.jwic.base.IOuterLayout#getOuterTemplateName()
	 */
	@Override
	public String getOuterTemplateName() {
		return PanelSectionContainer.class.getName()+"_outerLayout";
	}

	
	
}
