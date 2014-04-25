/**
 * 
 */
package de.xwic.appkit.core.transport.xml;

import java.util.Map;

import org.dom4j.Element;

import de.xwic.appkit.core.config.model.EntityDescriptor;
import de.xwic.appkit.core.dao.EntityKey;


/**
 * @author Adrian Ionescu
 */
public interface IEntityNodeParser {

	/**
	 * @param elmEntity
	 * @param context
	 * @param descr
	 * @param xmlBeanSerializer
	 * @return
	 * @throws TransportException
	 */
	public Object parseElement(Element elmEntity, Map<EntityKey, Integer> context, Class entityClass, EntityDescriptor descr, XmlBeanSerializer xmlBeanSerializer) throws TransportException; 
	
}
