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
package de.xwic.appkit.core.model.util;

import java.beans.PropertyDescriptor;
import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;
import java.util.StringTokenizer;

import de.xwic.appkit.core.model.entities.IPicklistEntry;

/**
 * @author Florian Lippisch
 */
public class TemplateUtil {

	/**
	 * Merges a simple template with an object. The template
	 * looks like this:
	 * ${property} text ${property2.subproperty}
	 * 
	 * @param template
	 * @param object
	 * @return
	 */
	public static String parse(String template, Object object) {
		
		StringBuffer sb = new StringBuffer();
		int len = template.length();
		boolean initial = false;
		boolean inbr = false;
		int start = 0;
		for (int i = 0; i < len; i++) {
			char c = template.charAt(i);
			if (c == '$' && !initial) {
				initial = true;
			} else if (c == '{' && initial) {
				inbr = true;
				start = i + 1;
			} else if (c == '}' && inbr) {
				inbr = false;
				initial = false;
				String prop = template.substring(start, i);
				sb.append(readProperty(object, prop));
			} else if (!initial) {
				sb.append(c);
			}
		}
		
		if (initial) {
			if (inbr) {
				sb.append("${");
				sb.append(template.substring(start));
			} else {
				sb.append("$");
			}
		}
		
		return sb.toString();
	}

	/**
	 * @param object
	 * @param prop
	 * @return
	 */
	public static String readProperty(Object element, String prop) {

		try {
			StringTokenizer stk = new StringTokenizer(prop, ".");
			Method[] readMethods = new Method[stk.countTokens()];
			
			int idx = 0;
			Class<? extends Object> clazz = element.getClass();
			
			while (stk.hasMoreTokens()) {
				String propertyName = stk.nextToken();
	
				PropertyDescriptor desc = new PropertyDescriptor(propertyName, clazz);
				clazz = desc.getPropertyType();
				readMethods[idx++] = desc.getReadMethod();
			}
			
			Object value = element;
			for (int i = 0; i < readMethods.length && value != null; i++) {
				value = readMethods[i].invoke(value, (Object[]) null);
			}
		
			return toString(value);
		} catch (Exception e) {
			return "!" + prop + "!";
		}
	}
	
	/**
	 * Transform an object into an string.
	 * @param value
	 * @param propertyEditor
	 * @return
	 */
	public static String toString(Object value) {
		String text;
		if (value == null) {
			text = "";
		} else if (value instanceof Date) {
			// special treatment for date objects
			DateFormat df = DateFormat.getDateInstance();
			text = df.format((Date)value);
		} else if (value instanceof Calendar) {
			Calendar c = (Calendar)value;
			DateFormat df = DateFormat.getDateInstance();
			text = df.format(c.getTime());
		} else if (value instanceof Long) {
			NumberFormat nf = NumberFormat.getNumberInstance();
			text = nf.format(((Long)value).longValue());
		} else if (value instanceof Double) {
			NumberFormat nf = NumberFormat.getNumberInstance();
			nf.setMinimumFractionDigits(2);
			text = nf.format(((Double)value).doubleValue());
		} else if (value instanceof IPicklistEntry) {
			text = ((IPicklistEntry)value).getBezeichnung(Locale.getDefault().getLanguage());
		} else if (value instanceof Set) {
			// set of picklist entries
			StringBuffer sb = new StringBuffer();
			for (Iterator<?> it = ((Set<?>)value).iterator(); it.hasNext(); ) {
				Object o = it.next();
				if (sb.length() != 0) {
					sb.append(",");
				}
				sb.append(toString(o));
			}
			text = sb.toString();
		} else if (value instanceof Object[]) {
			StringBuffer sb = new StringBuffer();
			Object[] values = (Object[])value;
			for (int i = 0; i < values.length; i++) {
				if (sb.length() != 0) {
					sb.append(",");
				}
				sb.append(toString(values[i]));
			}
			text = sb.toString();
		} else {
			PropertyEditor propertyEditor = PropertyEditorManager.findEditor(value.getClass());
			if (propertyEditor != null) {
				propertyEditor.setValue(value);
				text = propertyEditor.getAsText();
			} else {
				text = value.toString();
			}
		}
		return text;

	}
	
}
