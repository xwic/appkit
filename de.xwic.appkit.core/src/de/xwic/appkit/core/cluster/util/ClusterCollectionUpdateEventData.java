package de.xwic.appkit.core.cluster.util;

import java.io.Serializable;

/**
 * @author Razvan Pat on 1/15/2015.
 */
public class ClusterCollectionUpdateEventData implements Serializable {
	public enum EventType { ADD_ELEMENT, REMOVE_ELEMENT, CLEAR, ADD_ALL }
	private Serializable obj;
	private long lastUpdate;
	private EventType eventType;
	
	/**
	 * @param obj
	 * @param lastUpdate
	 * @param eventType
	 */
	public ClusterCollectionUpdateEventData(Serializable obj, long lastUpdate, EventType eventType) {
		this.obj = obj;
		this.lastUpdate = lastUpdate;
		this.eventType = eventType;;
	}

	/**
	 * @return
	 */
	public Serializable getData() {
		return obj;
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
