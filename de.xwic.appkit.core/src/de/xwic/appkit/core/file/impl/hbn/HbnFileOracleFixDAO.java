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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import de.xwic.appkit.core.dao.DataAccessException;
import de.xwic.appkit.core.dao.impl.hbn.HibernateUtil;

public class HbnFileOracleFixDAO extends HbnFileDAO {

	protected int storeFile(final String fileName, Session session) throws DataAccessException {
		final HbnFile hFile = new HbnFile();

		File file = new File(fileName);
		hFile.setFilename(fileName);
		hFile.setFilesize(file.length());
		FileInputStream in = null;
		try {
			in = new FileInputStream(file);
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			byte[] buf = new byte[4000];
			
			for (;;) {
				int dataSize = in.read(buf);
				if (dataSize == -1)
					break;
				out.write(buf, 0, dataSize);
			}
			out.close();
			hFile.setData( Hibernate.getLobCreator(session).createBlob(out.toByteArray()));
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

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.file.impl.hbn.HbnFileDAO#loadFileInputStream(int)
	 */
	@Override
	public InputStream loadFileInputStream(final int id) throws DataAccessException {
		InputStream stream = null;
		
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();

		HbnFile hFile = (HbnFile)session.load(HbnFile.class, new Integer(id));
		tx.commit();

		if (hFile != null) {
			try {
				byte[] result = hFile.getData().getBytes(1, (int) hFile.getData().length()); 
				stream = new ByteArrayInputStream(result);
			} catch (SQLException e) {
				throw new DataAccessException("Error while trying to get Stream from Blob!", e);
			}
		} 
		return stream;
	}
}