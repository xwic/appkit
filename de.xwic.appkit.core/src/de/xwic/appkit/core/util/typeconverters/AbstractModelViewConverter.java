/*
 * Copyright (c) NetApp Inc. - All Rights Reserved
 * 
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 * 
 * de.xwic.appkit.core.util.typeconverters.AbstractModelViewConverter 
 */

package de.xwic.appkit.core.util.typeconverters;

import de.xwic.appkit.core.util.IModelViewTypeConverter;

/**
 * @author Andrei Pat
 *
 */
public abstract class AbstractModelViewConverter<M, V> implements IModelViewTypeConverter<M, V> {

	/**
	 * Handy if you have a type converter already set up, but you want to switch the model type with the view type
	 * 
	 * @return
	 */
	public IModelViewTypeConverter<V, M> reverse() {
		return new IModelViewTypeConverter<V, M>() {

			@Override
			public M convertToViewType(V modelValue) {
				return AbstractModelViewConverter.this.convertToModelType(modelValue);
			}

			@Override
			public V convertToModelType(M viewValue) {
				return AbstractModelViewConverter.this.convertToViewType(viewValue);
			}

		};
	}

	/**
	 * Daisychains two converters together so the output of one converter becomes the input of the other converter
	 * 
	 * @param otherConverter
	 * @return
	 */
	public <M1> IModelViewTypeConverter<M1, V> chain(final IModelViewTypeConverter<M1, M> otherConverter) {		
		return new IModelViewTypeConverter<M1, V>() {

			@Override
			public V convertToViewType(M1 modelValue) {
				return AbstractModelViewConverter.this.convertToViewType(otherConverter.convertToViewType(modelValue));
			}

			@Override
			public M1 convertToModelType(V viewValue) {
				return otherConverter.convertToModelType(AbstractModelViewConverter.this.convertToModelType(viewValue));
			}
		};
	}
}
