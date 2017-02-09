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
package de.xwic.appkit.core.file.impl.hbn;

import java.sql.Blob;

/**
 * POJO used to store and restore a file in the database.
 * @author Florian Lippisch
 */
public class HbnFile {
	
	private long id = 0;
	private String filename = null;
	private String contentType = null;
	private long filesize = 0;
	private Blob data = null;
	
	
	/**
	 * @return Returns the data.
	 */
	public Blob getData() {
		return data;
	}
	/**
	 * @param data The data to set.
	 */
	public void setData(Blob data) {
		this.data = data;
	}
	/**
	 * @return Returns the filename.
	 */
	public String getFilename() {
		return filename;
	}
	/**
	 * @param filename The filename to set.
	 */
	public void setFilename(String filename) {
		this.filename = filename;
	}
	/**
	 * @return Returns the filesize.
	 */
	public long getFilesize() {
		return filesize;
	}
	/**
	 * @param filesize The filesize to set.
	 */
	public void setFilesize(long filesize) {
		this.filesize = filesize;
	}
	/**
	 * @return Returns the id.
	 */
	public long getId() {
		return id;
	}
	/**
	 * @param id The id to set.
	 */
	public void setId(long id) {
		this.id = id;
	}
	/**
	 * Returns the content type.
	 * @return
	 */
	public String getContentType() {
		return contentType;
	}
	/**
	 * Sets the content type.
	 * @param contentType
	 */
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

}
