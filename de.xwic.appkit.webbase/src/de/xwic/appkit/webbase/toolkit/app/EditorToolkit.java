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
/**
 * 
 */
package de.xwic.appkit.webbase.toolkit.app;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.jwic.base.IControl;
import de.jwic.base.IControlContainer;
import de.jwic.controls.CheckBoxGroup;
import de.jwic.controls.DatePicker;
import de.jwic.controls.InputBox;
import de.jwic.util.IHTMLElement;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.dao.ValidationResult;
import de.xwic.appkit.webbase.toolkit.app.helper.ToolkitCheckBoxControl;
import de.xwic.appkit.webbase.toolkit.app.helper.ToolkitDateInputControl;
import de.xwic.appkit.webbase.toolkit.app.helper.ToolkitEmployeeControl;
import de.xwic.appkit.webbase.toolkit.app.helper.ToolkitInputBoxControl;
import de.xwic.appkit.webbase.toolkit.app.helper.ToolkitMultiAttachmentControl;
import de.xwic.appkit.webbase.toolkit.app.helper.ToolkitPicklistSelectionControl;
import de.xwic.appkit.webbase.toolkit.app.helper.ToolkitPicklistSelectionMultiControl;
import de.xwic.appkit.webbase.toolkit.app.helper.ToolkitSingleAttachmentControl;
import de.xwic.appkit.webbase.toolkit.attachment.SingleAttachmentControl;
import de.xwic.appkit.webbase.toolkit.comment.SingleCommentEditorControl;
import de.xwic.appkit.webbase.toolkit.components.EmployeeSelectionCombo;
import de.xwic.appkit.webbase.toolkit.editor.EditorModel;
import de.xwic.appkit.webbase.utils.picklist.PicklistEntryControl;
import de.xwic.appkit.webbase.utils.picklist.PicklistEntryMultiSelectControl;

/**
 * Editortoolkit to create controls for editors.
 * The controls and/or custom controls have to be registered.
 * 
 * @author Ronny Pfretzschner
 */
public class EditorToolkit {

	private final Log log = LogFactory.getLog(getClass());
	
	public static Map<Class<? extends IControl>, IToolkitControlHelper> allControls = new HashMap<Class<? extends IControl>, IToolkitControlHelper>();
	private Map<String, IControl> registeredControls = new HashMap<String, IControl>();
	
	private Map<String, String> controlStyles = null;
	
	public final static String FIELD_NAME_PREFIX = "fld";
	
	private EditorModel model = null;
	private String langId = null;
	
	private Object baseObj;
	
	
	static {
		allControls.put(DatePicker.class, new ToolkitDateInputControl());
		allControls.put(EmployeeSelectionCombo.class, new ToolkitEmployeeControl());
		allControls.put(InputBox.class, new ToolkitInputBoxControl());
		allControls.put(PicklistEntryControl.class, new ToolkitPicklistSelectionControl());
		allControls.put(PicklistEntryMultiSelectControl.class, new ToolkitPicklistSelectionMultiControl());
		allControls.put(CheckBoxGroup.class, new ToolkitCheckBoxControl());
		allControls.put(SingleAttachmentControl.class, new ToolkitSingleAttachmentControl());
		allControls.put(SingleCommentEditorControl.class, new ToolkitMultiAttachmentControl());
	}
	
	
	/**
	 * Create the editor toolkit
	 * 
	 * @param model
	 * @param langId
	 */
	public EditorToolkit(EditorModel model, String langId) {
		this.model = model;
		this.langId = langId;
		this.controlStyles = new HashMap<String, String>();
	}
	
	public EditorToolkit(Object baseObj) {
		this.baseObj = baseObj;
	}

	/**
	 * Create a control by given control type like InputBox.class
	 * 
	 * @param controlType the class type
	 * @param container parent
	 * @param propertyName of Entity
	 * @return a control
	 */
	public <C extends IControl> C createControl(final Class<C> controlType, final IControlContainer container, 
			final String propertyName) {
		return createControl(controlType, container, propertyName, null);
	}

