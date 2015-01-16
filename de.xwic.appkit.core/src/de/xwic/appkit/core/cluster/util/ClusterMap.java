package de.xwic.appkit.core.cluster.util;

import de.xwic.appkit.core.cluster.ClusterEvent;
import de.xwic.appkit.core.cluster.ClusterEventListener;
import de.xwic.appkit.core.cluster.EventTimeOutException;
import de.xwic.appkit.core.cluster.ICluster;
import de.xwic.appkit.core.util.AbstractMapDecorator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * A map implementation that synchronizes over the cluster.
 * A unique identifier must be given to each instance.
 *
 * @author Razvan Pat on 1/15/2015.
 */
public class ClusterMap<K extends Serializable, V extends Serializable> extends AbstractMapDecorator<K, V> {

	private static final Log log = LogFactory.getLog(ClusterMap.class);

	public static final String EVENT_NAMESPACE = "ClusterMapUpdate";

	private String identifier;
	private ICluster cluster;
	private long lastUpdate = 0;

	/**
	 * Package private, use ClusterCollections to instantiate
	 *
	 * @param identifier
	 * @param cluster
	 * @param map
	 */
	ClusterMap(String identifier, ICluster cluster, Map<K, V> map) {
		super(map);
		this.identifier = identifier;
		this.cluster = cluster;

		registerListener();
	}

	/**
	 * @param key
	 * @param value
	 * @return
	 */
	@Override
	public V put(K key, V value) {
		V result = super.put(key, value);
		sendClusterUpdate();
		return result;
	}

	/**
	 * @param m
	 */
	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		super.putAll(m);
		sendClusterUpdate();
	}

	/**
	 * @param key
	 * @return
	 */
	@Override
	public V remove(Object key) {
		V result = super.remove(key);
		sendClusterUpdate();
		return result;
	}

	/**
	 */
	@Override
	public void clear() {
		super.clear();
		sendClusterUpdate();
	}

	/**
	 * Sends update message to the cluster
	 */
	private void sendClusterUpdate() {
		if (cluster != null) {
			lastUpdate = new Date().getTime();
			try {
				ClusterMapUpdateEventData data = new ClusterMapUpdateEventData(map, lastUpdate);
				log.debug("Sending ClusterMap update for " + identifier);
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
					ClusterMapUpdateEventData data = (ClusterMapUpdateEventData) event.getData();
					if (data.getLastUpdate() > lastUpdate) {
						log.debug("Updating ClusterMap " + identifier);
						map = (Map<K, V>) data.getMap();
					}
				}
			}
		}, EVENT_NAMESPACE);
	}
}
