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
import java.lang.reflect.Method;

import javax.servlet.http.HttpServletResponse;

import de.xwic.appkit.core.remote.client.IRemoteFunctionCallConditions;
import de.xwic.appkit.core.transport.xml.RemoteFunctionCallSerializer;
import de.xwic.appkit.core.transport.xml.TransportException;
import de.xwic.appkit.core.transport.xml.XmlBeanSerializer;

/**
 * @author dotto
 *
 */
public class RemoteFunctionCallHandler implements IRequestHandler {
	
	public final static String PARAM_FUNCTION_CALL = "pfc";
	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.remote.server.IRequestHandler#getAction()
	 */
	@Override
	public String getAction() {
		
		return RemoteDataAccessServlet.ACTION_FUNCTION_CALL_HANDLE;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.remote.server.IRequestHandler#handle(de.xwic.appkit.core.remote.server.IParameterProvider, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public void handle(IParameterProvider pp, HttpServletResponse resp) throws TransportException {
		String strFunctionCall = pp.getParameter(PARAM_FUNCTION_CALL);
		
		if (strFunctionCall == null || strFunctionCall.trim().isEmpty()) {
			throw new IllegalArgumentException("The string is empty!");
		}
		
		IRemoteFunctionCallConditions conditions = RemoteFunctionCallSerializer.deseralize(strFunctionCall);
		
		Class<?> c;
		Object result = null;
		try {
			c = Class.forName(conditions.className());
			Method m = c.getMethod(conditions.functionName(), conditions.getClass());
			result = m.invoke(null, conditions);
			
		} catch (Exception e1) {
			throw new IllegalArgumentException("Error while trying to execute the function!", e1);
		} 
		String serialized = XmlBeanSerializer.serializeToXML("result", result, true);
		PrintWriter pwOut;
		try {
			pwOut = resp.getWriter();
		} catch (IOException e) {
			throw new TransportException(e);
		}
		pwOut.write(serialized);
	}

}
