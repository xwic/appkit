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
package de.xwic.appkit.core.remote.server;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import de.xwic.appkit.core.dao.UseCase;
import de.xwic.appkit.core.transport.xml.TransportException;
import de.xwic.appkit.core.transport.xml.UseCaseSerializer;
import de.xwic.appkit.core.transport.xml.XmlBeanSerializer;

/**
 * @author Alexandru Bledea
 * @since Jan 13, 2014
 */
public class UseCaseHandler implements IRequestHandler {

	public final static String PARAM_USE_CASE = "uc";
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.remote.server.IRequestHandler#getAction()
	 */
	@Override
	public String getAction() {
		return RemoteDataAccessServlet.ACTION_EXECUTE_USE_CASE;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.remote.server.IRequestHandler#handle(de.xwic.appkit.core.remote.server.IParameterProvider, javax.servlet.http.HttpServletResponse, java.io.PrintWriter)
	 */
	@Override
	public void handle(final IParameterProvider pp, final HttpServletResponse resp) throws TransportException {
		
		String strUseCase = pp.getParameter(PARAM_USE_CASE);
		
		if (strUseCase == null || strUseCase.trim().isEmpty()) {
			throw new IllegalArgumentException("The string is empty!");
		}
		
		UseCase uc = UseCaseSerializer.deseralize(strUseCase);
		
		Object result = uc.execute();
		String serialized = XmlBeanSerializer.serializeToXML("result", result);
		PrintWriter pwOut;
		try {
			pwOut = resp.getWriter();
		} catch (IOException e) {
			throw new TransportException(e);
		}
		pwOut.write(serialized);
	}

}
