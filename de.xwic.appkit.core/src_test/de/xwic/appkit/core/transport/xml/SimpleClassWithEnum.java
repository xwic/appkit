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
package de.xwic.appkit.core.transport.xml;
/**
 * @author Alexandru Bledea
 * @since Oct 8, 2014
 */
class SimpleClassWithEnum {

	private SimpleClassWithEnumEnum enumeration;

	/**
	 * @param enumeration
	 *            the enumeration to set
	 */
	public void setEnumeration(final SimpleClassWithEnumEnum enumeration) {
		this.enumeration = enumeration;
	}

	/**
	 * @return the enumeration
	 */
	public SimpleClassWithEnumEnum getEnumeration() {
		return enumeration;
	}

	/**
	 * @param enumeration
	 * @return
	 */
	static SimpleClassWithEnum of(final SimpleClassWithEnumEnum enumeration) {
		final SimpleClassWithEnum enumObject = new SimpleClassWithEnum();
		enumObject.setEnumeration(enumeration);
		return enumObject;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((enumeration == null) ? 0 : enumeration.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final SimpleClassWithEnum other = (SimpleClassWithEnum) obj;
		if (enumeration != other.enumeration) {
			return false;
		}
		return true;
	}

	/**
	 * @author Alexandru Bledea
	 * @since Oct 8, 2014
	 */
	enum SimpleClassWithEnumEnum {
		SMALL, MEDIUM, LARGE;
	}

}