/*
 * $Id: AddAttachmentDialog.java,v 1.1 2008/05/21 12:40:39 rpfretzschner Exp $
 *
 * Copyright (c) 2007 Network Appliance.
 * All rights reserved.

 * com.netapp.balanceit.tools.attachment.AddAttachmentDialog.java
 * Created on 25.02.2008
 * 
 * @author Ronny Pfretzschner
 */
package de.xwic.appkit.webbase.toolkit.attachment;

import java.io.IOException;
import java.io.InputStream;
import java.text.NumberFormat;

import de.jwic.base.IControlContainer;
import de.jwic.controls.ButtonControl;
import de.jwic.controls.FileUploadControl;
import de.jwic.controls.WindowControl;
import de.jwic.ecolib.controls.ErrorWarningControl;
import de.jwic.events.SelectionEvent;
import de.jwic.events.SelectionListener;
import de.xwic.appkit.webbase.toolkit.app.Site;
import de.xwic.appkit.webbase.toolkit.components.Dialog;
import de.xwic.appkit.webbase.toolkit.util.ImageLibrary;


/**
 * Small dialog for uploading a file from local (client side) file system.
 * 
 * Created on 25.02.2008
 * @author Ronny Pfretzschner
 */
public class AddAttachmentDialog extends  Dialog {

	public static final int MAX_FILE_SIZE_UNKNOWN = -1;
	
	private FileUploadControl fileUpload;
	private final static int MAX_FILE_SIZE = 4;
	
	private ErrorWarningControl error = null;
	
	private String fileName = null;
	private long fileSize = 0;
	
	private InputStream fileInputStream = null;
	
	private int maxFileSize = MAX_FILE_SIZE;
	
	/**
	 * Creates small dialog for uploading a file from local (client side) file system.
	 * 
	 * @param site
	 */
	public AddAttachmentDialog(Site site) {
		super(site);
	}

	/* (non-Javadoc)
	 * @see de.jwic.wap.core.Dialog#createControls(de.jwic.base.IControlContainer)
	 */
	public void createControls(IControlContainer container) {
		WindowControl win = new WindowControl(container, "scrollcontainer");
		win.setTemplateName(getClass().getName());
		win.setWidth("450");
		win.setAlign("center");
		win.setTitle("File Upload");
	
		error = new ErrorWarningControl(win, "error");
		
        fileUpload = new FileUploadControl(win, "fileUpload");
        fileUpload.setWidth("100%");
        
        
		ButtonControl btFinish = new ButtonControl(win, "Finish");
		btFinish.setTitle("Upload");
		btFinish.setIconEnabled(ImageLibrary.ICON_SAVECLOSE_ACTIVE);
		btFinish.setIconDisabled(ImageLibrary.ICON_SAVECLOSE_INACTIVE);
		btFinish.setSubmitButton(true);
		btFinish.addSelectionListener(new SelectionListener() {
			public void objectSelected(SelectionEvent event) {
				boolean erg = validate();
				if (erg) {
					finish();
				}
			}
		});
	
		ButtonControl btAbort = new ButtonControl(win, "Abort");
		btAbort.setTitle("Cancel");
		btAbort.setIconDisabled(ImageLibrary.ICON_ABORT_INACTIVE);
		btAbort.setIconEnabled(ImageLibrary.ICON_ABORT_ACTIVE);
		
		btAbort.addSelectionListener(new SelectionListener() {
			public void objectSelected(SelectionEvent event) {
				abort();
			}
		});
	}
	
	/**
	 * Validate the selection.
	 * 
	 * @return
	 */
	private boolean validate() {
        if (!fileUpload.isFileUploaded()) {
            error.showError("You must select a file");
            return false;
        }

		if (maxFileSize != MAX_FILE_SIZE_UNKNOWN) {
			// check size! Recalculate to byte!
			if (fileUpload.getFileSize() > (maxFileSize * 1024 * 1024)) {
				error.showError("The filesize is too large for an upload. Only files smaller than "
						+ NumberFormat.getIntegerInstance().format(new Integer(MAX_FILE_SIZE)) + "MB are allowed!");
				return false;
			}
		}
        
        return true;
	}

	/*
	 * (non-Javadoc)
	 * @see de.xwic.appkit.webbase.toolkit.components.Dialog#finish()
	 */
	public void finish() {
		try {
			this.fileInputStream = fileUpload.getInputStream();
		} catch (IOException e) {
			error.showError(e);
			return;
		}
		this.fileName = fileUpload.getFileName();
		this.fileSize = fileUpload.getFileSize();
		super.finish();
	}
	
	/**
	 * 
	 * @return the FileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * 
	 * @return the file size.
	 */
	public long getFileSize() {
		return fileSize;
	}

	/**
	 * 
	 * @return file inputstream
	 */
	public InputStream getFileInputStream() {
		return fileInputStream;
	}
	
	@Override
	public void destroy() {
		
	}

	/**
	 * @return the maxFileSize
	 */
	public int getMaxFileSize() {
		return maxFileSize;
	}

	/**
	 * @param maxFileSize the maxFileSize to set
	 */
	public void setMaxFileSize(int maxFileSize) {
		this.maxFileSize = maxFileSize;
	}
	
}
