package de.xwic.appkit.core.cluster.util;

import de.xwic.appkit.core.cluster.AbstractClusterService;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Takes care of synchronizing the collections when nodes connect
 *
 * @author Razvan Pat on 2/12/2015.
 */
public class ClusterCollectionsService extends AbstractClusterService {
	public static final String NAME = "ClusterCollectionsService";
	private List<AbstractClusterCollection> clusterCollections = new ArrayList<AbstractClusterCollection>();

	/**
	 * @param collection
	 */
	public void registerCollection(AbstractClusterCollection collection) {
		clusterCollections.add(collection);
	}

	/**
	 * @return
	 */
	@Override
	public Serializable surrenderMasterRole() {
		Serializable result = super.surrenderMasterRole();
		synchronizeCollections();
		return result;
	}

	/**
	 * @param remoteMasterData
	 */
	@Override
	public void obtainMasterRole(Serializable remoteMasterData) {
		super.obtainMasterRole(remoteMasterData);
		synchronizeCollections();
	}

	/**
	 *
	 */
	protected void synchronizeCollections() {
		for (AbstractClusterCollection clusterCollection : clusterCollections) {
			clusterCollection.sendFullUpdate();
		}
	}
}
