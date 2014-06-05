package de.xwic.appkit.webbase.utils;

import de.xwic.appkit.core.model.queries.PropertyQuery;
import de.xwic.appkit.core.model.queries.QueryElement;

/**
 * @author Alexandru Bledea
 * @since Jun 2, 2014
 */
public class ColumnFilterAliasHelper {

	private final String property;
	private final boolean requiresAlias;
	private final String joinProperty;

	/**
	 * @param property
	 * @return
	 */
	public static ColumnFilterAliasHelper of(final String property) {
		if (property == null) {
			return new ColumnFilterAliasHelper(property, null);
		}

		final int length = property.length();
		final int lastIndexOf = property.lastIndexOf('.');
		if (lastIndexOf < 1 || lastIndexOf == length - 1) { // if not found or if the property starts or ends with a dot
			return new ColumnFilterAliasHelper(property, null);
		}
		final String joinProperty = property.substring(0, lastIndexOf);
		final String prop = property.substring(lastIndexOf + 1, length);
		return new ColumnFilterAliasHelper(prop, joinProperty);
	}

	/**
	 * @param element
	 * @param leftJoins
	 */
	public ColumnFilterAliasHelper(final QueryElement element, final PropertyQuery leftJoins) {
		this(element.getPropertyName(), leftJoins.getAliasMapping(element.getAlias()));
	}

	/**
	 * @param property
	 * @param joinProperty
	 */
	private ColumnFilterAliasHelper(final String property, final String joinProperty) {
		this.property = property;
		this.joinProperty = joinProperty;
		this.requiresAlias = joinProperty != null;
	}

	/**
	 * @return
	 */
	public String getInitialProperty() {
		if (joinProperty == null) {
			return property;
		}
		return joinProperty + '.' + property;
	}

	/**
	 * @return the property
	 */
	public String getProperty() {
		return property;
	}

	/**
	 * @return the requiresAlias
	 */
	public boolean isRequiresAlias() {
		return requiresAlias;
	}

	/**
	 * @return the joinProperty
	 */
	public String getJoinProperty() {
		return joinProperty;
	}

}