/*
 * Copyright 2005 pol GmbH
 *
 * de.xwic.appkit.core.model.util.ServerConfig
 * Created on 23.09.2005
 *
 */
package de.xwic.appkit.core.model.util;

import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.model.daos.IServerConfigPropertyDAO;
import de.xwic.appkit.core.model.entities.IServerConfigProperty;

/**
 * Utility class to access the server configuration.
 * @author Florian Lippisch
 */
public class ServerConfig {

	/**
	 * Private constructor.
	 *
	 */
	private ServerConfig() {
		
	}
	
	/**
	 * Returns the string value for the specified key.
	 * @param key
	 * @return
	 */
	public static String getString(String key) {
		return getString(key, null);
	}

	/**
	 * Returns the string value for the specified key.
	 * @param key
	 * @return
	 */
	public static String getString(String key, String defaultValue) {
		IServerConfigPropertyDAO dao = (IServerConfigPropertyDAO)DAOSystem.getDAO(IServerConfigPropertyDAO.class);
		String value =  dao.getConfigString(key);
		if (value == null) {
			return defaultValue;
		}
		return value;
	}

	/**
	 * Returns the string value for the specified key.
	 * @param key
	 * @return
	 */
	public static int getInt(String key) {
		return getInt(key, 0);
	}

	/**
	 * Returns the string value for the specified key.
	 * @param key
	 * @return
	 */
	public static int getInt(String key, int defaultValue) {
		IServerConfigPropertyDAO dao = (IServerConfigPropertyDAO)DAOSystem.getDAO(IServerConfigPropertyDAO.class);
		IServerConfigProperty value = dao.getConfigProperty(key);
		if (value == null) {
			return defaultValue;
		}
		return dao.getConfigInteger(key);
	}
	
	/**
	 * Returns the string value for the specified key.
	 * @param key
	 * @return
	 */
	public static double getDouble(String key) {
		return getDouble(key, 0);
	}

	/**
	 * Returns the string value for the specified key.
	 * @param key
	 * @return
	 */
	public static double getDouble(String key, double defaultValue) {
		IServerConfigPropertyDAO dao = (IServerConfigPropertyDAO)DAOSystem.getDAO(IServerConfigPropertyDAO.class);
		IServerConfigProperty value = dao.getConfigProperty(key);
		if (value == null) {
			return defaultValue;
		}
		return dao.getConfigDouble(key);
	}

	/**
	 * Returns the string value for the specified key.
	 * @param key
	 * @return
	 */
	public static boolean getBoolean(String key) {
		return getBoolean(key, false);
	}

	/**
	 * Returns the string value for the specified key.
	 * @param key
	 * @return
	 */
	public static boolean getBoolean(String key, boolean defaultValue) {
		IServerConfigPropertyDAO dao = (IServerConfigPropertyDAO)DAOSystem.getDAO(IServerConfigPropertyDAO.class);
		IServerConfigProperty value = dao.getConfigProperty(key);
		if (value == null) {
			return defaultValue;
		}
		return dao.getConfigBoolean(key);
	}

	
	/**
	 * Store a value.
	 * @param key
	 * @param value
	 */
	public static void set(String key, String value) {
		IServerConfigPropertyDAO dao = (IServerConfigPropertyDAO)DAOSystem.getDAO(IServerConfigPropertyDAO.class);
		dao.setConfigProperty(key, value);
	}

	/**
	 * Store a value.
	 * @param key
	 * @param value
	 */
	public static void set(String key, int value) {
		IServerConfigPropertyDAO dao = (IServerConfigPropertyDAO)DAOSystem.getDAO(IServerConfigPropertyDAO.class);
		dao.setConfigProperty(key, value);
	}
	
	/**
	 * Store a value.
	 * @param key
	 * @param value
	 */
	public static void set(String key, double value) {
		IServerConfigPropertyDAO dao = (IServerConfigPropertyDAO)DAOSystem.getDAO(IServerConfigPropertyDAO.class);
		dao.setConfigProperty(key, value);
	}

	/**
	 * Store a value.
	 * @param key
	 * @param value
	 */
	public static void set(String key, boolean value) {
		IServerConfigPropertyDAO dao = (IServerConfigPropertyDAO)DAOSystem.getDAO(IServerConfigPropertyDAO.class);
		dao.setConfigProperty(key, value);
	}

	
}
