package de.xwic.appkit.core.cluster.util;

import de.xwic.appkit.core.cluster.ClusterManager;
import de.xwic.appkit.core.cluster.ICluster;
import de.xwic.appkit.core.util.ConfigurationUtil;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Razvan Pat on 1/15/2015.
 */
public class ClusterCollections {
	private static Set<String> cacheMapIdentifiers = new HashSet<String>();

	public static <K extends Serializable, V extends Serializable>
	ClusterMap<K, V> toCacheMap(Map<K, V> map, String identifier) {
		validateCacheMapIdentifier(identifier);
		ICluster cluster = null;
		if(ConfigurationUtil.isClusterMode()) {
			cluster = ClusterManager.getCluster();
		}
		return new ClusterMap<K,V>(identifier, cluster, map);
	}

	private static void validateCacheMapIdentifier(String id) {
		if(!cacheMapIdentifiers.add(id)) {
			throw new RuntimeException("CacheMap identifier " + id + " already exists.");
		}
	}
}
