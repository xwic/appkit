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
