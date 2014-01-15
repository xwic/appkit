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
public class RemoteFileAccessHandler {

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

	/**
	 * @param pp
	 * @param resp
	 * @param pwOut
	 * @return
	 * @throws TransportException
	 */
	void upload(final IParameterProvider pp, final HttpServletResponse resp, final PrintWriter pwOut) throws TransportException {
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
			int storeFile = DAOSystem.getFileHandler().storeFile(newFilePath);
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
	 * @param pwOut
	 * @throws TransportException
	 */
	private void delete(final IParameterProvider pp, final HttpServletResponse resp, final PrintWriter pwOut) throws TransportException {
		int id = pp.getInt(PARAM_FH_ID);
		log.info("Received request to delete " + id);
		localHandler.deleteFile(id);
		printResponse(pwOut, RESPONSE_OK);
	}

	/**
	 * @param pp
	 * @param resp
	 * @param pwOut
	 * @throws TransportException
	 */
	public void handle(final IParameterProvider pp, final HttpServletResponse resp, final PrintWriter pwOut) throws TransportException {
		String action = pp.getMandatory(PARAM_FH_ACTION);
		if (action.equals(PARAM_FH_ACTION_DELETE)) {
			delete(pp, resp, pwOut);
		} else if (action.equals(PARAM_FH_ACTION_UPLOAD)) {
			upload(pp, resp, pwOut);
		} else {
			throw new UnsupportedOperationException("No such implementation " + action);
		}
	}

}
