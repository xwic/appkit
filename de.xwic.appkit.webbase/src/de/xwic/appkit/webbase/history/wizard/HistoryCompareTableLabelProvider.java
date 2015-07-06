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
package de.xwic.appkit.webbase.history.wizard;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;

import de.jwic.base.ImageRef;
import de.jwic.controls.tableviewer.CellLabel;
import de.jwic.controls.tableviewer.ITableLabelProvider;
import de.jwic.controls.tableviewer.RowContext;
import de.jwic.controls.tableviewer.TableColumn;
import de.xwic.appkit.core.config.ConfigurationException;
import de.xwic.appkit.core.config.ConfigurationManager;
import de.xwic.appkit.core.config.model.EntityDescriptor;
import de.xwic.appkit.core.dao.DAO;
import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.model.entities.IPicklistEntry;
import de.xwic.appkit.webbase.history.HistorySelectionModel;
import de.xwic.appkit.webbase.history.HistoryVersion;
import de.xwic.appkit.webbase.history.PropertyVersionHelper;
import de.xwic.appkit.webbase.toolkit.util.ImageLibrary;

/**
 * 
 * 
 * @author Aron Cotrau
 */
public class HistoryCompareTableLabelProvider implements ITableLabelProvider {

	private final static String PATTERN = "dd-MMM-yyyy HH:mm:ss";
	private final SimpleDateFormat format = new SimpleDateFormat(PATTERN);

	// private final static Image warning =
	// UIToolsPlugin.getImageDescriptor("icons/warning.jpg").createImage();

	private final static Set<String> entityProperties = new HashSet<String>();

	private HistorySelectionModel model = null;

	static {
		entityProperties.add("lastModifiedAt");
		entityProperties.add("lastModifiedFrom");
		entityProperties.add("createdAt");
		entityProperties.add("createdFrom");
		entityProperties.add("version");
		entityProperties.add("deleted");
		entityProperties.add("changed");
	}

	/**
	 * @param model
	 */
	public HistoryCompareTableLabelProvider(HistorySelectionModel model, TimeZone timeZone) {
		this.model = model;
		format.setTimeZone(timeZone);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.jwic.ecolib.tableviewer.ITableLabelProvider#getCellLabel(java.lang.Object,
	 *      de.jwic.ecolib.tableviewer.TableColumn,
	 *      de.jwic.ecolib.tableviewer.RowContext)
	 */
	public CellLabel getCellLabel(Object row, TableColumn column, RowContext rowContext) {

		PropertyVersionHelper helper = (PropertyVersionHelper) row;
		String langID = Locale.getDefault().getLanguage();
		// get column relevant version from helper
		int columnIndex = column.getIndex();
		HistoryVersion historyVersion = helper.getHistoryVersion(columnIndex);

		if (null != historyVersion) {
			IEntity hisEntity = historyVersion.getHistoryEntity();
			// get getter
			Method propGetter = helper.getPropertyMethod();

			// default string, if no value was set... or new value
			// is null
			String valString = model.getResourceString("historycompare.app.label.defaultvalue");
			ImageRef image = null;
			String toolTip = null;
			
			if (historyVersion.isChanged()) {
				image = ImageLibrary.ICON_WARNING;
				toolTip = "This value has been changed since previous version!";
			}
			
			
			
			
			switch (columnIndex) {
			case 0: {
				// in first column the name of property is shown
				String value = generateLabel(hisEntity.type().getName(), helper.getPropertyName());
				return new CellLabel(value);
			}
			default: {

				if (hisEntity != null) {
					try {
						Object val = propGetter.invoke(hisEntity, new Object[0]);

						// show correct picklistentry text
						if (val instanceof IPicklistEntry) {
							IPicklistEntry entry = (IPicklistEntry) val;
							valString = entry.getBezeichnung(langID);
						} else if (val instanceof Set) {
							// set of picklist entries
							StringBuffer sb = new StringBuffer();
							for (Iterator<?> it = ((Set<?>) val).iterator(); it.hasNext();) {
								val = it.next();
								if (sb.length() != 0) {
									sb.append(", ");
								}
								if (val instanceof IPicklistEntry) {
									sb.append(((IPicklistEntry) val).getBezeichnung(langID));
								} else {
									sb.append(val.toString());
								}
							}
							if (sb.length() > 0) {
								valString = sb.toString();
							}
						} else if (val instanceof Date) {
							valString = format.format((Date) val);
						} else if (val instanceof IEntity) {
							DAO dao = DAOSystem.findDAOforEntity(((IEntity) val).type().getName());
							valString = dao.buildTitle((IEntity) val);
						} else if (val instanceof Boolean) {
							if ((Boolean)val) {
								valString = "Yes";
							} else {
								valString = "No";
							}
						}
						
						else if (val != null) {
							// just toString of others
							valString = val.toString();
						}
						CellLabel label = new CellLabel(valString);
						if (image != null) {
							label.image = image;
						} 
						if (toolTip != null) {
							label.toolTip = toolTip;
						}
						return label;

					} catch (Exception e) {
						return new CellLabel(e.getLocalizedMessage());
					}
				}
			}
			}
		}

		return new CellLabel("");
	}

	/**
	 * Small helper to generate the label for the displayed properties in the
	 * history.
	 * <p>
	 * 
	 * The key is generated from clazzName and propertyName to get it from the
	 * ResourceBundle.
	 * 
	 * @param clazzName
	 * @param propertyName
	 * @return
	 */
	private String generateLabel(String clazzName, String propertyName) {
		String key = null;
		if (propertyName == null || clazzName == null) {
			key = "<no clazz or property found!>";
		} else {
			String temp;
			if (entityProperties.contains(propertyName)) {
				temp = IEntity.class.getName() + "." + propertyName;
			} else {
				temp = clazzName + "." + propertyName;
			}
			key = "!" + temp + "!";
			try {
				EntityDescriptor eDesc = ConfigurationManager.getSetup().getEntityDescriptor(clazzName);
				key = eDesc.getDomain().getBundle(Locale.getDefault().getLanguage()).getString(temp);
			} catch (ConfigurationException e) {
				key = "Configuration error! " + e.getLocalizedMessage();
			}
		}
		return key;
	}

}
