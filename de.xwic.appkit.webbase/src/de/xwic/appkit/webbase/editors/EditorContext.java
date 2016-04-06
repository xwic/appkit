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
package de.xwic.appkit.webbase.editors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptException;

import de.xwic.appkit.core.dao.*;
import de.xwic.appkit.webbase.editors.events.IEditorListenerFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.jwic.base.IControl;
import de.xwic.appkit.core.config.Bundle;
import de.xwic.appkit.core.config.ConfigurationException;
import de.xwic.appkit.core.config.editor.EditorConfiguration;
import de.xwic.appkit.core.config.editor.UIElement;
import de.xwic.appkit.core.config.model.EntityDescriptor;
import de.xwic.appkit.core.config.model.Property;
import de.xwic.appkit.core.dao.ValidationResult.Severity;
import de.xwic.appkit.core.model.EntityModelException;
import de.xwic.appkit.core.model.EntityModelFactory;
import de.xwic.appkit.core.model.IEntityModel;
import de.xwic.appkit.core.script.ScriptEngineProvider;
import de.xwic.appkit.webbase.editors.events.EditorEvent;
import de.xwic.appkit.webbase.editors.events.EditorListener;
import de.xwic.appkit.webbase.editors.mappers.MappingException;
import de.xwic.appkit.webbase.editors.mappers.PropertyMapper;
import de.xwic.appkit.webbase.editors.mappers.StandardMapperFactory;

/**
 * Contains the widgtes and property-mappers for the editor session.
 *
 * @author Florian Lippisch
 */
public class EditorContext implements IBuilderContext {

	static final int EVENT_AFTERSAVE = 0;
	static final int EVENT_LOADED = 1;
	static final int EVENT_BEFORESAVE = 2;
	static final int EVENT_PAGES_CREATED = 3;

	private final static Log log = LogFactory.getLog(EditorContext.class);
	/**
	 * Editor entity listener custom extension point name.
	 */
	private static final String EP_EDITOR_LISTENER = "editorListener";
	/**
	 * Editor entity validator custom extension point name.
	 */
	private static final String EP_EDITOR_VALIDATOR = "editorValidator";

	private GenericEditorInput input = null;
	private EditorConfiguration config = null;
	private IEntityModel model = null;
	private Bundle bundle = null;
	private boolean dirty = false;
	private boolean editable = true;

	/** indicates if the fields are modified during save/load operation */
	private boolean inTransaction = false;
	private Map<String, PropertyMapper<IControl>> mappers = new HashMap<String, PropertyMapper<IControl>>();

	// properties - page assignment
	private EditorContentPage currPage = null;
	private Map<String, EditorContentPage> propertyPageMap = new HashMap<String, EditorContentPage>();
	private Map<String, IControl> widgetMap = new HashMap<String, IControl>();
	private List<EditorContentPage> pages = new ArrayList<EditorContentPage>();
	private List<EditorListener> listeners = new ArrayList<EditorListener>();
	private List<HideWhenWidgetWrapper> hideWhenWidgets = new ArrayList<HideWhenWidgetWrapper>();

	/** This list contains errors that happened during the start. They are displayed to the user. */
	private List<String> initErrors = new ArrayList<String>();

	private ScriptEngine scriptEngine;

