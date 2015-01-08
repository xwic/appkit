package de.xwic.appkit.core.mail;

import java.net.URL;
import java.util.Locale;
import java.util.Map;

/**
 * Template engine for generating email content.
 * 
 * @author Ronny Pfretzschner
 * 
 */
public interface ITemplateEngine {

	/**
	 * Generates the content by the given template URL and the given context objects defined by String keys and object values.
	 * 
	 * @param absoluteFileName
	 * @param contextObjects
	 * @return
	 */
	public String generateContentFromTemplate(URL templateUrl, Map<String, Object> contextObjects);

	/**
	 * Generates the content by the given template file name and the given context objects defined by String keys and object values.
	 * 
	 * @param absoluteFileName
	 * @param contextObjects
	 * @return
	 */
	public String generateContentFromTemplateFile(String absoluteFileName, Map<String, Object> contextObjects);

	/**
	 * Generates the content from the given template string.
	 * 
	 * @param template
	 * @param contextObjects
	 * @return
	 */
	public String generateContentFromTemplateString(String template, Map<String, Object> contextObjects);

	/**
	 * @return the locale
	 */
	public Locale getLocale();

	/**
	 * @param locale
	 *            the locale to set
	 */
	public void setLocale(Locale locale);

}
