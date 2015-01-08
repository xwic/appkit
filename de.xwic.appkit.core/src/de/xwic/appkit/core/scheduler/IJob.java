package de.xwic.appkit.core.scheduler;

/**
 * @author dotto
 * 
 */
public interface IJob {

	/**
	 * To be implemented by extended Job class.
	 * 
	 * @throws Exception
	 */
	public abstract String execute() throws Exception;

	/**
	 * @return
	 */
	public abstract String getTitle();

}
