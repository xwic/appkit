package de.xwic.appkit.webbase.toolkit.app;

import de.jwic.base.ControlContainer;
import de.jwic.base.IControlContainer;
import de.jwic.base.IOuterLayout;

public class PanelSectionContainer extends ControlContainer implements IOuterLayout{

	public PanelSectionContainer(IControlContainer container, String name) {
		super(container, name);
		// TODO Auto-generated constructor stub
	}

	public PanelSectionContainer(IControlContainer container) {
		super(container);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getOuterTemplateName() {
		// TODO Auto-generated method stub
		return this.getClass().getName()+"_outerLayout";
	}

	
	
}
