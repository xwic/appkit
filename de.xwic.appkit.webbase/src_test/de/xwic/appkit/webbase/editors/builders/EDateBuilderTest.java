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

import de.jwic.base.*;
import de.jwic.controls.DatePicker;
import de.jwic.controls.DateTimePicker;
import de.xwic.appkit.core.config.editor.EDate;
import de.xwic.appkit.core.config.model.Property;
import de.xwic.appkit.webbase.editors.IBuilderContext;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Locale;
import java.util.TimeZone;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test for EDate usages.
 *
 * @author Aron Cotrau
 */
public class EDateBuilderTest {

    private ControlContainer controlContainer;

    @Before
    public void setUp() throws Exception {
        final SessionContext sessionContext = Mockito.mock(SessionContext.class);
        Mockito.when(sessionContext.getLocale()).thenReturn(new Locale("en", "EN"));
        Mockito.when(sessionContext.getTimeZone()).thenReturn(TimeZone.getDefault());
        final ControlContainer spy = Mockito.spy(new ControlContainer(sessionContext));

        Mockito.when(spy.getSessionContext()).thenReturn(sessionContext);
        controlContainer = spy;
    }

    @Test
    public void shouldBuildDatePicker() {
        // given
        IBuilderContext context = Mockito.mock(IBuilderContext.class);

        final EDate text = new EDate();
        text.setReadonly(true);
        final EDateBuilder builder = new EDateBuilder();
        // when
        final IControl control = builder.buildComponents(text, controlContainer, context);
        // then

        assertTrue("Should be date picker instance", control instanceof DatePicker);
        final DatePicker datePicker = (DatePicker) control;
        assertTrue("Should be readonly" , datePicker.isReadonly());

    }

    @Test
    public void shouldBuildBothDateTimePicker() {
        // given
        IBuilderContext context = Mockito.mock(IBuilderContext.class);

        final EDate text = new EDate();
        Property property = new Property();
        property.setDateType(Property.DATETYPE_DATETIME);
        text.setProperty(new Property[]{property});
        final EDateBuilder builder = new EDateBuilder();
        // when
        final IControl control = builder.buildComponents(text, controlContainer, context);

        // then
        assertTrue("Should be date picker instance", control instanceof DateTimePicker);
        final DateTimePicker datePicker = (DateTimePicker) control;
        assertFalse("Should show date and time ", datePicker.getTimeOnly());
    }

    @Test
    public void shouldBuildTimePicker() {
        // given
        IBuilderContext context = Mockito.mock(IBuilderContext.class);

        final EDate text = new EDate();
        Property property = new Property();
        property.setDateType(Property.DATETYPE_TIME);
        text.setProperty(new Property[]{property});
        final EDateBuilder builder = new EDateBuilder();
        // when
        final IControl control = builder.buildComponents(text, controlContainer, context);
        // then

        assertTrue("Should be date picker instance", control instanceof DateTimePicker);
        final DateTimePicker datePicker = (DateTimePicker) control;
        assertTrue("Should show time only", datePicker.getTimeOnly());
    }

    @Test
    public void shouldBuildDateOnlyPicker() {
        // given
        IBuilderContext context = Mockito.mock(IBuilderContext.class);

        final EDate text = new EDate();
        Property property = new Property();
        property.setDateType(Property.DATETYPE_DATE);
        text.setProperty(new Property[]{property});
        final EDateBuilder builder = new EDateBuilder();
        // when
        final IControl control = builder.buildComponents(text, controlContainer, context);
        // then

        assertTrue("Should be date picker instance", control instanceof DatePicker);
    }

    @Test
    public void shouldBuildTimeOverridePicker() {
        // given
        IBuilderContext context = Mockito.mock(IBuilderContext.class);

        final EDate text = new EDate();
        Property property = new Property();
        property.setDateType(Property.DATETYPE_DATE);
        text.setProperty(new Property[]{property});
        text.setMode(EDate.TIME_ONLY);
        final EDateBuilder builder = new EDateBuilder();
        // when
        final IControl control = builder.buildComponents(text, controlContainer, context);
        // then

        assertTrue("Should override date and be time only picker instance", control instanceof DateTimePicker);
    }
}
