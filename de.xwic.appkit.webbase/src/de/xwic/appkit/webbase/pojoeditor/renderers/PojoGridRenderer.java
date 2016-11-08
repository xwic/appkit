/*
 * Copyright (c) NetApp Inc. - All Rights Reserved
 * 
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 * 
 * de.xwic.appkit.webbase.pojoeditor.PojoGridRenderer 
 */

package de.xwic.appkit.webbase.pojoeditor.renderers;

import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.jwic.base.Control;
import de.jwic.base.ControlContainer;
import de.jwic.base.IControlRenderer;
import de.jwic.base.JWicRuntime;
import de.jwic.base.RenderContext;
import de.xwic.appkit.core.pojoeditor.annotations.GridRender;
import de.xwic.appkit.core.pojoeditor.annotations.GridRenderer;
import de.xwic.appkit.webbase.pojoeditor.PojoEditorControl;
import de.xwic.appkit.webbase.pojoeditor.PojoEditorFactory;
import de.xwic.appkit.webbase.pojoeditor.PojoEditorField;

/**
 * @author Andrei Pat
 *
 */
public class PojoGridRenderer implements IPojoRenderer {

	private static final Log log = LogFactory.getLog(PojoGridRenderer.class);

	private static final String DEFAULT_CONTROL_RENDERER = "jwic.renderer.Default";

	private Class pojoClass;
	private List<PojoEditorField> fields;
	private PojoEditorControl editor;

	private int columns;

	/**
	 * @param pojoClass
	 */
	public PojoGridRenderer(Class pojoClass, PojoEditorControl editor, List<PojoEditorField> fields) {
		this.pojoClass = pojoClass;
		this.fields = fields;
		this.editor = editor;
		GridRenderer gr = (GridRenderer) pojoClass.getAnnotation(GridRenderer.class);
		this.columns = gr.columns();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.webbase.pojoeditor.renderers.IPojoRenderer#render(de.jwic.base.ControlContainer, java.lang.Class, java.util.List,
	 * de.jwic.base.RenderContext)
	 */
	@Override
	public void render(RenderContext context) {
		List<GridCell> cells = parseGridConfig(pojoClass, fields);
		renderGrid(editor, cells, context);
	}

	/**
	 * @param editor
	 * @param cells
	 * @param context
	 */
	private void renderGrid(ControlContainer editor, List<GridCell> cells, RenderContext context) {

		IControlRenderer controlRenderer = JWicRuntime.getRenderer(DEFAULT_CONTROL_RENDERER);

		PrintWriter writer = context.getWriter();
		writer.write("<table>");
		int i = 1;
		writer.write("<tr>");

		for (GridCell c : cells) {
			if (shouldRenderControl(c)) {
				renderControl(c, context, controlRenderer);

				if (i % columns == 0) {
					writer.write("</tr><tr>");
				}
				i += c.colspan;
			}

		}
		writer.write("</tr>");
		writer.write("</table>");

	}

	/**
	 * @param ctrl
	 * @param context
	 * @param controlRenderer
	 */
	private void renderControl(GridCell cell, RenderContext context, IControlRenderer controlRenderer) {

		Control ctrl = cell.field.getControl();
		PrintWriter w = context.getWriter();
		w.write("<th style=\"padding-right:15px\">");
		w.write(cell.field.getLabel());
		w.write("</th><td " + colspan(cell.colspan) + " style=\"padding-right:15px\">");
		controlRenderer.renderControl(ctrl, context);
		w.write("</td>");
	}

	/**
	 * Insert a colspan if needed
	 * 
	 * @param colspan
	 * @return
	 */
	private String colspan(int colspan) {
		if (colspan > 1) {
			int cols = colspan * 2 - 1;
			return "colspan=" + cols;
		} else {
			return "";
		}
	}

	/**
	 * @param c
	 * @return
	 */
	private boolean shouldRenderControl(GridCell c) {
		return true;
	}

	/**
	 * @param pojoClass
	 * @param fields
	 * @return
	 */
	private static List<GridCell> parseGridConfig(Class pojoClass, List<PojoEditorField> fields) {
		List<GridCell> gridCells = new ArrayList<PojoGridRenderer.GridCell>();
		for (PojoEditorField field : fields) {
			Field f = PojoEditorFactory.findField(pojoClass, field.getPropertyName());
			if (f == null) {
				log.error("Missing field:" + field.propertyName);
				continue;
			}

			int order = Integer.MAX_VALUE;
			int colspan = 1;
			if (f.isAnnotationPresent(GridRender.class)) {
				GridRender annotation = f.getAnnotation(GridRender.class);
				order = annotation.order();
				colspan = annotation.colspan();

			}
			GridCell cell = new GridCell(field, order, colspan);
			gridCells.add(cell);

		}
		Collections.sort(gridCells);
		return gridCells;

	}

	private static class GridCell implements Comparable<GridCell> {

		private PojoEditorField field;
		private int order;
		private int colspan;

		public GridCell(PojoEditorField field, int order, int colspan) {
			super();
			this.field = field;
			this.order = order;
			this.colspan = colspan;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Comparable#compareTo(java.lang.Object)
		 */
		@Override
		public int compareTo(GridCell o) {
			return (order < o.order) ? -1 : ((order == o.order) ? 0 : 1);
		}
	}

	/**
	 * @return the columns
	 */
	public int getColumns() {
		return columns;
	}

	/**
	 * @param columns
	 *            the columns to set
	 */
	public void setColumns(int columns) {
		this.columns = columns;
	}

}
