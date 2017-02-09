package de.xwic.appkit.webbase.toolkit.app.helper;


import de.jwic.base.IControlContainer;
import de.jwic.controls.NumericIntegerInputBox;
import de.xwic.appkit.webbase.toolkit.app.IToolkitControlHelper;

/**
 * @author bogdan
 * 
 */
public final class ToolkitNumericIntegerInputBox implements IToolkitControlHelper<NumericIntegerInputBox> {

	public static final ToolkitNumericIntegerInputBox INSTANCE = new ToolkitNumericIntegerInputBox();

	private ToolkitNumericIntegerInputBox() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.webbase.toolkit.app.IToolkitControlHelper#create(de.jwic.base.IControlContainer, java.lang.String,
	 * java.lang.Object)
	 */
	@Override
	public NumericIntegerInputBox create(IControlContainer container, String name, Object optionalParam) {
		return new NumericIntegerInputBox(container, name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.webbase.toolkit.app.IToolkitControlHelper#loadContent(de.jwic.base.IControl, java.lang.Object)
	 */
	@Override
	public void loadContent(NumericIntegerInputBox control, Object obj) {
		if (obj instanceof Number) {
			Number nr = (Number) obj;
			control.setNumber((double) nr.intValue());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.webbase.toolkit.app.IToolkitControlHelper#getContent(de.jwic.base.IControl)
	 */
	@Override
	public Object getContent(NumericIntegerInputBox control) {
		return control.getNumberAsInt();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.webbase.toolkit.app.IToolkitControlHelper#markField(de.jwic.base.IControl, java.lang.String)
	 */
	@Override
	public void markField(NumericIntegerInputBox control, String cssClass) {
		control.setCssClass(cssClass);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.webbase.toolkit.app.IToolkitControlHelper#getFieldMarkedCssClass(de.jwic.base.IControl)
	 */
	@Override
	public String getFieldMarkedCssClass(NumericIntegerInputBox control) {
		return control.getCssClass();
	}

}
