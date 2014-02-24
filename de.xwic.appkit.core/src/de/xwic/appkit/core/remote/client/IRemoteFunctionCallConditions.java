/**
 * 
 */
package de.xwic.appkit.core.remote.client;


/**
 * Interface to define remote function call. Client and server need to have
 * access to this implementation. Currently it's only implemented for static 
 * functions. The method which should be called needs to have following structure:
 * Object functionName(IRemoteFunctionCallConditions conditions);
 * @author dotto
 *
 */
public abstract interface IRemoteFunctionCallConditions {
	
	/**
	 * Classname which contains the method to be called
	 * @return
	 */
	String className();
	
	/**
	 * Name of the function to be called.
	 * @return
	 */
	String functionName();
}
