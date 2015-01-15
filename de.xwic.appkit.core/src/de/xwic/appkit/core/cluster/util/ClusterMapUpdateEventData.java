package de.xwic.appkit.core.cluster.util;

import java.io.Serializable;
import java.util.Map;

/**
 * @author Razvan Pat on 1/15/2015.
 */
public class ClusterMapUpdateEventData implements Serializable {
	private Map map;
	private long lastUpdate;

	public ClusterMapUpdateEventData(Map map, long lastUpdate) {
		this.map = map;
		this.lastUpdate = lastUpdate;
	}

	public Map getMap() {
		return map;
	}

	public long getLastUpdate() {
		return lastUpdate;
	}
}
