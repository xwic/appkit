/*
 * (c) Copyright 2005, 2006 by pol GmbH 
 * 
 * de.xwic.appkit.webbase.tools.extensions.IEntityEditorPerspective
 * Created on Jun 13, 2007 by Aron Cotrau
 *
 */
package de.xwic.appkit.webbase.tools.extensions;

/**
 * Interface for wrapping Entity editor perspectives.
 *
 * @author Aron Cotrau
 */
public interface IEntityEditorPerspective {

	/**
	 * @return the name of the perspective
	 */
	public String getPerspectiveId(); 
	
	/**
	 * @return the entity shown in the editor
	 */
	public String getEntityType();
}
