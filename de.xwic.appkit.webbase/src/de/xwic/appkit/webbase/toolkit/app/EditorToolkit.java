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

import java.beans.IntrospectionException;
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
import de.xwic.appkit.core.util.ITypeConverter;
import de.xwic.appkit.core.util.typeconverters.BooleanStringConverter;
import de.xwic.appkit.core.util.typeconverters.DoubleStringConverter;
import de.xwic.appkit.core.util.typeconverters.FloatStringConverter;
import de.xwic.appkit.core.util.typeconverters.IntegerStringConverter;
import de.xwic.appkit.core.util.typeconverters.LongStringConverter;
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
 * Editortoolkit to create controls for editors. The controls and/or custom controls have to be registered.
 * 
 * @author Ronny Pfretzschner
 */
public class EditorToolkit {

	private final Log log = LogFactory.getLog(getClass());

	public static Map<Class<? extends IControl>, IToolkitControlHelper> allControls = new HashMap<Class<? extends IControl>, IToolkitControlHelper>();
	private Map<String, IControl> registeredControls = new HashMap<String, IControl>();
	private Map<String, ITypeConverter> registeredConverters = new HashMap<String, ITypeConverter>();

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
	 * @param controlType
	 *            the class type
	 * @param container
	 *            parent
	 * @param propertyName
	 *            of Entity
	 * @return a control
	 */
	public <C extends IControl> C createControl(final Class<C> controlType, final IControlContainer container, final String propertyName) {
		return createControl(controlType, container, propertyName, null);
	}

	/**
	 * Create a control by given control type like InputBox.class
	 * 
	 * @param controlType
	 *            the class type
	 * @param container
	 *            parent
	 * @param propertyName
	 *            of Entity
	 * @return a control
	 */
	public <C extends IControl> C createControl(final Class<C> controlType, final IControlContainer container, final String propertyName,
			ITypeConverter converter) {
		return createControl(controlType, container, propertyName, null, converter);
	}

	/**
	 * Create a control by given control type like InputBox.class
	 * 
	 * @param controlType
	 *            the class type
	 * @param container
	 *            parent
	 * @param propertyName
	 *            of Entity
	 * @param optionalParam
	 * @return a control
	 */
	public <C extends IControl> C createControl(final Class<C> controlType, final IControlContainer container, final String propertyName,
			final Object optionalParam) {
		return createControl(controlType, container, propertyName, optionalParam, null);

	}

	public <C extends IControl> C createControl(final Class<C> controlType, final IControlContainer container, final String propertyName,
			final Object optionalParam, ITypeConverter converter) {

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
		registeredConverters.put(propertyName, converter);

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
	public <C extends IControl> Collection<C> massCreateControls(Class<C> controlClass, IControlContainer container, boolean hasOptional,
			Object... elements) {
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
			IControl con = (IControl) iterator.next();
			//String propName = getPropertyName(control);

			IToolkitControlHelper helper = allControls.get(con.getClass());

			if (helper == null) {
				throw new RuntimeException("Could not find control helper: " + con.getClass());
			}

			if (helper != null && con.getName().startsWith(FIELD_NAME_PREFIX)) {
				String fieldName = con.getName().substring(FIELD_NAME_PREFIX.length());

				if (result.getErrorMap().get(fieldName) != null) {
					//save old css class, to get it back after removing the error style!
					if (!controlStyles.containsKey(con.getName())) {
						controlStyles.put(con.getName(), helper.getFieldMarkedCssClass(con));
					}
					helper.markField(con, "error");
				} else if (helper.getFieldMarkedCssClass(con) != null && ("error".equals(helper.getFieldMarkedCssClass(con))
						|| "valueError".equals(helper.getFieldMarkedCssClass(con)))) {

					if (controlStyles.containsKey(con.getName())) {
						helper.markField(con, controlStyles.get(con.getName()));
					} else {
						helper.markField(con, null);
					}
				}
			}
		}
	}

