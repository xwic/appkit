/*
 * (c) Copyright 2005, 2006 by pol GmbH 
 * 
 * de.xwic.appkit.core.config.editor.Style
 * Created on 14.11.2006 by Florian Lippisch
 *
 */
package de.xwic.appkit.core.config.editor;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

import de.xwic.appkit.core.config.ParseException;

/**
 * A Style represents a set layout informations that 
 * can be applied to an UIElement.
 * 
 * @author Florian Lippisch
 */
public class Style {

	/** verticalAlignment specifies how controls will be positioned 
	 * vertically within a cell.
	 */
	public final static String VERTICAL_ALIGNMENT = "verticalAlignment";
	/**
	 * horizontalAlignment specifies how controls will be positioned 
	 * horizontally within a cell. 
	 */
	public final static String HORIZONTAL_ALIGNMENT = "horizontalAlignment";
	
	/**
	 * widthHint specifies the preferred width in pixels. This value
	 * is the wHint passed into Control.computeSize(int, int, boolean) 
	 * to determine the preferred size of the control.
	 * 
	 * @see Control#computeSize(int, int, boolean)
	 */
	public final static String WIDTH_HINT = "widthHint";
	
	/**
	 * heightHint specifies the preferred height in pixels. This value
	 * is the hHint passed into Control.computeSize(int, int, boolean) 
	 * to determine the preferred size of the control.
	 * 
	 * @see Control#computeSize(int, int, boolean)
	 */
	public final static String HEIGHT_HINT = "heightHint";
	
	/**
	 * horizontalIndent specifies the number of pixels of indentation
	 * that will be placed along the left side of the cell.
	 */
	public final static String HORIZONTAL_INDENT = "horizontalIndent";
	
	/**
	 * verticalIndent specifies the number of pixels of indentation
	 * that will be placed along the top side of the cell.
	 */
	public final static String VERTICAL_INDENT = "verticalIndent";
	
	/**
	 * horizontalSpan specifies the number of column cells that the control
	 * will take up.
	 */
	public final static String HORIZONTAL_SPAN = "horizontalSpan";
	
	/**
	 * verticalSpan specifies the number of row cells that the control
	 * will take up.
	 */
	public final static String VERTICAL_SPAN = "verticalSpan";
	
	/**
	 * <p>grabExcessHorizontalSpace specifies whether the width of the cell 
	 * changes depending on the size of the parent Composite. 	 
	 * @see GridData#minimumWidth
	 * @see GridData#grabExcessHorizontalSpace
	 * @see GridData#widthHint
	 */	
	public final static String GRAB_EXCESS_HORIZONTAL_SPACE = "grabExcessHorizontalSpace";
	
	/**
	 * <p>grabExcessVerticalSpace specifies whether the height of the cell 
	 * changes depending on the size of the parent Composite.  
	 * @see GridData#grabExcessVerticalSpace 
	 * @see GridData#minimumHeight
	 * @see GridData#heightHint
	 */	
	public final static String GRAB_EXCESS_VERTICAL_SPACE = "grabExcessVerticalSpace";

	/**
	 * makeColumnsEqualWidth specifies whether all columns in the layout
	 * will be forced to have the same width.
	 */
	public final static String MAKE_COLUMNS_EQUAL_WIDTH = "makeColumnsEqualWidth";
	
	/**
	 * marginWidth specifies the number of pixels of horizontal margin
	 * that will be placed along the left and right edges of the layout.
	 */
 	public final static String MARGIN_WIDTH = "marginWidth";
 	
	/**
	 * marginHeight specifies the number of pixels of vertical margin
	 * that will be placed along the top and bottom edges of the layout.
	 */
 	public final static String MARGIN_HEIGHT = "marginHeight";

 	/**
	 * marginLeft specifies the number of pixels of horizontal margin
	 * that will be placed along the left edge of the layout.
	 */
	public final static String MARGIN_LEFT = "marginLeft";

	/**
	 * marginTop specifies the number of pixels of vertical margin
	 * that will be placed along the top edge of the layout.
	 */
	public final static String MARGIN_TOP = "marginTop";

	/**
	 * marginRight specifies the number of pixels of horizontal margin
	 * that will be placed along the right edge of the layout.
	 */
	public final static String MARGIN_RIGHT = "marginRight";

	/**
	 * marginBottom specifies the number of pixels of vertical margin
	 * that will be placed along the bottom edge of the layout.
	 */
	public final static String MARGIN_BOTTOM = "marginBottom";

