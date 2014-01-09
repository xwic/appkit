/**
 * 
 */
package de.xwic.appkit.core.transport.xml;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.dom4j.Element;

import de.xwic.appkit.core.config.model.EntityDescriptor;
import de.xwic.appkit.core.config.model.Property;
import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.dao.EntityKey;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.export.XmlExport;
import de.xwic.appkit.core.model.entities.IPicklistEntry;
import de.xwic.appkit.core.transfer.EntityTransferObject;
import de.xwic.appkit.core.transfer.PropertyValue;


/**
 * @author Adrian Ionescu
 */
public class EtoEntityNodeParser implements IEntityNodeParser {

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.transport.xml.IEntityNodeParser#parseElement(org.dom4j.Element, java.util.Map, de.xwic.appkit.core.config.model.EntityDescriptor, de.xwic.appkit.core.transport.xml.XmlBeanSerializer)
	 */
	@Override
	public Object parseElement(Element elmEntity, Map<EntityKey, Integer> context, Class entityClass, EntityDescriptor descr, XmlBeanSerializer xmlBeanSerializer) throws TransportException {
		String sId = elmEntity.attributeValue("id");
		String sVersion = elmEntity.attributeValue("version");
		if (sId == null || sId.length() == 0 || sVersion == null || sVersion.length() == 0) {
			throw new TransportException("Missing id and version.");
		}
		
		Class etoClass = entityClass;
		
		String sType = elmEntity.attributeValue("type");
		if (etoClass != null && !etoClass.getName().equals(sType) && sType != null && sType.length() != 0) {
			try {
				etoClass = DAOSystem.getModelClassLoader().loadClass(sType);
			} catch (ClassNotFoundException e) {
				throw new TransportException("Wront type specified" + sType, e);
			}
		}
		
		EntityTransferObject eto = new EntityTransferObject(sId, sVersion, etoClass);
		eto.setModified(false);
		Map<String, PropertyValue> values = new HashMap<String, PropertyValue>();
		eto.setPropertyValues(values);
		
		// add deleted property value
		PropertyValue pvDeleted = new PropertyValue();
		pvDeleted.setValue(XmlBeanSerializer.ATTRVALUE_TRUE.equals(elmEntity.attributeValue("deleted")));
		pvDeleted.setType(boolean.class);
		values.put("deleted", pvDeleted);
		
		for (Iterator<Element> itP = elmEntity.elementIterator(); itP.hasNext(); ) {
			Element elmProp = itP.next();
			Property prop = descr.getProperty(elmProp.getName());
			if (prop != null) {
				
				
				Class<?> propertyType = prop.getDescriptor().getPropertyType();
				boolean isEntityRef = IEntity.class.isAssignableFrom(propertyType);
				boolean isPicklistRef = IPicklistEntry.class.isAssignableFrom(propertyType);
				boolean isNull = elmProp.element(XmlExport.ELM_NULL) != null || XmlBeanSerializer.ATTRVALUE_TRUE.equals(elmProp.attributeValue("null"));
				
				PropertyValue pv = new PropertyValue();
				pv.setType(propertyType);
				if (isNull) {
					pv.setValue(null);
					pv.setLoaded(true);
				} else {
					if ((isEntityRef && !isPicklistRef)) {
						String id = elmProp.attributeValue("id");
						if (id != null && !id.isEmpty()) {
							int refId = Integer.parseInt(id);
							pv.setEntityId(refId);
							pv.setLoaded(false);
						} else {
							pv.setLoaded(true);
							
						}
					} else if (prop.isCollection() && XmlBeanSerializer.ATTRVALUE_TRUE.equals(elmProp.attributeValue("lazy"))) {
						pv.setLoaded(false);
					} else {
						pv.setValue(xmlBeanSerializer.readValue(context, elmProp, prop.getDescriptor()));
					}
					
					if (XmlBeanSerializer.ATTRVALUE_TRUE.equals(elmProp.attributeValue("modified"))) {
						pv.setModified(true);
					}
					
					if (XmlBeanSerializer.ATTRVALUE_TRUE.equals(elmProp.attributeValue("entityType"))) {
						pv.setEntityType(true);
					}
				}
				
				values.put(elmProp.getName(), pv);
			}
		}
		
		return eto;
	}

}
