/**
 * 
 */
package de.xwic.appkit.cluster.comm;

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
	
	private String command = null;
	private String argument = null;
	private Serializable container = null;
	
	/**
	 * @param command
	 */
	public Message(String command) {
		super();
		this.command = command;
	}
	
	/**
	 * @param command
	 * @param argument
	 */
	public Message(String command, String argument) {
		super();
		this.command = command;
		this.argument = argument;
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
			return "[" + command + "] (" + argument + ")";
		} 
		return "[" + command + "]";
	}

	
}
