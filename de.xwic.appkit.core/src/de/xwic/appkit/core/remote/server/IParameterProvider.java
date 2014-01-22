/**
 *
 */
package de.xwic.appkit.core.remote.server;

import java.util.Map;

import de.jwic.upload.UploadFile;
import de.xwic.appkit.core.transport.xml.TransportException;

/**
 * @author Alexandru Bledea
 * @since Jan 15, 2014
 */
public interface IParameterProvider {

	/**
	 * @param name
	 * @return
	 */
	String getParameter(String name) throws TransportException;

	/**
	 * @param name
	 * @return
	 */
	String getMandatory(String name) throws TransportException;

	/**
	 * @param name
	 * @return
	 */
	int getInt(String name) throws TransportException;

	/**
	 * @return
	 */
	boolean isMultipart();

	/**
	 * @return
	 * @throws TransportException
	 */
	Map<String, UploadFile> getFiles() throws TransportException;

	/**
	 * @return
	 */
	String getUploadDir();

	/**
	 * @return
	 * @throws TransportException
	 */
	String getRemoteId() throws TransportException;
}
