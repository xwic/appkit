package de.xwic.appkit.core.file.impl.hbn;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import de.xwic.appkit.core.dao.DataAccessException;
import de.xwic.appkit.core.dao.IFileHandler;

/**
 * DAO implementation for remote file handling.
 *
 * @author Alexandru Bledea
 * @since Jan 13, 2014
 */
public class RemoteFileDAO implements IFileHandler {

	private final IRemoteFileDAOClient client;

	/**
	 * @param client
	 */
	public RemoteFileDAO(final IRemoteFileDAOClient client) {
		this.client = client;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.IFileHandler#storeFile(java.lang.String)
	 */
	@Override
	public int storeFile(final String filename) throws DataAccessException {
		File file = new File(filename);
		long length = file.length();
		InputStream in = null;
		try {
			in = new FileInputStream(file);
			return client.storeFile(filename, length, in);
		} catch (Exception e) {
			throw new DataAccessException("Could not create Blob!", e);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					throw new DataAccessException(e);
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.IFileHandler#deleteFile(int)
	 */
	@Override
	public void deleteFile(final int id) {
		client.delete(id);
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.IFileHandler#loadFile(int, java.lang.String)
	 */
	@Override
	public File loadFile(final int id, final String destination) throws DataAccessException {
		throw new DataAccessException("This method is not supported on the server.");
	}

	/* (non-Javadoc)
	 * @see de.pol.crm.dao.IFileHandler#loadFileInputStream(int)
	 */
	@Override
	public InputStream loadFileInputStream(final int id) throws DataAccessException {
		return client.loadFileInputStream(id);
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
