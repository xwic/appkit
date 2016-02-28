/**
 * 
 */
package de.xwic.appkit.webbase.editors;

import de.jwic.events.ElementSelectedEvent;
import de.jwic.events.ElementSelectedListener;
import de.jwic.events.ValueChangedEvent;
import de.jwic.events.ValueChangedListener;
import de.xwic.appkit.core.config.model.Property;

/**
 * Listens to ElementSelected and ValueChanged events on controls to notify
 * the context about a change in a widget related to the specified property.
 * 
 * @author lippisch
 */
public class FieldChangeListener implements ElementSelectedListener, ValueChangedListener {

	private IBuilderContext context;
	private Property[] property;

	/**
	 * 
	 */
	public FieldChangeListener(IBuilderContext context, Property[] property) {
		this.context = context;
		this.property = property;
		
	}

	/* (non-Javadoc)
	 * @see de.jwic.events.ElementSelectedListener#elementSelected(de.jwic.events.ElementSelectedEvent)
	 */
	@Override
	public void elementSelected(ElementSelectedEvent event) {
		context.fieldChanged(property);
	}

	/* (non-Javadoc)
	 * @see de.jwic.events.ValueChangedListener#valueChanged(de.jwic.events.ValueChangedEvent)
	 */
	@Override
	public void valueChanged(ValueChangedEvent event) {
		context.fieldChanged(property);
	}

}
