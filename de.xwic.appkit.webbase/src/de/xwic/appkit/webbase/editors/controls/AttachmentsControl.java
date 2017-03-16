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
package de.xwic.appkit.webbase.editors.controls;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.jwic.base.Control;
import de.jwic.base.IControlContainer;
import de.jwic.base.IResourceControl;
import de.jwic.base.ImageRef;
import de.jwic.base.JWicRuntime;
import de.jwic.controls.dialogs.DialogEvent;
import de.jwic.controls.dialogs.DialogListener;
import de.xwic.appkit.core.dao.DAOProviderAPI;
import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.dao.EntityList;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.dao.IFileHandler;
import de.xwic.appkit.core.dao.UseCase;
import de.xwic.appkit.core.file.uc.AttachmentUseCase;
import de.xwic.appkit.core.file.uc.AttachmentWrapper;
import de.xwic.appkit.core.file.uc.IAttachmentWrapper;
import de.xwic.appkit.core.model.daos.IAnhangDAO;
import de.xwic.appkit.core.model.entities.IAnhang;
import de.xwic.appkit.core.model.queries.PropertyQuery;
import de.xwic.appkit.webbase.toolkit.app.ExtendedApplication;
import de.xwic.appkit.webbase.toolkit.editor.EditorModel;
import de.xwic.appkit.webbase.toolkit.util.FileDetailsProvider;
import de.xwic.appkit.webbase.utils.MimeTypeUtil;

/**
 * Allows users to upload, download or delete attachments linked to the underlying entity.
 * 
 */
public class AttachmentsControl extends Control implements IResourceControl, DialogListener {

	
	
	private List<IAttachmentWrapper> attachments = null;
    private int attachmentCount = 0;
    
    private boolean deleteOnlyUnusedTmpFilesAtDestroy;
    private boolean readonlyView = false;
    
    private int width = -1;
    private int height = -1; 
    
    private IFileHandler handler;

    /**
     * Creates the attachment control.
     * 
     * @param container
     * @param name
     * @param model
     * @param handler
     */
 	public AttachmentsControl(IControlContainer container, String name) {
		super(container, name);
		this.handler = DAOSystem.getFileHandler();
		this.setTemplateName(AttachmentsControl.class.getName());
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
    	IAnhangDAO ahDAO = (IAnhangDAO) DAOSystem.getDAO(IAnhangDAO.class);
    	attachments = new ArrayList<IAttachmentWrapper>();

    	//new entity! no attachments possible!
    	if (entity != null && entity.getId() > 0) {
    	
	    	PropertyQuery query = new PropertyQuery();
	        query.addEquals("entityType", entity.type().getName());
	        query.addEquals("entityID", entity.getId());
	        
	        List<IAnhang> list = ahDAO.getEntities(null, query);
	        int attmCount = 0;
	        
	        for (IAnhang ah : list) {
				AttachmentWrapper attm = new AttachmentWrapper("atm" + (++attmCount), ah);
	            attm.setEntity(entity);
	            attachments.add(attm);
			}
    	}
		attachmentCount = attachments.size();
	}
	
	/**
	 * save attachment
	 * 
	 * @param entity
	 * @return
	 */
    public void saveAttachmentList(IEntity entity) {

    	if (attachments != null && entity != null) {
    		if (entity.getId() == 0) {
    			throw new IllegalStateException("Cannot save attachments on an entity that is new. Save entity first.");
    		}
    		
	        for (IAttachmentWrapper attm : attachments) {
	
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
	        
			final AttachmentUseCase att = new AttachmentUseCase(attachments, DAOSystem.getFileHandler());
			new UseCase() {
				@Override
				protected Object execute(DAOProviderAPI api) {
					att.run(api);
					return null;
				}
			}.execute();

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
			
			res.setContentType(MimeTypeUtil.getMimeTypeForFileName(filename));
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
        	if (attachments == null) {
            	attachments = new ArrayList<IAttachmentWrapper>();
        	}
        	
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
		if (attachments != null){
			for (Iterator iterator = attachments.iterator(); iterator.hasNext();) {
				IAttachmentWrapper attachment = (IAttachmentWrapper) iterator.next();
				if (attachment != null && !attachment.isDeleted()) {
					return true;
				}
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
		requireRedraw();
	}

	/**
	 * @return the attachments
	 */
	public List<IAttachmentWrapper> getAttachments() {
		return attachments;
	}

	/**
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @param width the width to set
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * @return the height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * @param height the height to set
	 */
	public void setHeight(int height) {
		this.height = height;
	}
	
	/**
	 * Returns an image that represents the file type.
	 * @param attm
	 * @return
	 */
	public ImageRef getFileIcon(IAttachmentWrapper attm) {
		return FileDetailsProvider.getFileIcon(attm.getFileName());
	}
	
	/**
	 * Returns details such as size formatted.
	 * @param attm
	 * @return
	 */
	public String getFileInfo(IAttachmentWrapper attm) {
		
		DecimalFormat decimalFormat = (DecimalFormat) getSessionContext().getDecimalFormat().clone();
		decimalFormat.setMaximumFractionDigits(1);
		decimalFormat.setMinimumFractionDigits(0);
		
		StringBuilder sb = new StringBuilder();
		sb.append(FileDetailsProvider.getFileSizeString(attm.getContentLength(), decimalFormat));
		return sb.toString();
	}
	
}
