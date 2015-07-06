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
