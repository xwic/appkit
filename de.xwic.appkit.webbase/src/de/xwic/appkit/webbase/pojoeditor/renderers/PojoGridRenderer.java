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
import de.jwic.base.IControl;
import de.jwic.base.IControlRenderer;
import de.jwic.base.IHaveEnabled;
import de.jwic.base.JWicRuntime;
import de.jwic.base.RenderContext;
import de.jwic.controls.InputBox;
import de.xwic.appkit.core.pojoeditor.annotations.GridCell;
import de.xwic.appkit.core.pojoeditor.annotations.GridRenderer;
import de.xwic.appkit.core.pojoeditor.annotations.GridRenderer.LabelPosition;
import de.xwic.appkit.webbase.pojoeditor.IPojoEditorFieldRenderLogic;
import de.xwic.appkit.webbase.pojoeditor.PojoEditorControl;
import de.xwic.appkit.webbase.pojoeditor.PojoEditorFactory;
import de.xwic.appkit.webbase.pojoeditor.PojoEditorField;

/**
 * @author Andrei Pat
 *
 */
public class PojoGridRenderer implements IPojoRenderer {

	private static final Log log = LogFactory.getLog(PojoGridRenderer.class);

	protected Class<?> pojoClass;
	protected List<PojoEditorField> fields;
	protected PojoEditorControl editor;
	protected IPojoEditorFieldRenderLogic fieldRenderLogic;

	private int columns = 1;
	protected LabelPosition labelPosition = LabelPosition.SIDE;

	/**
	 * @param pojoClass
	 */
	public PojoGridRenderer() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.webbase.pojoeditor.renderers.IPojoRenderer#render(de.jwic.base.ControlContainer, java.lang.Class, java.util.List,
	 * de.jwic.base.RenderContext)
	 */
	@Override
	public void render(RenderContext context) {
		List<PojoGridCell> cells = parseGridConfig(pojoClass, fields);
		renderGrid(editor, cells, context);
	}

	/**
	 * @param editor
	 * @param cells
	 * @param context
	 */
	private void renderGrid(PojoEditorControl editor, List<PojoGridCell> cells, RenderContext context) {

		PrintWriter writer = context.getWriter();

		writer.write("<table style=\"border-collapse: separate; border-spacing: 10px 10px; ");
		if (editor.getWidth() > 0) {
			writer.write("width:" + editor.getWidth() + "px");
		} else {
			writer.write("width:100%");
		}
		writer.write("\">");
		int column = 1;
		writer.write("<tr>");

		for (PojoGridCell c : cells) {
			String fieldName = c.field.getPropertyName();
			if (fieldRenderLogic.isRenderField(fieldName)) {
				enableControl(c.field, fieldRenderLogic.isEnabled(fieldName));
				IControlRenderer controlRenderer = JWicRuntime.getRenderer(c.field.getControl().getRendererId());
				renderControl(c, context, controlRenderer);

				column += c.colspan;

				if (column > columns) {
					writer.write("</tr><tr>");
					column = 1;
				}
			}

		}
		writer.write("</tr>");
		writer.write("</table>");

	}

	/**
	 * Enable/disable control
	 * 
	 * @param c
	 * @param isEnabled
	 */
	protected void enableControl(PojoEditorField field, boolean isEnabled) {
		Control control = field.getControl();
		if (control instanceof IHaveEnabled) {
			((IHaveEnabled) control).setEnabled(isEnabled);
		} else if (control instanceof InputBox) {
			((InputBox) control).setReadonly(!isEnabled);
		}
	}

	/**
	 * @param ctrl
	 * @param context
	 * @param controlRenderer
	 */
	protected void renderControl(PojoGridCell cell, RenderContext context, IControlRenderer controlRenderer) {

		Control ctrl = cell.field.getControl();
		PrintWriter w = context.getWriter();

		if (labelPosition == LabelPosition.SIDE) {
			w.write("<th style=\"padding-right:15px\">");
			w.write(cell.field.getLabel());
			w.write("</th>");
		}
		w.write("<td " + colspan(cell.colspan) + ">");

		if (labelPosition == LabelPosition.TOP) {
			w.write("<label for=" + ctrl.getControlID() + " style=\"font-weight:bold\">" + cell.field.getLabel() + "</label><br />");
		}
		w.write("<div>");
		controlRenderer.renderControl(ctrl, context);
		w.write("</div>");
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
	 * @param pojoClass
	 * @param fields
	 * @return
	 */
	private static List<PojoGridCell> parseGridConfig(Class pojoClass, List<PojoEditorField> fields) {
		List<PojoGridCell> gridCells = new ArrayList<PojoGridRenderer.PojoGridCell>();
		for (PojoEditorField field : fields) {
			Field f = PojoEditorFactory.findField(pojoClass, field.getPropertyName());
			if (f == null) {
				log.error("Missing field:" + field.propertyName);
				continue;
			}

			int order = Integer.MAX_VALUE;
			int colspan = 1;
			if (f.isAnnotationPresent(GridCell.class)) {
				GridCell annotation = f.getAnnotation(GridCell.class);
				order = annotation.order();
				colspan = annotation.colspan();

			}
			PojoGridCell cell = new PojoGridCell(field, order, colspan);
			gridCells.add(cell);

		}
		Collections.sort(gridCells);
		return gridCells;

	}

	protected static class PojoGridCell implements Comparable<PojoGridCell> {

		private PojoEditorField field;
		private int order;
		private int colspan;

		public PojoGridCell(PojoEditorField field, int order, int colspan) {
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
		public int compareTo(PojoGridCell o) {
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

	/**
	 * @param pojoClass
	 *            the pojoClass to set
	 */
	@Override
	public void setPojoClass(Class pojoClass) {
		this.pojoClass = pojoClass;
		GridRenderer gr = (GridRenderer) pojoClass.getAnnotation(GridRenderer.class);
		this.labelPosition = gr.labelPosition();
		this.columns = gr.columns();
	}

	/**
	 * @param fields
	 *            the fields to set
	 */
	@Override
	public void setFields(List<PojoEditorField> fields) {
		this.fields = fields;
	}

	/**
	 * @param editor
	 *            the editor to set
	 */
	@Override
	public void setEditorControl(PojoEditorControl editor) {
		this.editor = editor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.webbase.pojoeditor.renderers.IPojoRenderer#setFieldRenderLogic(de.xwic.appkit.webbase.pojoeditor.
	 * IPojoEditorFieldRenderLogic)
	 */
	@Override
	public void setFieldRenderLogic(IPojoEditorFieldRenderLogic logic) {
		this.fieldRenderLogic = logic;
	}

}
