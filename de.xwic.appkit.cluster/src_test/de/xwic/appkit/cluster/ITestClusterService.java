/**
 * 
 */
package de.xwic.appkit.cluster;

/**
 * @author lippisch
 *
 */
public interface ITestClusterService {

	String SERVICE_NAME = "NumberSequence";

	/**
	 * Returns the next number.
	 * @return
	 */
	public long takeNextNumber();
	
}
