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
/**
 *
 */
package de.xwic.appkit.core.file.impl.hbn;

import static de.xwic.appkit.core.remote.server.RemoteDataAccessServlet.ACTION_FILE_HANDLE;
import static de.xwic.appkit.core.remote.server.RemoteDataAccessServlet.PARAM_ACTION;
import static de.xwic.appkit.core.remote.server.RemoteFileAccessHandler.PARAM_FH_ACTION;
import static de.xwic.appkit.core.remote.server.RemoteFileAccessHandler.PARAM_FH_ACTION_DELETE;
import static de.xwic.appkit.core.remote.server.RemoteFileAccessHandler.PARAM_FH_ACTION_LOAD;
import static de.xwic.appkit.core.remote.server.RemoteFileAccessHandler.PARAM_FH_ACTION_UPLOAD;
import static de.xwic.appkit.core.remote.server.RemoteFileAccessHandler.PARAM_FH_ID;
import static de.xwic.appkit.core.remote.server.RemoteFileAccessHandler.PARAM_FH_STREAM;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
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
		MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.STRICT);
		multipartEntity.addPart(PARAM_ACTION, new StringBody(ACTION_FILE_HANDLE));
		multipartEntity.addPart(PARAM_FH_ACTION, new StringBody(PARAM_FH_ACTION_UPLOAD));
		multipartEntity.addPart(PARAM_FH_STREAM, new FileBody(file));
		return URemoteAccessClient.multipartRequestInt(multipartEntity, config);
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.dao.IFileHandler#deleteFile(int)
	 */
	@Override
	public void deleteFile(final int id) {
		Map<String, String> createParams = createParams(id, PARAM_FH_ACTION_DELETE);
		URemoteAccessClient.postRequest(createParams, config);
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.file.impl.hbn.IRemoteFileDAOClient#loadFileInputStream(int)
	 */
	@Override
	public InputStream loadFileInputStream(final int id) {
		Map<String, String> createParams = createParams(id, PARAM_FH_ACTION_LOAD);
		// It's somehow bad to load the attachment to memory instead of streaming it,
		// on the other side, response has to be correctly closed.
		return new ByteArrayInputStream(URemoteAccessClient.getReponseByteArray(createParams, config));
	}

	/**
	 * @param id
	 * @param action
	 * @return
	 */
	private final Map<String, String> createParams(final int id, final String action) {
		Map<String, String> param = new HashMap<String, String>();
		param.put(PARAM_ACTION, ACTION_FILE_HANDLE);
		param.put(PARAM_FH_ID, String.valueOf(id));
		param.put(PARAM_FH_ACTION, action);
		return param;
	}
}
