/*
 * Copyright 2005 pol GmbH
 *
 * de.xwic.appkit.core.dao.IFileHandler
 * Created on 08.09.2005
 *
 */
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
	public void deleteFile(int id);
	
	/**
	 * Stores the specified file and returns a unique ID.
	 * @param filename
	 * @return
	 */
	public int storeFile(String filename) throws DataAccessException;
	
	
	/**
	 * Loads the file with the specified ID and stores it at the specified location.
	 * The destination location can be either a directory or a filename.
	 * @param id
	 * @param destination
	 * @return a File handle to the loaded file.
	 */
	public File loadFile(int id, String destination) throws DataAccessException;
	
	
	/**
	 * Loads the file with the specified ID.
	 * 
	 * @param id
	 * @return an InputStream
	 */
	public InputStream loadFileInputStream(int id) throws DataAccessException;
	
	public int storeFileInUC(final String fileName) throws DataAccessException;
	
	public void deleteFileInUC(final int id);
}
