/**
 * 
 */
package de.xwic.appkit.core.remote.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.util.zip.GZIPInputStream;

/**
 * @author dotto
 *
 */
public class GZipDecompressionHelper {

	/**
     * Gzip magic number, fixed values in the beginning to identify the gzip
     * format <br>
     * http://www.gzip.org/zlib/rfc-gzip.html#file-format
     */
    private static final byte GZIP_ID1 = 0x1f;
    /**
     * Gzip magic number, fixed values in the beginning to identify the gzip
     * format <br>
     * http://www.gzip.org/zlib/rfc-gzip.html#file-format
     */
    private static final byte GZIP_ID2 = (byte) 0x8b;

    /**
     * Return decompression input stream if needed.
     * 
     * @param input
     *            original stream
     * @return decompression stream
     * @throws IOException
     *             exception while reading the input
     */
    public static InputStream decompressStream(InputStream input) throws IOException
    {
        PushbackInputStream pushbackInput = new PushbackInputStream(input, 2);

        byte[] signature = new byte[2];
        pushbackInput.read(signature);
        pushbackInput.unread(signature);

        if (signature[0] == GZIP_ID1 && signature[1] == GZIP_ID2)
        {
            return new GZIPInputStream(pushbackInput);
        }
        return pushbackInput;
    }
}
