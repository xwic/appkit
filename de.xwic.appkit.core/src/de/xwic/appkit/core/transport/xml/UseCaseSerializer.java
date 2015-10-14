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
package de.xwic.appkit.core.transport.xml;

import java.io.ByteArrayInputStream;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import de.xwic.appkit.core.dao.UseCase;
import de.xwic.appkit.core.remote.client.ETOSessionCache;

/**
 * @author Adrian Ionescu
 */
public class UseCaseSerializer {

	/**
	 * @param useCase
	 * @return
	 * @throws TransportException
	 */
	public static String serialize(UseCase useCase) throws TransportException {
		return XmlBeanSerializer.serializeToXML("usecase", useCase);
	}
	
	/**
	 * @param strUseCase
	 * @return
	 * @throws TransportException
	 */
	public static UseCase deseralize(String strUseCase) throws TransportException {
		try {
			ByteArrayInputStream in = new ByteArrayInputStream(strUseCase.getBytes());
			SAXReader xmlReader = new SAXReader();
			Document doc = xmlReader.read(in);
			Element root = doc.getRootElement();
			return (UseCase) new XmlBeanSerializer(ETOSessionCache.getInstance().getSessionCache()).deserializeBean(root.element("bean"));
		} catch (DocumentException e) {
			throw new TransportException("Unexpected DocumentException while deseiralizing query.", e);
		}
	}
}
