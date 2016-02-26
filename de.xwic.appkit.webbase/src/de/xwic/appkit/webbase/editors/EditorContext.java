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
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import de.jwic.base.IControl;
import de.jwic.controls.InputBox;
import de.jwic.controls.RadioGroup;
import de.jwic.events.SelectionEvent;
import de.jwic.events.SelectionListener;
import de.xwic.appkit.core.config.Bundle;
import de.xwic.appkit.core.config.ConfigurationException;
import de.xwic.appkit.core.config.editor.EditorConfiguration;
import de.xwic.appkit.core.config.model.EntityDescriptor;
import de.xwic.appkit.core.config.model.Property;
import de.xwic.appkit.core.dao.DAO;
import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.dao.DataAccessException;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.dao.IHistory;
import de.xwic.appkit.core.dao.ValidationResult;
import de.xwic.appkit.core.dao.ValidationResult.Severity;
import de.xwic.appkit.core.model.EntityModelException;
import de.xwic.appkit.core.model.EntityModelFactory;
import de.xwic.appkit.core.model.IEntityModel;
import de.xwic.appkit.webbase.editors.events.EditorEvent;
import de.xwic.appkit.webbase.editors.events.EditorListener;
import de.xwic.appkit.webbase.editors.mappers.*;
import de.xwic.appkit.webbase.utils.picklist.IPicklistEntryControl;

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
	
	private GenericEditorInput input = null;
	private EditorConfiguration config = null;
	private IEntityModel model = null;
	private Bundle bundle = null;
	private boolean dirty = false;
	private boolean editable = true;

	/** indicates if the fields are modified during save/load operation */
	private boolean inTransaction = false;
	private List<PropertyMapper> mappers = new ArrayList<PropertyMapper>();

	// default mappers
	private InputboxMapper textMapper;
	private PicklistEntryMapper plEntryMapper;
	private RadioGroupMapper1 radioGroupMapper1;
//	private DateMapper dateMapper;
//	private CheckboxPropertyMapper checkMapper;

	// properties - page assignment
	private EditorContentPage currPage = null;
	private Map<String, EditorContentPage> propertyPageMap = new HashMap<String, EditorContentPage>();
	private Map widgetMap = new HashMap();
	private List<EditorContentPage> pages = new ArrayList<EditorContentPage>();
	private List listeners = new ArrayList();

	// default listeners
