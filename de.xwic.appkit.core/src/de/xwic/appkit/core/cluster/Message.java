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

import java.io.Serializable;

/**
 * Carrier for information between any type of client and the actual server/node. 
 * Allows embedding of additional objects.
 * 
 * @author lippisch
 *
 */
public class Message implements Serializable {

	public final static String CMD_IDENTIFY_CLIENT = "IDME";
	
	private final static long INTERNAL_MAX_ID = Long.MAX_VALUE - 200; 
	
	private static long nextInternalId = 1;
	
	private String command = null;
	private String argument = null;
	private Serializable container = null;
	
	private long messageId = 0;
	
	/**
	 * @param command
	 */
	public Message(String command) {
		super();
		this.command = command;
		createInternalId();
	}
	
	/**
	 * @param command
	 * @param argument
	 */
	public Message(String command, String argument) {
		super();
		this.command = command;
		this.argument = argument;
		createInternalId();
	}

	/**
	 * @param command
	 * @param argument
	 * @param container
	 */
	public Message(String command, String argument, Serializable container) {
		super();
		this.command = command;
		this.argument = argument;
		this.container = container;
		createInternalId();
	}

	/**
	 * For performance reasons, this method is not synchronized.
	 */
	private void createInternalId() {
		messageId = nextInternalId++;
		if (nextInternalId > INTERNAL_MAX_ID) {
			nextInternalId = 1;
		}
	}



	/**
	 * @return the argument
	 */
	public String getArgument() {
		return argument;
	}
	/**
	 * @return the container
	 */
	public Serializable getContainer() {
		return container;
	}



	/**
	 * @return the command
	 */
	public String getCommand() {
		return command;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		if (argument != null) {
			return "#" + messageId + " [" + command + "] (" + argument + ")";
		} 
		return "#" + messageId + " [" + command + "]";
	}

	/**
	 * @return the messageId
	 */
	public long getMessageId() {
		return messageId;
	}

	
}
