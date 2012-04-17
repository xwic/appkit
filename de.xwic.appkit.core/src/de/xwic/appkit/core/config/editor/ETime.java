/*
 * (c) Copyright 2005, 2006 by pol GmbH 
 * 
 * de.xwic.appkit.core.config.editor.ETime
 * Created on Sep 27, 2007 by Aron Cotrau
 *
 */
package de.xwic.appkit.core.config.editor;


/**
 * Time field.
 *
 * @author Aron Cotrau
 * @editortag time
 */
public class ETime extends EField {
	
    boolean seconds = false;
    
    /**
	 * @return Returns the seconds.
	 */
	public boolean getSeconds() {
		return seconds;
	}
	/**
	 * @param seconds The seconds to set.
	 * @default false
	 */
	public void setSeconds(boolean seconds) {
		this.seconds = seconds;
	}
}
