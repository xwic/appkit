/*
 * Copyright (c) NetApp Inc. - All Rights Reserved
 * 
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 * 
 * com.netapp.pulse.start.ui.home.news.INewsEditorHandler 
 */
package de.xwic.appkit.webbase.home.news;

/**
 * Simple handler to act on editor updates. For a simple editor like the NewsEditor, more convenient 
 * than a full blown event mechanism.
 * @author lippisch
 */
public interface INewsEditorHandler {

	/**
	 * Invoked when the editor is either saved & closed or aborted.
	 * @param saved
	 */
	public void editorDone(boolean saved);
	
}
