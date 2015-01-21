package de.xwic.appkit.core.cluster.util;

import static org.junit.Assert.assertEquals;

import java.io.Serializable;
import java.io.StringReader;
import java.util.Date;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import de.xwic.appkit.core.cluster.ClusterEvent;
import de.xwic.appkit.core.cluster.ClusterEventListener;
import de.xwic.appkit.core.cluster.EventTimeOutException;
import de.xwic.appkit.core.cluster.ICluster;
import de.xwic.appkit.core.cluster.util.ClusterCollectionUpdateEventData.EventType;
import de.xwic.appkit.core.transport.xml.TransportException;
import de.xwic.appkit.core.transport.xml.XmlBeanSerializer;

/**
 * @author Razvan Pat on 1/15/2015.
 */
public abstract class AbstractClusterCollection<K, V> {
	
	protected static final Log log = LogFactory.getLog(ClusterMap.class);

	protected static final String EVENT_NAMESPACE = "ClusterCollectionUpdate";

	protected String identifier;
	protected ICluster cluster;
	protected long lastUpdate = 0;

	
	public AbstractClusterCollection(String identifier, ICluster cluster) {
		this.identifier = identifier;
		this.cluster = cluster;
		registerListener();
	}
	
	/**
	 * Sends update message to the cluster
	 */
	protected void sendClusterUpdate(Object obj, EventType eventType) {
		if (cluster != null) {
			lastUpdate = new Date().getTime();
			try {
				String serializedObject = XmlBeanSerializer.serializeToXML("serializedObject", obj);
				ClusterCollectionUpdateEventData data = new ClusterCollectionUpdateEventData(serializedObject, lastUpdate, eventType);
				log.debug("Sending ClusterCollection update for " + identifier);
				cluster.sendEvent(new ClusterEvent(EVENT_NAMESPACE, identifier, data), true);
			} catch (EventTimeOutException e) {
				log.error("Unable to send cache update message for " + identifier, e);
			} catch (TransportException e) {
				log.error("Serialization of the update object has failed. Unable to send cache update message for " + identifier, e);
			}
		}
	}

	/**
	 * Registers the listener that checks for incoming updates
	 */
	private void registerListener() {
		if(cluster == null) {
			return;
		}

		cluster.addEventListener(new ClusterEventListener() {

			@SuppressWarnings("unchecked")
			@Override
			public void receivedEvent(ClusterEvent event) {
				if (event.getName().equals(identifier)) {
					ClusterCollectionUpdateEventData data = (ClusterCollectionUpdateEventData) event.getData();
					if (data.getLastUpdate() > lastUpdate) {
						log.debug("Updating ClusterCollection " + identifier);
						Document doc;
						try {
							doc = new SAXReader().read(new StringReader(data.getSerializedObject()));
						
						Element root = doc.getRootElement().element("serializedObject");
						Object deserialized = new XmlBeanSerializer().deserializeBean(root);
						eventReceived(deserialized, data.getEventType());
						} catch (DocumentException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}catch (TransportException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		}, EVENT_NAMESPACE);
	}
	
	public abstract void eventReceived(Object obj, EventType eventType);
}
