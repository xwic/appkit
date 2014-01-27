/**
 *
 */
package de.xwic.appkit.core.remote.client;

import static de.xwic.appkit.core.remote.server.RemoteDataAccessServlet.PARAM_RSID;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Map.Entry;

import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.remote.server.RemoteDataAccessServlet;
import de.xwic.appkit.core.security.IUser;

/**
 * @author Alexandru Bledea
 * @since Jan 15, 2014
 */
class SimpleRequestHelper implements IRequestHelper {

	private final int contentLen;
	private final String urlParameters;
	private final String targetUrl;

	/**
	 * @param param
	 * @param config
	 * @throws RemoteDataAccessException
	 */
	public SimpleRequestHelper(final Map<String, String> param, final RemoteSystemConfiguration config) throws RemoteDataAccessException {
		try {
			targetUrl = config.getRemoteBaseUrl() + config.getApiSuffix();
			param.put(PARAM_RSID, config.getRemoteSystemId());
			
			IUser currentUser = DAOSystem.getSecurityManager().getCurrentUser();
			if (currentUser != null) {
				param.put(RemoteDataAccessServlet.PARAM_USERNAME, currentUser.getLogonName());
			}

			StringBuilder sbParam = new StringBuilder();
			for (Entry<String, String> entry : param.entrySet()) {
				if (sbParam.length() != 0) {
					sbParam.append("&");
				}
				sbParam.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue(), "UTF-8"));
			}
			urlParameters = sbParam.toString();
			contentLen = urlParameters.getBytes().length;
		} catch (Exception e) {
			throw new RemoteDataAccessException(e);
		}
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.remote.client.IRequestHelper#getContentType()
	 */
	@Override
	public String getContentType() {
		return "application/x-www-form-urlencoded";
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.remote.client.IRequestHelper#writeToStream(java.io.OutputStream)
	 */
	@Override
	public void writeToStream(final DataOutputStream wr) throws IOException {
		wr.writeBytes(urlParameters);
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.remote.client.IRequestHelper#getContentLen()
	 */
	@Override
	public long getContentLen() {
		return contentLen;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.remote.client.IRequestHelper#getTargetUrl()
	 */
	@Override
	public String getTargetUrl() {
		return targetUrl;
	}

}
