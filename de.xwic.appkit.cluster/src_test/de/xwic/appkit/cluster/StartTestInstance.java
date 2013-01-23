/**
 * 
 */
package de.xwic.appkit.cluster;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.log4j.BasicConfigurator;

import de.xwic.appkit.cluster.impl.Cluster;

/**
 * @author lippisch
 *
 */
public class StartTestInstance {

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
		
		
		if (args.length > 1) {
			for (int i = 1; i < args.length; i++) {
				cc.addKnownNode(new NodeAddress("localhost", Integer.parseInt(args[i])));
			}
		}
		
		System.out.println("Initializing Cluster....");
		ClusterManager.init(cc);
		
		
		// be in wait state...
		System.out.println("Press ENTER to exit.");
		BufferedReader bis = new BufferedReader(new InputStreamReader(System.in));
		try {
			bis.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
}
