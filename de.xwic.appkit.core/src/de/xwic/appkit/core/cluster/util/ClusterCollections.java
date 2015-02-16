package de.xwic.appkit.core.cluster.util;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.xwic.appkit.core.cluster.ClusterManager;
import de.xwic.appkit.core.cluster.ICluster;
import de.xwic.appkit.core.util.ConfigurationUtil;

/**
 * @author Razvan Pat on 1/15/2015.
 */
public class ClusterCollections {
	private static Set<String> cacheMapIdentifiers = new HashSet<String>();

	/**
	 * Surrounds given map with cluster cache map implementation. 
	 * In case the map is not empty send update.
	 *
	 * @param map
	 * @param identifier
	 * @return
	 */
	public static <K , V>
	Map<K, V> toCacheMap(Map<K, V> map, String identifier) {
		// in case cluster is not used we can just return given hash map.
		if(!ConfigurationUtil.isClusterMode()){
			return map;
		}
		validateCacheMapIdentifier(identifier);
		ICluster cluster = null;
		
		cluster = ClusterManager.getCluster();
		ClusterMap<K, V> clusterMap = new ClusterMap<K,V>(identifier, cluster, map);
		// if map is not empty, send update to synchronize
		if(!clusterMap.isEmpty()){
			clusterMap.sendFullUpdate();
		}

		ClusterCollectionsService clusterService = (ClusterCollectionsService) cluster.getClusterService(ClusterCollectionsService.NAME);
		clusterService.registerCollection(clusterMap);

		return clusterMap;
	}

	/**
	 * Same as toCacheMap, but for lists
	 *
	 * @param list
	 * @param identifier
	 * @param <T>
	 * @return
	 */
	public static <T> List<T> toCacheList(List<T> list, String identifier) {
		if(!ConfigurationUtil.isClusterMode()) {
			return list;
		}
		validateCacheMapIdentifier(identifier);

		ICluster cluster = ClusterManager.getCluster();
		ClusterList<T> clusterList = new ClusterList<T>(identifier, cluster, list);

		if(!clusterList.isEmpty()) {
			clusterList.sendFullUpdate();
		}

		ClusterCollectionsService clusterService = (ClusterCollectionsService) cluster.getClusterService(ClusterCollectionsService.NAME);
		clusterService.registerCollection(clusterList);

		return clusterList;
	}

	private static void validateCacheMapIdentifier(String id) {
		if(!cacheMapIdentifiers.add(id)) {
			throw new RuntimeException("CacheMap identifier " + id + " already exists.");
		}
	}
}
