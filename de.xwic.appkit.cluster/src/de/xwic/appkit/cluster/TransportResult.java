/**
 * 
 */
package de.xwic.appkit.cluster;


/**
 * Wrapper for responses returning as a result of a message send to the cluster.
 * Contains information about the node the response and details about errors
 * if they occure.
 * @author lippisch
 */
public class TransportResult {

	private Response response;
	private INode node;
	private Throwable exception = null;
	/**
	 * @param response
	 * @param node
	 * @param exception
	 */
	public TransportResult(Response response, INode node, Throwable exception) {
		super();
		this.response = response;
		this.node = node;
		this.exception = exception;
	}
	/**
	 * @return the response
	 */
	public Response getResponse() {
		return response;
	}
	/**
	 * @return the node
	 */
	public INode getNode() {
		return node;
	}
	/**
	 * @return the exception
	 */
	public Throwable getException() {
		return exception;
	}
	
	
	
}