	/**
	 * @param input
	 * @throws ConfigurationException
	 * @throws EntityModelException
	 */
	public EditorContext(GenericEditorInput input, String langId) throws ConfigurationException,
			EntityModelException {
		this.input = input;
		this.config = input.getConfig();
		this.bundle = config.getEntityType().getDomain().getBundle(langId);
		this.model = EntityModelFactory.createModel(input.getEntity());
		this.dirty = input.getEntity().getId() == 0; // is a new entity.

		DAO<?> dao = DAOSystem.findDAOforEntity(config.getEntityType().getClassname());
		this.editable = dao.hasRight(input.getEntity(), "UPDATE")
							&& !input.getEntity().isDeleted()
							&& !(input.getEntity() instanceof IHistory);

		scriptEngine = ScriptEngineProvider.instance().createEngine("Editor(" + config.getEntityType() + ":" + config.getId() + ")");
		Bindings bindings = scriptEngine.getBindings(ScriptContext.ENGINE_SCOPE);
		bindings.put("entity", model);
		bindings.put("bundle", bundle);
		bindings.put("context", this);

		try {
			if (config.getGlobalScript() != null && !config.getGlobalScript().trim().isEmpty()) {
				scriptEngine.eval(config.getGlobalScript());
			}
		} catch (ScriptException se) {
			initErrors.add("ScriptException in GlobalScript for editor configuration (" + se + ")");
			log.error("Error evaluating global script in Editor(" + config.getEntityType() + ":" + config.getId() + ")", se);
		}
        final String entityType = getEntityDescriptor().getClassname();
        final List<IEditorListenerFactory> listenerExtensions = EditorExtensionUtils.getExtensions(EP_EDITOR_LISTENER, entityType);
        for (IEditorListenerFactory listenerExtension : listenerExtensions) {
            EditorListener editorListener = listenerExtension.createListener();
            addEditorListener(editorListener);
        }
    }

	/**
	 * Add an EditorListener.
	 *
	 * @param listener
	 */
	public synchronized void addEditorListener(EditorListener listener) {
		listeners.add(listener);
	}

	/**
	 * Remove an EditorListener.
	 *
	 * @param listener
	 */
	public synchronized void removeEditorListener(EditorListener listener) {
		listeners.remove(listener);
	}

	/**
	 * Fire an event.
	 *
	 * @param event
	 */
	void fireEvent(int eventId, boolean isNewEntity) {
		Object[] tmp = listeners.toArray();
		EditorEvent event = new EditorEvent(this, isNewEntity);
		for (int i = 0; i < tmp.length; i++) {
			EditorListener listener = (EditorListener) tmp[i];
			switch (eventId) {
				case EVENT_AFTERSAVE:
					listener.afterSave(event);
					break;
				case EVENT_LOADED:
					listener.entityLoaded(event);
					break;
				case EVENT_BEFORESAVE:
					listener.beforeSave(event);
					break;
				case EVENT_PAGES_CREATED:
					listener.pagesCreated(event);
					break;
			}
		}
	}

