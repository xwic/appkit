/*
 * Copyright (c) NetApp Inc. - All Rights Reserved
 * 
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 * 
 * com.netapp.pulse.system.dyneditor.two.annotations.DynEditor 
 */

package de.xwic.appkit.core.pojoeditor.annotations;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * @author Andrei Pat
 *
 */
@Retention(RUNTIME)
@Target(TYPE)
@Inherited
public @interface PojoEditor {

	public enum AutoLabels {
		OFF,
		FIELD_NAME,
		SPLIT_FIELD_NAME
	}

	String name() default "";

	AutoLabels autoLabels() default AutoLabels.OFF;
}
