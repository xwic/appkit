/**
 * 
 */
package de.xwic.appkit.cluster;

/**
 * Test implementation of a service that returns a number in sequence unique across the cluster.
 * @author lippisch
 */
public class TestClusterService extends AbstractClusterService implements IClusterService, ITestClusterService {

	private long nextNumber = 0;
	
	
	/**
	 * Returns the next number in the sequence.
	 * @return
	 */
	public long takeNextNumber() {
		
		long result = nextNumber;
		nextNumber++;
		
		return result;
	}

}
