/**
 *
 */
package de.xwic.appkit.webbase.utils.picklist;

import de.xwic.appkit.core.model.entities.IPicklistEntry;
import de.xwic.appkit.core.util.Function;

/**
 * @author Alexandru Bledea
 * @since Oct 3, 2014
 */
public final class Picklists {

    /**
     *
     */
    private Picklists() {
    }

    public static final Function<IPicklistEntry, String> GET_KEY = new PicklistKeyExtractor();

    /**
     * @author Alexandru Bledea
     * @since Oct 3, 2014
     */
    private static class PicklistKeyExtractor implements Function<IPicklistEntry, String> {

        /* (non-Javadoc)
         * @see de.xwic.appkit.core.util.Function#evaluate(java.lang.Object)
         */
        @Override
        public String evaluate(final IPicklistEntry obj) {
            return obj.getKey();
        }

    }

}
