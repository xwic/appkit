package de.xwic.appkit.webbase.entityselection;

import java.util.List;

import de.xwic.appkit.webbase.toolkit.model.ModelEvent;
import de.xwic.appkit.webbase.toolkit.model.ModelEventSupport;

/**
 * 
 * <a href="mailto:vzhovtiuk@gmail.com">Vitaliy Zhovtyuk</a>
 */
public class EntitySelectionModel extends ModelEventSupport {

	public static final int ENTITY_CHANGED = 10;
	public static final int CLOSE_ENTITY_SELECTION = 20;
	public static final int FIELD_CLEARED = 30;
	
	private int selectedEntityId = 0;
	
	private List<String> queryProperties;

	/**
	 * @return the selectedCustomerId
	 */
	public int getSelectedEntityId() {
		return selectedEntityId;
	}

	/**
	 * @param selectedCustomerId the selectedCustomerId to set
	 */
	public void setSelectedEntityId(int selectedCustomerId) {
		this.selectedEntityId = selectedCustomerId;
	}
	
	/**
	 * closes the entity selection page
	 */
	public void closeEntitySelection() {
		fireModelChangedEvent(new ModelEvent(CLOSE_ENTITY_SELECTION, this));
	}
	
	public void fieldCleared() {
		fireModelChangedEvent(new ModelEvent(FIELD_CLEARED, this));
	}
	
	public void entityChanged() {
		fireModelChangedEvent(new ModelEvent(ENTITY_CHANGED, this));
	}

	/**
	 * @return the queryProperties
	 */
	public List<String> getQueryProperties() {
		return queryProperties;
	}

	/**
	 * @param queryProperties the queryProperties to set
	 */
	public void setQueryProperties(List<String> queryProperties) {
		this.queryProperties = queryProperties;
	}
	
	
}
