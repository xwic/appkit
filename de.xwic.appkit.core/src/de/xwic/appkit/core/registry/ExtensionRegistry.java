/**
 * 
 */
package de.xwic.appkit.core.registry;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import de.xwic.appkit.core.registry.impl.DefaultExtension;
import de.xwic.appkit.core.registry.impl.DefaultExtensionPoint;

/**
 * @author Adrian Ionescu
 */
public class ExtensionRegistry {
	
	private final static Log log = LogFactory.getLog(ExtensionRegistry.class);

	private static ExtensionRegistry instance;
	
	private Map<String, IExtensionPoint> extensionPoints;
	private Map<String, Map<String, List<IExtension>>> extensions; //<extensionPointId, <extensionId, List<IExtension>>>
	
	private ExtensionSortIndexComparator sortIndexComparator = new ExtensionSortIndexComparator();
	private ExtensionPriorityComparator priorityComparator = new ExtensionPriorityComparator();
	
	/**
	 * 
	 */
	private ExtensionRegistry() {
		extensionPoints = new HashMap<String, IExtensionPoint>();
		extensions = new HashMap<String, Map<String, List<IExtension>>>();
	
		// load extensions from resources
		try {
			for (Enumeration<URL> exts = getClass().getClassLoader().getResources("extensions.xml"); exts.hasMoreElements();) {
				URL url = exts.nextElement();
				try {
					processResourceFile(url);
				} catch (Exception e) {
					log.error("Error processing resource file " + url.getPath(), e);
				}
			}
			
		} catch (IOException e) {
			log.error("Error loading extension resources", e);
		}
	}
	
	/**
	 * @return
	 */
	public static ExtensionRegistry getInstance() {
		if(instance == null) {
			instance = new ExtensionRegistry();
		}
		
		return instance;
	}
	
	/**
	 * @param file
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private void processResourceFile(URL fileUrl) throws Exception {
		SAXReader reader = new SAXReader();
		Document doc = reader.read(fileUrl);

		Element root = doc.getRootElement();

		// process extension points
		Element elExtensionPoints = root.element("extensionPoints");
		if (elExtensionPoints != null) {
			for (Iterator<Element> it = elExtensionPoints.elementIterator("extensionPoint"); it.hasNext();) {
				Element elExtensionPoint = it.next();
				String id = elExtensionPoint.attributeValue("id");
				if (id != null && !id.isEmpty()) {
					if (!extensionPoints.containsKey(id)) {
						registerExtensionPoint(id);
					}
				} else {
					log.warn("File '" + fileUrl.getPath() + "' has an invalid format: no id specified for extension point");
				}
			}
		}

		// process extensions
		Element elExtensions = root.element("extensions");
		if(elExtensions != null) {
			for (Iterator<Element> it = elExtensions.elementIterator("extension"); it.hasNext();) {
				Element elExtension = it.next();
	
				String extensionPointId = elExtension.attributeValue("extensionPointId");
				String id = elExtension.attributeValue("id");
				int priority = 0;
				try {
					String strPriority = elExtension.attributeValue("priority");
					if (strPriority != null && !strPriority.trim().isEmpty()) {
						priority = Integer.parseInt(strPriority);
					}
				} catch (Exception ex) {
					log.warn("File '" + fileUrl.getPath() + "' has an invalid format: priority has a non-numeric value");
				}
				int sortIndex = 0;
				try {
					String strSortIndex = elExtension.attributeValue("sortIndex");
					if (strSortIndex != null && !strSortIndex.trim().isEmpty()) {
						sortIndex = Integer.parseInt(strSortIndex);
					}
				} catch (Exception ex) {
					log.warn("File '" + fileUrl.getPath() + "' has an invalid format: priority has a non-numeric value");
				}
				String className = elExtension.attributeValue("className");
	
				if (extensionPointId == null || extensionPointId.isEmpty()) {
					log.warn("File '" + fileUrl.getPath() + "' has an invalid format: no extensionPointId specified for extension");
					continue;
				}
				if (id == null || id.isEmpty()) {
					log.warn("File '" + fileUrl.getPath() + "' has an invalid format: no id specified for extension");
					continue;
				}
				if (className == null || className.isEmpty()) {
					log.warn("File '" + fileUrl.getPath() + "' has an invalid format: no className specified for extension");
					continue;
				}
	
				DefaultExtension newExtension = new DefaultExtension(extensionPointId, id, priority, className, sortIndex);
	
				for (Iterator<Element> itAttr = elExtension.elementIterator("attribute"); itAttr.hasNext();) {
					Element attrElem = itAttr.next();
	
					String key = attrElem.attributeValue("key");
					String value = attrElem.getTextTrim();
	
					if (key == null || key.isEmpty()) {
						log.warn("File '" + fileUrl.getPath() + "' has an invalid format: no key specified for attribute");
						continue;
					}
	
					newExtension.addAttribute(key, value);
				}
				
				registerExtension(newExtension);
			}
		}
	}
	
	/**
	 * @param id
	 */
	public void registerExtensionPoint(String id) {
		registerExtensionPoint(new DefaultExtensionPoint(id));
	}
	
