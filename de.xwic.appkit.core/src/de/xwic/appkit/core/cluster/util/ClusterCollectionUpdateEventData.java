package de.xwic.appkit.core.cluster.util;

import static org.junit.Assert.assertEquals;

import java.io.Serializable;
import java.io.StringReader;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import de.xwic.appkit.core.transport.xml.TransportException;
import de.xwic.appkit.core.transport.xml.XmlBeanSerializer;

/**
 * @author Razvan Pat on 1/15/2015.
 */
public class ClusterCollectionUpdateEventData implements Serializable {
	public enum EventType { ADD_ELEMENT, REMOVE_ELEMENT, CLEAR, REPLACE_ALL }
	private String serializedObject;
	private long lastUpdate;
	private EventType eventType;
	
	/**
	 * @param obj
	 * @param lastUpdate
	 * @param eventType
	 */
	public ClusterCollectionUpdateEventData(String serializedObject, long lastUpdate, EventType eventType) {
		this.serializedObject = serializedObject;
		this.lastUpdate = lastUpdate;
		this.eventType = eventType;;
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
