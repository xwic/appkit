/*******************************************************************************
 * Copyright 2015 xWic group (http://www.xwic.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 		http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 *  
 *******************************************************************************/
/**
 *
 */
package de.xwic.appkit.core.util;

import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.model.entities.IPicklistEntry;
import de.xwic.appkit.core.model.entities.util.Picklists;

/**
 * @author Alexandru Bledea
 * @since Aug 22, 2014
 * @deprecated - use de.xwic.appkit.core.util.Functions instead
 */
@Deprecated()
public final class Evaluators {

	private static final Function<? extends IEntity, Long> ID_EXTRACTOR = new EntityIdExtractor<IEntity>();
	
	/**
	 *
	 */
	private Evaluators() {
	}

	@SuppressWarnings ("rawtypes")
	private static Function IDENTITY_EVALUATOR = new Function() {

		@Override
		public Object evaluate(final Object obj) {
			return obj;
		}
	};

	/**
	 * @return
	 */
	@SuppressWarnings ("unchecked")
	public static <E> Function<E, E> identity() {
		return IDENTITY_EVALUATOR;
	}
	
	

	/**
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static final <T extends IEntity> Function<T, Long> idExtractor() {
		return (Function<T, Long>) ID_EXTRACTOR;
	}

	public static final Function<Number, Integer> NUMBER_TO_INT = new Function<Number, Integer>() {
		@Override
		public Integer evaluate(final Number obj) {
			return obj.intValue();
		}
	};

	public static final Function<Number, Long> NUMBER_TO_LONG = new Function<Number, Long>() {
		@Override
		public Long evaluate(final Number obj) {
			return obj.longValue();
		}
	};

	public static final Function<IEntity, Long> VERSION_EVAL = new Function<IEntity, Long>() {
		@Override
		public Long evaluate(final IEntity obj) {
			return obj.getVersion();
		}
	};

	public static final Function<Integer, String> INT_TO_STRING = new Function<Integer, String>() {
		@Override
		public String evaluate(final Integer obj) {
			if (obj == null) {
				return null;
			}
			return obj.toString();
		}
	};

	public static final Function<Long, String> LONG_TO_STRING = new Function<Long, String>() {
		@Override
		public String evaluate(final Long obj) {
			if (obj == null) {
				return null;
			}
			return obj.toString();
		}
	};

	private final static Function<? extends IEntity, Long> ENTITY_ID = new Function<IEntity, Long>() {

		@Override
		public Long evaluate(final IEntity obj) {
			return obj.getId();
		}
	};

	/**
	 * @return
	 */
	@SuppressWarnings ("unchecked")
	public static <T extends IEntity> Function<T, Long> entityIdEvaluator(){
		return (Function<T, Long>) ENTITY_ID;
	}

	public static <E extends Exception> Function<E,String> exceptionMessageEvaluator(){
		return new Function<E,String>() {

			@Override
			public String evaluate(final E arg0) {
				return arg0.getMessage();
			}
		};
	}

	/**
	 * @param pe
	 * @return
	 */
	public static String getPicklistKey(final IPicklistEntry pe) {
		return evaluateIfNotNull(pe, Picklists.GET_KEY);
	}

	/**
	 * @param entity
	 * @return
	 */
	public static Long getVersion(final IEntity entity) {
		return evaluateIfNotNull(entity, VERSION_EVAL);
	}

	/**
	 * @param evaluator
	 * @param o
	 * @return
	 */
	public static <T, O> T evaluateIfNotNull(final O o, final Function<O, T> evaluator) {
		if (o != null) {
			return evaluator.evaluate(o);
		}
		return null;
	}

	/**
	 * @return
	 */
	public static <T extends IEntity> Function<T, String> idToString() {
		Function<T, Long> idExtractor = Evaluators.entityIdEvaluator();
		return Functions.compose(idExtractor, Functions.<Long> toStringFunction());
	}


	/**
	 * @author Alexandru Bledea
	 * @since Oct 23, 2014
	 * @param <T>
	 */
	private static class EntityIdExtractor<T extends IEntity> implements Function<T, Long> {

		/* (non-Javadoc)
		 * @see de.xwic.appkit.core.util.Function#evaluate(java.lang.Object)
		 */
		@Override
		public Long evaluate(final T obj) {
			return obj.getId();
		}

	}

}
