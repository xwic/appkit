package de.xwic.appkit.webbase.utils;

import de.jwic.base.IControl;
import de.jwic.base.SessionContext;
import de.xwic.appkit.core.util.VoidEvaluator;

/**
 * @author Adrian Ionescu
 */
public class NotificationUtil {

	public static final String WARNING_CSS_CLASS = "warning";
	public static final String ERROR_CSS_CLASS = "error";
	public static final String INFO_CSS_CLASS = "information";


	/**
	 * @param message
	 * @param sessionContext
	 */
	public static void showWarning(String message, SessionContext sessionContext){
		sessionContext.notifyMessage(message, WARNING_CSS_CLASS);
	}

	/**
	 * @param message
	 * @param sessionContext
	 * @param duration (in seconds)
	 */
	public static void showWarning(String message, SessionContext sessionContext, double duration) {
		sessionContext.notifyMessage(message, WARNING_CSS_CLASS, duration, 0);
	}

	/**
	 * @param message
	 * @param sessionContext
	 */
	public static void showError(String message, SessionContext sessionContext){
		sessionContext.notifyMessage(message, ERROR_CSS_CLASS);
	}

	/**
	 * @param message
	 * @param sessionContext
	 * @param duration (in seconds)
	 */
	public static void showError(String message, SessionContext sessionContext, double duration) {
		sessionContext.notifyMessage(message, ERROR_CSS_CLASS, duration, 0);
	}

	/**
	 * @param message
	 * @param sessionContext
	 */
	public static void showInfo(String message, SessionContext sessionContext){
		sessionContext.notifyMessage(message, INFO_CSS_CLASS);
	}

	/**
	 * @param message
	 * @param sessionContext
	 * @param duration (in seconds)
	 */
	public static void showInfo(String message, SessionContext sessionContext, double duration) {
		sessionContext.notifyMessage(message, INFO_CSS_CLASS, duration, 0);
	}

	/**
	 * @author Alexandru Bledea
	 * @since Sep 7, 2013
	 */
	public static class LazyNotificationUtil extends VoidEvaluator<String>{

		private final SessionContext sessionContext;

		/**
		 * @param c
		 */
		public LazyNotificationUtil(IControl c) {
			if (c == null) {
				throw new IllegalStateException("No such control");
			}
			sessionContext = c.getSessionContext();
		}

		/* (non-Javadoc)
		 * @see de.xwic.appkit.core.util.VoidEvaluator#evaluateNoResult(java.lang.Object)
		 */
		@Override
		public void evaluateNoResult(String obj) {
			showError(obj, sessionContext);
		}

	}


}
