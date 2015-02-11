package de.xwic.appkit.core.cluster.util;

import java.io.Serializable;

/**
 * @author Razvan Pat on 1/15/2015.
 */
public class ClusterCollectionUpdateEventData implements Serializable {
	public enum EventType { ADD_ELEMENT, REMOVE_ELEMENT, CLEAR, REPLACE_ALL, ADD_ALL, RETAIN_ALL, SET_ELEMENT, REMOVE_ALL }
	private String serializedObject;
	private long lastUpdate;
	private EventType eventType;
	
	/**
	 * @param lastUpdate
	 * @param eventType
	 */
	public ClusterCollectionUpdateEventData(String serializedObject, long lastUpdate, EventType eventType) {
		this.serializedObject = serializedObject;
		this.lastUpdate = lastUpdate;
		this.eventType = eventType;
	}

	/**
	 * @return
	 */
	public String getSerializedObject() {
		return serializedObject;
	}

	/**
	 * @return
	 */
	public long getLastUpdate() {
		return lastUpdate;
	}

	/**
	 * @return the eventType
	 */
	public EventType getEventType() {
		return eventType;
	}
}
