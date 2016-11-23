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
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import de.xwic.appkit.core.dao.DAOProviderAPI;
import de.xwic.appkit.core.dao.DataAccessException;
import de.xwic.appkit.core.dao.IFileHandler;
import de.xwic.appkit.core.dao.UseCase;
import de.xwic.appkit.core.dao.impl.hbn.HibernateUtil;

/**
 * DAO implementation for the HbnFile object.
 * 
 * This implementation is hibernate specific.
 * 
 * @author Florian Lippisch
 */
public class HbnFileDAO implements IFileHandler {

	/**
	 * Store a file in the database.
	 * 
	 * @param filename
	 * @param dataHandler
	 * @return
	 */
	public long storeFile(final String filename) throws DataAccessException {
		Integer res = (Integer) new UseCase() {

			@Override
			protected Object execute(DAOProviderAPI api) {
				Session session = HibernateUtil.currentSession();
				long res = storeFile(filename, session);
				return res;
			}
		}.execute();
		if (res != null) {
			return res;
		} else {
			throw new DataAccessException("Could not store file: " + filename);
		}
	}

	/**
	 * @param filename
	 * @param session
	 * @return
	 * @throws DataAccessException
	 */
	protected long storeFile(final String filename, Session session) throws DataAccessException {
		final HbnFile hFile = new HbnFile();
		File file = new File(filename);

		hFile.setFilename(filename);
		hFile.setFilesize(file.length());
		FileInputStream in = null;
		try {
			in = new FileInputStream(file);
			hFile.setData(Hibernate.getLobCreator(session).createBlob(in, file.length()));
			session.save(hFile);
		} catch (Exception e) {
			throw new HibernateException("Could not create Blob!", e);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					throw new DataAccessException(e);
				}
			}
		}
		return hFile.getId();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.core.dao.IFileHandler#deleteFile(int)
	 */
	@Override
	public void deleteFile(final long id) {
		new UseCase() {

			@Override
			protected Object execute(DAOProviderAPI api) {
				Session session = HibernateUtil.currentSession();
				HbnFile hFile = (HbnFile) session.load(HbnFile.class, new Long(id));
				if (hFile != null) {
					session.delete(hFile);
				}
				return true;
			}
		}.execute();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.core.dao.IFileHandler#loadFile(int, java.lang.String)
	 */
	@Override
	public File loadFile(long id, String destination) throws DataAccessException {
		throw new DataAccessException("This method is not supported on the server.");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.pol.crm.dao.IFileHandler#loadFileInputStream(int)
	 */
	public InputStream loadFileInputStream(final long id) throws DataAccessException {
		InputStream stream = null;

		Session session = HibernateUtil.currentSession();
		Transaction tx = HibernateUtil.currentSession().beginTransaction();

		HbnFile hFile = (HbnFile) session.load(HbnFile.class, new Long(id));
		tx.commit();

		if (hFile != null) {
			try {
				stream = hFile.getData().getBinaryStream();
			} catch (SQLException e) {
				throw new DataAccessException("Error while trying to get Stream from Blob!", e);
			}
		}
		return stream;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.core.dao.IFileHandler#storeFileInUC(java.lang.String)
	 */
	@Override
	public long storeFileInUC(final String filename) throws DataAccessException {
		Session session = HibernateUtil.currentSession();
		return storeFile(filename, session);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.core.dao.IFileHandler#deleteFileInUC(int)
	 */
	@Override
	public void deleteFileInUC(final long id) {
		Session session = HibernateUtil.currentSession();
		HbnFile hFile = (HbnFile) session.load(HbnFile.class, new Long(id));
		if (hFile != null) {
			session.delete(hFile);
		}
	}
}
