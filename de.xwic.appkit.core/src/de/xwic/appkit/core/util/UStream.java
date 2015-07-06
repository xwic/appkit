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
