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
package de.xwic.appkit.core.model.entities.util;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import de.xwic.appkit.core.model.entities.IMitarbeiter;
import de.xwic.appkit.core.model.entities.impl.Mitarbeiter;

/**
 * @author Alexandru Bledea
 * @since Oct 7, 2014
 */
@SuppressWarnings ("static-method")
public class TestMitarbeiters {

    private static final String separator = ";";

    /**
     * @throws Exception
     */
    @Test
    public void testGetNames() throws Exception {
        final IMitarbeiter mit1 = createMitarbeiter("Nach", "Vor");
        final String expectedMit1Name = "Nach, Vor";

        assertEquals(expectedMit1Name, Mitarbeiters.getName(mit1));

        final IMitarbeiter mit2 = createMitarbeiter("Nach", null);
        final String expectedMit2Name = "Nach";
        assertEquals(expectedMit2Name, Mitarbeiters.getName(mit2));

        final IMitarbeiter mit3 = createMitarbeiter(null, null);
        final String expectedMit3Name = "";
        assertEquals(expectedMit3Name, Mitarbeiters.getName(mit3));

        final String expectedMit4Name = "Vor";
        final IMitarbeiter mit4 = createMitarbeiter(null, "Vor");
        assertEquals(expectedMit4Name, Mitarbeiters.getName(mit4));

        final List<IMitarbeiter> mitarbeiters = Arrays.asList(mit1, mit2, mit3, null, mit4);
        final String expectedNames = expectedMit1Name + separator + expectedMit2Name + separator + expectedMit3Name + separator
                + expectedMit4Name;
        assertEquals(expectedNames, Mitarbeiters.getNames(mitarbeiters, separator, ""));

        assertEquals("No Managers", Mitarbeiters.getNames(Collections.<IMitarbeiter> emptyList(), null, "No Managers"));
        assertEquals("", Mitarbeiters.getName(null));
    }

    /**
     * @param nachname
     * @param vorname
     * @return
     */
    private static IMitarbeiter createMitarbeiter(final String nachname, final String vorname) {
        final IMitarbeiter mitarbeiter = new Mitarbeiter();
        mitarbeiter.setVorname(vorname);
        mitarbeiter.setNachname(nachname);
        return mitarbeiter;
    }
}
