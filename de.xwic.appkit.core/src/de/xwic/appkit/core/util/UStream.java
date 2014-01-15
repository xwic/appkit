/**
 *
 */
package de.xwic.appkit.core.util;

import java.io.Closeable;
import java.io.IOException;

import org.apache.commons.logging.LogFactory;

/**
 * @author Alexandru Bledea
 * @since Jan 15, 2014
 */
public class UStream {

	/**
	 * @param closeables
	 */
	public static void close(final Closeable... closeables) {
		for (Closeable closeable : closeables) {
			if (closeable == null) {
				continue;
			}
			try {
				closeable.close();
			} catch (IOException e) {
				LogFactory.getLog(UStream.class).error("Failed to close stream", e);
			}
		}
	}
}
