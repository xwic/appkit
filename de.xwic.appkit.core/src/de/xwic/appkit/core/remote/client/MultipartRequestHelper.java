/**
 *
 */
package de.xwic.appkit.core.remote.client;

import static de.xwic.appkit.core.remote.server.RemoteDataAccessServlet.PARAM_RSID;
import static org.apache.http.entity.ContentType.TEXT_PLAIN;

import java.io.DataOutputStream;
import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;

/**
 * @author Alexandru Bledea
 * @since Jan 15, 2014
 */
public class MultipartRequestHelper implements IRequestHelper {

	private final String contentType;
	private final long contentLength;
	private final HttpEntity entity;
	private final String targetUrl;

	/**
	 * @param builder
	 * @param config
	 */
	public MultipartRequestHelper(final MultipartEntityBuilder builder, final RemoteSystemConfiguration config) {
		targetUrl = config.getRemoteBaseUrl() + config.getApiSuffix();
		builder.addPart(PARAM_RSID, new StringBody(config.getRemoteSystemId(), TEXT_PLAIN));
		entity = builder.build();
		contentType = entity.getContentType().getValue();
		contentLength = entity.getContentLength();
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.remote.client.IRequestHelper#getContentType()
	 */
	@Override
	public String getContentType() {
		return contentType;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.remote.client.IRequestHelper#getContentLen()
	 */
	@Override
	public long getContentLen() {
		return contentLength;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.remote.client.IRequestHelper#writeToStream(java.io.DataOutputStream)
	 */
	@Override
	public void writeToStream(final DataOutputStream wr) throws IOException {
		entity.writeTo(wr);
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.remote.client.IRequestHelper#getTargetUrl()
	 */
	@Override
	public String getTargetUrl() {
		return targetUrl;
	}

}
