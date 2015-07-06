/*******************************************************************************
 * Copyright 2015 xWic group (http://www.xwic.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 		http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 *  
 *******************************************************************************/
package de.xwic.appkit.webbase.toolkit.attachment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.jwic.base.Control;
import de.jwic.base.IControlContainer;
import de.jwic.base.IResourceControl;
import de.jwic.base.JWicRuntime;
import de.jwic.controls.dialogs.DialogEvent;
import de.jwic.controls.dialogs.DialogListener;
import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.file.uc.AttachmentWrapper;
import de.xwic.appkit.core.file.uc.IAttachmentWrapper;
import de.xwic.appkit.core.model.entities.IAnhang;
import de.xwic.appkit.core.model.entities.impl.Anhang;
import de.xwic.appkit.webbase.toolkit.app.ExtendedApplication;
import de.xwic.appkit.webbase.toolkit.editor.EditorModel;
import de.xwic.appkit.webbase.utils.MimeTypeUtil;

/**
 * Single Attachment control for one attachment.
 * 
 * Created on 25.02.2008
 * @author Ronny Pfretzschner
 */
public class SingleAttachmentControl extends Control implements
		IResourceControl, DialogListener {

    private IAttachmentWrapper attachment = null;
    private EditorModel model;
    private boolean deleteOnlyUnusedTmpFilesAtDestroy;
    private boolean readonlyView = false;
    
    /**
     * Creates the singel attachment control.
     * 
     * @param container
     * @param name
     * @param model
     * @param handler
     */
 	public SingleAttachmentControl(IControlContainer container, String name, EditorModel model) {
		super(container, name);
		this.model = model;
		this.setTemplateName(SingleAttachmentControl.class.getName());
	}

 	/**
 	 * 
 	 * @return readonly flag
 	 */
    public boolean readonly() {
    	return readonlyView;
    }

    /**
     * load the attachment
     * 
     * @param anhang
     */
	public void loadAttachment(IAnhang anhang) {
        if (anhang != null && attachment == null) {
            attachment = new AttachmentWrapper("atm", anhang);
        }
		
	}
	
	public IAttachmentWrapper saveAttachment() {
		return saveAttachment(model.getEntity());
	}
	
	/**
	 * save attachment
	 * 
	 * @param entity
	 * @return
	 */
    public IAttachmentWrapper saveAttachment(IEntity entity) {

        IAttachmentWrapper attm = attachment;
         if(attm == null)
            return null;
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
                    return attm;
                }
            }
            else if (attm.getTmpFileName() != null) { // is new...
                attm.setAnhang(new Anhang());
            	attm.setEntity(entity);
                
                return attm;
            }
        return attm;
    }
	
	
	/* (non-Javadoc)
	 * @see de.jwic.base.IResourceControl#attachResource(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void attachResource(HttpServletRequest req, HttpServletResponse res)
			throws IOException {
		
		if (attachment != null) {
			String filename = attachment.getFileName();
			
			res.setContentType(MimeTypeUtil.getMimeTypeForFileName(filename));
			res.setHeader ("Content-Disposition","attachment; filename=\"" + filename + "\"");
//		  	res.setContentLength((int)attachment.getContentLength());

			InputStream in = null;
		  	
            if (attachment.getTmpFileName() != null) {
                // its a temporary file, so read it from the file
                in = new FileInputStream(attachment.getTmpFileName());
            } else {
            	in = DAOSystem.getFileHandler().loadFileInputStream(attachment.getFileId());
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

	}

	
	
	/**
	 * Delete the attachment.
	 * @param key
	 */
    public void actionDelete(String key) {

        if (attachment!= null) {
            attachment.setDeleted(true);
            requireRedraw();
        }
    }

    /**
     * Add attachment.
     * @param key
     */
    public void actionAddAttm(String key) {
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
                
                if(attachment == null)
                attachment = 
                    new AttachmentWrapper(
                        "atm",
                        dialog.getFileName(),
                        file.getAbsolutePath(),
                        "",
                        dialog.getFileSize()                
                );
                else {
                    attachment.setDeleted(false);
                    attachment.setFileName(dialog.getFileName());
                    attachment.setContentLength(dialog.getFileSize());
                    attachment.setTmpFileName(path + File.separator + tmpName);
                }
                
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
		return attachment != null && !attachment.isDeleted();
	}
	
	/**
	 * 
	 * @param attachment
	 * @return download url
	 */
	public String getDownloadURL(IAttachmentWrapper attachment) {
		return getSessionContext().getCallBackURL() + "&_resreq=1&controlId=" + getControlID() + "&file=" + attachment.getKey();
	}

	public IAttachmentWrapper getAttachment() {
		return attachment;
	}
	
    String path = JWicRuntime.getJWicRuntime().getRootPath() + File.separator + "WEB-INF" + File.separator + "tempfiles";

    /*
     * (non-Javadoc)
     * @see de.jwic.base.Control#destroy()
     */
    public void destroy() {
        super.destroy();

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

	/**
	 * @param readonlyView The readonlyView to set.
	 */
	public void setReadonlyView(boolean readonlyView) {
		this.readonlyView = readonlyView;
		setRequireRedraw(true);
	}
}
