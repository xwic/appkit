package de.xwic.appkit.core.remote.client;

import java.io.DataOutputStream;
import java.io.IOException;


/**
 * @author Alexandru Bledea
 * @since Jan 15, 2014
 */
interface IRequestHelper {

	/**
	 * @return
	 */
	String getContentType();

	/**
	 * @return
	 */
	long getContentLen();

	/**
	 * @param wr
	 * @throws IOException
	 */
	void writeToStream(DataOutputStream wr) throws IOException;

	/**
	 * @return
	 */
	String getTargetUrl();

}