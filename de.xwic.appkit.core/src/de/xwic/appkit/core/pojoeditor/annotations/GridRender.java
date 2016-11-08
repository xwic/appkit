/*
 * Copyright (c) NetApp Inc. - All Rights Reserved
 * 
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 * 
 * com.netapp.pulse.system.dyneditor.two.annotations.DynGrid 
 */

package de.xwic.appkit.core.pojoeditor.annotations;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(FIELD)
@Inherited
/**
 * @author Andrei Pat
 *
 */
public @interface GridRender {

	/**
	 * allows specifying an order for the controls
	 * 
	 * @return
	 */
	int order() default Integer.MAX_VALUE;

	/**
	 * how many columns does a control need to span
	 * 
	 * @return
	 */
	int colspan() default 1;
}