//	private ModifyListener defaultModifyListener = new ModifyListener() {
//		public void modifyText(ModifyEvent e) {
//			setDirty(true);
//		}
//	};

	private SelectionListener defaultSelectionListener = new SelectionListener() {
		public void objectSelected(SelectionEvent event) {
			setDirty(true);
		}
	};

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
		
		DAO dao = DAOSystem.findDAOforEntity(config.getEntityType().getClassname());
		this.editable = dao.hasRight(input.getEntity(), "UPDATE") 
							&& !input.getEntity().isDeleted()
							&& !(input.getEntity() instanceof IHistory);
		createStandardMappers();
	}

	/**
	 * 
	 */
	private void createStandardMappers() {

		textMapper = new InputboxMapper(config.getEntityType());
		mappers.add(textMapper);

		plEntryMapper = new PicklistEntryMapper(config.getEntityType());
		mappers.add(plEntryMapper);

		radioGroupMapper1 = new RadioGroupMapper1(config.getEntityType());
		mappers.add(radioGroupMapper1);
//
//		dateMapper = new DateMapper(config.getEntityType());
//		mappers.add(dateMapper);
//
//		checkMapper = new CheckboxPropertyMapper(config.getEntityType());
//		mappers.add(checkMapper);

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

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.client.uitools.editors.IBuilderContext#registerField(de.xwic.appkit.core.config.model.Property[], org.eclipse.swt.widgets.Widget, java.lang.String)
	 */
	public void registerField(Property[] property, IControl widget, String id) {
		registerField(property, widget, id, null);
	}

	/**
	 * Register a field that uses a custom mapper.
	 * 
	 * @param property
	 * @param widget
	 * @param id
	 * @param customMapper
	 */
	public void registerField(Property[] property, IControl widget, String id, PropertyMapper customMapper) {
		registerField(property, widget, id, customMapper, false);
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.client.uitools.editors.IBuilderContext#registerField(de.xwic.appkit.core.config.model.Property[], org.eclipse.swt.widgets.Widget, java.lang.String, de.xwic.appkit.core.client.uitools.editors.mapper.PropertyMapper)
	 */
	public void registerField(Property[] property, IControl widget, String id, PropertyMapper customMapper, boolean infoMode) {

		PropertyMapper mapper = null;
		if (customMapper != null) {
			mappers.add(customMapper);
			mapper = customMapper;
		} else {
			if (widget instanceof InputBox) {
				mapper = textMapper;
			} else if (widget instanceof IPicklistEntryControl) {
				mapper = plEntryMapper;
			} else if (widget instanceof RadioGroup) {
				mapper = radioGroupMapper1;
//			} else if (widget instanceof DateInputControl) {
//				mapper = dateMapper;
//			} else if (widget instanceof CheckboxControl) {
//				mapper = checkMapper;
			} else {
				throw new IllegalArgumentException("No default mapper for such a widget (" + widget.getClass().getName() + ")");
			}
			if (null != mapper) {
				mapper.registerProperty(widget, property, infoMode);
			}
		}
		if (currPage == null) {
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

		if (id != null) {
			widgetMap.put(id, widget);
		}

		// set initial editable flag.
		mapper.setEditable(widget, property, editable && (finalprop == null || finalprop.hasReadWriteAccess()));
	}

	/**
	 * Changes the editable flag of all registered widgets/properties.
	 * @param editable
	 */
	public void setAllEditable(boolean editable) {
		if (!editable || this.editable) {
			for (PropertyMapper mapper : mappers) {
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
			for (PropertyMapper mapper : mappers) {
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
			for (PropertyMapper mapper : mappers) {
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
			
		} finally {
			inTransaction = false;
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
			for (PropertyMapper mapper : mappers) {
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
			
			// now that all validations are done, highlight fields with errors
			for (PropertyMapper mapper : mappers) {
				mapper.highlightValidationResults(result);
			}			
			
			return result;
		} finally {
			inTransaction = false;
		}
	}

	/**
	 * @return ValidationResult
	 * @throws ValidationException
	 * @throws MappingException
	 * @throws EntityModelException
	 */
	public ValidationResult saveToEntity() throws ValidationException, MappingException, EntityModelException {
		inTransaction = true;
		try {
			IEntity entity = (IEntity) model;
			fireEvent(EVENT_BEFORESAVE, entity.getId() == 0);
			updateModel();

			DAO dao = DAOSystem.findDAOforEntity(config.getEntityType().getClassname());
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
			}else{
				// RCPErrorDialog.openError(UIToolsPlugin.getResourceString("error.dialog.EditorContext.validationfail"));
			}
			displayValidationResults(result);
			return result;

		} finally {
			inTransaction = false;
		}
	}
	
	/**
	 * Write the property values to the model without saving (commiting) the model.
	 * @throws MappingException
	 * @throws ValidationException
	 */
	public void updateModel() throws MappingException, ValidationException {
		
		IEntity entity = (IEntity) model;
		for (Iterator it = mappers.iterator(); it.hasNext();) {
			PropertyMapper mapper = (PropertyMapper) it.next();
			mapper.storeContent(entity);
		}
	}

	/**
	 * @param result
	 */
	void displayValidationResults(ValidationResult result) {

		for (EditorContentPage page : pages) {
			page.resetMessages();
		}
		EditorContentPage mainPage = pages.get(0);

		// assign warnings
		Map warnings = result.getWarningMap();
		String prefix = config.getEntityType().getClassname();
		for (Iterator it = warnings.keySet().iterator(); it.hasNext();) {
			String prop = (String) it.next();
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
		Map errors = result.getErrorMap();
		for (Iterator it = errors.keySet().iterator(); it.hasNext();) {
			String prop = (String) it.next();
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
	 * @return the defaultSelectionListener
	 */
	public SelectionListener getDefaultSelectionListener() {
		return defaultSelectionListener;
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

}

