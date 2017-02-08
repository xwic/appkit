/**
 * 
 */
package de.xwic.appkit.dev.engine.model.impl;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Node;

import de.xwic.appkit.dev.engine.ConfigurationException;
import de.xwic.appkit.dev.engine.EngineUtil;
import de.xwic.appkit.dev.engine.model.EntityModel;
import de.xwic.appkit.dev.engine.model.EntityProperty;

/**
 * @author lippisch
 *
 */
public class XMLEntityModel implements EntityModel {

	private Node node;
	private String name;
	private String defaultDisplayProperty;
	private String description;
	
	public XMLEntityModel(Node node) {
		this.node = node;
		this.name = node.valueOf("@name");
		this.defaultDisplayProperty = node.valueOf("@defaultDisplayProperty");
		this.description = node.valueOf("@description");
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.dev.engine.model.EntityModel#getProperties()
	 */
	@Override
	public List<EntityProperty> getProperties() throws ConfigurationException {

		List<EntityProperty> properties = new ArrayList<EntityProperty>();
		
		List<Node> nodes = node.selectNodes("property");
		for (Node pNode : nodes) {
			properties.add(new XMLEntityProperty(pNode));
		}
		
		return properties;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.dev.engine.model.EntityProperty#getIdColumn()
	 */
	@Override
	public String getIdColumn() {
		
		String idCol = node.valueOf("@idColumn");
		if (idCol != null && !idCol.isEmpty()) {
			return idCol;
		}
		return EngineUtil.toDBName(name) + "_ID";
	}
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.dev.engine.model.EntityProperty#getTableName()
	 */
	@Override
	public String getTableName() {
		
		String table = node.valueOf("@table");
		if (table != null && !table.isEmpty()) {
			return table;
		}
		
		return EngineUtil.toDBName(name);
	}
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.dev.engine.model.EntityModel#getDefaultDisplayProperty()
	 */
	@Override
	public String getDefaultDisplayProperty() {
		return defaultDisplayProperty;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.dev.engine.model.EntityModel#getDescription()
	 */
	@Override
	public String getDescription() {
		return description;
	}
	
}
