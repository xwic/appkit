/**
 *
 */
package de.xwic.appkit.core.mail.impl;

import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.net.URL;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.TimeZone;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import de.xwic.appkit.core.mail.ITemplateEngine;
import de.xwic.appkit.core.util.ConfigurationUtil;
import de.xwic.appkit.core.util.VTLHelper;

/**
 * Template Engine with Velocity.
 * 
 * @author Ronny Pfretzschner
 * 
 */
public class VelocityTemplateEngine implements ITemplateEngine {

	private VelocityEngine velocityEngine = null;

	private Log log = LogFactory.getLog(VelocityTemplateEngine.class);
	private Locale locale = Locale.getDefault();
	private TimeZone timeZone = null;

	/**
	 *
	 */
	public VelocityTemplateEngine() {
		velocityEngine = new VelocityEngine();
		try {
			Properties prop = new Properties();
			prop.setProperty("file.resource.loader.path", ConfigurationUtil.getConfigPath());
			prop.setProperty("runtime.log.invalid.references", "false");
			prop.setProperty("runtime.log.error.stacktrace", "false");
			prop.setProperty("runtime.log.warn.stacktrace", "false");
			prop.setProperty("runtime.log.info.stacktrace", "false");
			prop.setProperty("runtime.log.logsystem.class", NoLogSystem.class.getName());
			velocityEngine.init(prop);
		} catch (Exception ex) {
			log.error(ex);
			throw new RuntimeException("VelocityEngine initialization failed: " + ex, ex);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.netapp.pulse.model.util.mail.ITemplateEngine#generateContentFromTemplate(java.net.URL, java.util.Map)
	 */
	@Override
	public String generateContentFromTemplate(URL templateUrl, Map<String, Object> contextObjects) {

		try {
			VelocityContext vlContext = new VelocityContext();

			Iterator<String> i = contextObjects.keySet().iterator();

			vlContext.put("format", new VTLHelper(locale, timeZone));

			while (i.hasNext()) {
				String key = i.next();
				vlContext.put(key, contextObjects.get(key));
			}
			InputStream in = templateUrl.openStream();
			try {
				Reader reader = new InputStreamReader(in);
				StringWriter writer = new StringWriter();
				velocityEngine.evaluate(vlContext, writer, templateUrl.getPath(), reader);

				return writer.toString();
			} finally {
				in.close();
			}
		} catch (Exception ex) {
			log.error(ex);
			return ex.getMessage();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.netapp.pulse.model.util.mail.ITemplateEngine#generateContentFromTemplateFile(java.lang.String, java.util.Map)
	 */
	@Override
	public String generateContentFromTemplateFile(String absoluteFileName, Map<String, Object> contextObjects) {

		try {
			VelocityContext vlContext = new VelocityContext();
			vlContext.put("format", new VTLHelper(locale, timeZone));
			Iterator<String> i = contextObjects.keySet().iterator();

			while (i.hasNext()) {
				String key = i.next();

				vlContext.put(key, contextObjects.get(key));
			}

			FileReader reader = new FileReader(absoluteFileName);
			StringWriter writer = new StringWriter();
			velocityEngine.evaluate(vlContext, writer, absoluteFileName, reader);

			return writer.toString();
		} catch (Exception ex) {
			log.error(ex);
			return ex.getMessage();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.netapp.pulse.model.util.mail.ITemplateEngine#generateContentFromTemplateString(java.lang.String, java.util.Map)
	 */
	@Override
	public String generateContentFromTemplateString(String template, Map<String, Object> contextObjects) {

		try {
			VelocityContext vlContext = new VelocityContext();
			vlContext.put("format", new VTLHelper(locale, timeZone));

			Iterator<String> i = contextObjects.keySet().iterator();

			while (i.hasNext()) {
				String key = i.next();

				vlContext.put(key, contextObjects.get(key));
			}

			StringWriter writer = new StringWriter();
			velocityEngine.evaluate(vlContext, writer, "VTE", template);
			return writer.toString();
		} catch (Exception ex) {
			log.error(ex);
			return ex.getMessage();
		}
	}

	/**
	 * @return the locale
	 */
	@Override
	public Locale getLocale() {
		return locale;
	}

	/**
	 * @param locale
	 *            the locale to set
	 */
	@Override
	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	/**
	 * @param timeZone
	 *            the timeZone to set
	 */
	public void setTimeZone(TimeZone timeZone) {
		this.timeZone = timeZone;
	}

	/**
	 * @return the timeZone
	 */
	public TimeZone getTimeZone() {
		return timeZone;
	}

}
