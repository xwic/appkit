/**
 * 
 */
package de.xwic.appkit.core.cluster;

import java.io.Serializable;

/**
 * An event is a message that is delivered to all nodes in the cluster in a sequence.
 * @author lippisch
 */
public class ClusterEvent implements Serializable {

	private String namespace;
	private String name;
	private Serializable data;
	/**
	 * @param namespace
	 * @param name
	 */
	public ClusterEvent(String namespace, String name) {
		super();
		this.namespace = namespace;
		this.name = name;
	}
	/**
	 * @param namespace
	 * @param name
	 * @param data
	 */
	public ClusterEvent(String namespace, String name, Serializable data) {
		super();
		this.namespace = namespace;
		this.name = name;
		this.data = data;
	}
	
	/**
	 * @return the namespace
	 */
	public String getNamespace() {
		return namespace;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @return the data
	 */
	public Serializable getData() {
		return data;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Event[" + namespace + ":" + name + "]";
	}
	
}
