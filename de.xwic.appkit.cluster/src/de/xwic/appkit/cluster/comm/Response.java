/**
 * 
 */
package de.xwic.appkit.cluster.comm;

import java.io.Serializable;

/**
 * @author lippisch
 *
 */
public class Response implements Serializable {

	private boolean success;
	private String reason = null;
	private Message details = null;
	
	/**
	 * @param success
	 */
	public Response(boolean success) {
		super();
		this.success = success;
	}
	
	
	/**
	 * @param success
	 * @param reason
	 */
	public Response(boolean success, String reason) {
		super();
		this.success = success;
		this.reason = reason;
	}


	/**
	 * @param success
	 * @param details
	 */
	public Response(boolean success, Message details) {
		super();
		this.success = success;
		this.details = details;
	}
	/**
	 * @return the success
	 */
	public boolean isSuccess() {
		return success;
	}
	/**
	 * @return the details
	 */
	public Message getDetails() {
		return details;
	}
	/**
	 * @return the reason
	 */
	public String getReason() {
		return reason;
	} 
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return (success ? "Success" : "Failed") + (reason != null ? " (" + reason + ")" : "");
	}
	
}
