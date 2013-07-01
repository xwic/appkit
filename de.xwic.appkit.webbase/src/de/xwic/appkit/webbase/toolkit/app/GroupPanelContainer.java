package de.xwic.appkit.webbase.toolkit.app;

import de.jwic.base.ControlContainer;
import de.jwic.base.IControlContainer;
import de.jwic.base.IOuterLayout;

/**
 * @author bogdan
 *
 */
public class GroupPanelContainer extends ControlContainer implements IOuterLayout {

	private String title = "";

	/**
	 * @param container
	 */
	public GroupPanelContainer(IControlContainer container) {
		super(container);
	}

	/**
	 * @param container
	 * @param name
	 */
	public GroupPanelContainer(IControlContainer container, String name) {
		super(container, name);
	}

	/* (non-Javadoc)
	 * @see de.jwic.base.IOuterLayout#getOuterTemplateName()
	 */
	@Override
	public String getOuterTemplateName() {
		return this.getClass().getName()+"_outerLayout";
	}

	/**
	 * @param title
	 */
	public void setTitle(String title){
		this.title  = title;
	}
	
	/**
	 * @return
	 */
	public String getTitle(){
		return this.title;
	}
	
}
