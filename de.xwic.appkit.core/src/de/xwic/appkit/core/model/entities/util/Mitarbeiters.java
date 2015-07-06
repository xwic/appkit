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

import java.util.Collection;
import java.util.Collections;

import de.xwic.appkit.core.model.entities.IMitarbeiter;
import de.xwic.appkit.core.util.CollectionUtil;
import de.xwic.appkit.core.util.Function;

/**
 * @author Alexandru Bledea
 * @since Oct 7, 2014
 */
public final class Mitarbeiters {

    /**
     *
     */
    private Mitarbeiters() {
    }

    public static final Function<IMitarbeiter, String> GET_NAME = new GetMitarbeiterName();

    /**
     * @param beiter
     * @return
     */
    public static String getName(final IMitarbeiter beiter) {
        return getNames(Collections.singleton(beiter), null, "");
    }

    /**
     * @param beiters
     * @param separator
     * @param emptyMessage
     * @return
     */
    public static String getNames(final Collection<IMitarbeiter> beiters, final String separator, final String emptyMessage) {
        return CollectionUtil.join(beiters, GET_NAME, separator, emptyMessage);
    }

    /**
     * @author Alexandru Bledea
     * @since Oct 7, 2014
     */
    private static class GetMitarbeiterName implements Function<IMitarbeiter, String> {

        /* (non-Javadoc)
         * @see de.xwic.appkit.core.util.Function#evaluate(java.lang.Object)
         */
        @Override
        public String evaluate(final IMitarbeiter obj) {
            final StringBuilder sb = new StringBuilder();

            final String nachname = obj.getNachname();
            final String vorname = obj.getVorname();

            final boolean hasNachname = null != nachname;
            final boolean hasVorname = null != vorname;

            if (hasNachname) {
                sb.append(nachname);
                if (hasVorname) {
                    sb.append(", ");
                }
            }
            if (hasVorname) {
                sb.append(vorname);
            }
            return sb.toString();
        }
    }

}
