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
package de.xwic.appkit.core.file.uc;

import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.model.entities.IAnhang;

public interface IAttachmentWrapper {

	public abstract boolean isDeleted();

	public abstract int getFileId();

	public abstract String getFileName();

	public void setFileName(String fileName);
	
	public void setContentLength(long length);
	
	public abstract long getContentLength();

	public abstract int getEntityId();

	public abstract String getEntityType();

	public abstract void setEntity(IEntity entity);

	public abstract String getKey();

	public abstract void setKey(String key);

	public abstract String getTmpFileName();

	public abstract void setTmpFileName(String tmpFileName);

	public abstract String getContentType();

	public abstract void setContentType(String contentType);

	public void setFileId(int fileId);
	
	public void setDeleted(boolean deleted);

	/**
	 * @return the anhang
	 */
	public IAnhang getAnhang();

	/**
	 * @param anhang the anhang to set
	 */
	public void setAnhang(IAnhang anhang);
}