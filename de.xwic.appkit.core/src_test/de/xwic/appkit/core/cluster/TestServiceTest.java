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
