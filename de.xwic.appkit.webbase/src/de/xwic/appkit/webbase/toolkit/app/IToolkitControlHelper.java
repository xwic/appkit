/**
 *
 */
package de.xwic.appkit.webbase.toolkit.app;

import de.jwic.base.IControl;
import de.jwic.base.IControlContainer;

/**
 * Helper Class for Controls used via the EditorToolKit.
 *
 * @author Ronny Pfretzschner
 *
 */
public interface IToolkitControlHelper<C extends IControl> {


	/**
	 * Creates the Control.
	 *
	 * @param container parent
	 * @param name control name
	 * @return the created Control
	 */
	public C create(IControlContainer container, String name, Object optionalParam);

	/**
	 * Tries to set the given Object value into the field value.
	 *
	 * @param obj
	 */
	public void loadContent(C control, Object obj);

	/**
	 * @return the entered value of the field value
	 */
	public Object getContent(C control);

	/**
	 * Marks the field as with given class.
	 */
	public void markField(C control, String cssClass);


	public String getFieldMarkedCssClass(C control);
}
