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

import de.jwic.base.ControlContainer;
import de.jwic.base.IControlContainer;
import de.jwic.controls.Button;
import de.jwic.controls.ErrorWarning;
import de.jwic.controls.FileUpload;
import de.jwic.controls.Window;
import de.jwic.events.SelectionEvent;
import de.jwic.events.SelectionListener;
import de.xwic.appkit.webbase.dialog.CenteredWindow;
import de.xwic.appkit.webbase.dialog.DialogContent;
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
	private final static int MAX_FILE_SIZE = 4;
	
	private String fileName = null;
	private long fileSize = 0;
	private InputStream fileInputStream = null;
	
	private int maxFileSize = MAX_FILE_SIZE;

	protected ErrorWarning error = null;
	protected FileUpload fileUpload;
	protected Button btFinish;
	protected Button btAbort;
	
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
		
		error = new ErrorWarning(container, "error");
		
        fileUpload = new FileUpload(container, "fileUpload");
//        fileUpload.setWidth(53);
        
		btFinish = this.btOk;
		btFinish.setTitle("Upload");
		btFinish.setIconEnabled(ImageLibrary.ICON_SAVECLOSE_ACTIVE);
		btFinish.setIconDisabled(ImageLibrary.ICON_SAVECLOSE_INACTIVE);
//		btFinish.setSubmitButton(true);
		btFinish.addSelectionListener(new SelectionListener() {
			public void objectSelected(SelectionEvent event) {
				boolean erg = validate();
				if (erg) {
					finish();
				}
			}
		});
	
		btAbort = this.btCancel;
		btAbort.setTitle("Cancel");
		btAbort.setVisible(true);
		btAbort.setIconDisabled(ImageLibrary.ICON_ABORT_INACTIVE);
		btAbort.setIconEnabled(ImageLibrary.ICON_ABORT_ACTIVE);
		
		btAbort.addSelectionListener(new SelectionListener() {
			public void objectSelected(SelectionEvent event) {
				abort();
			}
		});
	}
	
	@Override
	protected void createContent(DialogContent content) {
		ControlContainer wrapper = new ControlContainer(content,"wrapper");
		wrapper.setTemplateName(AddAttachmentDialog.class.getName()+"_layout");
		this.createControls(wrapper);
	}
	
	/**
	 * Validate the selection.
	 * 
	 * @return
	 */
	protected boolean validate() {
        if (!fileUpload.isFileUploaded()) {
            error.showError("You must select a file");
            return false;
        }

		if (maxFileSize != MAX_FILE_SIZE_UNKNOWN) {
			// check size! Recalculate to byte!
			if (fileUpload.getFileSize() > (maxFileSize * 1024 * 1024)) {
				error.showError("The filesize is too large for an upload. Only files smaller than "
						+ NumberFormat.getIntegerInstance().format(new Integer(maxFileSize)) + "MB are allowed!");
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

	/**
	 * @return the btFinish
	 */
	public Button getBtFinish() {
		return btFinish;
	}

	/**
	 * @return the btAbort
	 */
	public Button getBtAbort() {
		return btAbort;
	}

	
}
