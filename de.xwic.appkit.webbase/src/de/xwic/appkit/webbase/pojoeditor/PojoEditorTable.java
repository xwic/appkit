/*
 * Copyright (c) NetApp Inc. - All Rights Reserved
 * 
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 * 
 * de.xwic.appkit.webbase.pojoeditor.PojoEditorTable 
 */

package de.xwic.appkit.webbase.pojoeditor;

import java.lang.reflect.Field;
import java.util.List;

import de.jwic.base.ControlContainer;
import de.jwic.base.IControlContainer;
import de.jwic.base.IHaveEnabled;
import de.jwic.controls.Button;
import de.jwic.controls.ToolBar;
import de.jwic.controls.ToolBarGroup;
import de.jwic.controls.tableviewer.TableColumn;
import de.jwic.controls.tableviewer.TableModel;
import de.jwic.controls.tableviewer.TableViewer;
import de.jwic.data.ListContentProvider;
import de.jwic.events.ElementSelectedEvent;
import de.jwic.events.ElementSelectedListener;
import de.jwic.events.SelectionEvent;
import de.jwic.events.SelectionListener;
import de.xwic.appkit.core.pojoeditor.annotations.PojoControl;
import de.xwic.appkit.webbase.toolkit.util.ImageLibrary;

/**
 * @author Andrei Pat
 *
 */
public class PojoEditorTable extends ControlContainer implements IHaveEnabled {

	private PojoEditorTableModel editorModel;

	private boolean enabled;

	private TableViewer table;
	private Button newButton;
	private Button editButton;
	private Button deleteButton;

	private PojoEditorControl editor;

	private boolean editMode;

	private IPojoEditorFieldRenderLogic fieldRenderLogic;

	/**
	 * @param container
	 * @param clazz
	 */
	public PojoEditorTable(IControlContainer container, String name, Class<?> rowClass, IPojoEditorFieldRenderLogic fieldRenderLogic) {
		super(container, name);
		editorModel = new PojoEditorTableModel(rowClass);
		this.fieldRenderLogic = fieldRenderLogic;
		createTable();
		createEditorButtons();
	}

	/**
	 * 
	 */
	private void createEditorButtons() {
		Button saveButton;
		Button cancelButton;

		saveButton = new Button(this, "saveButton");
		saveButton.setTitle("Save");
		saveButton.addSelectionListener(new SelectionListener() {

			@Override
			public void objectSelected(SelectionEvent event) {
				editor.save();
				editorModel.save();
				
				editMode = false;
				PojoEditorTable.this.requireRedraw();

			}
		});

		cancelButton = new Button(this, "cancelButton");
		cancelButton.setTitle("Cancel");
		cancelButton.addSelectionListener(new SelectionListener() {

			@Override
			public void objectSelected(SelectionEvent event) {
				editMode = false;
				editorModel.cancel();
				
				PojoEditorTable.this.requireRedraw();
			}
		});

	}

	/**
	 * 
	 */
	private void createTable() {
		createTableButtons();

		table = new TableViewer(this, "table");
		createTableColumns(editorModel.getRowClass());

		table.setShowStatusBar(false);
		table.setTableLabelProvider(new PojoTableLabelProvider());
		table.setScrollable(true);
		table.setResizeableColumns(true);
		table.setFillWidth(true);
		table.getModel().setSelectionMode(TableModel.SELECTION_SINGLE);
		table.getModel().addElementSelectedListener(new ElementSelectedListener() {

			@Override
			public void elementSelected(ElementSelectedEvent event) {
				toggleButtons();
				if (event.isDblClick()) {
					Object selectedElement = table.getModel().getContentProvider().getObjectFromKey(table.getModel().getFirstSelectedKey());
					createEditor(selectedElement, fieldRenderLogic);
					editMode = true;
				}
			}
		});
	}

