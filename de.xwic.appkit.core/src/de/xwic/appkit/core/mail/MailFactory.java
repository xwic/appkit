/**
 * 
 */
package de.xwic.appkit.core.mail;

import java.util.Properties;
import java.util.TimeZone;

import de.xwic.appkit.core.mail.impl.JavaMailManager;
import de.xwic.appkit.core.mail.impl.VelocityTemplateEngine;

/**
 * Factory for email access.
 * 
 * @author Ronny Pfretzschner
 * 
 */
public class MailFactory {

	private static Properties mailProperties = null;

	/**
	 * @return
	 */
	public static IMailManager getJavaMailManager() {
		if (mailProperties == null) {
			throw new IllegalStateException("Mailproperties are not set! Call initialize first.");
		}
		return new JavaMailManager(mailProperties);
	}

	/**
	 * 
	 * @return a new instance of the template engine.
	 */
	public static ITemplateEngine getTemplateEngine() {
		return new VelocityTemplateEngine();
	}

	/**
	 * Initializes the factory.
	 * 
	 * @param mailProperties
	 */
	public static void initialize(Properties mailProperties) {
		MailFactory.mailProperties = mailProperties;
	}

	/**
	 * @param tz
	 * @return
	 */
	public static ITemplateEngine getTemplateEngine(TimeZone tz) {
		VelocityTemplateEngine vte = new VelocityTemplateEngine();
		vte.setTimeZone(tz);
		return vte;
	}
}
