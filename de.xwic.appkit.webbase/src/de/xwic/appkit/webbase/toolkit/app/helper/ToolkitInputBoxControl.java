/**
 *
 */
package de.xwic.appkit.webbase.toolkit.app.helper;

import de.jwic.base.IControlContainer;
import de.jwic.controls.InputBox;

/**
 * @author Ronny Pfretzschner
 *
 */
public class ToolkitInputBoxControl extends AbstractToolkitHTMLElementControl<InputBox> {

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.toolkit.app.IToolkitControlHelper#create(de.jwic.base.IControlContainer, java.lang.String)
	 */
	@Override
	public InputBox create(IControlContainer container, String name, Object optionalParam) {
		InputBox control = new InputBox(container, name);
		control.setFillWidth(true);
		return control;
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.toolkit.app.IToolkitControlHelper#getContent(de.jwic.base.IControl)
	 */
	@Override
	public Object getContent(InputBox control) {
		return control.getText();
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.toolkit.app.IToolkitControlHelper#loadContent(de.jwic.base.IControl, java.lang.Object)
	 */
	@Override
	public void loadContent(InputBox control, Object obj) {
		control.setText(obj != null ? obj.toString() : "");
	}
}
