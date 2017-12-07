/**
 * 
 */
package de.xwic.appkit.webbase.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import de.xwic.appkit.core.config.ConfigurationException;
import de.xwic.appkit.core.config.ConfigurationManager;
import de.xwic.appkit.core.config.Setup;
import de.xwic.appkit.core.config.model.EntityDescriptor;
import de.xwic.appkit.core.registry.ExtensionRegistry;

/**
 * Generates a single HTML page with a summary of the Product configuration.
 * Must be configured in the web.xml of the host platform.
 * 
 * @author lippisch
 */
public class ConfigDumpServlet extends HttpServlet {

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		Setup setup = ConfigurationManager.getSetup();
		EntityDescriptor ed = null;
		
		String template = "ConfigDump.vtl";
		String entity = req.getParameter("entity");
		
		if (entity != null) {
			template = "ConfigDump-Entity.vtl";
			try {
				 ed = setup.getEntityDescriptor(entity);
			} catch (ConfigurationException e) {
				resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Entity not found.");
				return;
			}
		}
		
		InputStream tpl = getClass().getResourceAsStream(template);
		
		if (setup == null) {
			resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "The Setup is not available or not loaded yet.");
		} else if (tpl == null) {
			resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Cannot find ConfigDump.vtl template.");
		} else {
		
			resp.setContentType("text/html");
			PrintWriter writer = resp.getWriter();
			
			VelocityContext ctx = new VelocityContext();
			ctx.put("setup", setup);
			ctx.put("util", new VelocityUtil());
			ctx.put("exRegistry", ExtensionRegistry.getInstance());
			ctx.put("ed", ed);
			
			
			if (ed != null) {
				if (ed.getDomain().hasBundle("en")) {
					try {
						ctx.put("entityTitle", ed.getDomain().getBundle("en").getString(ed.getId()));
					} catch (ConfigurationException e) {
						ctx.put("entityTitle", ed.getId());
					}
				} else {
					ctx.put("entityTitle", ed.getId());
				}
			}
			
			VelocityEngine ve = new VelocityEngine();
			try {
				ve.evaluate(ctx, writer, "ConfigDump", new InputStreamReader(tpl));
			} finally {
				tpl.close();
				writer.flush();
				writer.close();
			}
		}
		
	}
	
}
