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
package de.xwic.appkit.core.model.queries;

import java.util.Collection;

/**
 * Represents one element of a PropertyQuery.
 * @author Florian Lippisch
 */
public class QueryElement {
	
	/** Logic AND */
	public final static int AND = 0;
	/** Logic OR */
	public final static int OR = 1;
	
	/** key for equals operation */
	public final static String EQUALS = "=";

	/** key for like operation */
	public final static String LIKE = "LIKE";

	/** key for not like operation */
	public final static String NOT_LIKE = "NOT LIKE";

	/** key for not equals operation */
	public final static String NOT_EQUALS = "!=";
	
	/** key for greater then operation */
	public final static String GREATER_THEN = ">";
	
	/** key for lower then operation */
	public final static String LOWER_THEN = "<";
	
	/** key for greater equals then operation */
	public final static String GREATER_EQUALS_THEN = ">=";
	
	/** key for lower equals then operation */
	public final static String LOWER_EQUALS_THEN = "<=";
	
	/** key for in operation */
	public final static String IN = "IN";

	/** key for not in operation */
	public final static String NOT_IN = "NOT IN";

	/** key for is empty operation */
	public final static String IS_EMPTY = "IS EMPTY";

	/** key for is not empty operation */
	public final static String IS_NOT_EMPTY = "IS NOT EMPTY";

	/** the maximum number of elements allowed in {@link #IN} or {@link #NOT_IN}*/
	public final static int MAXIMUM_ELEMENTS_IN = 1000;

	private int linkType = AND;
	private String alias = "obj";
	private String propertyName = null;
	private String operation = EQUALS;
	private Object value = null;
	private boolean timestamp = false;
	private PropertyQuery subQuery = null;
	
	private boolean isCollectionElement = false;
	private boolean rewriteIn;
	private int inLinkType = AND;

	/**
	 * Constructor.
	 */
	public QueryElement() {
		
	}

	/**
	 * @param propertyName
	 * @param operation
	 * @param value
	 */
	public QueryElement(String propertyName, String operation, Object value) {
		super();
		this.propertyName = propertyName;
		this.operation = operation;
		this.value = value;
	}

	/**
	 * @param linkType
	 * @param propertyName
	 * @param operation
	 * @param value
	 */
	public QueryElement(int linkType, String propertyName, String operation, Object value) {
		super();
		this.linkType = linkType;
		this.propertyName = propertyName;
		this.operation = operation;
		this.value = value;
	}
	
	/**
	 * @param linkType
	 * @param subQuery
	 */
	public QueryElement(int linkType, PropertyQuery subQuery) {
		super();
		this.linkType = linkType;
		this.subQuery = subQuery;
	}

	/**
	 * @return the linkType
	 */
	public int getLinkType() {
		return linkType;
	}

	/**
	 * @param linkType the linkType to set
	 */
	public void setLinkType(int linkType) {
		this.linkType = linkType;
	}

	/**
	 * @return the operation
	 */
	public String getOperation() {
		return operation;
	}

	/**
	 * @param operation the operation to set
	 */
	public void setOperation(String operation) {
		this.operation = operation;
	}

	/**
	 * @return the propertyName
	 */
	public String getPropertyName() {
		return propertyName;
	}

	/**
	 * @param propertyName the propertyName to set
	 */
	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	/**
	 * @return the subQuery
	 */
	public PropertyQuery getSubQuery() {
		return subQuery;
	}

	/**
	 * @param subQuery the subQuery to set
	 */
	public void setSubQuery(PropertyQuery subQuery) {
		this.subQuery = subQuery;
	}

	/**
	 * @return the timestamp
	 */
	public boolean isTimestamp() {
		return timestamp;
	}

