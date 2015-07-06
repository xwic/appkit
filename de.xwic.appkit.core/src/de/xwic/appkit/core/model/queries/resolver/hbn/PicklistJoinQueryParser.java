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
package de.xwic.appkit.core.model.queries.resolver.hbn;

/**
 * @author Ronny Pfretzschner
 *
 */
public class PicklistJoinQueryParser {
	
	static final char PICKTEXT_MARK = '#';

	static final String OBJ_ALIAS = "obj";

	static final String OBJ_PREFIX = OBJ_ALIAS + ".";

	static final String PTXT_POSTFIX = ".bezeichnung ";

	private String userLang;

	private StringBuffer queryBuf;

	private StringBuffer whereBuf;

	// private String qstr;
	int pickRefCnt = 0;

	PicklistJoinQueryParser(String entityTypeName, String fromClause, String userClause, String userLang, boolean justCount) {
		this.userLang = userLang;
		this.queryBuf = new StringBuffer(fromClause);
		/*if (justCount) {
			this.queryBuf.append("\nselect\n\t").append("count(*)");
		} else {
			this.queryBuf.append("\nselect\n\t").append(OBJ_ALIAS);
		}
		this.queryBuf.append("\nfrom\n\t").append(entityTypeName).append(" as ").append(OBJ_ALIAS); */

		this.whereBuf = new StringBuffer(256);
		this.whereBuf.append("\nwhere\n");
		StringBuffer userWhere = enhancedUserClause(userClause);
		this.whereBuf.append("(\n").append(userWhere).append("\n)\n");
		// qstr= this.queryBuf.append(whereBuf).toString();
	}

	private String addPicklistJoin(String propNamePath) {
		String propName = propNamePath.substring(propNamePath.lastIndexOf('.') + 1);
		String ptxtPropName = "ptxt" + Integer.toString(++pickRefCnt) + '_' + propName;

		this.queryBuf.append("\n\tleft outer join ") // add picklist join to from
											// clause
				.append(propNamePath).append(".pickTextValues as ").append(ptxtPropName);
		this.whereBuf.append("\t") // add language constraint to where
									// clause
				.append("(lower(")
				.append(ptxtPropName).append(".languageID) = '").append(userLang.toLowerCase()).append("'")
				.append(" OR ").append(ptxtPropName).append(".languageID IS NULL")
				.append(") and\n");

		return ptxtPropName;
	}

	private StringBuffer enhancedUserClause(String s) {
		StringBuffer buf = new StringBuffer(1024); // size guess
		boolean inQuotes = false;
		for (int p = 0; p < s.length();) {
			char c = s.charAt(p++);
			switch (c) {
				case '\'':
					inQuotes = !inQuotes; // toggle quotes
					buf.append(c);
					break;

				case '\\':
					buf.append(c);
					if (p < s.length())
						buf.append(c = s.charAt(p++)); // consume
														// subsequent char
					break;

				case PICKTEXT_MARK:
					if (inQuotes)
						buf.append(c);
					else {
						String propNamePath = extractedPicklistProperty(buf, s, p);
						if (propNamePath != null) {
							String ptextName = addPicklistJoin(propNamePath);
							buf.append(ptextName).append(PTXT_POSTFIX);
						}
					}
					break;

				default:
					buf.append(c);
					break;
			}
		}// for
		return buf;
	}

	String extractedPicklistProperty(StringBuffer buf, String userQuery, int p) {
		String propNamePath = null;
		int objPos = userQuery.lastIndexOf(OBJ_PREFIX, p - 1);
		if (objPos >= 0) {
			propNamePath = userQuery.substring(objPos, p - 1).trim();
			buf.delete(buf.length() - (p - objPos - 1), buf.length()); // remove
																		// Picklist
																		// property
		}
		return propNamePath;
	}

	public String toString() {
		return queryBuf.toString() + whereBuf;
	}

	/**
	 * returns the query string.
	 * 
	 * @return
	 */
	public String getQueryString() {
		return queryBuf.toString();
	}

	/**
	 * Returns the where string.
	 * 
	 * @return
	 */
	public String getWhereString() {
		return whereBuf.toString();
	}
}
