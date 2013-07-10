package de.xwic.appkit.webbase.toolkit.app;

import de.jwic.base.ControlContainer;
import de.jwic.base.IControlContainer;
import de.jwic.base.IOuterLayout;

/**
 * @author bogdan
 *
 */
public class DataColumn extends ControlContainer implements IOuterLayout{

	/**
	 * @param container
	 */
	public DataColumn(IControlContainer container) {
		super(container);
		setRendererId(DEFAULT_OUTER_RENDERER);
	}

	/**
	 * @param container
	 * @param name
	 */
	public DataColumn(IControlContainer container, String name) {
		super(container, name);
		setRendererId(DEFAULT_OUTER_RENDERER);
	}

	/* (non-Javadoc)
	 * @see de.jwic.base.IOuterLayout#getOuterTemplateName()
	 */
	@Override
	public String getOuterTemplateName() {
		return this.getClass().getName()+"_outerLayout";
	}
	
	/**
	 * @return new DataBlock
	 */
	public DataBlock addDataBlock(){
		return new DataBlock(this);
	}
	
	/**
	 * @param name of DataBlock 
	 * @return new DataBlock with given name
	 */
	public DataBlock addDataBlock(String name){
		return new DataBlock(this,name);
	}

	
	
}
