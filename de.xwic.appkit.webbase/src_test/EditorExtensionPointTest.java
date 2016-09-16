import de.xwic.appkit.core.registry.ExtensionRegistry;
import de.xwic.appkit.core.registry.IExtension;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertFalse;

/**
 * @author <a href="mailto:vzhovtiuk@gmail.com">Vitaliy Zhovtyuk</a>
 */
public class EditorExtensionPointTest {
    private static final String EP_ENTITY_EDITOR = "entityEditors";

    @Test
    public void shouldParseExtensionsResource() {
        List<IExtension> tabExtensions = ExtensionRegistry.getInstance().getExtensions(EP_ENTITY_EDITOR);
        assertFalse("Extension point " + EP_ENTITY_EDITOR + " should be defined", tabExtensions.isEmpty());
    }
}
