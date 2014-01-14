/**
 *
 */
package de.xwic.appkit.core.file.impl.hbn;

import static de.xwic.appkit.core.remote.server.RemoteDataAccessServlet.ACTION_FILE_HANDLE;
import static de.xwic.appkit.core.remote.server.RemoteDataAccessServlet.PARAM_ACTION;
import static de.xwic.appkit.core.remote.server.RemoteFileAccessHandler.PARAM_FH_ACTION;
import static de.xwic.appkit.core.remote.server.RemoteFileAccessHandler.PARAM_FH_DELETE;
import static de.xwic.appkit.core.remote.server.RemoteFileAccessHandler.PARAM_FH_ID;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import de.xwic.appkit.core.remote.client.RemoteSystemConfiguration;
import de.xwic.appkit.core.remote.client.URemoteAccessClient;

/**
 * @author Alexandru Bledea
 * @since Jan 13, 2014
 */
public class RemoteFileAccessClient extends RemoteFileDAO {

	private final RemoteSystemConfiguration config;

	/**
	 * @param config
	 */
	public RemoteFileAccessClient(final RemoteSystemConfiguration config) {
		this.config = config;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.file.impl.hbn.RemoteFileDAO#storeFile(java.lang.String, long, java.io.InputStream)
	 */
	@Override
	protected int storeFile(final String filename, final long length, final InputStream in) {
		throw new UnsupportedOperationException("storeFile is not implemented yet!");
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.IFileHandler#deleteFile(int)
	 */
	@Override
	public void deleteFile(final int id) {
		Map<String, String> createParams = createParams();
		createParams.put(PARAM_FH_ACTION, PARAM_FH_DELETE);
		createParams.put(PARAM_FH_ID, String.valueOf(id));
		URemoteAccessClient.postRequest(createParams, config);
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.file.impl.hbn.IRemoteFileDAOClient#loadFileInputStream(int)
	 */
	@Override
	public InputStream loadFileInputStream(final int id) {
		throw new UnsupportedOperationException("loadFileInputStream is not implemented yet!");
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