	/**
	 * Create a control by given control type like InputBox.class
	 * 
	 * @param controlType the class type
	 * @param container parent
	 * @param propertyName of Entity
	 * @param optionalParam 
	 * @return a control
	 */
	public <C extends IControl> C createControl(final Class<C> controlType, final IControlContainer container, 
			final String propertyName, final Object optionalParam) {

		final IToolkitControlHelper<C> implclass = allControls.get(controlType);
		if (implclass == null) {
			throw new IllegalArgumentException("Control type not registered: " + controlType);
		}

		final C con = implclass.create(container, FIELD_NAME_PREFIX + propertyName, optionalParam);
		if (con == null) {
			log.error("Implementation doesn't return anything, bad implementation " + implclass.getClass(), new IllegalStateException());
			return null;
		}

		registeredControls.put(propertyName, con);
		return con;
	}
	

	/**
	 * @param control
	 * @return the propertyname or, if no prefix is found, the control name
	 */
	public String getPropertyName(IControl control) {
		String name = control.getName();
		if (name.startsWith(FIELD_NAME_PREFIX)) {
			return name.substring(FIELD_NAME_PREFIX.length());
		}
		return name;
	}

	/**
	 * @param controlClass
	 * @param container
	 * @param elements
	 * @return
	 */
	public <C extends IControl> Collection<C> massCreateControls(Class<C> controlClass, IControlContainer container, Object... elements) {
		return massCreateControls(controlClass, container, true, elements);
	}

	/**
	 * @param controlClass
	 * @param container
	 * @param hasOptional
	 * @param elements
	 * @return
	 */
	public <C extends IControl> Collection<C> massCreateControls(Class<C> controlClass, IControlContainer container, boolean hasOptional, Object... elements) {
		Collection<C> controls = new ArrayList<C>();
		for (int i = 0; i < elements.length; i++) {
			String name = (String) elements[i];
			Object optional = null;
			if (hasOptional) {
				i++;
				optional = elements[i];
			}
			controls.add(createControl(controlClass, container, name, optional));
		}
		return controls;
	}

	/**
	 * Marks the fields as invalid.
	 * 
	 * @param result
	 */
	public void markInvalidFields(ValidationResult result) {

		for (Iterator<IControl> iterator = registeredControls.values().iterator(); iterator.hasNext();) {
			IControl con = (IControl)iterator.next();
			//String propName = getPropertyName(control);

			IToolkitControlHelper helper = allControls.get(con.getClass());
			
			if (helper == null) {
				throw new RuntimeException("Could not find control helper: "
						+ con.getClass());
			}

			if (helper != null && con.getName().startsWith(FIELD_NAME_PREFIX)) {
				String fieldName = con.getName().substring(FIELD_NAME_PREFIX.length());
				
				if (result.getErrorMap().get(fieldName) != null) {
					//save old css class, to get it back after removing the error style!
					if (!controlStyles.containsKey(con.getName())) {
						controlStyles.put(con.getName(), helper.getFieldMarkedCssClass(con));
					}
					helper.markField(con, "error");
				} else if (helper.getFieldMarkedCssClass(con) != null
						&& ("error".equals(helper.getFieldMarkedCssClass(con)) || "valueError".equals(helper.getFieldMarkedCssClass(con)))) {

					if (controlStyles.containsKey(con.getName())) {
						helper.markField(con, controlStyles.get(con
								.getName()));
					} else {
						helper.markField(con, null);
					}
				}
			}
		}
	}
	
