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

import java.io.File;
import java.io.IOException;

import de.xwic.appkit.core.dao.DataAccessException;
import de.xwic.appkit.core.dao.IFileHandler;

/**
 * DAO implementation for remote file handling.
 *
 * @author Alexandru Bledea
 * @since Jan 13, 2014
 */
public abstract class RemoteFileDAO implements IFileHandler {

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.IFileHandler#storeFile(java.lang.String)
	 */
	@Override
	public final int storeFile(final String filename) throws DataAccessException {
		try {
			File file = new File(filename);
			if (!file.exists()) {
				throw new DataAccessException("No such file " + file);
			}
			return storeFile(file);
		} catch (DataAccessException dae) {
			throw dae;
		} catch (Exception e) {
			throw new DataAccessException("Failed to store file", e);
		}
	}

	/**
	 * @param file
	 * @return
	 * @throws IOException
	 */
	protected abstract int storeFile(File file) throws IOException;

	/**
	 * @param filename
	 * @param length
	 * @param file
	 * @return
	 * @throws IOException
	 */

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.IFileHandler#loadFile(int, java.lang.String)
	 */
	@Override
	public File loadFile(final int id, final String destination) throws DataAccessException {
		throw new DataAccessException("This method is not supported on the server.");
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.IFileHandler#storeFileInUC(java.lang.String)
	 */
	@Override
	public int storeFileInUC(final String fileName) throws DataAccessException {
		return storeFile(fileName);
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.IFileHandler#deleteFileInUC(int)
	 */
	@Override
	public void deleteFileInUC(final int id) {
		deleteFile(id);
	}
}
