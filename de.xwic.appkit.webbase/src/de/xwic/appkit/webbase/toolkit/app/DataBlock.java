package de.xwic.appkit.webbase.toolkit.app;

import de.jwic.base.ControlContainer;
import de.jwic.base.IControlContainer;

/**
 * @author bogdan
 *
 */
public class DataBlock extends ControlContainer {

	private String title;
	
	/**
	 * @param container
	 */
	public DataBlock(IControlContainer container) {
		this(container,null);
	}

	/**
	 * @param container
	 * @param name
	 */
	public DataBlock(IControlContainer container, String name) {
		super(container, name);
		this.setTitle(name==null ? "" : name);
	}
	
	/**
	 * @return
	 */
	public String getTitle() {
		return title;
	}
	
	/**
	 * @param title
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	
}
