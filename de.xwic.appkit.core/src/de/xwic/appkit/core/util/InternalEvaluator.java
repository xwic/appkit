/**
 *
 */
package de.xwic.appkit.core.util;

/**
 * @author Alexandru Bledea
 * @since Jul 31, 2013
 */
class InternalEvaluator {

	/**
	 * @param obj
	 * @param skipNullObjects
	 * @param generator
	 * @param skipNullValues
	 * @param result
	 * @return
	 * @throws DuplicateKeyException
	 */
	static <Result, Obj> EvaluationResult<Result> evaluate(Obj obj, boolean skipNullObjects, ILazyEval<Obj, Result> generator,
			boolean skipNullValues, boolean allowDupes, IDupeChecker<Result> where, EvaluationResult<Result> result)
			throws DuplicateKeyException {
		result.clear();
		boolean skip = false;
		if (obj == null) {
			if (skipNullObjects) {
				skip = true;
			} else {
				throw new NullPointerException("Null Value in Collection");
			}
		} else {
			Result evaluate = generator.evaluate(obj);
			if (evaluate == null && skipNullValues) {
				skip = true;
			} else {
				if (!allowDupes && where.checkIfDupe(evaluate)) {
					throw new DuplicateKeyException(evaluate);
				}
				result.result = evaluate;
			}
		}
		result.skip = skip;
		return result;
	}

	/**
	 * @param obj
	 * @param generator
	 * @param result
	 * @return
	 */
	static <Result, Obj> EvaluationResult<Result> evaluate(Obj obj, ILazyEval<Obj, Result> generator, EvaluationResult<Result> result) {
		result.clear();
		boolean skip = false;
		if (obj == null) {
			skip = true;
		} else {
			Result evaluate = generator.evaluate(obj);
			if (evaluate == null) {
				skip = true;
			} else {
				result.result = evaluate;
			}
		}
		result.skip = skip;
		return result;
	}

	/**
	 * @author Alexandru Bledea
	 * @since Jul 31, 2013
	 * @param <What>
	 */
	static interface IDupeChecker<What> {

		/**
		 * @param what
		 * @return
		 */
		boolean checkIfDupe(What what);
	}

	/**
	 * @author Alexandru Bledea
	 * @since Jul 31, 2013
	 * @param <Result>
	 */
	static class EvaluationResult<Result> {

		private boolean skip;
		private Result result;

		/**
		 * @return the skip
		 */
		public boolean skip() {
			return skip;
		}

		/**
		 * @return the result
		 */
		public Result getResult() {
			return result;
		}

		/**
		 *
		 */
		public void clear() {
			skip = false;
			result = null;
		}
	}
}
