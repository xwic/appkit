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
package de.xwic.appkit.core.remote.client;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Alexandru Bledea
 * @since Aug 28, 2014
 */
final class IoUtil {
	private static final Log log = LogFactory.getLog(IoUtil.class);

	private static final int EOF = -1;

	/**
	 *
	 */
	private IoUtil() {
	}

	/**
	 * @param closeMe
	 */
	public static void close(final Closeable closeMe) {
		if (null == closeMe) {
			return;
		}
		try {
			closeMe.close();
		} catch (final IOException ex) {
			log.error("Failed to close resource", ex);
		}
	}

	/**
	 * @param in
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings ("resource")
	public static byte[] readBytes(final InputStream in) throws IOException {
		final ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			copy(in, bos);
			return bos.toByteArray();
		} finally {
			close(bos);
		}
	}

	/**
	 * @param input
	 * @param output
	 * @param buffer
	 * @return
	 * @throws IOException
	 */
	private static void copy(final InputStream in, final OutputStream out) throws IOException {
		int n;
		final byte[] buffer = new byte[4096];
		while (EOF != (n = in.read(buffer))) {
			out.write(buffer, 0, n);
		}
	}

}
