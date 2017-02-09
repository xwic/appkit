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
package de.xwic.appkit.core.remote.server;

import static de.xwic.appkit.core.remote.server.RemoteDataAccessServlet.RESPONSE_OK;
import static de.xwic.appkit.core.remote.server.RemoteDataAccessServlet.printResponse;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.util.IOUtils;

import de.jwic.upload.UploadFile;
import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.dao.IFileHandler;
import de.xwic.appkit.core.transport.xml.TransportException;
import de.xwic.appkit.core.util.UStream;

/**
 * @author Alexandru Bledea
 * @since Jan 13, 2014
 */
public class RemoteFileAccessHandler implements IRequestHandler {

	private final Log log = LogFactory.getLog(getClass());

	public static final String PARAM_FH_ACTION = "fha";

	public static final String PARAM_FH_ID = "fh_i";

	public static final String PARAM_FH_ACTION_DELETE = "fh_d";
	public static final String PARAM_FH_ACTION_LOAD = "fh_l";

	public static final String PARAM_FH_ACTION_UPLOAD = "fh_u";
	public static final String PARAM_FH_STREAM = "fh_is";

	private final IFileHandler localHandler;

	/**
	 *
	 */
	RemoteFileAccessHandler() {
		localHandler = DAOSystem.getFileHandler();
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.remote.server.IRequestHandler#getAction()
	 */
	@Override
	public String getAction() {
		return RemoteDataAccessServlet.ACTION_FILE_HANDLE;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.remote.server.IRequestHandler#handle(de.xwic.appkit.core.remote.server.IParameterProvider, javax.servlet.http.HttpServletResponse, java.io.PrintWriter)
	 */
	@Override
	public void handle(final IParameterProvider pp, final HttpServletResponse resp) throws TransportException {
		String action = pp.getMandatory(PARAM_FH_ACTION);
		if (action.equals(PARAM_FH_ACTION_DELETE)) {
			delete(pp, resp);
		} else if (action.equals(PARAM_FH_ACTION_UPLOAD)) {
			upload(pp, resp);
		} else if (action.equals(PARAM_FH_ACTION_LOAD)) {
			stream(pp, resp);
		} else {
			throw new UnsupportedOperationException("No such implementation " + action);
		}
	}

	/**
	 * @param pp
	 * @param resp
	 * @return
	 * @throws TransportException
	 */
	void upload(final IParameterProvider pp, final HttpServletResponse resp) throws TransportException {
		Map<String, UploadFile> files = pp.getFiles();
		UploadFile uploadFile = files.get(PARAM_FH_STREAM);
		if (files.size() != 1 || uploadFile == null){
			throw new InvalidRequestException();
		}
		String name = uploadFile.getName();
		InputStream in = null;
		OutputStream out = null;
		File file = null;
		try {
			String newFilePath = getNewFilePath(pp, name);
			file = new File(newFilePath);
			in = uploadFile.getInputStream();
			File parent = file.getParentFile();
			if (!parent.exists()) {
				parent.mkdirs();
			}
			out = new FileOutputStream(file);
			IOUtils.copy(in, out);
			long storeFile = DAOSystem.getFileHandler().storeFile(newFilePath);
			PrintWriter pwOut = resp.getWriter();
			pwOut.write(String.valueOf(storeFile));
			pwOut.close();
		} catch (IOException e) {
			throw new InvalidRequestException(e);
		} finally {
			UStream.close(in, out);
			uploadFile.destroy();
			if (file != null && file.exists()) {
				file.delete();
			}
		}
	}

	/**
	 * @param pp
	 * @return
	 * @throws TransportException
	 */
	private String getNewFilePath(final IParameterProvider pp, final String name) throws TransportException {
		return pp.getUploadDir() + File.separator + pp.getRemoteId() + File.separator + name;
	}

	/**
	 * @param pp
	 * @param resp
	 * @throws TransportException
	 * @throws IOException
	 */
	private void delete(final IParameterProvider pp, final HttpServletResponse resp) throws TransportException {
		int id = pp.getInt(PARAM_FH_ID);
		log.info("Received request to delete " + id);
		localHandler.deleteFile(id);
		try {
			printResponse(resp.getWriter(), RESPONSE_OK);
		} catch (IOException e) {
			throw new TransportException(e);
		}
	}

	/**
	 * @param pp
	 * @param resp
	 * @throws TransportException
	 */
	private void stream(final IParameterProvider pp, final HttpServletResponse resp) throws TransportException {
		int id = pp.getInt(PARAM_FH_ID);
		log.info("Received request to stream " + id);
		InputStream in = null;
		OutputStream out = null;
		try {
			in = localHandler.loadFileInputStream(id);
			out = resp.getOutputStream();
			IOUtils.copy(in, out);
		} catch (IOException io) {
			throw new TransportException("Failed to transfer stream", io);
		} finally {
			UStream.close(in, out);
		}
	}
}
