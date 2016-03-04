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
import de.jwic.controls.DatePicker;
import de.jwic.controls.DateTimePicker;
import de.xwic.appkit.core.config.editor.EDate;
import de.xwic.appkit.core.config.model.Property;
import de.xwic.appkit.webbase.editors.IBuilderContext;
import de.xwic.appkit.webbase.editors.mappers.DatePickerMapper;
import de.xwic.appkit.webbase.editors.mappers.InputboxMapper;
import org.apache.commons.lang.StringUtils;

/**
 * Defines the InputBox builder class.
 *
 * @author Aron Cotrau
 */
public class EDateBuilder extends Builder<EDate> {
    /*
             * (non-Javadoc)
             *
             * @see de.xwic.appkit.webbase.editors.builders.Builder#buildComponents(de.xwic.appkit.core.config.editor.UIElement,
             *      de.jwic.base.IControlContainer,
             *      de.xwic.appkit.webbase.editors.IBuilderContext)
             */
    public IControl buildComponents(EDate eDate, IControlContainer parent, IBuilderContext context) {
        DatePicker datePicker = null;

        Property property = eDate.getFinalProperty();
        if (property != null) {
            String mode = eDate.getMode();
            if (StringUtils.isNotEmpty(mode)) {
                if (EDate.TIME_ONLY.equals(mode)) {
                    datePicker = buildTimePicker(parent, eDate);
                } else if (EDate.DATETIME.equals(mode)) {
                    datePicker = buildDateTimePicker(parent, eDate);
                }
            } else {
                switch (property.getDateType()) {
                    case Property.DATETYPE_DATE: {
                        datePicker = new DatePicker(parent);
                        break;
                    }
                    case Property.DATETYPE_DATETIME: {
                        datePicker = buildDateTimePicker(parent, eDate);
                        break;
                    }
                    case Property.DATETYPE_TIME: {
                        datePicker = buildTimePicker(parent, eDate);
                        break;
                    }
                    default: {
                        throw new RuntimeException("Unsupported date type " + property.getDateType());
                    }
                }
            }
        }
        if (datePicker == null) {
            datePicker = new DatePicker(parent);
        }
        datePicker.setReadonly(eDate.isReadonly());
        context.registerField(eDate.getProperty(), datePicker, eDate, DatePickerMapper.MAPPER_ID);
        return datePicker;
    }

    private DateTimePicker buildTimePicker(IControlContainer parent, EDate eDate) {
        DateTimePicker dateTimePicker = new DateTimePicker(parent);
        dateTimePicker.setTimeOnly(true);
        dateTimePicker.setShowSecond(eDate.getSeconds());
        return dateTimePicker;
    }

    private DateTimePicker buildDateTimePicker(IControlContainer parent, EDate eDate) {
        DateTimePicker dateTimePicker = new DateTimePicker(parent);
        dateTimePicker.setTimeOnly(false);
        dateTimePicker.setShowSecond(eDate.getSeconds());
        return dateTimePicker;
    }
}
