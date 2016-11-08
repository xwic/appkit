/*
 * Copyright (c) NetApp Inc. - All Rights Reserved
 * 
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 * 
 * com.netapp.pulse.system.dyneditor.renderers.DynEditorControl 
 */

package de.xwic.appkit.core.pojoeditor.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import de.jwic.base.Control;
import de.xwic.appkit.core.util.IModelViewTypeConverter;

/**
 * @author Andrei Pat
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
@Inherited
public @interface PojoControl {

	/**
	 * Control label
	 * 
	 * @return
	 */
	String label() default "";

	/**
	 * Control to be displayed
	 * 
	 * @return
	 */
	Class<? extends Control> controlClass();

	/**
	 * Converter to be used to map the property value to the control
	 * 
	 * @return
	 */
	Class<? extends IModelViewTypeConverter> converter() default NullConverter.class;

	/**
	 * Annotations don't allow null values as defaults, so create a dummy class to represent null
	 * 
	 * @author Andrei Pat
	 *
	 */
	public static final class NullConverter implements IModelViewTypeConverter {		
		/*
		 * (non-Javadoc)
		 * 
		 * @see de.xwic.appkit.core.util.IModelViewTypeConverter#convertToViewType(java.lang.Object)
		 */
		@Override
		public Object convertToViewType(Object modelValue) {
			return modelValue;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see de.xwic.appkit.core.util.IModelViewTypeConverter#convertToModelType(java.lang.Object)
		 */
		@Override
		public Object convertToModelType(Object viewValue) {
			return viewValue;
		}

	}
}
