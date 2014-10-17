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