	/**
	 * Loads the field values into the controls. Entity is coming
	 * by internal editor model.
	 */
	public void loadFieldValues() {
		Object obj = null;
		Class<?> clazz = null;
		if (null != baseObj) {
			obj = baseObj;
			clazz = obj.getClass();
		} else if (null != model) {
			obj = model.getEntity();
			clazz = ((IEntity) obj).type();
		}
		
		for (Iterator<String> iterator = registeredControls.keySet().iterator(); iterator
				.hasNext();) {
			String controlId = iterator.next();
			IControl control = registeredControls.get(controlId);

			String propName = getPropertyName(control);
			
			Object value = null;

			try {
				PropertyDescriptor propInfo = new PropertyDescriptor(propName,
						clazz);
				value = propInfo.getReadMethod().invoke(obj, (Object []) null);
			} catch (Exception ex) {
				throw new RuntimeException("Property not found: " + propName,
						ex);
			}

			
			IToolkitControlHelper helper = allControls.get(control.getClass());
			
			if (helper == null) {
				throw new RuntimeException("Could not find control helper: "
						+ control.getClass());
			}
			
			helper.loadContent(control, value);
			
//			if (control instanceof DateInputBox) {
//				if (null != value) {
//					((DateInputBox) control).setDate((Date) value);
//				}
////			} else if (control instanceof PicklistEntryControl) {
////				PicklistEntryControl plcombo = (PicklistEntryControl) control;
////				if (null == value || value instanceof IPicklistEntry) {
////					plcombo.selectEntry((IPicklistEntry) value);
////				}
////			} else if (control instanceof PicklistEntryCheckControl) {
////				PicklistEntryCheckControl check = (PicklistEntryCheckControl) control;
////				check.selectEntry((Set) value);
//			} 
//			if (control instanceof DateTimeSelectionControl) {
//				if (null != value) {
//					((DateTimeSelectionControl) control).setDateTime((Date) value, null);
//				}
//			}
//			else if (control instanceof IEntityListBoxControl) {
//				IEntityListBoxControl ent = (IEntityListBoxControl) control;
//				ent.selectEntry(((IEntity) value));
//			} else if (control instanceof LabelControl) {
//				LabelControl lblControl = (LabelControl) control;
//
//				if (value instanceof IPicklistEntry) {
//					lblControl.setText(((IPicklistEntry) value).getBezeichnung(
//							plDao, langId));
//				} else {
//					lblControl.setText(value != null ? value.toString() : "");
//				}
//			} else if (control instanceof SingleAttachmentControl) {
//				SingleAttachmentControl sac = (SingleAttachmentControl) control;
//				sac.loadAttachment((IAttachment) value);
//			} else if (control instanceof InputBox) {
//				InputBox inp = (InputBox) control;
//				inp.setText(value != null ? value.toString() : "");
//			} else if (control instanceof CheckBox) {
//				CheckBox inp = (CheckBox) control;
//				boolean boolVal = ((Boolean) value).booleanValue();
//				if (boolVal) {
//					inp.setSelectedKey(propName);
//				}
//			} 
		}
	}

	
	
	
	/**
	 * Saves the values from GUI controls into the entity of the editormodel.
	 * No update on DB is triggered.
	 */
	public void saveFieldValues() {

		Object obj = null;
		Class<?> clazz = null;
		if (null != baseObj) {
			obj = baseObj;
			clazz = obj.getClass();
		} else if (null != model) {
			obj = model.getEntity();
			clazz = ((IEntity) obj).type();
		}
		

		for (Iterator<IControl> iterator = registeredControls.values().iterator(); iterator.hasNext();) {
			
			IControl control = iterator.next();
			String propName = getPropertyName(control);

			Object value = null;
			PropertyDescriptor propInfo = null;

			try {
				propInfo = new PropertyDescriptor(propName, clazz);
				value = propInfo.getReadMethod().invoke(obj, (Object[]) null);
			} catch (Exception ex) {
				throw new RuntimeException("Could not write property: "
						+ propName
						+ " of entityclass: "
						+ (obj == null ? "No entity!" : obj.getClass()
								.getName()), ex);
			}

			if (!control.isVisible()) {
				continue;
			}
			
			if (control instanceof IHTMLElement) {
				IHTMLElement htmlElement = (IHTMLElement)control;
				if (!htmlElement.isEnabled()) {
					continue;
				}
			}
			
			IToolkitControlHelper helper = allControls.get(control.getClass());
			
			if (helper == null) {
				throw new RuntimeException("Could not find control helper: "
						+ control.getClass());
			}
			
			value = helper.getContent(control);
			
			//yet, no extra "converter" classes
//			if (control instanceof DateInputBox) {
//				value = ((DateInputBox) control).getDate();
//			} else if (control instanceof PicklistEntryControl) {
//				PicklistEntryControl plcombo = (PicklistEntryControl) control;
//				value = plcombo.getSelectionEntry();
//			}else if (control instanceof ExpertiseControl) {
//				ExpertiseControl plcombo = (ExpertiseControl) control;
//				value = plcombo.getSelectedEntries();
//			} else if (control instanceof InputBox) {
//				InputBox inp = (InputBox) control;
//				value = inp.getText();
//			} else if (control instanceof PicklistEntryCheckControl) {
//				PicklistEntryCheckControl checkCon = (PicklistEntryCheckControl) control;
//				value = checkCon.getSelectionEntrySet();
//			} else if (control instanceof CheckBox) {
//				CheckBox inp = (CheckBox) control;
//				value = Boolean.valueOf(propName.equals(inp.getSelectedKey()));
//			} else if (control instanceof IEntityListBoxControl) {
//				IEntityListBoxControl entityCon = (IEntityListBoxControl) control;
//				value = entityCon.getSelectedEntry();
//			} else if (control instanceof SingleAttachmentControl) {
//				SingleAttachmentControl sac = (SingleAttachmentControl) control;
//				IAttachmentWrapper wrapper = sac.saveAttachment(entity);
//				if (wrapper == null)
//					value = null;
//				else if (wrapper.isDeleted()) {
//					value = null;
//				} else {
//					value = wrapper.getAnhang();
//				}
//			} 
//			else if (control instanceof GeoListBoxControl) {
//				GeoListBoxControl geo = (GeoListBoxControl)control;
//				value = geo.getGeo();
//			} 
//			
//			else {
//				continue; // skip unknown types...
//			}

			if (value != null) {
				if (propInfo.getPropertyType().equals(Integer.class)
						|| propInfo.getPropertyType().equals(int.class)) {
					if (value.getClass().equals(String.class)) {
						// convert int to String
						String s = (String) value;
						if(s == null || s.length() == 0){
							Class<?> propertyType = propInfo.getPropertyType();
							if(propertyType.equals(Integer.class)){
								value = null;
							}else {
								value = 0;
							}
						}else {
							value = Integer.valueOf(s);
						}
					} else if (value.getClass().equals(Integer.class)) {
						value = ((Integer) value);
					}
				}
				if (propInfo.getPropertyType().equals(Double.class)
						|| propInfo.getPropertyType().equals(double.class)) {
					if (value.getClass().equals(String.class)) {
						// convert String to decimal
						String s = ((String) value).trim();
						if(s == null || s.length() == 0){
							Class<?> propertyType = propInfo.getPropertyType();
							if(propertyType.equals(Double.class)){
								value = null;
							}else {
								value = new Double(0);
							}
						} else {
							value = Double.valueOf(s);
						}
					}
				}
			}
			
			try {
//				//generic part
//				if (isPicklist && !propertyFound) {
//					String picklistKey = ((IPicklistEntryControl)control).getPicklistKey();
//					if (value instanceof Set) {
//						Set set = (Set)value;
//						saveFieldPicklistEntries((IPdbPicklistEntryRelationsEntity)entity, set, picklistKey);
//					} else {
//						IPicklistEntry entry = (IPicklistEntry)value;
//						saveFieldPicklistEntries((IPdbPicklistEntryRelationsEntity)entity, entry, picklistKey);
//					}
//					
//				} else if (isPicklist && propertyFound) {
//					
//				} else {
//					if (propertyFound) {
//						propInfo.getWriteMethod().invoke(entity, new Object[] { value });
//					} else {
//						// save or delete property as PropertyValue
//						opDao.update(propertyValues, entity, propName, value);
//					}
//				}
				propInfo.getWriteMethod().invoke(obj, new Object[] { value });
			} catch (Exception ex) {
				throw new RuntimeException("Could not write property: "
						+ propName
						+ " of entityclass: "
						+ (obj == null ? "No entity!" : obj.getClass()
								.getName()), ex);
			}
		}
	}


	/**
	 * @return the editor model holding the entity
	 */
	public EditorModel getModel() {
		return model;
	}
}
