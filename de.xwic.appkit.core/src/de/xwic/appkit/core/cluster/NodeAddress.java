/**
 * 
 */
package de.xwic.appkit.core.cluster;

import java.io.Serializable;

/**
 * Represents the address of a node, which includes the hostname and port.
 * @author lippisch
 */
public class NodeAddress implements Serializable {

	private String hostname;
	private int port;
	/**
	 * @param hostname
	 * @param port
	 */
	public NodeAddress(String hostname, int port) {
		super();
		this.hostname = hostname;
		this.port = port;
		
	}
	/**
	 * @return the hostname
	 */
	public String getHostname() {
		return hostname;
	}
	/**
	 * @return the port
	 */
	public int getPort() {
		return port;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((hostname == null) ? 0 : hostname.hashCode());
		result = prime * result + port;
		return result;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NodeAddress other = (NodeAddress) obj;
		if (hostname == null) {
			if (other.hostname != null)
				return false;
		} else if (!hostname.equals(other.hostname))
			return false;
		if (port != other.port)
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return hostname + ":" + port;
	}
	
}
