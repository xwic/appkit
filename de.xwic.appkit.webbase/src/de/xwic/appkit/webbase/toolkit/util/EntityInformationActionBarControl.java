/*
 * de.jwic.controls.TabStripControl
 * $Id: EntityInformationActionBarControl.java,v 1.1 2008/10/10 14:22:30 ronnyp Exp $
 */
package de.xwic.appkit.webbase.toolkit.util;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.jwic.base.Control;
import de.jwic.base.ControlContainer;
import de.jwic.base.IControlContainer;
import de.jwic.base.JWicException;
import de.jwic.controls.ActionBarControl;
import de.jwic.controls.ButtonControl;
import de.jwic.controls.LabelControl;

/**
 * Special actionbar control showing entity related information like
 * created at and so on.
 * 
 * Created on 07.03.2008
 * @author Ronny Pfretzschner
 */
public class EntityInformationActionBarControl extends ControlContainer {

	private static final long serialVersionUID = 1L;

	private List<Control> elements = new ArrayList<Control>();
	private List<Control> rightElements = new ArrayList<Control>();

	private boolean showInformation = false;
	
	private LabelControl lastModi, lastModiAt, createdFrom, createdAt;
	
	/**
	 * @param container
	 */
	public EntityInformationActionBarControl(IControlContainer container) {
		super(container);
	}
	/**
	 * @param container
	 * @param name
	 */
	public EntityInformationActionBarControl(IControlContainer container, String name) {
		super(container, name);
		
		lastModi = new LabelControl(this, "lastModifiedFrom");
		lastModiAt = new LabelControl(this, "lastModifiedAt");
		createdFrom = new LabelControl(this, "createdFrom");
		createdAt = new LabelControl(this, "createdAt");

	}
	
	/* (non-Javadoc)
	 * @see de.jwic.base.ControlContainer#registerControl(de.jwic.base.Control, java.lang.String)
	 */
	public void registerControl(Control control, String name) throws JWicException {
		super.registerControl(control, name);

		if (control instanceof ButtonControl) {
			elements.add(control);
			control.setTemplateName(control.getTemplateName() + "_Action");
		}
		else if (!(control instanceof LabelControl)) {
			elements.add(control);
		}
	}
	
	/* (non-Javadoc)
	 * @see de.jwic.base.ControlContainer#unregisterControl(de.jwic.base.Control)
	 */
	public void unregisterControl(Control control) {
		super.unregisterControl(control);
		elements.remove(control);
	}
	
	/**
	 * Get the controls in the right order as they were inserted
	 */	
	public List<Control> getElements() {
		return elements;
	}
	
	/**
	 * Returns the controls aligned to the right.
	 * @return
	 */
	public List<Control> getRightElements() {
		return rightElements;
	}


	/**
	 * Change the alignment of a control.
	 * @param control
	 * @param align
	 */
	public void setPosition(Control control, ActionBarControl.Align align) {
		
		// move the element between lists
		if (!(elements.contains(control) || rightElements.contains(control))) {
			throw new IllegalArgumentException("The specified control is not a member of this action bar.");
		}
		
		List<Control> a = (align == ActionBarControl.Align.LEFT ? rightElements : elements);
		List<Control> b = (align == ActionBarControl.Align.LEFT ? elements : rightElements);
		
		if (a.contains(control)) { // only move if it is not already there...
			a.remove(control);
			b.add(control);
		}
		
	}

	
	/**
	 * @return Returns the showInformation.
	 */
	public boolean isShowInformation() {
		return showInformation;
	}
	/**
	 * @param showInformation The showInformation to set.
	 */
	public void setShowInformation(boolean showInformation) {
		this.showInformation = showInformation;
	}

	public void setLastModifiedFrom(String strlastmodi) {
		lastModi.setText(strlastmodi == null ? "-/-" : strlastmodi);
		
	}

	public void setLastModifiedAt(Date lastmodi) {
		lastModiAt.setText(lastmodi == null ? "-/-" : DateFormat.getDateTimeInstance().format(lastmodi));
		
	}

	public void setCreatedFrom(String strcreatedFrom) {
		createdFrom.setText(strcreatedFrom == null ? "-/-" : strcreatedFrom);
		
	}

	public void setCreatedAt(Date dtcreatedAt) {
		createdAt.setText(dtcreatedAt == null ? "-/-" : DateFormat.getDateTimeInstance().format(dtcreatedAt));		
		
	}

}
