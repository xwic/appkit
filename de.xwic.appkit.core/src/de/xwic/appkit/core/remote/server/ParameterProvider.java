/**
 *
 */
package de.xwic.appkit.core.remote.server;

import static java.util.Collections.EMPTY_MAP;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.jwic.upload.Upload;
import de.jwic.upload.UploadFile;
import de.xwic.appkit.core.transport.xml.TransportException;

/**
 * @author Alexandru Bledea
 * @since Jan 15, 2014
 */
public final class ParameterProvider implements IParameterProvider {

	private static final String UPLOAD_DIR = "remote_upload";

	private final Log log = LogFactory.getLog(getClass());

	private final HttpServletRequest request;

	private final Map<String, List<String>> multiPartParams;
	private final Map<String, UploadFile> files;

	private final boolean multipart;


	/**
	 * @param request
	 * @throws IOException
	 */
	public ParameterProvider(final HttpServletRequest request) throws IOException {
		this.request = request;

		multipart = isMultipart(request);

		if (multipart) {
			int maxPostSize = Integer.MAX_VALUE; // 2gb
//			int maxPostSize = 1024 * 5120; // 5mb
			int memoryUsage = 1024 * 1024;

			Upload upload = new Upload(request, getUploadDir(), maxPostSize, memoryUsage);
			multiPartParams = unmodifiable(upload.getParams());
			files = unmodifiable(upload.getFiles());
		} else {
			multiPartParams = Collections.emptyMap();
			files = Collections.emptyMap();
		}
	}

	/**
	 * @param request
	 * @return
	 */
	private static final boolean isMultipart(final HttpServletRequest request) {
		if (!"POST".equalsIgnoreCase(request.getMethod())) {
			return false;
		}
		String contentType = request.getContentType();
		if (contentType == null) {
			return false;
		}
		return contentType.toLowerCase().startsWith("multipart/");
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.remote.server.IParameterProvider#getParameter(java.lang.String)
	 */
	@Override
	public String getParameter(final String name) {
		if (multipart) {
			List<String> params = multiPartParams.get(name);
			if(params == null) {
				return null;
			}
			return params.get(0);
		}
		return request.getParameter(name);
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.remote.server.IParameterProvider#getMandatory(java.lang.String)
	 */
	@Override
	public String getMandatory(final String name) throws TransportException {
		String parameter = getParameter(name);
		if (parameter == null) {
			throw new InvalidRequestException();
		}
		return parameter;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.remote.server.IParameterProvider#getInt(java.lang.String)
	 */
	@Override
	public int getInt(final String name) throws TransportException {
		String stringInt = getMandatory(name);
		try {
			return Integer.parseInt(stringInt);
		} catch (NumberFormatException nfe) {
			log.error(String.format("Invalid request!", nfe));
			throw new InvalidRequestException(nfe);
		}
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.remote.server.IParameterProvider#isMultipart()
	 */
	@Override
	public boolean isMultipart() {
		return multipart;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.remote.server.IParameterProvider#getFiles()
	 */
	@Override
	public Map<String, UploadFile> getFiles() {
		return files;
	}

	/**
	 * @param map
	 * @return
	 */
	private static <K, V> Map<K, V> unmodifiable(final Map<K, V> map) {
		if (map == null) {
			return EMPTY_MAP;
		}
		return Collections.unmodifiableMap(map);
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.remote.server.IParameterProvider#getUploadDir()
	 */
	@Override
	public String getUploadDir() {
		return UPLOAD_DIR;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.remote.server.IParameterProvider#getRemoteId()
	 */
	@Override
	public String getRemoteId() throws TransportException {
		return getMandatory(RemoteDataAccessServlet.PARAM_RSID);
	}
}
