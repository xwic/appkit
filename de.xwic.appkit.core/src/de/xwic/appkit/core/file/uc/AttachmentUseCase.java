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
/**
 * 
 */
package de.xwic.appkit.core.file.uc;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.xwic.appkit.core.dao.DAOCallback;
import de.xwic.appkit.core.dao.DAOProviderAPI;
import de.xwic.appkit.core.dao.DataAccessException;
import de.xwic.appkit.core.dao.IFileHandler;
import de.xwic.appkit.core.model.entities.IAnhang;
import de.xwic.appkit.core.model.entities.impl.Anhang;

/**
 * @author Ronny Pfretzschner
 *
 */
public class AttachmentUseCase implements DAOCallback {

	private List attachments;
	private IFileHandler handler;
	
	private Log log = LogFactory.getLog(AttachmentUseCase.class);
	
	public AttachmentUseCase(List attachmentWrappers, IFileHandler handler) {
		this.handler = handler;
		this.attachments = attachmentWrappers;
	}
	
	
	/* (non-Javadoc)
	 * @see de.jwic.entitytools.base.DAOCallback#run(de.jwic.entitytools.base.DAOProviderAPI)
	 */
	public Object run(DAOProviderAPI api) {

		for (Iterator iterator = attachments.iterator(); iterator.hasNext();) {
			IAttachmentWrapper wrapper = (IAttachmentWrapper) iterator.next();
			
			IAnhang attachment = wrapper.getAnhang();
			
            if (wrapper.isDeleted() && attachment != null) {
                api.delete(attachment);
                if (wrapper.getFileId() > 0) {
                    handler.deleteFileInUC(wrapper.getFileId());
                    wrapper.setFileId(-1);
                }
            }
            
            else if (wrapper.getTmpFileName() != null && !wrapper.isDeleted()) {

                if (wrapper.getEntityId() == 0) {
                    throw new DataAccessException("The entity has not (yet) been saved. Attachments can only be created if the entity has been saved before!");
                }
                
                if (attachment == null) {
                	attachment = new Anhang();
                }
                attachment.setEntityID(wrapper.getEntityId());
                attachment.setEntityType(wrapper.getEntityType());

                attachment.setDateiGroesse(wrapper.getContentLength());
                attachment.setDateiName(wrapper.getFileName());
                
                if (handler != null) {
                	attachment.setFileID(handler.storeFileInUC(wrapper.getTmpFileName()));
                }
                else {
                	log.error("Can not store file - FileHandler not available!");
                    throw new RuntimeException("Can not store file - FileHandler not available!");
                }
                api.update(attachment);
                // delete temporary file
                try {
                    File file = new File(wrapper.getTmpFileName());
                    
                    if (file != null && file.exists() && file.isFile()) {
                    	boolean deleted = file.delete();
                    	if (!deleted) {
                    		System.out.println("Can not delete file!");
                    	}
                    }
                }catch(Exception e) {
                	log.error("Can not delete file!", e);
                    throw new RuntimeException("Can not delete file!", e);
                }
                wrapper.setTmpFileName(null);
            }
		}
        return null;
	}
}
