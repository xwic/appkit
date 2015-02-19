package de.xwic.appkit.core.dao;

import de.xwic.appkit.core.transport.xml.ICustomObjectSerializer;
import de.xwic.appkit.core.transport.xml.TransportException;
import de.xwic.appkit.core.transport.xml.XmlBeanSerializer;
import org.dom4j.Element;

import java.util.List;
import java.util.Map;

/**
 * @author Razvan Pat on 2/18/2015.
 */
public class EntityListSerializer implements ICustomObjectSerializer {

	@Override
	public boolean handlesObject(Object object) {
		return object instanceof EntityList;
	}

	@Override
	public boolean handlesType(Class<?> clazz) {
		return EntityList.class.isAssignableFrom(clazz);
	}

	@Override
	public void serialize(XmlBeanSerializer serializer, Element elm, Object object) throws TransportException {
		elm.addAttribute("type", EntityList.class.getName());

		EntityList<?> eList = (EntityList<?>) object;

		Element elmList = elm.addElement("entityList");

		serializer.addValue(elmList, eList.getList(), true);

		boolean hasLimit = eList.getLimit() != null;
		elmList.addAttribute("has_limit", String.valueOf(hasLimit));

		if(hasLimit) {
			elmList.addAttribute("limit_start", String.valueOf(eList.getLimit().startNo));
			elmList.addAttribute("limit_max", String.valueOf(eList.getLimit().maxResults));
		}

		elmList.addAttribute("count", String.valueOf(eList.getTotalSize()));
	}

	@Override
	public Object deserialize(XmlBeanSerializer serializer, Map<EntityKey, Integer> context, Element elm) throws TransportException {
		@SuppressWarnings("unchecked")
		//TODO Razvan: this doesn't quite work yet, needs fixing!
		List<Object> list = (List<Object>) serializer.readValue(context, elm.element(XmlBeanSerializer.ELM_LIST), null);

		boolean hasLimit = Boolean.parseBoolean(elm.attribute("has_limit").getValue());

		Limit limit = null;
		if(hasLimit) {
			int limit_start = Integer.parseInt(elm.attribute("limit_start").getValue());
			int limit_max = Integer.parseInt(elm.attribute("limit_max").getValue());
			limit = new Limit(limit_start, limit_max);
		}
		int count = Integer.parseInt(elm.attribute("count").getValue());

		return new EntityList<Object>(list, limit, count);
	}

}
