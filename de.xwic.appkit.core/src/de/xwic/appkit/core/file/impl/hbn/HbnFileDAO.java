/*
 * Copyright 2005 pol GmbH
 *
 * de.pol.crm.fileservice.hbn.HbnFileDAO
 * Created on 08.09.2005
 *
 */
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

import de.xwic.appkit.core.dao.DataAccessException;
import de.xwic.appkit.core.dao.IFileHandler;
import de.xwic.appkit.core.dao.impl.hbn.HibernateUtil;

/**
 * DAO implementation for the HbnFile object.
 * 
 * This implementation is hibernate specific.
 * @author Florian Lippisch
 */
public class HbnFileDAO implements IFileHandler {

	/**
	 * Store a file in the database.
	 * @param filename
	 * @param dataHandler
	 * @return
	 */
	public int storeFile(final String filename) throws DataAccessException {
		final HbnFile hFile = new HbnFile();
		
		Session session = HibernateUtil.currentSession();
		Transaction tx = HibernateUtil.currentSession().beginTransaction();
		
		File file = new File(filename);
		
		hFile.setFilename(filename);
		hFile.setFilesize(file.length());
		FileInputStream in = null;
		try {
			in = new FileInputStream(file);
			hFile.setData( Hibernate.createBlob(in ));
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
			tx.commit();
		}
		return hFile.getId();
	}
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.IFileHandler#deleteFile(int)
	 */
	@Override
	public void deleteFile(final int id) {
		Session session = HibernateUtil.currentSession();
		Transaction tx = HibernateUtil.currentSession().beginTransaction();

		HbnFile hFile = (HbnFile)session.load(HbnFile.class, new Integer(id));
		if (hFile != null) {
			session.delete(hFile);
		}
		tx.commit();
	}
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.IFileHandler#loadFile(int, java.lang.String)
	 */
	@Override
	public File loadFile(int id, String destination) throws DataAccessException {
		throw new DataAccessException("This method is not supported on the server.");
	}

	/* (non-Javadoc)
	 * @see de.pol.crm.dao.IFileHandler#loadFileInputStream(int)
	 */
	public InputStream loadFileInputStream(final int id) throws DataAccessException {
		InputStream stream = null;
		
		Session session = HibernateUtil.currentSession();
		Transaction tx = HibernateUtil.currentSession().beginTransaction();

		HbnFile hFile = (HbnFile)session.load(HbnFile.class, new Integer(id));
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

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.IFileHandler#storeFileInUC(java.lang.String)
	 */
	@Override
	public int storeFileInUC(final String fileName) throws DataAccessException {
		final HbnFile hFile = new HbnFile();

		Session session = HibernateUtil.currentSession();

		File file = new File(fileName);

		hFile.setFilename(fileName);
		hFile.setFilesize(file.length());
		FileInputStream in = null;
		try {
			in = new FileInputStream(file);
			hFile.setData( Hibernate.createBlob( in));
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
	 * @see de.xwic.appkit.core.dao.IFileHandler#deleteFileInUC(int)
	 */
	@Override
	public void deleteFileInUC(final int id) {
		Session session = HibernateUtil.currentSession();
		HbnFile hFile = (HbnFile)session.load(HbnFile.class, new Integer(id));
		if (hFile != null) {
			session.delete(hFile);
		}
	}
}
