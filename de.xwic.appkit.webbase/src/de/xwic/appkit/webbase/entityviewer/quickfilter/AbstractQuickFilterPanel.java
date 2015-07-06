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
package de.xwic.appkit.webbase.entityviewer.quickfilter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import de.jwic.base.Control;
import de.jwic.base.ControlContainer;
import de.jwic.base.IControlContainer;
import de.jwic.controls.Button;
import de.jwic.controls.CheckBox;
import de.jwic.controls.InputBox;
import de.jwic.controls.ListBox;
import de.jwic.controls.combo.DropDown;
import de.jwic.events.ElementSelectedEvent;
import de.jwic.events.ElementSelectedListener;
import de.jwic.events.KeyEvent;
import de.jwic.events.KeyListener;
import de.jwic.events.SelectionEvent;
import de.jwic.events.SelectionListener;
import de.jwic.events.ValueChangedEvent;
import de.jwic.events.ValueChangedListener;
import de.xwic.appkit.core.model.queries.QueryElement;
import de.xwic.appkit.webbase.table.Column;
import de.xwic.appkit.webbase.table.EntityTableAdapter;
import de.xwic.appkit.webbase.table.EntityTableEvent;
import de.xwic.appkit.webbase.table.EntityTableModel;

/**
 * @author Adrian Ionescu
 */
public abstract class AbstractQuickFilterPanel extends ControlContainer {

	protected EntityTableModel tableModel;
	protected Button btnSearch;
	
	protected List<IQuickFilterControl> controls;
	
	private boolean notifyTheModel = true;
	private boolean notifyTheControl = true;
	
	private ElementSelectedListener elementSelectedListener;
	private KeyListener keyListener;
	private ValueChangedListener valueChangedListener;
	
	/**
	 * @param container
	 * @param name
	 * @param tableModel
	 */
	protected AbstractQuickFilterPanel(IControlContainer container, EntityTableModel tableModel) {
		super(container);
		
		this.tableModel = tableModel;		
		controls = new ArrayList<IQuickFilterControl>();
		
		tableModel.addEntityTableListener(new EntityTableAdapter() {
			@Override
			public void columnFiltered(EntityTableEvent event) {
				modelFiltersChanged(event.getColumn());
			}
		});
		
		createQuickFilterControls();
		
		// after the controls are created, add them to our local collection and
		// also listeners to them. We could've done this by overridding the
		// registerControl method, but that might have some timing issues with
		// all the super() constructor calls, so it's safer this way
		for (Iterator<Control> it = getControls(); it.hasNext();) {
			Control control = it.next();
			if (control instanceof IQuickFilterControl) {
				controls.add((IQuickFilterControl) control);
				
				if (control instanceof DropDown) {
					DropDown dd = (DropDown) control;
					// if the dd is multi-select it's not ok to fetch the data
					// for each checkbox that is clicked, so we leave that to
					// the search button 
					if (!dd.isMultiSelect()) {
						dd.addElementSelectedListener(getElementSelectedListener());
					}
				} else if (control instanceof ListBox) {
					((ListBox) control).addElementSelectedListener(getElementSelectedListener());
				} else if (control instanceof InputBox) {
					((InputBox) control).addKeyListener(getKeyListener());
				} else if (control instanceof CheckBox) {
					((CheckBox) control).addValueChangedListener(getValueChangedListener());
				}
			}
		}
		
		btnSearch = new Button(this, "btnSearch");
		btnSearch.setTitle("Search");
		btnSearch.addSelectionListener(new SelectionListener() {
			@Override
			public void objectSelected(SelectionEvent event) {
				applyQuickFilters();
			}
		});
	}
	
	/**
	 * 
	 */
	protected abstract void createQuickFilterControls();
	
	/**
	 * @param column 
	 */
	private void modelFiltersChanged(Column column) {
		notifyTheModel = false;
		if (notifyTheControl) {
			columnFilterChanged(column);
		}
		notifyTheModel = true;
	}
	
	/**
	 * 
	 */
	protected void applyQuickFilters() {
		if (notifyTheModel) {
			notifyTheControl = false;
			tableModel.applyQuickFilters(getFiltersMap());
			notifyTheControl = true;
		}
	}
	
	/**
	 * @param column
	 */
	protected void columnFilterChanged(Column column) {
		for (IQuickFilterControl control : controls) {
			control.columnFilterChanged(column);
		}
	}
	
	/**
	 * Returns a map of columns ids and their filters
	 * @return
	 */
	protected Map<String, QueryElement> getFiltersMap() {
		Map<String, QueryElement> filtersMap = new HashMap<String, QueryElement>();
		
		for (IQuickFilterControl qfc : controls) {
			filtersMap.put(qfc.getPropertyId(), qfc.getQueryElement());
		}
		
		return filtersMap;
	}
	
	/**
	 * The usual height for quick filter panels on one row is 32.
	 * Override if needed.
	 * @return
	 */
	public int getPrefferedHeight() {
		return 32;
	}

	/**
	 * @return the elementSelectedListener
	 */
	public ElementSelectedListener getElementSelectedListener() {
		if (elementSelectedListener == null) {
			elementSelectedListener = new ElementSelectedListener() {
				@Override
				public void elementSelected(ElementSelectedEvent event) {
					applyQuickFilters();
				}
			};
		}
		
		return elementSelectedListener;
	}

	/**
	 * @return the keyListener
	 */
	public KeyListener getKeyListener() {
		if (keyListener == null) {
			keyListener = new KeyListener() {
				@Override
				public void keyPressed(KeyEvent event) {
					applyQuickFilters();				
				}
			};
		}
		
		return keyListener;
	}

	/**
	 * @return the valueChangedListener
	 */
	public ValueChangedListener getValueChangedListener() {
		if (valueChangedListener == null) {
			valueChangedListener = new ValueChangedListener() {
				@Override
				public void valueChanged(ValueChangedEvent event) {
					applyQuickFilters();
				}
			};
		}
		
		return valueChangedListener;
	}
}
