/**
 * 
 */
package de.xwic.appkit.dev.engine.model;

import java.util.List;

/**
 * Represents the configuration of the data model.
 * @author lippisch
 */
public interface DataModel {
	
	/**
	 * Returns the domain id.
	 * @return
	 */
	public abstract String getDomainId();
	
	/**
	 * Returns the name of the project, which must equal the folder name.
	 * @return
	 */
	public abstract String getProjectName();
	
	/**
	 * Returns the package name.
	 * @return
	 */
	public abstract String getPackageName();
	
	/**
	 * Returns the list of Entities described in the model.
	 * @return
	 */
	public abstract List<EntityModel> getEntities();
	
}