	/**
	 * @param extensionPoint
	 */
	public void registerExtensionPoint(IExtensionPoint extensionPoint) {
		if (extensionPoints.containsKey(extensionPoint.getId())) {
			log.warn(String.format("The ExtensionPoint with id '%s' has been overridden by class '%s'",
					extensionPoint.getId(), extensionPoint.getClass().getName()));
		} else {
			log.info(String.format("Registering ExtensionPoint with id '%s' for class '%s'", extensionPoint
					.getId(), extensionPoint.getClass().getName()));
		}
		
		extensionPoints.put(extensionPoint.getId(), extensionPoint);
	}
	
	/**
	 * @param extension
	 */
	public void registerExtension(IExtension extension) {
		log.info(String.format("Registering Extension with id='%s' and class='%s' for ExtensionPoint '%s'",
				extension.getId(), extension.getClass().getName(), extension.getExtensionPointId()));
		
		if (!extensionPoints.containsKey(extension.getExtensionPointId())) {
			registerExtensionPoint(extension.getExtensionPointId());
		}

		Map<String, List<IExtension>> map = null;
		
		if (extensions.containsKey(extension.getExtensionPointId())) {
			map = extensions.get(extension.getExtensionPointId());
		} else {
			map = new LinkedHashMap<String, List<IExtension>>();
			extensions.put(extension.getExtensionPointId(), map);
		}

		List<IExtension> list = null;
		if (map.containsKey(extension.getId())) {
			list = map.get(extension.getId());
		} else {
			list = new ArrayList<IExtension>();
			map.put(extension.getId(), list);
		}

		list.add(extension);
	}

	/**
	 * we can have multiple extensions with the same ID - for example if we
	 * create more DateInputControls. However, the extension that is used will
	 * be the one with the highest priority
	 * 
	 * @param extensionPointId
	 * @param extensionId
	 * @return
	 */
	public IExtension getExtension(String extensionPointId, String extensionId) {
		if (extensions.containsKey(extensionPointId)) {
			Map<String, List<IExtension>> map = extensions.get(extensionPointId);
			if (map.containsKey(extensionId)) {
				List<IExtension> list = map.get(extensionId);
				return findGreatesPriorityExtension(list);
			}
		}
		
		return null;		
	}
	
	/**
	 * @param extensionPointId
	 * @return
	 */
	public List<IExtension> getExtensions(String extensionPointId) {
		List<IExtension> result = new ArrayList<IExtension>();

		if (extensions.containsKey(extensionPointId)) {
			Map<String, List<IExtension>> map = extensions.get(extensionPointId);

			for (List<IExtension> exts : map.values()) {
				result.add(findGreatesPriorityExtension(exts));
			}
		}

		if (result.size() > 1) {
			Collections.sort(result, sortIndexComparator);
		}
		
		return result;
	}

	/**
	 * @param exts
	 * @return
	 */
	private IExtension findGreatesPriorityExtension(List<IExtension> list) {
		if (list.size() > 0) {
			if (list.size() > 1) {
				Collections.sort(list, priorityComparator);
			}

			return list.get(0);
		}
		
		return null;
	}
	
	/**
	 * @return
	 */
	public List<IExtension> getAllExtensions(boolean doPriorityCheck) {
		List<IExtension> result = new ArrayList<IExtension>();

		if (doPriorityCheck) {
			for (String extensionPointId : extensionPoints.keySet()) {
				result.addAll(getExtensions(extensionPointId));
			}
		} else {
			for (Entry<String, Map<String, List<IExtension>>> entry : extensions.entrySet()) {
				for (Entry<String, List<IExtension>> entry2 : entry.getValue().entrySet()) {
					result.addAll(entry2.getValue());
				}
			}
		}

		if (result.size() > 1) {
			Collections.sort(result, sortIndexComparator);
		}
		
		return result;
	}
	
	/**
	 * @return
	 */
	public Collection<IExtensionPoint> getAllExtensionPoints() {
		return extensionPoints.values();
	}
}
