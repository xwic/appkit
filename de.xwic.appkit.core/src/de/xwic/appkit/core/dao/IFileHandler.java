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
package de.xwic.appkit.core.dao;

import java.io.File;
import java.io.InputStream;

/**
 * @author Florian Lippisch
 */
public interface IFileHandler {

	/**
	 * Delete the file with the specified id.
	 * @param id
	 */
	public void deleteFile(long id);
	
	/**
	 * Stores the specified file and returns a unique ID.
	 * @param filename
	 * @return
	 */
	public long storeFile(String filename) throws DataAccessException;
	
	
	/**
	 * Loads the file with the specified ID and stores it at the specified location.
	 * The destination location can be either a directory or a filename.
	 * @param id
	 * @param destination
	 * @return a File handle to the loaded file.
	 */
	public File loadFile(long id, String destination) throws DataAccessException;
	
	
	/**
	 * Loads the file with the specified ID.
	 * 
	 * @param id
	 * @return an InputStream
	 */
	public InputStream loadFileInputStream(long id) throws DataAccessException;
	
	public long storeFileInUC(final String fileName) throws DataAccessException;
	
	public void deleteFileInUC(final long id);
}
