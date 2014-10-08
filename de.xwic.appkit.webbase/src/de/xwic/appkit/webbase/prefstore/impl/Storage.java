/*
 * Copyright 2005 jWic group (http://www.jwic.de)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * de.jwic.wap.platform.pref.Storage
 * Created on 20.01.2006
 * $Id: Storage.java,v 1.1 2006/01/25 10:11:46 flippisch Exp $
 */
package de.xwic.appkit.webbase.prefstore.impl;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.util.IOUtils;

import de.xwic.appkit.core.util.StreamUtil;
import de.xwic.appkit.webbase.prefstore.IPreferenceStore;

/**
 * @author Florian Lippisch
 * @version $Revision: 1.1 $
 */
public class Storage implements IPreferenceStore {
	private final Log log = LogFactory.getLog(getClass());

	private Properties properties = null;
	private File file = null;
	private boolean isDirty = false;
	private boolean readonly = false;
	
	private PropertyChangeSupport propSupport = new PropertyChangeSupport(this);
	
	public Storage(File file) throws IOException {
		this.file = file;
		properties = new Properties();
		
		if (file.exists()) {
			loadData();
		}
		
	}

    /**
     * Load the data from disk.
     * @throws IOException
     */
    private void loadData() throws IOException {
        @SuppressWarnings ("resource")
        InputStream in = null;
        try {
            in = new FileInputStream(file);
            properties.load(in);
        } finally {
            StreamUtil.close(log, in);
        }
    }
	
	/**
	 * Returns the string value for the given key.
	 * @param key
	 * @return
	 */
	public String getString(String key) {
		return properties.getProperty(key);
	}
	
	/**
	 * Set a value.
	 * @param key
	 * @param value
	 */
	public void setValue(String key, String value) {
		
		propSupport.firePropertyChange(key, properties.getProperty(key), value);
		
		properties.setProperty(key, value);
		isDirty = true;
		
	}

	/**
	 * Returns true if the store needs to be flushed.
	 * @return
	 */
	public boolean isDirty() {
		return isDirty;
	}
	
    /**
     * Write changes to disk.
     * @throws IOException
     */
    @Override
    public void flush() throws IOException {
        @SuppressWarnings ("resource")
        OutputStream out = null;
        try {
            out = new FileOutputStream(file);
            properties.store(out, "jWic WAP PreferenceStore");
        } finally {
            StreamUtil.close(log, out);
        }
    }

	/* (non-Javadoc)
	 * @see de.jwic.wap.platform.IPreferenceStore#isReadonly()
	 */
	public boolean isReadonly() {
		return readonly;
	}

	/* (non-Javadoc)
	 * @see de.jwic.wap.platform.IPreferenceStore#addPropertyChangeListener(java.beans.PropertyChangeListener)
	 */
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		propSupport.addPropertyChangeListener(listener);
	}

	/* (non-Javadoc)
	 * @see de.jwic.wap.platform.IPreferenceStore#removePropertyChangeListener(java.beans.PropertyChangeListener)
	 */
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		propSupport.removePropertyChangeListener(listener);		
	}

	/* (non-Javadoc)
	 * @see de.jwic.wap.platform.IPreferenceStore#getString(java.lang.String, java.lang.String)
	 */
	public String getString(String key, String defaultValue) {
		String value = getString(key);
		return value == null ? defaultValue : value;
	}

	/* (non-Javadoc)
	 * @see de.jwic.wap.platform.IPreferenceStore#setReadonly(boolean)
	 */
	public void setReadonly(boolean readonly) {
		this.readonly = readonly;
	}
	
}
