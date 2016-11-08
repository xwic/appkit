/*
 * Copyright (c) NetApp Inc. - All Rights Reserved
 * 
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 * 
 * de.xwic.appkit.core.util.typeconverters.BooleanIntConverter 
 */

package de.xwic.appkit.core.util.typeconverters;


/**
 * @author Andrei Pat
 *
 */
public class IntegerBooleanConverter extends AbstractModelViewConverter<Integer, Boolean> {
	public static final IntegerBooleanConverter INSTANCE = new IntegerBooleanConverter();

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.util.IModelViewTypeConverter#convertToViewType(java.lang.Object)
	 */
	@Override
	public Boolean convertToViewType(Integer modelValue) {
		return modelValue == null? false: modelValue != 0; 
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.core.util.IModelViewTypeConverter#convertToModelType(java.lang.Object)
	 */
	@Override
	public Integer convertToModelType(Boolean viewValue) {
		if (viewValue == null || false == viewValue.booleanValue()) {
			return 0;
		} else {
			return 1;
		}
	}
}
