/*******************************************************************************
 * Copyright 2015 xWic group (http://www.xwic.de)
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License").
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package de.xwic.appkit.webbase.editors.builders;

import de.jwic.base.IControl;
import de.jwic.base.IControlContainer;
import de.jwic.controls.Label;
import de.xwic.appkit.core.config.ConfigurationException;
import de.xwic.appkit.core.config.ConfigurationManager;
import de.xwic.appkit.core.config.editor.EEntityField;
import de.xwic.appkit.core.config.editor.EGroup;
import de.xwic.appkit.core.config.editor.EPicklistCombo;
import de.xwic.appkit.core.config.editor.Style;
import de.xwic.appkit.core.config.model.EntityDescriptor;
import de.xwic.appkit.core.config.model.Property;
import de.xwic.appkit.core.dao.EntityQuery;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.webbase.editors.FieldChangeListener;
import de.xwic.appkit.webbase.editors.IBuilderContext;
import de.xwic.appkit.webbase.editors.mappers.EntitySelectorMapper;
import de.xwic.appkit.webbase.editors.mappers.PicklistEntryMapper;
import de.xwic.appkit.webbase.editors.mappers.PicklistEntrySetMapper;
import de.xwic.appkit.webbase.entityselection.EntityComboSelector;
import de.xwic.appkit.webbase.entityselection.EntitySelectionAdapter;
import de.xwic.appkit.webbase.entityselection.EntitySelectionModel;
import de.xwic.appkit.webbase.entityselection.GenericEntitySelectionContributor;
import de.xwic.appkit.webbase.entityview.EntityDisplayListModel;
import de.xwic.appkit.webbase.entityviewer.quickfilter.AbstractQuickFilterPanel;
import de.xwic.appkit.webbase.table.EntityTableModel;
import de.xwic.appkit.webbase.utils.picklist.PicklistEntryControl;

import java.beans.PropertyDescriptor;

/**
 * The Builder for the EEntityField.
 *
 * @author <a href="mailto:vzhovtiuk@gmail.com">Vitaliy Zhovtyuk</a>
 */
public class EEntitySelectorBuilder extends Builder<EEntityField> {

    /*
     * (non-Javadoc)
     *
     * @see de.xwic.appkit.webbase.editors.builders.Builder#buildComponents(de.xwic.appkit.core.config.editor.UIElement,
     *      de.jwic.base.IControlContainer,
     *      de.xwic.appkit.webbase.editors.IBuilderContext)
     */
    public IControl buildComponents(EEntityField entityField, IControlContainer parent, IBuilderContext context) {

        final PropertyDescriptor refPropDescriptor = entityField.getFinalProperty().getDescriptor();
        Class<? extends IEntity> entityType = (Class<? extends IEntity>) refPropDescriptor.getPropertyType();
        
        String[] queryProp = new String[0];
        
        try {
	        EntityDescriptor refEntityDescr = ConfigurationManager.getSetup().getEntityDescriptor(entityType.getName());
	        if (refEntityDescr.getDefaultDisplayProperty() != null) {
	        	queryProp = new String[] { refEntityDescr.getDefaultDisplayProperty() };
	        }
        
        } catch (ConfigurationException ce) {
        	log.error("Error resolving referenced entity type", ce);
        }

        GenericEntitySelectionContributor selectionContributor = new GenericEntitySelectionContributor(entityType,
                entityType.getSimpleName() + " Selection", "Please select " + entityType.getSimpleName(), queryProp);
        
        
        final EntityComboSelector<IEntity> comboSelector = new EntityComboSelector<IEntity>(parent, null, selectionContributor);
        comboSelector.addValueChangedListener(new FieldChangeListener(context, entityField.getProperty()));
        context.registerField(entityField.getProperty(), comboSelector, entityField, EntitySelectorMapper.MAPPER_ID);
        
		Style style = entityField.getStyle();
		if (style.isStyleSpecified(Style.WIDTH_HINT)) {
			comboSelector.setWidth(style.getStyleInt(Style.WIDTH_HINT));
		}

        
        return comboSelector;
    }

}
