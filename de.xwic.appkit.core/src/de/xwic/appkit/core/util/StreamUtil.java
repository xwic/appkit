/**
 *
 */
package de.xwic.appkit.core.util;

import java.io.Closeable;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Alexandru Bledea
 * @since Aug 14, 2013
 */
public final class StreamUtil {

    /**
     *
     */
    private StreamUtil() {
    }

    /**
     * @param closable
     * @param log
     */
    public static void close(final Log log, final Closeable... closables) {
        for (final Closeable closable : closables) {
            if (null == closable) {
                continue;
            }
            try {
                closable.close();
            } catch (final IOException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    /**
     * @param closable
     * @param log
     */
    public static void close(final Closeable... closables) {
        close(LogFactory.getLog(StreamUtil.class), closables);
    }

}
