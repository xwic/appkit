/**
 * 
 */
package de.xwic.appkit.dev.engine.model.impl;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Node;

import de.xwic.appkit.dev.engine.model.DataModel;
import de.xwic.appkit.dev.engine.model.EntityModel;

/**
 * DataModel container based on XML configuration files.
 * @author lippisch
 */
public class XMLDataModel implements DataModel {

	private Document document;

	public XMLDataModel(Document document) {
		this.document = document;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.dev.engine.model.DataModel#getDomainId()
	 */
	@Override
	public String getDomainId() {
		
		
		Node node = document.selectSingleNode("//domain");
		if (node != null) {
			return node.getText();
		} else {
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.dev.engine.model.DataModel#getProjectName()
	 */
	@Override
	public String getProjectName() {
		Node node = document.selectSingleNode("//project");
		return node != null ?  node.getText() : null;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.dev.engine.model.DataModel#getPackageName()
	 */
	@Override
	public String getPackageName() {
		Node node = document.selectSingleNode("//package");
		return node != null ? node.getText() : null;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.dev.engine.model.DataModel#getEntities()
	 */
	@Override
	public List<EntityModel> getEntities() {
		
		List<EntityModel> entities = new ArrayList<EntityModel>();
		
		List<Node> nodes = document.selectNodes("//entities/entity");
		for (Node node : nodes) {
			entities.add(new XMLEntityModel(node));
		}
		
		return entities;
	}
	
}
