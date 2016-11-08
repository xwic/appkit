/*
 * Copyright (c) NetApp Inc. - All Rights Reserved
 * 
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 * 
 * de.xwic.appkit.webbase.pojoeditor.PojoEditorControl 
 */

package de.xwic.appkit.webbase.pojoeditor;

import de.jwic.base.ControlContainer;
import de.jwic.base.IControlContainer;
import de.jwic.base.RenderContext;
import de.jwic.renderer.self.ISelfRenderingControl;
import de.jwic.renderer.self.SelfRenderer;
import de.xwic.appkit.webbase.pojoeditor.renderers.IPojoRenderer;
import de.xwic.appkit.webbase.toolkit.app.EditorToolkit;

/**
 * @author Andrei Pat
 *
 */
public class PojoEditorControl extends ControlContainer implements ISelfRenderingControl {

	private IPojoRenderer renderer;
	private EditorToolkit toolkit;

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
	public void save() {
		toolkit.saveFieldValues();
	}

	/**
	 * 
	 */
	public void load() {
		toolkit.loadFieldValues();
	}

	/**
	 * @param renderer
	 *            the renderer to set
	 */
	public void setRenderer(IPojoRenderer renderer) {
		this.renderer = renderer;
	}

}