	/**
	 * Loads the field values into the controls. Entity is coming by internal editor model.
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

		for (Iterator<String> iterator = registeredControls.keySet().iterator(); iterator.hasNext();) {
			String controlId = iterator.next();
			IControl control = registeredControls.get(controlId);

			String propName = getPropertyName(control);

			Object value = null;

			try {
				PropertyDescriptor propInfo = new PropertyDescriptor(propName, clazz);
				value = propInfo.getReadMethod().invoke(obj, (Object[]) null);
			} catch (Exception ex) {
				throw new RuntimeException("Property not found: " + propName, ex);
			}

			IToolkitControlHelper helper = allControls.get(control.getClass());

			if (helper == null) {
				throw new RuntimeException("Could not find control helper: " + control.getClass());
			}

			Object controlValue;
			ITypeConverter converter = registeredConverters.get(propName);
			if (converter != null) {
				//if we have a type converter registered for the property, convert the entity type to the control value type first
				controlValue = converter.convertLeft(value);
			} else {
				controlValue = value;
			}

			helper.loadContent(control, controlValue);
		}
	}

	/**
	 * Saves the values from GUI controls into the entity of the editormodel. No update on DB is triggered.
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

			PropertyDescriptor propInfo = null;

			try {
				propInfo = new PropertyDescriptor(propName, clazz);
			} catch (IntrospectionException ex) {
				throw new RuntimeException(
						"Could find property: " + propName + " of entityclass: " + (obj == null ? "No entity!" : obj.getClass().getName()),
						ex);
			}

			if (!control.isVisible()) {
				continue;
			}

			if (control instanceof IHTMLElement) {
				IHTMLElement htmlElement = (IHTMLElement) control;
				if (!htmlElement.isEnabled()) {
					continue;
				}
			}

			IToolkitControlHelper helper = allControls.get(control.getClass());

			if (helper == null) {
				throw new RuntimeException("Could not find control helper: " + control.getClass());
			}

			Object value;
			Object controlValue = helper.getContent(control);

			ITypeConverter converter = registeredConverters.get(propName);
			if (converter != null) {
				//if we have a type converter registered for the property, convert the control value type to the entity value type first
				value = converter.convertRight(controlValue);
			} else {
				value = autoConvertToEntityType(propInfo, controlValue);
			}

			try {
				propInfo.getWriteMethod().invoke(obj, new Object[] { value });
			} catch (Exception ex) {
				throw new RuntimeException("Could not write property: " + propName + " of entityclass: "
						+ (obj == null ? "No entity!" : obj.getClass().getName()), ex);
			}
		}
	}

	/**
	 * Try some common type mappings
	 * 
	 * @param propInfo
	 * @param controlValue
	 * @return
	 */
	private Object autoConvertToEntityType(PropertyDescriptor propInfo, Object controlValue) {

		Class propertyType = propInfo.getPropertyType();
		Class controlType = null;
		if (controlValue == null) {
			//for primitives, null gets mapped to the default value 
			if (int.class.equals(propertyType) || long.class.equals(propertyType) || double.class.equals(propertyType)
					|| float.class.equals(propertyType) || byte.class.equals(propertyType) || short.class.equals(propertyType)) {
				return 0;
			}
			if (boolean.class.equals(propertyType)) {
				return false;
			}
			if (char.class.equals(propertyType)) {
				return '\u0000';
			}
		} else {
			controlType = controlValue.getClass();
		}

		ITypeConverter converter = null;
		if (String.class.equals(controlType)) {
			if (Integer.class.equals(propertyType) || int.class.equals(propertyType)) {
				converter = IntegerStringConverter.INSTANCE;
			} else if (Long.class.equals(propertyType) || long.class.equals(propertyType)) {
				converter = LongStringConverter.INSTANCE;
			} else if (Float.class.equals(propertyType) || float.class.equals(propertyType)) {
				converter = FloatStringConverter.INSTANCE;
			} else if (Double.class.equals(propertyType) || double.class.equals(propertyType)) {
				converter = DoubleStringConverter.INSTANCE;
			} else if (Boolean.class.equals(propertyType) || boolean.class.equals(propertyType)) {
				converter = BooleanStringConverter.INSTANCE;
			}
			if (converter != null) {
				return converter.convertRight(controlValue);
			}
		}
		return controlValue;
	}

	/**
	 * @return the editor model holding the entity
	 */
	public EditorModel getModel() {
		return model;
	}
}