	/**
	 * @param timestamp the timestamp to set
	 */
	public void setTimestamp(boolean timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * @return the value
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(Object value) {
		this.value = value;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuffer sb = new StringBuffer();
		if (subQuery != null) {
			sb.append("(").append(subQuery).append(")");
		} else {
			sb.append(propertyName)
			  .append(" ")
			  .append(operation)
			  .append(" '")
			  .append(value)
			  .append("'");
		}
		return sb.toString();
	}

	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((alias == null) ? 0 : alias.hashCode());
		result = prime * result + inLinkType;
		result = prime * result + (isCollectionElement ? 1231 : 1237);
		result = prime * result + linkType;
		result = prime * result + ((operation == null) ? 0 : operation.hashCode());
		result = prime * result + ((propertyName == null) ? 0 : propertyName.hashCode());
		result = prime * result + (rewriteIn ? 1231 : 1237);
		result = prime * result + ((subQuery == null) ? 0 : subQuery.hashCode());
		result = prime * result + (timestamp ? 1231 : 1237);
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof QueryElement)) {
			return false;
		}
		QueryElement other = (QueryElement) obj;
		if (alias == null) {
			if (other.alias != null) {
				return false;
			}
		} else if (!alias.equals(other.alias)) {
			return false;
		}
		if (inLinkType != other.inLinkType) {
			return false;
		}
		if (isCollectionElement != other.isCollectionElement) {
			return false;
		}
		if (linkType != other.linkType) {
			return false;
		}
		if (operation == null) {
			if (other.operation != null) {
				return false;
			}
		} else if (!operation.equals(other.operation)) {
			return false;
		}
		if (propertyName == null) {
			if (other.propertyName != null) {
				return false;
			}
		} else if (!propertyName.equals(other.propertyName)) {
			return false;
		}
		if (rewriteIn != other.rewriteIn) {
			return false;
		}
		if (subQuery == null) {
			if (other.subQuery != null) {
				return false;
			}
		} else if (!subQuery.equals(other.subQuery)) {
			return false;
		}
		if (timestamp != other.timestamp) {
			return false;
		}
		if (value == null) {
			if (other.value != null) {
				return false;
			}
		} else if (!value.equals(other.value)) {
			return false;
		}
		return true;
	}

	/**
	 * Clone QueryElement and all objects.
	 * @return
	 */
	public QueryElement cloneElement() {
		QueryElement clone = new QueryElement();
		clone.linkType = linkType;
		clone.operation = operation;
		clone.alias = alias;
		clone.propertyName = propertyName;
		clone.timestamp = timestamp;
		clone.value = value;
		clone.isCollectionElement = isCollectionElement;
		clone.rewriteIn = rewriteIn;
		clone.inLinkType = inLinkType;
		if (subQuery != null){
			clone.subQuery = subQuery.cloneQuery();
		}
		return clone;
	}

	/**
	 * @return the alias
	 */
	public String getAlias() {
		return alias;
	}

	/**
	 * @param alias the alias to set
	 */
	public void setAlias(String alias) {
		this.alias = alias;
	}

	/**
	 * @return the isCollectionElement
	 */
	public boolean isCollectionElement() {
		return isCollectionElement;
	}

	/**
	 * @param isCollectionElement the isCollectionElement to set
	 */
	public void setCollectionElement(boolean isCollectionElement) {
		this.isCollectionElement = isCollectionElement;
	}

	/**
	 * @return the rewriteIn
	 */
	public boolean isRewriteIn() {
		return rewriteIn;
	}

	/**
	 * @param rewriteIn the rewriteIn to set
	 */
	public void setRewriteIn(final boolean rewriteIn) {
		this.rewriteIn = rewriteIn;
	}

	/**
	 * @return the inLinkType
	 */
	public int getInLinkType() {
		return inLinkType;
	}

	/**
	 * Link type for the IN operation when searching collection in collection.
	 * This property dictates if we search for all the elements inside the 
	 * collection or if we search for any in collection  
	 * <pre>(? in elements(property) OR ? in elements(property))</pre> or 
	 * <pre>(? in elements(property) AND ? in elements(property))</pre>
	 * @param inLinkType the inLinkType to set
	 */
	public void setInLinkType(final int inLinkType) {
		this.inLinkType = inLinkType;
	}

	/**
	 * @return
	 */
	public boolean requiresRewrite() {
		if (!rewriteIn || value == null || operation == null) {
			return false;
		}
		if (!QueryElement.IN.equals(operation) && !QueryElement.NOT_IN.equals(operation)) {
			return false;
		}
		if (!Collection.class.isAssignableFrom(value.getClass())) {
			return false;
		}
		return true;
	}
}
