package de.xwic.appkit.webbase.toolkit.model;

public interface IModelEventSupport {

	/**
	 * Add model listener to get the model changes.
	 * 
	 * @param listener
	 */
	public abstract void addModelListener(IModelListener listener);

	/**
	 * Removes the given listener.
	 * 
	 * @param listener
	 */
	public abstract void removeModelListener(IModelListener listener);

}