package de.xwic.appkit.core.config.editor;

import de.xwic.appkit.core.config.ParseException;
import de.xwic.appkit.core.config.model.EntityDescriptor;
import de.xwic.appkit.core.config.model.Model;
import org.junit.Test;

import java.beans.IntrospectionException;
import java.net.MalformedURLException;

import static org.junit.Assert.assertEquals;

/**
 * @author <a href="mailto:vzhovtiuk@gmail.com">Vitaliy Zhovtyuk</a>
 */
public class XmlEditorConfigReaderTest {

    @Test
    public void shouldReadSimpleConfig() throws IntrospectionException, ParseException, MalformedURLException {
        Model model = new Model();
        EntityDescriptor entityDescriptor = new EntityDescriptor(ICompany.class);
        model.addEntityDescriptor(entityDescriptor);
        EditorConfiguration xmlEditorConfigReader = XmlEditorConfigReader.readConfiguration(model, getClass().getClassLoader().getResource("company-editor.xml"));

        assertEquals(entityDescriptor, xmlEditorConfigReader.getEntityType());
    }
}
