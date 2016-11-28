/*
 * Copyright (c) NetApp Inc. - All Rights Reserved
 * 
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 * 
 * com.netapp.pulse.system.PojoEditor.two.PojoEditor 
 */

package de.xwic.appkit.webbase.pojoeditor;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.jwic.base.Control;
import de.jwic.base.IControlContainer;
import de.xwic.appkit.core.pojoeditor.annotations.CustomRenderer;
import de.xwic.appkit.core.pojoeditor.annotations.GridRenderer;
import de.xwic.appkit.core.pojoeditor.annotations.PojoControl;
import de.xwic.appkit.core.pojoeditor.annotations.PojoControl.NullConverter;
import de.xwic.appkit.core.pojoeditor.annotations.PojoEditor;
import de.xwic.appkit.core.pojoeditor.annotations.PojoEditor.AutoLabels;
import de.xwic.appkit.core.util.IModelViewTypeConverter;
import de.xwic.appkit.webbase.pojoeditor.renderers.IPojoRenderer;
import de.xwic.appkit.webbase.pojoeditor.renderers.PojoGridRenderer;
import de.xwic.appkit.webbase.toolkit.app.EditorToolkit;

/**
 * @author Andrei Pat
 *
 */
public class PojoEditorFactory {

	/**
	 * @param container
	 * @param pojo
	 * @return
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws ClassNotFoundException
	 */
	public static PojoEditorControl createEditor(IControlContainer container, Object pojo)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException {

		IPojoEditorFieldRenderLogic defaultFieldRenderLogic = new IPojoEditorFieldRenderLogic() {

			@Override
			public boolean isRenderField(String fieldName) {
				return true;
			}

			@Override
			public boolean isEnabled(String fieldName) {
				return false;
			}
		};

		return createEditor(container, pojo, defaultFieldRenderLogic);
	}

	public static PojoEditorControl createEditor(IControlContainer container, Object pojo, IPojoEditorFieldRenderLogic fieldRenderLogic)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		Class pojoClass = pojo.getClass();
		if (pojoClass.isAnnotationPresent(PojoEditor.class)) {
			PojoEditor editorConfig = (PojoEditor) pojoClass.getAnnotation(PojoEditor.class);
			EditorToolkit toolkit = new EditorToolkit(pojo);
			PojoEditorControl editor = new PojoEditorControl(container, editorConfig.name(), toolkit);
			AutoLabels autoLabelsStrategy = editorConfig.autoLabels();
			List<PojoEditorField> fields = createFields(editor, pojoClass, toolkit, autoLabelsStrategy, fieldRenderLogic);
			doLayout(pojoClass, editor, fields, fieldRenderLogic);
			return editor;
		} else {
			throw new RuntimeException("Missing annotation: PojoEditor");
		}
	}

	/**
	 * @param editor
	 * @param fieldRenderLogic
	 * @param pojo
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	private static List<PojoEditorField> createFields(PojoEditorControl editor, Class pojoClass, EditorToolkit toolkit,
			AutoLabels autoLabelsStrategy, IPojoEditorFieldRenderLogic fieldRenderLogic)
			throws InstantiationException, IllegalAccessException {

		List<Field> fields = new ArrayList<Field>();
		getAllFields(fields, pojoClass);

		List<PojoEditorField> editorFields = new ArrayList<PojoEditorField>();
		for (Field field : fields) {
			if (field.isAnnotationPresent(PojoControl.class) && fieldRenderLogic.isRenderField(field.getName())) {
				PojoControl ctrlAnnotation = (PojoControl) field.getAnnotation(PojoControl.class);

				String label = ctrlAnnotation.label();

				if (label == null || label.isEmpty()) {
					label = determineLabel(field.getName(), autoLabelsStrategy);
				}
				Class<? extends IModelViewTypeConverter> converterClass = ctrlAnnotation.converter();
				IModelViewTypeConverter converter;
				if (NullConverter.class.equals(converterClass)) {
					converter = null;
				} else {
					converter = converterClass.newInstance();
				}

				Control control = toolkit.createControl(ctrlAnnotation.controlClass(), editor, field.getName(), converter);

				PojoEditorField editorField = new PojoEditorField(label, control, field.getName());
				editorFields.add(editorField);
			}
		}
		return editorFields;
	}

	/**
	 * If a label is not set, try to guess one, based on the AutoLabels strategy chosen
	 * 
	 * @param fieldName
	 * @param strategy
	 * @return
	 */
	private static String determineLabel(String fieldName, AutoLabels strategy) {
		switch (strategy) {
		case OFF:
			return "";
		case FIELD_NAME:
			return upperCaseFirstLetter(fieldName);
		case SPLIT_FIELD_NAME:
			String[] words = fieldName.split("(?=\\p{Upper})");
			StringBuffer sb = new StringBuffer();
			for (String w : words) {
				sb.append(upperCaseFirstLetter(w)).append(" ");
			}
			sb.setLength(sb.length() - 1);
			return sb.toString();
		default:
			return "";
		}
	}

	/**
	 * Make the first character of the string uppercase
	 * 
	 * @param input
	 * @return
	 */
	private static String upperCaseFirstLetter(String input) {
		if (input != null && !input.isEmpty()) {
			return input.substring(0, 1).toUpperCase() + input.substring(1);
		} else {
			return "";
		}
	}

	/**
	 * @param editor
	 * @param fields
	 * @param fieldRenderLogic
	 * @param pojo
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	private static void doLayout(Class<?> pojoClass, PojoEditorControl editor, List<PojoEditorField> fields,
			IPojoEditorFieldRenderLogic fieldRenderLogic) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		IPojoRenderer renderer = null;
		if (pojoClass.isAnnotationPresent(CustomRenderer.class)) {
			String rendererClass = pojoClass.getAnnotation(CustomRenderer.class).clazz();
			renderer = (IPojoRenderer) Class.forName(rendererClass).newInstance();
		}
		if (pojoClass.isAnnotationPresent(GridRenderer.class)) {
			renderer = new PojoGridRenderer();
		}
		renderer.setPojoClass(pojoClass);
		renderer.setEditorControl(editor);
		renderer.setFields(fields);
		renderer.setFieldRenderLogic(fieldRenderLogic);
		editor.setRenderer(renderer);
	}

	/**
	 * get all fields from a class, including superclass(es) fields
	 * 
	 * @param fields
	 * @param type
	 * @return
	 */
	public static List<Field> getAllFields(List<Field> fields, Class<?> type) {
		fields.addAll(Arrays.asList(type.getDeclaredFields()));

		if (type.getSuperclass() != null) {
			fields = getAllFields(fields, type.getSuperclass());
		}

		return fields;
	}

	/**
	 * Find a field in the class or super class hierarchy
	 * 
	 * @param clazz
	 * @param name
	 * @return
	 */
	public static Field findField(Class<?> clazz, String name) {
		Class<?> searchType = clazz;
		while (!Object.class.equals(searchType) && searchType != null) {
			Field[] fields = searchType.getDeclaredFields();
			for (Field field : fields) {
				if (name.equals(field.getName())) {
					return field;
				}
			}
			searchType = searchType.getSuperclass();
		}
		return null;
	}

}
