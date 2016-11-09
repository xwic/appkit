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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.jwic.base.IControl;
import de.jwic.base.IControlContainer;
import de.jwic.controls.CheckBox;
import de.jwic.controls.CheckBoxGroup;
import de.jwic.controls.DatePicker;
import de.jwic.controls.DateTimePicker;
import de.jwic.controls.InputBox;
import de.jwic.controls.NumericInputBox;
import de.jwic.controls.NumericIntegerInputBox;
import de.jwic.util.IHTMLElement;
import de.xwic.appkit.core.dao.ValidationResult;
import de.xwic.appkit.core.util.IModelViewTypeConverter;
import de.xwic.appkit.webbase.toolkit.app.helper.ToolkitCheckBoxControl;
import de.xwic.appkit.webbase.toolkit.app.helper.ToolkitDatePickerControl;
import de.xwic.appkit.webbase.toolkit.app.helper.ToolkitDateTimeControl;
import de.xwic.appkit.webbase.toolkit.app.helper.ToolkitEmployeeControl;
import de.xwic.appkit.webbase.toolkit.app.helper.ToolkitInputBoxControl;
import de.xwic.appkit.webbase.toolkit.app.helper.ToolkitMultiAttachmentControl;
import de.xwic.appkit.webbase.toolkit.app.helper.ToolkitNumericInputBox;
import de.xwic.appkit.webbase.toolkit.app.helper.ToolkitNumericIntegerInputBox;
import de.xwic.appkit.webbase.toolkit.app.helper.ToolkitPicklistEntryCheckboxControl;
import de.xwic.appkit.webbase.toolkit.app.helper.ToolkitPicklistSelectionControl;
import de.xwic.appkit.webbase.toolkit.app.helper.ToolkitPicklistSelectionMultiControl;
import de.xwic.appkit.webbase.toolkit.app.helper.ToolkitSimpleCheckBoxControl;
import de.xwic.appkit.webbase.toolkit.app.helper.ToolkitSingleAttachmentControl;
import de.xwic.appkit.webbase.toolkit.app.modeladapters.EditorToolkitBeanModelAdapter;
import de.xwic.appkit.webbase.toolkit.app.modeladapters.EditorToolkitEntityModelAdapter;
import de.xwic.appkit.webbase.toolkit.app.modeladapters.IEditorToolkitModelAdapter;
import de.xwic.appkit.webbase.toolkit.attachment.SingleAttachmentControl;
import de.xwic.appkit.webbase.toolkit.comment.SingleCommentEditorControl;
import de.xwic.appkit.webbase.toolkit.components.EmployeeSelectionCombo;
import de.xwic.appkit.webbase.toolkit.editor.EditorModel;
import de.xwic.appkit.webbase.utils.picklist.PicklistEntryCheckboxControl;
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
	private Map<String, IModelViewTypeConverter> registeredConverters = new HashMap<String, IModelViewTypeConverter>();

	private Map<String, String> controlStyles = null;

	public final static String FIELD_NAME_PREFIX = "fld";

	private EditorModel model = null;

	private IEditorToolkitModelAdapter modelAdapter;

	static {
		allControls.put(InputBox.class, new ToolkitInputBoxControl());
		allControls.put(NumericInputBox.class, ToolkitNumericInputBox.INSTANCE);
		allControls.put(NumericIntegerInputBox.class, ToolkitNumericIntegerInputBox.INSTANCE);

		allControls.put(DatePicker.class, new ToolkitDatePickerControl());
		allControls.put(DateTimePicker.class, new ToolkitDateTimeControl());

		allControls.put(CheckBox.class, new ToolkitSimpleCheckBoxControl());
		allControls.put(CheckBoxGroup.class, new ToolkitCheckBoxControl());

		allControls.put(EmployeeSelectionCombo.class, new ToolkitEmployeeControl());

		allControls.put(PicklistEntryControl.class, new ToolkitPicklistSelectionControl());
		allControls.put(PicklistEntryMultiSelectControl.class, new ToolkitPicklistSelectionMultiControl());
		allControls.put(PicklistEntryCheckboxControl.class, new ToolkitPicklistEntryCheckboxControl());

		allControls.put(SingleAttachmentControl.class, new ToolkitSingleAttachmentControl());
		allControls.put(SingleCommentEditorControl.class, new ToolkitMultiAttachmentControl());
	}

	/**
	 * Create the editor toolkit
	 * 
	 * @param model
	 * @param langId
	 */
	@Deprecated
	public EditorToolkit(EditorModel model, String langId) {
		this(model);
	}

	/**
	 * Create the editor toolkit with an EditorModel/entity as backing model
	 * 
	 * @param model
	 * @param langId
	 */
	public EditorToolkit(EditorModel model) {
		this(new EditorToolkitEntityModelAdapter(model.getEntity()));
		this.model = model;
		this.controlStyles = new HashMap<String, String>();
	}

	/**
	 * Create the editor toolkit with a POJO as backing model
	 * 
	 * @param model
	 * @param langId
	 */
	public EditorToolkit(Object baseObj) {
		this(new EditorToolkitBeanModelAdapter(baseObj));
	}

	/**
	 * @param modelAdapter
	 */
	public EditorToolkit(IEditorToolkitModelAdapter modelAdapter) {
		this.modelAdapter = modelAdapter;
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
			IModelViewTypeConverter converter) {
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
			final Object optionalParam, IModelViewTypeConverter converter) {

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
		for (IControl control : registeredControls.values()) {
			String propName = getPropertyName(control);

			IToolkitControlHelper helper = allControls.get(control.getClass());
			if (helper == null) {
				throw new RuntimeException("Could not find control helper: " + control.getClass());
			}

			Object controlValue = modelAdapter.read(propName, registeredConverters.get(propName));

			helper.loadContent(control, controlValue);
		}
	}

	/**
	 * Saves the values from GUI controls into the entity of the editormodel. No update on DB is triggered.
	 */
	public void saveFieldValues() {

		Object obj = null;

		for (IControl control : registeredControls.values()) {

			String propName = getPropertyName(control);

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

			Object controlValue = helper.getContent(control);

			IModelViewTypeConverter converter = registeredConverters.get(propName);
			modelAdapter.write(propName, controlValue, converter);
		}
	}

	/**
	 * @return the editor model holding the entity
	 */
	public EditorModel getModel() {
		return model;
	}
}