	/**
	 * horizontalSpacing specifies the number of pixels between the right
	 * edge of one cell and the left edge of its neighbouring cell to
	 * the right.
	 */
 	public final static String HORIZONTAL_SPACING = "horizontalSpacing";

	/**
	 * verticalSpacing specifies the number of pixels between the bottom
	 * edge of one cell and the top edge of its neighbouring cell underneath.
	 */
 	public final static String VERTICAL_SPACING = "verticalSpacing";

	
	
	private Map<String, String> data = new HashMap<String, String>();
	private Style parentStyle = null;
	
	/**
	 * Create an internal abbrevation map for style ids. 
	 */
	private final static Map<String, String> ABBR_MAP = new HashMap<String, String>();
	static {
		ABBR_MAP.put("height", HEIGHT_HINT);
		ABBR_MAP.put("width", WIDTH_HINT);
		ABBR_MAP.put("hAlign", HORIZONTAL_ALIGNMENT);
		ABBR_MAP.put("hIndent", HORIZONTAL_INDENT);
		ABBR_MAP.put("hSpan", HORIZONTAL_SPAN);
		ABBR_MAP.put("vAlign", VERTICAL_ALIGNMENT);
		ABBR_MAP.put("vIndent", VERTICAL_INDENT);
		ABBR_MAP.put("vSpan", VERTICAL_SPAN);
		ABBR_MAP.put("grabHSpace", GRAB_EXCESS_HORIZONTAL_SPACE);
		ABBR_MAP.put("grabVSpace", GRAB_EXCESS_VERTICAL_SPACE);
		ABBR_MAP.put("hSpacing", HORIZONTAL_SPACING);
		ABBR_MAP.put("vSpacing", VERTICAL_SPACING);
	}
	
	/**
	 * Default Constructor.
	 */
	public Style() {
		
	}
	
	/**
	 * Constructs a style as child of the specified parent style.
	 * @param parent
	 */
	public Style(Style parent) {
		this.parentStyle = parent;
	}
	
	/**
	 * Change a style's value.
	 * @param key
	 * @param value
	 */
	public void setStyle(String key, String value) {
		data.put(key, value);
	}
	
	/**
	 * Reads the value of a specified style.
	 * @param key
	 * @return
	 */
	public String getStyle(String key) {
		if (data.containsKey(key)) {
			return data.get(key);
		} else if (parentStyle != null) {
			return parentStyle.getStyle(key);
		}
		return null;
	}
	
	/**
	 * Returns the style value as Integer. 
	 * @param key
	 * @return
	 */
	public int getStyleInt(String key) {
		String value = getStyle(key);
		if (value != null) {
			return Integer.parseInt(value);
		}
		return 0;
	}

	/**
	 * Returns true if the style value is set to 'true', '1' or 'on'.
	 * @param key
	 * @return
	 */
	public boolean getStyleBoolean(String key) {
		String value = getStyle(key);
		if (value != null) {
			return value.equals("true") || value.equals("1") || value.equals("on");
		}
		return false;
	}

	/**
	 * Returns true if the specified style is specified by 
	 * this style.
	 * @param key
	 * @return
	 */
	public boolean isStyleSpecified(String key) {
		if (data.containsKey(key)) {
			return true;
		} else if (parentStyle != null) {
			return parentStyle.isStyleSpecified(key);
		}
		return false;
	}
	
	/**
	 * Read style values from a string.
	 * @param style
	 */
	public void readStyles(String style) throws ParseException {
		
		// first: split into value pairs by ;
		StringTokenizer stk = new StringTokenizer(style, ";");
		while (stk.hasMoreTokens()) {
			String token = stk.nextToken();
			String key = null;
			String value = null;
			int idx = token.indexOf(':');
			if (idx == -1) {
				// boolean expression
				key = token.trim();
				value = "true";
			} else {
				key = token.substring(0, idx).trim();
				value = token.substring(idx + 1).trim();
			}
			if (key != null) {
				// check if its an abbrevation
				if (ABBR_MAP.containsKey(key)) {
					key = ABBR_MAP.get(key);
				}
				setStyle(key, value);
			}
		}
		
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		
		StringBuffer sb = new StringBuffer();
		for (Iterator<String> it = data.keySet().iterator(); it.hasNext(); ) {
			String key = it.next();
			String value = data.get(key);
			sb.append(key).append(": ").append(value).append(";\n");
		}
		
		return sb.toString();
		
	}

	/**
	 * @return the parentStyle
	 */
	public Style getParentStyle() {
		return parentStyle;
	}

	/**
	 * @param parentStyle the parentStyle to set
	 */
	public void setParentStyle(Style parentStyle) {
		this.parentStyle = parentStyle;
	}
}
