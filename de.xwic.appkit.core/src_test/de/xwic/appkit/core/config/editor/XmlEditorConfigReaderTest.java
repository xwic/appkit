package de.xwic.appkit.core.config.editor;

import de.xwic.appkit.core.config.ParseException;
import de.xwic.appkit.core.config.model.EntityDescriptor;
import de.xwic.appkit.core.config.model.Model;
import org.junit.Test;

import java.beans.IntrospectionException;
import java.net.MalformedURLException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * @author <a href="mailto:vzhovtiuk@gmail.com">Vitaliy Zhovtyuk</a>
 */
public class XmlEditorConfigReaderTest {

    @Test
    public void shouldReadListViewFromConfig() throws IntrospectionException, ParseException, MalformedURLException {
        Model model = new Model();
        EntityDescriptor entityDescriptor = new EntityDescriptor(ICompany.class);
        model.addEntityDescriptor(entityDescriptor);
        EditorConfiguration xmlEditorConfigReader = XmlEditorConfigReader.readConfiguration(model, getClass().getClassLoader().getResource("company-editor.xml"));

        assertEquals(entityDescriptor, xmlEditorConfigReader.getEntityType());
        List<ESubTab> eSubTabs = xmlEditorConfigReader.getSubTabs();
        assertFalse(eSubTabs.isEmpty());
        ESubTab eSubTab = eSubTabs.get(0);
        assertEquals(1, eSubTab.getChilds().size());
        EListView listView = (EListView) eSubTab.getChilds().get(0);
        assertEquals("de.xwic.sandbox.demoapp.model.entities.IContact", listView.getType());
        assertEquals("customer", listView.getFilterOn());
        assertEquals("default", listView.getListProfile());
    }
}
