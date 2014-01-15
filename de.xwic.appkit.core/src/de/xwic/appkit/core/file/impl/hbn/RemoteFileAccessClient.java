/**
 *
 */
package de.xwic.appkit.core.file.impl.hbn;

import static de.xwic.appkit.core.remote.server.RemoteDataAccessServlet.ACTION_FILE_HANDLE;
import static de.xwic.appkit.core.remote.server.RemoteDataAccessServlet.PARAM_ACTION;
import static de.xwic.appkit.core.remote.server.RemoteFileAccessHandler.PARAM_FH_ACTION;
import static de.xwic.appkit.core.remote.server.RemoteFileAccessHandler.PARAM_FH_ACTION_DELETE;
import static de.xwic.appkit.core.remote.server.RemoteFileAccessHandler.PARAM_FH_ACTION_UPLOAD;
import static de.xwic.appkit.core.remote.server.RemoteFileAccessHandler.PARAM_FH_ID;
import static de.xwic.appkit.core.remote.server.RemoteFileAccessHandler.PARAM_FH_STREAM;
import static org.apache.http.entity.ContentType.TEXT_PLAIN;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;

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
	protected int storeFile(final File file) throws IOException {
		MultipartEntityBuilder multipartEntity = MultipartEntityBuilder.create();
		multipartEntity.addPart(PARAM_ACTION, new StringBody(ACTION_FILE_HANDLE, TEXT_PLAIN));
		multipartEntity.addPart(PARAM_FH_ACTION, new StringBody(PARAM_FH_ACTION_UPLOAD, TEXT_PLAIN));
		multipartEntity.addPart(PARAM_FH_STREAM, new FileBody(file));
		return URemoteAccessClient.multipartRequestInt(multipartEntity, config);
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.IFileHandler#deleteFile(int)
	 */
	@Override
	public void deleteFile(final int id) {
		Map<String, String> createParams = createParams();
		createParams.put(PARAM_FH_ACTION, PARAM_FH_ACTION_DELETE);
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
