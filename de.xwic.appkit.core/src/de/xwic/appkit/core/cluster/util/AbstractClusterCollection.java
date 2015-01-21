package de.xwic.appkit.core.cluster.util;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.xwic.appkit.core.cluster.ClusterEvent;
import de.xwic.appkit.core.cluster.ClusterEventListener;
import de.xwic.appkit.core.cluster.EventTimeOutException;
import de.xwic.appkit.core.cluster.ICluster;
import de.xwic.appkit.core.cluster.util.ClusterCollectionUpdateEventData.EventType;

/**
 * @author Razvan Pat on 1/15/2015.
 */
public abstract class AbstractClusterCollection {
	
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
	protected void sendClusterUpdate(Serializable obj, EventType eventType) {
		if (cluster != null) {
			lastUpdate = new Date().getTime();
			try {
				ClusterCollectionUpdateEventData data = new ClusterCollectionUpdateEventData(obj, lastUpdate, eventType);
				log.debug("Sending ClusterCollection update for " + identifier);
				cluster.sendEvent(new ClusterEvent(EVENT_NAMESPACE, identifier, data), true);
			} catch (EventTimeOutException e) {
				log.error("Unable to send cache update message for " + identifier, e);
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
						eventReceived(data.getData(), data.getEventType());
					}
				}
			}
		}, EVENT_NAMESPACE);
	}
	
	public abstract void eventReceived(Serializable obj, EventType eventType);
}
