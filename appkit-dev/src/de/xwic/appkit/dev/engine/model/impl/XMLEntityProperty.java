/**
 * 
 */
package de.xwic.appkit.dev.engine.model.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.dom4j.Node;

import de.xwic.appkit.dev.engine.ConfigurationException;
import de.xwic.appkit.dev.engine.EngineUtil;
import de.xwic.appkit.dev.engine.model.EntityPicklistEntry;
import de.xwic.appkit.dev.engine.model.EntityProperty;

/**
 * @author lippisch
 *
 */
public class XMLEntityProperty implements EntityProperty {

	private Node pNode;
	private String name;
	private String upName;
	private String type;
	private String length;
	private String columnName;
	private String picklistId;
	private boolean required;

	private static Set<String> BASIC_TYPES = new HashSet<String>();
	static {
		BASIC_TYPES.add("String");
		BASIC_TYPES.add("Integer");
		BASIC_TYPES.add("int");
		BASIC_TYPES.add("Long");
		BASIC_TYPES.add("long");
		BASIC_TYPES.add("Double");
		BASIC_TYPES.add("double");
		BASIC_TYPES.add("Boolean");
		BASIC_TYPES.add("boolean");
		BASIC_TYPES.add("Date");
		BASIC_TYPES.add("date");
		BASIC_TYPES.add("BigNumber");
		BASIC_TYPES.add("Float");
		BASIC_TYPES.add("float");
	}
	
	/**
	 * @param pNode
	 * @throws ConfigurationException 
	 */
	public XMLEntityProperty(Node pNode) throws ConfigurationException {
		this.pNode = pNode;
		
		this.name = pNode.valueOf("@name");
		
		if (this.name == null || this.name.isEmpty()) {
			throw new ConfigurationException("The name of a property is not defined.");
		}
		this.upName = this.name.substring(0, 1).toUpperCase() + this.name.substring(1);
		this.type = pNode.valueOf("@type");
		this.length = pNode.valueOf("@length");
		this.picklistId = pNode.valueOf("@picklistId");
		this.required = "true".equals(pNode.valueOf("@required"));
		
		if (this.type == null || this.type.isEmpty()) {
			throw new ConfigurationException("The type is not defined for property " + this.name);
		}
		
		this.columnName = pNode.valueOf("@column");
		if (this.columnName == null || this.columnName.isEmpty()) {
			this.columnName = EngineUtil.toDBName(name);
		}
		
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Returns the name of the 'getter' method for this property. 
	 * @return
	 */
	public String getNameGetter() {
		if (type.equals("boolean") || type.equals("Boolean")) {
			return "is" + upName;
		} else {
			return "get" + upName;
		}
	}
	
	/**
	 * Returns the setter name.
	 * @return
	 */
	public String getNameSetter() {
		return "set" + upName;
	}
	
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @return the length
	 */
	public String getLength() {
		return length;
	}

	/**
	 * @return the required
	 */
	public boolean isRequired() {
		return required;
	}

	/**
	 * @return the columnName
	 */
	public String getColumnName() {
		return columnName;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.dev.engine.model.EntityProperty#isBasicType()
	 */
	@Override
	public boolean isBasicType() {
		return BASIC_TYPES.contains(type);
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.dev.engine.model.EntityProperty#getPicklistId()
	 */
	@Override
	public String getPicklistId() {
		return picklistId;
	}
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.dev.engine.model.EntityProperty#getPicklistEntries()
	 */
	@Override
	public List<EntityPicklistEntry> getPicklistEntries() {

		List<EntityPicklistEntry> entries = new ArrayList<EntityPicklistEntry>();
		
		List<Node> nodes = pNode.selectNodes("picklistEntry");
		for (Node n : nodes) {
			entries.add(new XMLEntityPicklistEntry(n));
		}
		
		return entries;

	}
	
}
