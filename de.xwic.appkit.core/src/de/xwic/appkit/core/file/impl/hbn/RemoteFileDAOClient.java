/**
 *
 */
package de.xwic.appkit.core.file.impl.hbn;

import static de.xwic.appkit.core.remote.server.RemoteDataAccessServlet.ACTION_FILE_HANDLE;
import static de.xwic.appkit.core.remote.server.RemoteDataAccessServlet.PARAM_ACTION;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Alexandru Bledea
 * @since Jan 13, 2014
 */
public class RemoteFileDAOClient implements IRemoteFileDAOClient {

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.file.impl.hbn.IRemoteFileDAOClient#storeFile(java.lang.String, long, java.io.InputStream)
	 */
	@Override
	public int storeFile(final String filename, final long length, final InputStream in) {
		return 0;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.file.impl.hbn.IRemoteFileDAOClient#delete(int)
	 */
	@Override
	public void delete(final int id) {

	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.file.impl.hbn.IRemoteFileDAOClient#loadFileInputStream(int)
	 */
	@Override
	public InputStream loadFileInputStream(final int id) {
		return null;
	}

	/**
	 * @return
	 */
	private final Map<String, String> createParams() {
		Map<String, String> param = new HashMap<String, String>();
		param.put(PARAM_ACTION, ACTION_FILE_HANDLE);
		return param;
	}

}
