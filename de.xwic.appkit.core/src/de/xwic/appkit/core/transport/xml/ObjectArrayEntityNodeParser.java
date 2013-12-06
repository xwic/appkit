/**
 * 
 */
package de.xwic.appkit.core.transport.xml;

import java.util.Iterator;
import java.util.Map;

import org.dom4j.Element;

import de.xwic.appkit.core.config.model.EntityDescriptor;
import de.xwic.appkit.core.dao.EntityKey;


/**
 * @author Adrian Ionescu
 */
public class ObjectArrayEntityNodeParser implements IEntityNodeParser {

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.transport.xml.IEntityNodeParser#parseElement(org.dom4j.Element, java.util.Map, de.xwic.appkit.core.config.model.EntityDescriptor, de.xwic.appkit.core.transport.xml.XmlBeanSerializer)
	 */
	@Override
	public Object parseElement(Element elmEntity, Map<EntityKey, Integer> context, Class entityClass, EntityDescriptor descr, XmlBeanSerializer xmlBeanSerializer) throws TransportException {
		
		Object[] result = new Object[elmEntity.elements().size()];
		int i = 0;
		
		for (Iterator<Element> it = elmEntity.elementIterator() ; it.hasNext() ; ) {
			Element elm = it.next();
			result[i++] = xmlBeanSerializer.readValue(null, elm, null);
		}
		
		return result;
	}

}