	/**
	 * 
	 */
	private void createTableButtons() {

		ToolBar t = new ToolBar(this, "toolbar");
		ToolBarGroup tg = t.addGroup();

		newButton = tg.addButton();
		newButton.setTitle("New");
		newButton.setIconEnabled(ImageLibrary.ICON_NEW_ACTIVE);
		newButton.setIconDisabled(ImageLibrary.ICON_NEW_INACTIVE);

		newButton.addSelectionListener(new SelectionListener() {

			@Override
			public void objectSelected(SelectionEvent event) {
				createEditor(editorModel.makeNewRow(), fieldRenderLogic);
				editMode = true;
			}
		});

		editButton = tg.addButton();
		editButton.setTitle("Edit");
		editButton.setIconEnabled(ImageLibrary.ICON_EDIT_ACTIVE);
		editButton.setIconDisabled(ImageLibrary.ICON_EDIT_INACTIVE);
		editButton.setEnabled(false);

		editButton.addSelectionListener(new SelectionListener() {

			@Override
			public void objectSelected(SelectionEvent event) {
				Object selectedElement = table.getModel().getContentProvider().getObjectFromKey(table.getModel().getFirstSelectedKey());
				createEditor(selectedElement, fieldRenderLogic);
				editMode = true;
			}
		});

		deleteButton = tg.addButton();
		deleteButton.setTitle("Delete");
		deleteButton.setIconEnabled(ImageLibrary.ICON_DELETE_ACTIVE);
		deleteButton.setIconDisabled(ImageLibrary.ICON_DELETE_INACTIVE);
		deleteButton.setEnabled(false);

		deleteButton.addSelectionListener(new SelectionListener() {

			@Override
			public void objectSelected(SelectionEvent event) {
				Object selectedElement = table.getModel().getContentProvider().getObjectFromKey(table.getModel().getFirstSelectedKey());
				editorModel.remove(selectedElement);				
				table.requireRedraw();
				table.getModel().clearSelection();
				toggleButtons();
			}
		});

	}

	private void createEditor(Object pojo, IPojoEditorFieldRenderLogic fieldRenderLogic) {
		try {
			this.removeControl("editor");
			editor = PojoEditorFactory.createEditor(this, "editor", pojo, fieldRenderLogic);
			editor.load();
		} catch (Exception e) {
			log.error(e, e);
		}
	}

	/**
	 * @param table
	 * @param clazz
	 */
	protected void createTableColumns(Class<?> clazz) {
		List<Field> fields = PojoEditorFactory.getAllFields(clazz);
		for (Field f : fields) {
			if (f.isAnnotationPresent(PojoControl.class) && fieldRenderLogic.isRenderField(f.getName())) {
				PojoControl annotation = f.getAnnotation(PojoControl.class);
				TableColumn column = new TableColumn(annotation.label());
				column.setUserObject(f);
				table.getModel().addColumn(column);
			}
		}
	}

	/**
	 * @return the table
	 */
	public TableViewer getTable() {
		return table;
	}

	/**
	 * @return the editMode
	 */
	public boolean isEditMode() {
		return editMode;
	}

	/**
	 * @return the editor
	 */
	public PojoEditorControl getEditor() {
		return editor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.jwic.base.IHaveEnabled#isEnabled()
	 */
	@Override
	public boolean isEnabled() {
		return enabled;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.jwic.base.IHaveEnabled#setEnabled(boolean)
	 */
	@Override
	public void setEnabled(boolean enabled) {
		toggleButtons();
		this.enabled = enabled;

		if (enabled) {
			table.getModel().setSelectionMode(TableModel.SELECTION_SINGLE);
		} else {
			table.getModel().setSelectionMode(TableModel.SELECTION_NONE);
		}
	}

	/**
	 * 
	 */
	private void toggleButtons() {
		newButton.setEnabled(enabled);
		boolean rowSelected = table.getModel().getFirstSelectedKey() != null;
		editButton.setEnabled(rowSelected && enabled);
		deleteButton.setEnabled(rowSelected && enabled);
	}

	/**
	 * @param contents
	 */
	public void setData(List<?> data) {
		editorModel.setData(data);
		table.setContentProvider(new ListContentProvider(data));
	}

}
