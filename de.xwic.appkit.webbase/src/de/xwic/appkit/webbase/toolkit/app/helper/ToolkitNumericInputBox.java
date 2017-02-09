package de.xwic.appkit.webbase.toolkit.app.helper;

import de.jwic.base.IControlContainer;
import de.jwic.controls.NumericInputBox;
import de.xwic.appkit.webbase.toolkit.app.IToolkitControlHelper;

/**
 * @author bogdan
 * 
 */
public final class ToolkitNumericInputBox implements IToolkitControlHelper<NumericInputBox> {

	public static final ToolkitNumericInputBox INSTANCE = new ToolkitNumericInputBox();

	private ToolkitNumericInputBox() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.webbase.toolkit.app.IToolkitControlHelper#create(de.jwic.base.IControlContainer, java.lang.String,
	 * java.lang.Object)
	 */
	@Override
	public NumericInputBox create(IControlContainer container, String name, Object optionalParam) {
		return new NumericInputBox(container, name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.webbase.toolkit.app.IToolkitControlHelper#loadContent(de.jwic.base.IControl, java.lang.Object)
	 */
	@Override
	public void loadContent(NumericInputBox control, Object obj) {
		if (obj instanceof Number) {
			Number nr = (Number) obj;
			control.setNumber(nr.doubleValue());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.webbase.toolkit.app.IToolkitControlHelper#getContent(de.jwic.base.IControl)
	 */
	@Override
	public Object getContent(NumericInputBox control) {
		return control.getNumber();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.webbase.toolkit.app.IToolkitControlHelper#markField(de.jwic.base.IControl, java.lang.String)
	 */
	@Override
	public void markField(NumericInputBox control, String cssClass) {
		control.setCssClass(cssClass);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.webbase.toolkit.app.IToolkitControlHelper#getFieldMarkedCssClass(de.jwic.base.IControl)
	 */
	@Override
	public String getFieldMarkedCssClass(NumericInputBox control) {
		return control.getCssClass();
	}

}
