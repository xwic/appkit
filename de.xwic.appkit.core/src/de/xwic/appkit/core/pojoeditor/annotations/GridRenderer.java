/*
 * Copyright (c) NetApp Inc. - All Rights Reserved
 * 
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 * 
 * com.netapp.pulse.system.dyneditor.two.annotations.DynEditorRenderer 
 */

package de.xwic.appkit.core.pojoeditor.annotations;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(TYPE)
@Inherited
/**
 * @author Andrei Pat
 *
 */
public @interface GridRenderer {

	/**
	 * @return
	 */
	int columns() default 1;
}
