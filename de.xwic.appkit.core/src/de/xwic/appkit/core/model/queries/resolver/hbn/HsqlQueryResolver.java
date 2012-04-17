/*
 * Copyright 2005 pol GmbH
 *
 * de.xwic.appkit.core.model.queries.resolver.hbn.HsqlQueryResolver
 * Created on 12.09.2005
 *
 */
package de.xwic.appkit.core.model.queries.resolver.hbn;

import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;

import de.xwic.appkit.core.dao.EntityQuery;
import de.xwic.appkit.core.dao.impl.hbn.HibernateUtil;
import de.xwic.appkit.core.model.queries.HsqlQuery;
import de.xwic.appkit.core.model.util.DateUtils;

/**
 * @author Florian Lippisch
 * @author Christian Jäckel
 */
public class HsqlQueryResolver extends QueryResolver {
	/** logger */
	protected final Log log = LogFactory.getLog(getClass());

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.xwic.appkit.core.dao.IEntityQueryResolver#resolve(de.xwic.appkit.core.dao.EntityQuery)
	 */
	public Object resolve(Class<? extends Object> entityClass, EntityQuery entityQuery, boolean justCount) {

		Session session = HibernateUtil.currentSession();
		HsqlQuery query = (HsqlQuery) entityQuery;
		PicklistEnhancedQueryString queryStr = new PicklistEnhancedQueryString(entityClass.getName(), query.getHsqlQuery(),
				query.getUserLanguage(), justCount);
		if (!justCount) {
			queryStr.whereBuf.append(addSortingClause(entityQuery, "obj", true, queryStr.queryBuf, entityClass));
		}

		String theQuery = queryStr.toString();
		theQuery = DateUtils.getInstance().addSQLConvert(new Locale(query.getLanguageId(), query.getUserCountry()), theQuery);
		log.debug(theQuery);
		return session.createQuery(theQuery);
	}

	/**
	 * Query enhancer.
	 * <p>
	 * Finds occurrences of PICKTEXT_MARK in userClause and replaces them with
	 * picklist enhancements...
	 * <p>
	 * (Handles PICKTEXT_MARK in quoted text correctly.)
	 */
	public static class PicklistEnhancedQueryString {
		static final char PICKTEXT_MARK = '#';

		static final String OBJ_ALIAS = "obj";

		static final String OBJ_PREFIX = OBJ_ALIAS + ".";

		static final String PTXT_POSTFIX = ".bezeichnung ";

		private String userLang;

		private StringBuffer queryBuf;

		private StringBuffer whereBuf;

		// private String qstr;
		int pickRefCnt = 0;

		PicklistEnhancedQueryString(String entityTypeName, String userClause, String userLang, boolean justCount) {
			this.userLang = userLang;
			this.queryBuf = new StringBuffer(512);
			if (justCount) {
				this.queryBuf.append("\nselect\n\t").append("count(*)");
			} else {
				this.queryBuf.append("\nselect\n\t").append(OBJ_ALIAS);
			}
			this.queryBuf.append("\nfrom\n\t").append(entityTypeName).append(" as ").append(OBJ_ALIAS);

			this.whereBuf = new StringBuffer(256);
			this.whereBuf.append("\nwhere\n");
			StringBuffer userWhere = enhancedUserClause(userClause);
			this.whereBuf.append("(\n").append(userWhere).append("\n)\n");
			// qstr= this.queryBuf.append(whereBuf).toString();
		}

		private String addPicklistJoin(String propNamePath) {
			String propName = propNamePath.substring(propNamePath.lastIndexOf('.') + 1);
			String ptxtPropName = "ptxt" + Integer.toString(++pickRefCnt) + '_' + propName;

			this.queryBuf.append("\n\tjoin ") // add picklist join to from
												// clause
					.append(propNamePath).append(".pickTextValues as ").append(ptxtPropName);
			this.whereBuf.append("\t") // add language constraint to where
										// clause
					.append(ptxtPropName).append(".languageID = '").append(userLang).append("' and\n");

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

	}// QueryString
}
