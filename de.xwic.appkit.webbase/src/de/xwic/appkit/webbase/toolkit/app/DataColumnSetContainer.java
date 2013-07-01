package de.xwic.appkit.webbase.toolkit.app;

import de.jwic.base.ControlContainer;
import de.jwic.base.IControlContainer;
import de.jwic.base.IOuterLayout;

public class DataColumnSetContainer extends ControlContainer implements IOuterLayout{

	public DataColumnSetContainer(IControlContainer container) {
		super(container);
	}

	public DataColumnSetContainer(IControlContainer container, String name) {
		super(container, name);
	}

	@Override
	public String getOuterTemplateName() {
		// TODO Auto-generated method stub
		return this.getClass().getName() + "_outerLayout";
	}

}
