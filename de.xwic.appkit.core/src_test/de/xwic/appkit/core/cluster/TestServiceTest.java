/**
 * 
 */
package de.xwic.appkit.core.cluster;


/**
 * @author lippisch
 */
public class TestServiceTest implements Runnable {

	
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
					System.err.println("ERROR: service is null");
				} else {
					
					long nextNumber = service.takeNextNumber();
					//System.out.println("Next Number is: " + nextNumber);
					
					cluster.sendEvent(new ClusterEvent("TestNumberSequence", "NumberToken", new Long(nextNumber)), true);
					StartTestInstance.addNumber(nextNumber);
				}
				
			} catch (Throwable t) {
				t.printStackTrace();
			}
			
		}
		
	}
	
}
