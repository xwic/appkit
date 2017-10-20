/*
 * Copyright (c) NetApp Inc. - All Rights Reserved
 * 
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 * 
 * com.netapp.cxed.model.kpi.impl.dataprovider.SqlStatement 
 */

package de.xwic.appkit.core.util.sql;

import java.util.ArrayList;
import java.util.List;

import de.jwic.data.Range;

/**
 * Construct a SQL statement.
 * 
 * @author lippisch
 */
public class SqlStringBuilder {
	private List<String> columns;
	private String fromTable;
	private List<String> whereConditions;
	private List<String> groupedColumns;
	private List<String> orderByColumns;
	private List<String> orderBySortDirection;
	private List<Object> conditionValues;

	/**
	 * 
	 */
	public SqlStringBuilder(String fromTable) {

		columns = new ArrayList<String>();
		this.fromTable = fromTable;
		whereConditions = new ArrayList<String>();
		groupedColumns = new ArrayList<String>();
		conditionValues = new ArrayList<Object>();
		orderByColumns = new ArrayList<String>();
		orderBySortDirection = new ArrayList<String>();
	}

	/**
	 * Add a column to the SELECT part.
	 * 
	 * @param column
	 */
	public void addColumn(String column) {
		columns.add(column);
	}

	/**
	 * Add a condition to the WHERE statement. All conditions are concatenated using an AND operator.
	 * 
	 * @param condition
	 */
	public void addCondition(String condition) {
		whereConditions.add(condition);
	}

	/**
	 * Add a column to the group by clause.
	 * 
	 * @param column
	 */
	public void addGroupBy(String column) {
		groupedColumns.add(column);
	}

	/**
	 * Add a column to the order by list.
	 * 
	 * @param column
	 */
	public void addOrderBy(String column, boolean ascending) {
		orderByColumns.add(column );
		orderBySortDirection.add((ascending ? "ASC" : "DESC"));
	}

	/**
	 * Generates the SQL Sql Statement.
	 * 
	 * @return
	 */
	public String buildStatement() {
		return buildStatement(false);
	}

	/**
	 * Generates the SQL Sql Statement.
	 * 
	 * @return
	 */
	public String buildStatement(boolean count) {

		StringBuilder sb = new StringBuilder();
		sb.append("SELECT ");
		boolean first = true;

		if (count) {
			sb.append(" COUNT(*) ");
		} else {
			for (String col : columns) {
				if (first) {
					first = false;
				} else {
					sb.append(", ");
				}
				sb.append(col);
			}
		}
		
		sb.append("\n FROM ").append(fromTable);

		if (!whereConditions.isEmpty()) {
			sb.append("\n WHERE ");
			first = true;
			for (String w : whereConditions) {
				if (first) {
					first = false;
				} else {
					sb.append(" AND "); // always concatinate with AND
				}
				sb.append(w);
			}
		}

		if (!groupedColumns.isEmpty() && !count) {
			sb.append("\n GROUP BY ");
			first = true;
			for (String grp : groupedColumns) {
				if (first) {
					first = false;
				} else {
					sb.append(", ");
				}
				sb.append(grp);
			}
		}

		if (!orderByColumns.isEmpty() && !count) {
			sb.append("\n ORDER BY ");
			first = true;
			int idx = 0;
			for (String obc : orderByColumns) {
				if (first) {
					first = false;
				} else {
					sb.append(", ");
				}
				sb.append(obc);
				sb.append(" ").append(orderBySortDirection.get(idx));
				idx++;
			}
		}

		return sb.toString();

	}

	/**
	 * Generates the SQL Sql Statement.
	 * 
	 * @return
	 */
	public String buildStatement(Range range) {
		StringBuilder sb = new StringBuilder();
		sb.append(buildStatement());

		sb.append("\nOFFSET (").append(range.getStart()).append(") ROWS ");
		sb.append("\nFETCH NEXT ").append(range.getMax()).append(" ROWS ONLY");

		return sb.toString();
	}

	/**
	 * @param value
	 */
	public void addConditionValue(Object value) {
		conditionValues.add(value);
	}

	/**
	 * @return the conditionValues
	 */
	public List<Object> getConditionValues() {
		return conditionValues;
	}

	/**
	 * @return the fromTable
	 */
	public String getFromTable() {
		return fromTable;
	}

	/**
	 * @param fromTable
	 *            the fromTable to set
	 */
	public void setFromTable(String fromTable) {
		this.fromTable = fromTable;
	}

	/**
	 * @return the columns
	 */
	public List<String> getColumns() {
		return columns;
	}

	/**
	 * @return the whereConditions
	 */
	public List<String> getWhereConditions() {
		return whereConditions;
	}

	/**
	 * @return the groupedColumns
	 */
	public List<String> getGroupedColumns() {
		return groupedColumns;
	}

	/**
	 * @return the orderByColumns
	 */
	public List<String> getOrderByColumns() {
		return orderByColumns;
	}

	
	/**
	 * @return the orderBySortDirection
	 */
	public List<String> getOrderBySortDirection() {
		return orderBySortDirection;
	}

}
