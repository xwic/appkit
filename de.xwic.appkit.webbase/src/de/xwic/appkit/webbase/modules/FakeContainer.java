package de.xwic.appkit.webbase.modules;

import de.jwic.base.Control;
import de.jwic.base.IControlContainer;
import de.jwic.base.SessionContext;

import java.lang.Override;import java.lang.String;import java.util.Iterator;

/**
 * Created by boogie on 3/6/15.
 */
final class FakeContainer implements IControlContainer{

	@Override
	public void adopt(Control control, String name) {

	}

	@Override
	public Control getControl(String name) {
		return null;
	}

	@Override
	public Iterator<Control> getControls() {
		return null;
	}

	@Override
	public SessionContext getSessionContext() {
		return null;
	}

	@Override
	public void registerControl(Control control, String name) {

	}

	@Override
	public void unregisterControl(Control control) {

	}

	@Override
	public void removeControl(String name) {

	}

	@Override
	public boolean isRequireRedraw() {
		return false;
	}

	@Override
	public void setRequireRedraw(boolean requireRedraw) {

	}

	@Override
	public boolean isRenderingRelevant(Control childControl) {
		return false;
	}
}
