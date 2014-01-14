/**
 *
 */
package de.xwic.appkit.core.file.impl.hbn;

import java.io.InputStream;

/**
 * @author Alexandru Bledea
 * @since Jan 13, 2014
 */
public interface IRemoteFileAccessClient {

	/**
	 * @param filename
	 * @param length
	 * @param in
	 * @return
	 */
	int storeFile(String filename, long length, InputStream in);

	/**
	 * Delete the file with the specified id.
	 * @param id
	 */
	void delete(int id);

	/**
	 * @param id
	 * @return
	 */
	InputStream loadFileInputStream(int id);

}
