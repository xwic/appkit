/**
 * 
 */
package de.xwic.appkit.core.cluster;

import java.io.Serializable;

/**
 * @author lippisch
 *
 */
public class Response implements Serializable {

	private boolean success;
	private String reason = null;
	private Serializable data = null;
	
	private long responseTo = 0;
	
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
	public Response(boolean success, String reason, Serializable data) {
		super();
		this.success = success;
		this.data = data;
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
	public Serializable getData() {
		return data;
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


	/**
	 * @return the responseTo
	 */
	public long getResponseTo() {
		return responseTo;
	}


	/**
	 * @param responseTo the responseTo to set
	 */
	public void setResponseTo(long responseTo) {
		this.responseTo = responseTo;
	}
	
}
