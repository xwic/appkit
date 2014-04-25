package de.xwic.appkit.webbase.toolkit.app;

import de.jwic.base.ControlContainer;
import de.jwic.base.IControlContainer;
import de.jwic.base.IOuterLayout;

/**
 * Container for data blocks
 * @author bogdan
 *
 */
public class DataColumnSetContainer extends ControlContainer implements IOuterLayout{

	/**
	 * @param container
	 */
	public DataColumnSetContainer(IControlContainer container) {
		super(container);
		setRendererId(DEFAULT_OUTER_RENDERER);
	}

	/**
	 * @param container
	 * @param name
	 */
	public DataColumnSetContainer(IControlContainer container, String name) {
		super(container, name);
		setRendererId(DEFAULT_OUTER_RENDERER);
	}

	/* (non-Javadoc)
	 * @see de.jwic.base.IOuterLayout#getOuterTemplateName()
	 */
	@Override
	public String getOuterTemplateName() {
		return this.getClass().getName() + "_outerLayout";
	}
	
	
	public DataColumn addDataColumn(){
		return new DataColumn(this);
	}
	
	public DataColumn addDataColumn(String name){
		return new DataColumn(this, name);
	}

	

}
