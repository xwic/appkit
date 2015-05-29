/*
 * Copyright (c) NetApp Inc. - All Rights Reserved
 * 
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 * 
 * de.xwic.appkit.webbase.sqlviewer.base.ISqlDataProviderHandler 
 */

package de.xwic.appkit.webbase.sqlviewer.base;

import java.sql.SQLException;
import java.util.List;

import de.jwic.data.Range;
import de.xwic.appkit.core.dao.Limit;
import de.xwic.appkit.core.util.SqlStringBuilder;


/**
 * @author dotto
 * @date May 29, 2015
 * 
 */
public interface ISqlDataProviderHandler {

	/**
	 * @return
	 */
	public SqlStringBuilder getSqlBuilder();
	
	/**
	 * @param sqlBuilder
	 */
	public void setSqlBuilder(SqlStringBuilder sqlBuilder);
	
	/**
	 * @param range
	 * @return
	 * @throws SQLException
	 */
	public List<Object[]> getData(Range range) throws SQLException;
	
	/**
	 * @return
	 * @throws SQLException
	 */
	public Integer getTotal() throws SQLException;
}
