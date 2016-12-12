/*
 * Copyright (c) NetApp Inc. - All Rights Reserved
 * 
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 * 
 * de.xwic.appkit.webbase.pojoeditor.PojoEditorControl 
 */

package de.xwic.appkit.webbase.pojoeditor;

import java.util.ArrayList;
import java.util.List;

import de.jwic.base.ControlContainer;
import de.jwic.base.IControlContainer;
import de.jwic.base.RenderContext;
import de.jwic.controls.Button;
import de.jwic.renderer.self.ISelfRenderingControl;
import de.jwic.renderer.self.SelfRenderer;
import de.xwic.appkit.webbase.pojoeditor.renderers.IPojoRenderer;
import de.xwic.appkit.webbase.toolkit.app.EditorToolkit;

/**
 * @author Andrei Pat
 *
 */
public class PojoEditorControl extends ControlContainer implements ISelfRenderingControl, IPojoEditorControl {

	private IPojoRenderer renderer;
	private EditorToolkit toolkit;
	
	private List<IPojoEditorControl> childControls = new ArrayList<IPojoEditorControl>();

	/**
	 * 
	 */
	public PojoEditorControl(IControlContainer container, String name, EditorToolkit toolkit) {		
		super(container, name);
		this.toolkit = toolkit;
		this.setRendererId(SelfRenderer.RENDERER_ID);		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.jwic.renderer.self.ISelfRenderingControl#render(de.jwic.base.RenderContext)
	 */
	@Override
	public void render(RenderContext renderContext) {
		renderer.render(renderContext);		
	}

	/**
	 * 
	 */
	@Override
	public void save() {
		for (IPojoEditorControl child : childControls) {
			child.save();
		}
		toolkit.saveFieldValues();
	}

	/**
	 * 
	 */
	@Override
	public void load() {
		for (IPojoEditorControl child : childControls) {
			child.load();
		}
		toolkit.loadFieldValues();
	}

	/**
	 * @param renderer
	 *            the renderer to set
	 */
	public void setRenderer(IPojoRenderer renderer) {
		this.renderer = renderer;
	}

	public void addChildControl(IPojoEditorControl control) {
		childControls.add(control);
	}
}