	/**
	 * Register a field that uses a custom mapper.
	 *
	 * @param property
	 * @param widget
	 * @param id
	 * @param customMapper
	 */
	public void registerField(Property[] property, IControl widget, UIElement uiDef, String mapperId) {
		registerField(property, widget, uiDef, mapperId, false);
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.client.uitools.editors.IBuilderContext#registerField(de.xwic.appkit.core.config.model.Property[], org.eclipse.swt.widgets.Widget, java.lang.String, java.lang.String, boolean)
	 */
	public void registerField(Property[] property, IControl widget, UIElement uiDef, String mapperId, boolean infoMode) {

		PropertyMapper<IControl> mapper = mappers.get(mapperId);
		if (mapper == null) {
			try {
				mapper = StandardMapperFactory.instance().createMapper(mapperId, getEntityDescriptor());
			} catch (Exception e) {
				String msg = "Error creating mapper " + mapperId;
				log.error(msg, e);
				initErrors.add(msg + " (" + e.toString() + ")");
				return;
			}
			mappers.put(mapperId, mapper);
		}
		mapper.registerProperty(widget, property, infoMode);

		if (currPage == null) {
			// This is a problem when the editor container is not creating the tabs properly.
			// before creating a tab, the current page should be set and after creating the
			// currPage should be set to null again.
			throw new IllegalStateException("No current page assigned!");
		}
		// build property name
		StringBuffer sb = new StringBuffer();
		Property finalprop = null;
		if (null != property) {
			finalprop = property[property.length - 1];
			for (int i = 0; i < property.length; i++) {
				if (i > 0) {
					sb.append(".");
				}
				sb.append(property[i].getName());
			}
		}
		//propertyPageMap.put(sb.toString(), currPage);
		propertyPageMap.put(finalprop.getEntityDescriptor().getClassname() + "." + finalprop.getName(), currPage);

		registerWidget(widget, uiDef);

		// set initial editable flag.
		mapper.setEditable(widget, property, editable && (finalprop == null || finalprop.hasReadWriteAccess()));
	}

	/**
	 * Register a widget with the given id. If the id is <code>null</code>, the widget
	 * will not be registered but no exception is thrown.
	 * <p>This is useful for widgets that should be accessible from script but are not a field by
	 * itself, like a container.
	 *
	 * @param id
	 * @param widget
	 */
	public void registerWidget(IControl widget, UIElement uiDef) {
		// register widget
		String id = uiDef.getId();
		if (id != null && !id.isEmpty()) {
			if (widgetMap.containsKey(id)) {
				// print a warning, in case this already exists. Either an id was used twice (where it
				// should be unique), or a builder has called registerWidget for a widget where
				// registerField(..) was already invoked.
				log.warn("A widget with the id '" + id + "' is already registered!");
			}
			widgetMap.put(id, widget);
		}

		if (uiDef.getHideWhen() != null && !uiDef.getHideWhen().trim().isEmpty()) {
			// the widget has some logic that is to be evaluated after refreshes. Add the
			// control to the hideWhenField list
			hideWhenWidgets.add(new HideWhenWidgetWrapper(widget, uiDef.getHideWhen()));
		}
	}

	/**
	 * Changes the editable flag of all registered widgets/properties.
	 * @param editable
	 */
	public void setAllEditable(boolean editable) {
		if (!editable || this.editable) {
			for (PropertyMapper<IControl> mapper : mappers.values()) {
				mapper.setEditable(editable);
			}
		}
	}

	/**
	 * Change the editable flag of a specified property.
	 * @param editable
	 * @param propertyKey
	 */
	public void setFieldEditable(boolean editable, String propertyKey) {
		if (!editable || this.editable) { // prevent listeners to enable editable when the entity is not editable.
			for (PropertyMapper<IControl> mapper : mappers.values()) {
				mapper.setFieldEditable(editable, propertyKey);
			}
		}
	}

	/**
	 * Load the values from the entity model into the fields.
	 *
	 * @throws MappingException
	 */
	public void loadFromEntity() throws MappingException {
		inTransaction = true;
		try {
			IEntity entity = (IEntity)model;
			for (PropertyMapper<IControl> mapper : mappers.values()) {
				mapper.loadContent(entity);
			}
			fireEvent(EVENT_LOADED, entity.getId() == 0);

			// if not new, validate
			if (entity.getId() != 0) {
				displayValidationResults(validateFields());
			} else {
				// must call displayValidationResult to make sure
				// that the error-composite is initialized correct.
				// this fixed an isse where the scrollBar was not visible
				// on new entities.
				displayValidationResults(new ValidationResult());
			}

			evaluateHideWhens();

		} finally {
			inTransaction = false;
		}
	}

	/**
	 * Run through all widgets with a hideWhen formula and execute it to
	 * hide/show those widgets.
	 */
	private void evaluateHideWhens() {

		for (HideWhenWidgetWrapper hwWidget : hideWhenWidgets) {
			try {
				hwWidget.evaluate(scriptEngine);
			} catch (ScriptException e) {
				log.error("Error evaluating 'hideWhen'", e);
			}
		}

	}

	/**
	 * Validates the fields on the form.
	 *
	 * @return A ValidationResult containing all errors
	 * @throws MappingException
	 */
	public ValidationResult validateFields() {
		inTransaction = true;
		ValidationResult result = new ValidationResult();
		try {
			// perform UI side validation first...
			for (PropertyMapper<IControl> mapper : mappers.values()) {
				ValidationResult tempResult = mapper.validateWidgets();
				result.addErrors(tempResult.getErrorMap());
				result.addWarnings(tempResult.getWarningMap());
			}

			DAO<?> dao = DAOSystem.findDAOforEntity(config.getEntityType().getClassname());
			if (dao == null) {
				throw new DataAccessException("Can not find a DAO for entity type " + config.getEntityType().getClassname());
			}
			// then perform DAO (backend) side validation
			ValidationResult daoResult = dao.validateEntity((IEntity) model);
			result.addErrors(daoResult.getErrorMap());
			result.addWarnings(daoResult.getWarningMap());

			validateEntity(result, getEntityDescriptor().getClassname());
			// now that all validations are done, highlight fields with errors
			for (PropertyMapper<IControl> mapper : mappers.values()) {
				mapper.highlightValidationResults(result);
			}
			return result;
		} finally {
			inTransaction = false;
		}
	}

	private void validateEntity(ValidationResult result, String entityType) {
		final List<IEditorValidator> listenerExtensions = EditorExtensionUtils.getExtensions(EP_EDITOR_VALIDATOR, entityType);
		for (IEditorValidator editorValidator : listenerExtensions) {
			editorValidator.validate(model, result);
		}
	}

	/**
	 * @return ValidationResult
	 * @throws ValidationException
	 * @throws MappingException
	 * @throws EntityModelException
	 */
	public ValidationResult saveToEntity() throws ValidationException, MappingException, EntityModelException {

		if (!initErrors.isEmpty()) {
			throw new IllegalStateException("Save is disabled as there have been exceptions during initialization of the editor.");
		}

		inTransaction = true;
		try {
			IEntity entity = (IEntity) model;
			fireEvent(EVENT_BEFORESAVE, entity.getId() == 0);
			updateModel();

			DAO<?> dao = DAOSystem.findDAOforEntity(config.getEntityType().getClassname());
			if (dao == null) {
				throw new MappingException("Can not find a DAO for entity type " + config.getEntityType().getClassname());
			}
			ValidationResult result = validateFields();
			if (!result.hasErrors()) {
				model.commit();
				boolean isNew = model.getOriginalEntity().getId() == 0;
				dao.update(model.getOriginalEntity());
				inTransaction = false;
				setDirty(false);
				fireEvent(EVENT_AFTERSAVE, isNew); // revalidate
				if (isNew) {
					//EntityBroker.getEntityBroker().fireEntitySavedNew(model.getOriginalEntity());
				} else {
					//EntityBroker.getEntityBroker().fireEntitySavedExisting(model.getOriginalEntity());
				}
				loadFromEntity(); // load probably modified data.
				result = dao.validateEntity(entity); // revalidate after
														// save.
				validateEntity(result, getEntityDescriptor().getClassname());
			}else{
				// RCPErrorDialog.openError(UIToolsPlugin.getResourceString("error.dialog.EditorContext.validationfail"));
			}
			displayValidationResults(result);
			return result;

		} finally {
			inTransaction = false;
		}
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.editors.IBuilderContext#fieldChanged(de.xwic.appkit.core.config.model.Property[])
	 */
	@Override
	public void fieldChanged(Property[] property) {

		long t = System.currentTimeMillis();

		if (inTransaction) {
			// ignore the events when the editor is in a transaction, i.e. loading or saving.
			return;
		}

		// something changed? set dirty.
		setDirty(true);

		// update the property on the entity (model), to perform the hideWhen evaluation
		IEntity entity = (IEntity) model;
		try {
			for (PropertyMapper<IControl> mapper : mappers.values()) {
				mapper.storeContent(entity, property);
			}
		} catch (Exception me) {
			// Just log an error, do not notify users
			log.warn("Error handling field change", me);
		}

		evaluateHideWhens();

		long dur = System.currentTimeMillis() - t;
		if (dur > 100) {
			log.info("fieldChange handling took more than 100ms in Editor for " + this.config.getEntityType().getClassname());
		}

	}

	/**
	 * Write the property values to the model without saving (commiting) the model.
	 * @throws MappingException
	 * @throws ValidationException
	 */
	public void updateModel() throws MappingException, ValidationException {

		IEntity entity = (IEntity) model;
		for (PropertyMapper<IControl> mapper : mappers.values()) {
			mapper.storeContent(entity);
		}
	}

	/**
	 * @param result
	 */
	public void displayValidationResults(ValidationResult result) {

		for (EditorContentPage page : pages) {
			page.resetMessages();
		}
		EditorContentPage mainPage = pages.get(0);

		// add any initialization error
		for (String msg : initErrors) {
			mainPage.addError(msg);
		}

		// assign warnings
		Map<String, String> warnings = result.getWarningMap();
		for (String prop : warnings.keySet()) {
			String msg = (String) warnings.get(prop);
			EditorContentPage page = propertyPageMap.get(prop);
			String title;
			if (prop.startsWith("#")) {
				title = bundle.getString(prop.substring(1));
			} else {
				title = bundle.getString(prop);
			}
			String message = title + ": " + bundle.getString(msg);
			if (page != null) {
				page.addWarn(message);
			} else {
				mainPage.addWarn(message);
			}
		}

		// assign errors
		Map<String, String> errors = result.getErrorMap();
		for (String prop : errors.keySet()) {
			String msg = (String) errors.get(prop);
			EditorContentPage page = propertyPageMap.get(prop);
			String title;
			if (prop.startsWith("#")) {
				title = bundle.getString(prop.substring(1));
			} else {
				title = bundle.getString(prop);
			}
			String message = title + ": " + bundle.getString(msg);
			if (page != null) {
				page.addError(message);
			} else {
				mainPage.addError(message);
			}
		}

		for (EditorContentPage page : pages) {
			if (page.hasErrors()) {
				page.setStateIndicator(Severity.ERROR);
			} else if (page.hasWarnings()) {
				page.setStateIndicator(Severity.WARN);
			} else if (page.hasInfos()) {
				page.setStateIndicator(null);
			} else {
				page.setStateIndicator(null);
			}
		}

	}

	/**
	 * Returns the EditorConfiguration.
	 *
	 * @return
	 */
	public EditorConfiguration getEditorConfiguration() {
		return config;
	}

	/**
	 * @return the input
	 */
	public GenericEditorInput getInput() {
		return input;
	}

	/**
	 * @return the dirty
	 */
	public boolean isDirty() {
		return dirty;
	}

	/**
	 * @param dirty
	 *            the dirty to set
	 */
	public void setDirty(boolean dirty) {
		if (dirty != this.dirty && !inTransaction) {
			this.dirty = dirty;
		}
	}

	/**
	 * When a page is rendered, it must set itself as current page so that the
	 * context can assign created properties to the page. Warnings and Error
	 * messages for properties can then be assigned to the right page.
	 *
	 * @param currPage
	 *            the currPage to set
	 */
	public void setCurrPage(EditorContentPage currPage) {
		if (this.currPage != null && currPage != null) {
			throw new IllegalStateException("Another process is still creating properties on a page.");
		}
		this.currPage = currPage;
		if (currPage != null) {
			if (!pages.contains(currPage)) {
				pages.add(currPage);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see de.xwic.appkit.core.client.uitools.editors.IBuilderContext#getEntityDescriptor()
	 */
	public EntityDescriptor getEntityDescriptor() {
		return getEditorConfiguration().getEntityType();
	}

	/**
	 * @return the inTransaction
	 */
	public boolean isInTransaction() {
		return inTransaction;
	}

	/**
	 * @param inTransaction the inTransaction to set
	 */
	public void setInTransaction(boolean inTransaction) {
		this.inTransaction = inTransaction;
	}

	/**
	 * Returns the model.
	 * @return
	 */
	public IEntityModel getModel() {
		return model;
	}

	/**
	 * @return the editable
	 */
	public boolean isEditable() {
		return editable;
	}

	public Bundle getBundle() {
		return bundle;
	}

	public String getResString(String key) {
		if (key != null && key.startsWith("#")) {
			return bundle != null ? bundle.getString(key.substring(1)) : "!" + key + "!";
		}
		return key;
	}

	public String getResString(Property property) {
		String key = property.getEntityDescriptor().getClassname() + "." + property.getName();
		return bundle != null ? bundle.getString(key) : "!" + key + "!";
	}

	public IControl getControlById(String id) {
		return (IControl) widgetMap.get(id);
	}

	/**
	 * Returns true if the entity being edited is new and unsaved.
	 * @return
	 */
	public boolean isNew() {
		return ((IEntity)model).getId() == 0;
	}

}

