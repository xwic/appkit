/*******************************************************************************
 * Copyright 2015 xWic group (http://www.xwic.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 		http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 *  
 *******************************************************************************/
/**
 * 
 */
package de.xwic.appkit.core.cluster;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.BasicConfigurator;

/**
 * @author lippisch
 *
 */
public class StartTestInstance {

	private static Set<Long> numbersToken = new HashSet<Long>();

	/**
	 * Startup a test instance of the server.
	 * @param args
	 */
	public static void main(String[] args) {
		
		// setup environment
		BasicConfigurator.configure();
		
		ClusterConfiguration cc = new ClusterConfiguration();

		if (args.length > 0) {
			cc.setPortNumber(Integer.parseInt(args[0]));
		}

		cc.setClusterName("Testcluster");
		cc.setNodeName("Node" + cc.getPortNumber());
		cc.setMasterPriority(cc.getPortNumber());
		
		
		if (args.length > 1) {
			for (int i = 1; i < args.length; i++) {
				cc.addKnownNode(new NodeAddress("localhost", Integer.parseInt(args[i])));
			}
		}
		
		System.out.println("Initializing Cluster....");
		ClusterManager.init(cc);
		
		
		ICluster cluster = ClusterManager.getCluster();
		/*
		cluster.addEventListener(new ClusterEventListener() {
			
			@Override
			public void receivedEvent(ClusterEvent event) {
				System.out.println("General Event received: " + event);
			}
		});

		cluster.addEventListener(new ClusterEventListener() {
			
			@Override
			public void receivedEvent(ClusterEvent event) {
				System.out.println("TEST Event received: " + event);
			}
		}, "test");
		*/

		// register the test service
		cluster.registerClusterService(ITestClusterService.SERVICE_NAME, new TestClusterService());
		
		// create a test-thread that issues a number from the testClusterService and validates its sequence

		for (int i = 0; i < 30; i++) {
			final TestServiceTest tst = new TestServiceTest();
			Thread t = new Thread(tst);
			t.setDaemon(true);
			t.start();
			
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		cluster.addEventListener(new ClusterEventListener() {
			
			@Override
			public void receivedEvent(ClusterEvent event) {
				if ("NumberToken".equals(event.getName())) {
					Long number = (Long)event.getData();
					if (number != null) {
						addNumber(number);
					}
				}
			}
		}, "TestNumberSequence");

		
		// create 20 test-threads that send messages all the time.
		/*
		for (int i = 0; i < 20; i++) {
			final String xn = "t" + i;
			Runnable tRun = new Runnable() {
				@Override
				public void run() {

					ICluster cluster = ClusterManager.instance();
					while (true) {
						try {
								cluster.sendEvent(new ClusterEvent(xn, Long.toString(System.currentTimeMillis())), false);
								Thread.sleep(new Random().nextInt(1000));
						} catch (EventTimeOutException e) {
							e.printStackTrace();
						} catch (InterruptedException e) {
						}
					}
					
				}
			};
			Thread t = new Thread(tRun);
			t.setDaemon(true);
			t.start();
		}
		*/
		
		// be in wait state...
		System.out.println("Press ENTER to exit.");
		BufferedReader bis = new BufferedReader(new InputStreamReader(System.in));
		try {
			bis.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	public static void addNumber(Long num) {
		synchronized (numbersToken) {
			
			if (numbersToken.contains(num)) {
				System.out.println("ERROR - Number " + num + " was already taken!");
			} else {
				numbersToken.add(num);
			}
		}
	}

}
