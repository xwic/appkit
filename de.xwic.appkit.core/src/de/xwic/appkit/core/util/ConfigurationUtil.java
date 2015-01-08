/**
 * 
 */
package de.xwic.appkit.core.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import java.util.TimeZone;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.jwic.base.JWicRuntime;
import de.xwic.appkit.core.ApplicationData;
import de.xwic.appkit.core.config.ConfigurationManager;
import de.xwic.appkit.core.config.Setup;
import de.xwic.appkit.core.config.XmlConfigLoader;
import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.security.IRole;
import de.xwic.appkit.core.security.IUser;

/**
 * @author dotto
 *
 */
public class ConfigurationUtil {

	private static final Log log = LogFactory.getLog(ConfigurationUtil.class);

	/**
	 * @param scope
	 * @return
	 */
	public static boolean hasAccess(String scope) {
		return hasRight(scope, ApplicationData.SECURITY_ACTION_ACCESS);
	}

	/**
	 * @param scope
	 * @param action
	 * @return
	 */
	public static boolean hasRight(String scope, String action) {
		return DAOSystem.getSecurityManager().hasRight(scope, action);
	}

	/**
	 * @param roleName
	 * @return
	 */
	public static boolean hasRole(String roleName) {
		IUser user = DAOSystem.getSecurityManager().getCurrentUser();

		for (IEntity r : user.getRoles()) {
			IRole role = (IRole) r;
			if (role.getName().equals(roleName)) {
				return true;
			}
		}

		return false;
	}
	
	/**
	 * @return
	 */
	public static boolean isTestMode() {
		return "true".equals(getConfigurationProperty("testmode", "true"));
	}
	
	/**
	 * @return
	 */
	public static String getSystemEmailAddress() {
		return getConfigurationProperty("system.email.address");
	}

	/**
	 * @return
	 */
	public static List<String> getEmergencyEmailReceiver() {
		return StringUtil.parseEmails(getConfigurationProperty("emergency.email.reciever"));
	}

	/**
	 * @return
	 */
	public static List<String> getTestEmailReceiver() {
		return StringUtil.parseEmails(getConfigurationProperty("test.email.reciever"));
	}
	
	/**
	 * @return Current System Name
	 */
	public static String getSystemName(){
		return getConfigurationProperty("server.name", "unnamed");
	}

	/**
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public static String getConfigurationProperty(String key, String defaultValue) {
		Setup setup = ConfigurationManager.getSetup();
		return setup == null ? defaultValue : setup.getProperty(key, defaultValue);
	}
	
	/**
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public static String getConfigurationProperty(String key) {
		return getConfigurationProperty(key, "");
	}

	/**
	 * @return
	 */
	public static String getRootPath() {
		return JWicRuntime.getJWicRuntime().getRootPath();
	}

	/**
	 * @return
	 */
	public static String getConfigPath() {
		String config = ConfigurationUtil.getConfigurationProperty("config_folder", "config");
		return getRootPath() + File.separator + config;
	}

	/**
	 *
	 */
	public static void reloadConfiguration() {
		// load product configuration
		File path = new File(getRootPath(), "config");
		Setup setup;
		try {
			setup = XmlConfigLoader.loadSetup(path.toURI().toURL());
		} catch (Exception e) {
			log.error("Error loading product configuration", e);
			throw new RuntimeException("Error loading product configuration: " + e, e);
		}

		// merge server specific settings
		InputStream in = null;
		Properties srvProp = new Properties();
		try {
			File serverPropertiesFile = new File(getRootPath(), "WEB-INF/server.properties");
			if (!serverPropertiesFile.exists()) {
				throw new RuntimeException("server.properties not found.");
			}
			in = new FileInputStream(serverPropertiesFile);
			if (in != null) {
				srvProp.load(in);
				for (Object prop : srvProp.keySet()) {
					String key = (String) prop;
					setup.setProperty(key, srvProp.getProperty(key));
				}
			}
		} catch (Exception e) {
			log.error("Error loading Server properties", e);
			throw new RuntimeException("Error loading Server properties", e);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (final IOException e) {
					log.error("Failed to close the input stream!", e);
				}
			}
		}

		ConfigurationManager.setSetup(setup);
		log.info("Configuration loaded: " + setup.getAppTitle() + " version " + setup.getVersion());
	}

	/**
	 * Returns scheduler Timezone
	 * @return
	 */
	public static TimeZone getDefaultTimeone() {
		String tz = getConfigurationProperty("scheduler.timezone", "PST");
		return TimeZone.getTimeZone(tz);
	}
	
}
