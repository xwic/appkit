/*
 * $Id: MultiAttachmentControl.java,v 1.6 2008/10/29 06:01:35 rpfretzschner Exp $
 *
 * Copyright (c) 2007 Network Appliance.
 * All rights reserved.

 * com.netapp.balanceit.tools.attachment.SingleAttachmentControl.java
 * Created on 25.02.2008
 * 
 * @author Ronny Pfretzschner
 */
package de.xwic.appkit.webbase.toolkit.attachment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.jwic.base.Control;
import de.jwic.base.IControlContainer;
import de.jwic.base.IResourceControl;
import de.jwic.base.JWicRuntime;
import de.jwic.ecolib.dialogs.DialogEvent;
import de.jwic.ecolib.dialogs.DialogListener;
import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.dao.IFileHandler;
import de.xwic.appkit.core.file.uc.AttachmentWrapper;
import de.xwic.appkit.core.file.uc.IAttachmentWrapper;
import de.xwic.appkit.webbase.toolkit.app.ExtendedApplication;
import de.xwic.appkit.webbase.toolkit.editor.EditorModel;

/**
 * Single Attachment control for one attachment.
 * 
 * Created on 25.02.2008
 * @author Ronny Pfretzschner
 */
public class MultiAttachmentControl extends Control implements
		IResourceControl, DialogListener {

    private List<IAttachmentWrapper> attachments = null;
    private int attachmentCount = 0;
    
    private EditorModel model;
    private boolean deleteOnlyUnusedTmpFilesAtDestroy;
    private boolean readonlyView = false;
    
    private IFileHandler handler;

    /**
     * Creates the singel attachment control.
     * 
     * @param container
     * @param name
     * @param model
     * @param handler
     */
 	public MultiAttachmentControl(IControlContainer container, String name, EditorModel model) {
		super(container, name);
		this.model = model;
		this.handler = DAOSystem.getFileHandler();
		this.setTemplateName(MultiAttachmentControl.class.getName());
	}

 	/**
 	 * 
 	 * @return readonly flag
 	 */
    public boolean isReadonlyView() {
    	return readonlyView;
    }

    /**
     * load the attachment
     * 
     * @param anhang
     */
	public void loadAttachmentList(IEntity entity) {
		attachments = model.getAttachmentsByEntity(entity);
		attachmentCount = attachments.size();
	}
	
	/**
	 * save attachment
	 * 
	 * @param entity
	 * @return
	 */
    public void saveAttachmentList(IEntity entity) {

    	if (attachments == null || entity == null) {
    		return;
    	}
    	
        for (Iterator iterator = attachments.iterator(); iterator.hasNext();) {
			IAttachmentWrapper attm = (IAttachmentWrapper) iterator.next();

	       if (!model.getAttachments().contains(attm))
	             model.getAttachments().add(attm);

	        if (attm.isDeleted()) {

                if (attm.getTmpFileName() != null) {
                	File file = new File(attm.getTmpFileName());
                	
                	if (file != null && file.isFile() && file.exists()) {
                		file.delete();
                	}
                }
                else if (attm.getFileId() != 0) {
                    attm.setDeleted(true);
                }
            }
            else if (attm.getTmpFileName() != null) { // is new...
                attm.setEntity(entity);
            }
		} 
    }
	
	
	/* (non-Javadoc)
	 * @see de.jwic.base.IResourceControl#attachResource(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void attachResource(HttpServletRequest req, HttpServletResponse res)
			throws IOException {
		
		
		IAttachmentWrapper attachment = null;
		String key = req.getParameter("file");
		
		for (IAttachmentWrapper att : attachments) {
			if (att != null && att.getKey().equals(key)) {
				attachment = att;
				break;
			}
		}
		
		if (attachment != null) {
			String filename = attachment.getFileName();
			
			//res.setContentType("application/octet-stream"); // force download (otherwise application/xls)
			res.setContentType("application/x-msdownload");
			res.setHeader ("Content-Disposition","attachment; filename=\"" + filename + "\"");
//		  	res.setContentLength((int)attachment.getContentLength());

			InputStream in = null;
		  	
            if (attachment.getTmpFileName() != null) {
                // its a temporary file, so read it from the file
                in = new FileInputStream(attachment.getTmpFileName());
            } else {
            	in = handler.loadFileInputStream(attachment.getFileId());
            }
		  	
            res.setContentLength(in != null ? in.available() : 0);
            
			OutputStream out = res.getOutputStream();
			
			//send the file as a stream of bytes
			try {
				byte[] buf = new byte[1024];
				int length = 0;

				while ((in != null) && ((length = in.read(buf)) != -1))
				{
					out.write(buf,0,length);
				}
			} catch (Exception e) {
				// error with server
				log.error("Error sending data to client (" + filename + ")", e);
			} finally {
				in.close();
				out.flush();
				out.close();
			}
		}
		else {
			res.setStatus(HttpServletResponse.SC_NOT_FOUND);
			res.getOutputStream().println("File not found/known");
		}

	}

	
	
	/**
	 * Delete the attachment.
	 * @param key
	 */
    public void actionDelete(String key) {

        IAttachmentWrapper attm = null;
        for (Iterator iterator = attachments.iterator(); iterator.hasNext();) {
			IAttachmentWrapper a = (IAttachmentWrapper) iterator.next();
            if (key != null && key.equals(a.getKey())) {
                attm = a;
                break;
            }
		} 
        
        if (attm != null) {
            attm.setDeleted(true);
            requireRedraw();
        }
        
    }

    /**
     * Add attachment.
     * @param key
     */
    public void actionAddAttm() {
    	AddAttachmentDialog dialog = new AddAttachmentDialog(ExtendedApplication.getInstance(this).getSite());
    	dialog.addDialogListener(this);
    	dialog.open();
    }

    /*
     * (non-Javadoc)
     * @see de.jwic.ecolib.dialogs.DialogListener#dialogAborted(de.jwic.ecolib.dialogs.DialogEvent)
     */
	public void dialogAborted(DialogEvent event) {
		// TODO Auto-generated method stub
		
	}

	/*
	 * (non-Javadoc)
	 * @see de.jwic.ecolib.dialogs.DialogListener#dialogFinished(de.jwic.ecolib.dialogs.DialogEvent)
	 */
	public void dialogFinished(DialogEvent event) {
        // store file temporary
        String path = JWicRuntime.getJWicRuntime().getRootPath() + File.separator + "WEB-INF" + File.separator + "tempfiles";

        long idx = new Date().getTime();
        String tmpName = "tmp_" + idx;
        AddAttachmentDialog dialog = (AddAttachmentDialog) event.getEventSource();
        InputStream in = dialog.getFileInputStream();
        try {
            if (in != null) {
            	//check, if directory exists
            	File tempPath = new File(path);
            	
            	if (!tempPath.exists()) {
            		tempPath.mkdir();
            	}
            	
                File file = new File(path + File.separator + tmpName);
            	
                FileOutputStream writer = new FileOutputStream(file);
                
                int charInt = in.read();
                
                while (charInt != -1) {
                	writer.write(charInt);
                	charInt = in.read();
                }
                
                writer.close();
                
                IAttachmentWrapper attachment = 
                    new AttachmentWrapper(
                        "atm" + (++attachmentCount),
                        dialog.getFileName(),
                        file.getAbsolutePath(),
                        "",
                        dialog.getFileSize()                
                );
                attachments.add(attachment);
                
                in.close();
                requireRedraw();
            }
        } catch (Exception ex) {
        	log.error(ex);
        	throw new RuntimeException(ex);
        }
		
	}

	/**
	 * 
	 * @return
	 */
	public boolean hasAttachments() {
		for (Iterator iterator = attachments.iterator(); iterator.hasNext();) {
			IAttachmentWrapper attachment = (IAttachmentWrapper) iterator.next();
			if (attachment != null && !attachment.isDeleted()) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @param attachment
	 * @return download url
	 */
	public String getDownloadURL(IAttachmentWrapper attachment) {
		return getSessionContext().getCallBackURL() + "&_resreq=1&controlId=" + getControlID() + "&file=" + attachment.getKey();
	}

	
    String path = JWicRuntime.getJWicRuntime().getRootPath() + File.separator + "WEB-INF" + File.separator + "tempfiles";

    /*
     * (non-Javadoc)
     * @see de.jwic.base.Control#destroy()
     */
    public void destroy() {
        super.destroy();
        
        if (attachments != null) {
	        for (Iterator iterator = attachments.iterator(); iterator.hasNext();) {
				IAttachmentWrapper attachment = (IAttachmentWrapper) iterator.next();
	
		        // delete unprocessed uploaded files
		        if (!deleteOnlyUnusedTmpFilesAtDestroy && attachment != null && attachment.getTmpFileName() != null) {
		            try {
		                File file = new File(attachment.getTmpFileName());
		                
		                if (file != null && file.isFile() && file.exists()) {
		                	file.delete();
		                }
		            }
		            catch (Exception e) {
		                log.error("Error deleting temporary file " + attachment.getTmpFileName());
		            }
		        }
			}
        }
    }

	/**
	 * @param readonlyView The readonlyView to set.
	 */
	public void setReadonlyView(boolean readonlyView) {
		this.readonlyView = readonlyView;
	}

	/**
	 * @return the attachments
	 */
	public List<IAttachmentWrapper> getAttachments() {
		return attachments;
	}
}
