/**
 * 
 */
package de.xwic.appkit.cluster.comm;

import java.net.Socket;
import java.util.Date;


/**
 * Handles a console client used by admins or test tools.
 * 
 * @author lippisch
 */
public class ConsoleClientProtocol implements ICommProtocol {

	public final static String CMD_TIME = "time";
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.cluster.comm.ICommProtocol#handleMessage(java.net.Socket, de.xwic.appkit.cluster.comm.Message)
	 */
	@Override
	public Response handleMessage(Socket socket, Message inMessage) {
		
		if (inMessage.getCommand().equals(CMD_TIME)) {
			return new Response(true, new Date().toString());
		} else {
			return new Response(false, "Unknown Command");
		}
		
		
	}

}
