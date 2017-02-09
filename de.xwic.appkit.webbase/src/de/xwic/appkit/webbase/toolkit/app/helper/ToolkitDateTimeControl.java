/**
 *
 */
package de.xwic.appkit.webbase.toolkit.app.helper;

import java.util.Date;

import de.jwic.base.IControlContainer;
import de.jwic.controls.DateTimePicker;

/**
 * @author dotto
 * 
 */
public class ToolkitDateTimeControl extends AbstractToolkitHTMLElementControl<DateTimePicker> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.webbase.toolkit.app.IToolkitControlHelper#create(de.jwic.base.IControlContainer, java.lang.String,
	 * java.lang.Object)
	 */
	@Override
	public DateTimePicker create(IControlContainer container, String name, Object optionalParam) {
		return new DateTimePicker(container, name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.webbase.toolkit.app.IToolkitControlHelper#getContent(de.jwic.base.IControl)
	 */
	@Override
	public Object getContent(DateTimePicker control) {
		return control.getDate();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.webbase.toolkit.app.IToolkitControlHelper#loadContent(de.jwic.base.IControl, java.lang.Object)
	 */
	@Override
	public void loadContent(DateTimePicker control, Object obj) {
		control.setDate((Date) obj);
	}
}
