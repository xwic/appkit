package de.xwic.appkit.core.cluster.util;

import org.junit.Before;
import org.junit.Test;

import de.xwic.appkit.core.cluster.ClusterConfiguration;
import de.xwic.appkit.core.cluster.ClusterEvent;
import de.xwic.appkit.core.cluster.EventTimeOutException;
import de.xwic.appkit.core.cluster.impl.Cluster;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class ClusterListTest {

	private List<String> source;
	private List<String> destination;
	private List<String> oneTwoThreeList;

	private class TestCluster extends Cluster {

		private TestCluster receiverCluster;

		public TestCluster(ClusterConfiguration config) {
			super(config);
		}

		public void setReceiverCluster(TestCluster receiverCluster) {
			this.receiverCluster = receiverCluster;
		}

		@Override
		public void sendEvent(ClusterEvent event, boolean asynchronous) throws EventTimeOutException {
			if(receiverCluster != null) {
				receiverCluster._receivedEvent(event);
			}
		}
	}

	@Before
	public void setUp() throws Exception {
		source = new ArrayList<String>();
		TestCluster sender = new TestCluster(null);
		TestCluster receiver = new TestCluster(null);
		sender.setReceiverCluster(receiver);
		source = new ClusterList<String>("test", sender, source);
		destination = new ArrayList<String>();
		destination = new ClusterList<String>("test", receiver, destination);
		oneTwoThreeList = Arrays.asList("1", "2", "3");
	}

	@Test
	public void testAdd() {
		source.add("FirstElement");

		assertFalse(destination.isEmpty());
	}

	@Test
	public void testAddAll() {
		source.addAll(oneTwoThreeList);

		assertEquals(3, destination.size());
	}

	@Test
	public void testRemove() {
		source.addAll(oneTwoThreeList);

		source.remove("2");

		assertEquals(2, destination.size());
		assertEquals("3", destination.get(1));
	}

	@Test
	public void testAddAllWithIndex() {
		source.addAll(oneTwoThreeList);

		source.addAll(1, Arrays.asList("5", "6"));

		assertEquals(5, destination.size());
		assertEquals("6", destination.get(2));
	}

	@Test
	public void testRemoveAll() {
		source.addAll(oneTwoThreeList);

		source.removeAll(Arrays.asList("2", "1"));

		assertEquals(1, destination.size());
		assertEquals("3", destination.get(0));
	}

	@Test
	public void testRetainAll() {
		source.addAll(oneTwoThreeList);

		source.retainAll(Arrays.asList("2", "1"));

		assertEquals(2, destination.size());
		assertEquals("2", destination.get(1));
	}

	@Test
	public void testClear() {
		source.addAll(oneTwoThreeList);

		source.clear();

		assertTrue(destination.isEmpty());
	}

	@Test
	public void testSet() {
		source.addAll(oneTwoThreeList);

		source.set(1, "7");

		assertEquals("7", destination.get(1));
	}

	@Test
	public void testAddWithIndex() {
		source.addAll(oneTwoThreeList);

		source.add(1, "8");

		assertEquals(4, destination.size());
		assertEquals("8", destination.get(1));
	}

	@Test
	public void testRemoveAtIndex() {
		source.addAll(oneTwoThreeList);

		source.remove(1);

		assertEquals(2, destination.size());
		assertEquals("3", destination.get(1));
	}
}
