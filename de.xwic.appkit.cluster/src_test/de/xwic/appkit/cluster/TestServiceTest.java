/**
 * 
 */
package de.xwic.appkit.cluster;

import java.util.HashSet;
import java.util.Set;

/**
 * @author lippisch
 */
public class TestServiceTest implements Runnable {

	private Set<Long> numbersToken = new HashSet<Long>();
	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {

		ICluster cluster = ClusterManager.getCluster();
		
		while (true) {
			
			try {
				Thread.sleep(3000); // wait 3 sec.
			} catch (InterruptedException e) {}
			
			try {
				ITestClusterService service = (ITestClusterService) cluster.getClusterService(ITestClusterService.SERVICE_NAME);
				if (service == null) {
					System.out.println("ERROR: service is null");
				} else {
					
					long nextNumber = service.takeNextNumber();
					System.out.println("Next Number is: " + nextNumber);
					
					cluster.sendEvent(new ClusterEvent("TestNumberSequence", "NumberToken", new Long(nextNumber)), true);
					addNumber(nextNumber);
				}
				
			} catch (Throwable t) {
				t.printStackTrace();
			}
			
		}
		
	}

	public void addNumber(Long num) {
		synchronized (numbersToken) {
			
			if (numbersToken.contains(num)) {
				System.out.println("ERROR - Number " + num + " was already taken!");
			} else {
				numbersToken.add(num);
			}
		}
	}
	
}
