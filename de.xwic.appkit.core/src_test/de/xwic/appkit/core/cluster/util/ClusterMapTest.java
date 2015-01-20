package de.xwic.appkit.core.cluster.util;

import de.xwic.appkit.core.cluster.*;
import de.xwic.appkit.core.cluster.util.ClusterCollectionUpdateEventData.EventType;

import org.junit.Test;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class ClusterMapTest  {

	@Test
	public void itWrapsAroundAMap() {
		HashMap<String, String> someHashMap = getDummyHashMap();
		String someHashMapKey = getFirstKeyOfHashMap(someHashMap);

		ClusterMap<String, String> clusterMap = new ClusterMap<String, String>("test", null, someHashMap);

		assertEquals(someHashMap.get(someHashMapKey), clusterMap.get(someHashMapKey));
	}

	@Test
	public void ifClusterIsNotNullItSendsAnAsyncClusterUpdateMessageWheneverTheDataChanges() {
		HashMap<String, String> someHashMap = getDummyHashMap();
		String someHashMapKey = getFirstKeyOfHashMap(someHashMap);
		DummyCluster dummyCluster = new DummyCluster();
		ClusterMap<String, String> clusterMap = new ClusterMap<String, String>("test", dummyCluster, new HashMap<String, String>());

		clusterMap.put("1", "A");
		assertEquals("A", ((SerializableEntry<String, String>)dummyCluster.getLastSentEventMap()).getValue());
		assertEquals(true, dummyCluster.eventModeSent);

		clusterMap.putAll(someHashMap);
		assertEquals(someHashMap.get(someHashMapKey), ((Map<String, String>)dummyCluster.getLastSentEventMap()).get(someHashMapKey));
		assertEquals(true, dummyCluster.eventModeSent);

		clusterMap.remove("1");
		assertEquals("1", dummyCluster.getLastSentEventMap());
		assertEquals(true, dummyCluster.eventModeSent);

		clusterMap.clear();
		assertNull(dummyCluster.getLastSentEventMap());
		assertEquals(true, dummyCluster.eventModeSent);
	}

	@Test
	public void clusterUpdatesAreNotSentIfClusterParameterIsNull() {
		ClusterMap<String, String> clusterMap = new ClusterMap<String, String>("test", null, new HashMap<String, String>());

		try {
			clusterMap.put("1", "A");
		} catch (Throwable t) {
			fail("No errors should have been thrown");
		}

		assertEquals("A", clusterMap.get("1"));
	}

	@Test
	public void clusterUpdatesIncludeTimeOfUpdate() {
		DummyCluster dummyCluster = new DummyCluster();
		ClusterMap<String, String> clusterMap = new ClusterMap<String, String>("test", dummyCluster, new HashMap<String, String>());

		clusterMap.put("x", "X");

		long updateTime = ((ClusterCollectionUpdateEventData) dummyCluster.eventSent.getData()).getLastUpdate();
		long timePassed = new Date().getTime() - updateTime;
		assertTrue(timePassed < 1000 * 60);
	}

	@Test
	public void clusterUpdatesIncludeIdentifier() {
		DummyCluster dummyCluster = new DummyCluster();
		ClusterMap<String, String> clusterMap = new ClusterMap<String, String>("testIdentifier", dummyCluster, new HashMap<String, String>());

		clusterMap.put("x", "X");

		assertEquals("testIdentifier", dummyCluster.eventSent.getName());
	}

	@Test
	public void clusterListenerIsRegisteredAtConstructionTime() {
		DummyCluster dummyCluster = new DummyCluster();

		new ClusterMap<String, String>("testIdentifier", dummyCluster, new HashMap<String, String>());

		assertNotNull(dummyCluster.addedListener);
		assertEquals(ClusterMap.EVENT_NAMESPACE, dummyCluster.listenerNamespace);
	}

	@Test
	public void mapGetsUpdatedIfClusterEventHasCorrectIdentifierAndNewerUpdateTime() {
		HashMap<String, String> someHashMap = getDummyHashMap();
		String someHashMapKey = getFirstKeyOfHashMap(someHashMap);
		DummyCluster dummyCluster = new DummyCluster();
		ClusterMap<String, String> clusterMap = new ClusterMap<String, String>("testIdentifier", dummyCluster, new HashMap<String, String>());
		clusterMap.put("x", "y"); //causes lastUpdate to be set to current date
		ClusterCollectionUpdateEventData data = new ClusterCollectionUpdateEventData(someHashMap, new Date().getTime() + 1000 * 60, EventType.REPLACE_ALL);
		ClusterEvent event = new ClusterEvent(ClusterMap.EVENT_NAMESPACE, "testIdentifier", data);

		dummyCluster.addedListener.receivedEvent(event);

		assertEquals(someHashMap.get(someHashMapKey), clusterMap.get(someHashMapKey));
		assertNull(clusterMap.get("x"));
	}

	@Test
	public void eventIsIgnoredIfDifferendIdentifier() {
		HashMap<String, String> someHashMap = getDummyHashMap();
		String someHashMapKey = getFirstKeyOfHashMap(someHashMap);
		DummyCluster dummyCluster = new DummyCluster();
		ClusterMap<String, String> clusterMap = new ClusterMap<String, String>("testIdentifier", dummyCluster, new HashMap<String, String>());
		clusterMap.put("x", "y"); //causes lastUpdate to be set to current date
		ClusterCollectionUpdateEventData data = new ClusterCollectionUpdateEventData(someHashMap, new Date().getTime() + 1000 * 60, EventType.ADD_ELEMENT);
		ClusterEvent event = new ClusterEvent(ClusterMap.EVENT_NAMESPACE, "otherIdentifier", data);

		dummyCluster.addedListener.receivedEvent(event);

		assertNull(clusterMap.get(someHashMapKey));
		assertEquals("y", clusterMap.get("x"));
	}

	@Test
	public void eventIsIgnoredIfOlderUpdate() {
		HashMap<String, String> someHashMap = getDummyHashMap();
		String someHashMapKey = getFirstKeyOfHashMap(someHashMap);
		DummyCluster dummyCluster = new DummyCluster();
		ClusterMap<String, String> clusterMap = new ClusterMap<String, String>("testIdentifier", dummyCluster, new HashMap<String, String>());
		clusterMap.put("x", "y"); //causes lastUpdate to be set to current date
		ClusterCollectionUpdateEventData data = new ClusterCollectionUpdateEventData(someHashMap, new Date().getTime() - 1000 * 60, EventType.ADD_ELEMENT);
		ClusterEvent event = new ClusterEvent(ClusterMap.EVENT_NAMESPACE, "testIdentifier", data);

		dummyCluster.addedListener.receivedEvent(event);

		assertNull(clusterMap.get(someHashMapKey));
		assertEquals("y", clusterMap.get("x"));
	}

	private String getFirstKeyOfHashMap(Map map) {
		return map.keySet().toArray()[0].toString();
	}

	private HashMap<String, String> getDummyHashMap() {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("tt11", "Success");
		return map;
	}
}

class DummyCluster implements ICluster {

	ClusterEvent eventSent;
	boolean eventModeSent;
	ClusterEventListener addedListener;
	String listenerNamespace;

	Serializable getLastSentEventMap() {
		return ((ClusterCollectionUpdateEventData) eventSent.getData()).getData();
	}

	@Override
	public void registerNode(NodeAddress na) {

	}

	@Override
	public ClusterConfiguration getConfig() {
		return null;
	}

	@Override
	public INode[] getNodes() {
		return new INode[0];
	}

	@Override
	public void sendEvent(ClusterEvent event, boolean asynchronous) throws EventTimeOutException {
		eventSent = event;
		eventModeSent = asynchronous;
	}

	@Override
	public TransportResult[] sendMessage(Message message) {
		return new TransportResult[0];
	}

	@Override
	public void addEventListener(ClusterEventListener listener) {
	}

	@Override
	public void addEventListener(ClusterEventListener listener, String namespace) {
		addedListener = listener;
		listenerNamespace = namespace;
	}

	@Override
	public void registerClusterService(String name, IClusterService service) {

	}

	@Override
	public IClusterService getClusterService(String name) {
		return null;
	}

	@Override
	public Collection<String> getInstalledClusterServiceNames() {
		return null;
	}
}
