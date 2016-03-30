package de.xwic.appkit.webbase.editors;

import de.xwic.appkit.core.registry.ExtensionRegistry;
import de.xwic.appkit.core.registry.IExtension;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:vzhovtiuk@gmail.com">Vitaliy Zhovtyuk</a>
 */
public class EditorExtensionUtils {
    private final static Log log = LogFactory.getLog(EditorExtensionUtils.class);

    public final static String EXT_ATTR_FOR_ENTITY = "forEntity";

    /**
     * Get extensions filtered by entity type.
     *
     * @param extPoint   extension point name
     * @param entityType type of entity
     * @return list of extensions
     */
    public static <T> List<T> getExtensions(String extPoint, String entityType) {
        List<T> extension = new ArrayList<T>();
        List<IExtension> rawExtList = ExtensionRegistry.getInstance().getExtensions(extPoint);
        for (IExtension ext : rawExtList) {

            String applyToEntity = ext.getAttribute(EXT_ATTR_FOR_ENTITY);
            if (applyToEntity != null && entityType.equals(applyToEntity)) {

                T extObj = null;
                try {
                    extObj = (T) ext.createExtensionObject();
                } catch (Exception e) {
                    log.error("Error creating EntityEditorExtension for " + entityType + " (id:" + ext.getId() + ")", e);
                    // extObj will be null, so we can continue
                }

                if (extObj != null) {
                    extension.add(extObj);
                }
            }

        }
        return extension;
    }
}
