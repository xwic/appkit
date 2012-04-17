/**
 * 
 */
package de.xwic.appkit.core.model.queries;


/**
 * @author Ronny Pfretzschner
 *
 */
public class SetRelatedQuery extends PropertyQuery {
	
	private String setProperty = null;
	private int entityID = -1;
	private String relatedEntityImplClazzName = null;
	
	/**
	 * Constructor.
	 *
	 */
	public SetRelatedQuery() {
		
	}
	
	/**
	 * default ctor. <p>
	 *
	 */
	public SetRelatedQuery(String wildCardPreferenceSetting) {
		super(wildCardPreferenceSetting);
	}


	/**
	 * @return the entityID
	 */
	public int getEntityID() {
		return entityID;
	}


	/**
	 * @param entityID the entityID to set
	 */
	public void setEntityID(int entityID) {
		this.entityID = entityID;
	}


	/**
	 * @return the setProperty
	 */
	public String getSetProperty() {
		return setProperty;
	}


	/**
	 * @param setProperty the setProperty to set
	 */
	public void setSetProperty(String setProperty) {
		this.setProperty = setProperty;
	}


	/**
	 * @return the relatedEntityClazzName
	 */
	public String getRelatedEntityImplClazzName() {
		return relatedEntityImplClazzName;
	}


	/**
	 * @param relatedEntityClazzName the relatedEntityClazzName to set
	 */
	public void setRelatedEntityImplClazzName(String relatedEntityImplClazzName) {
		this.relatedEntityImplClazzName = relatedEntityImplClazzName;
	}
	
	
}
