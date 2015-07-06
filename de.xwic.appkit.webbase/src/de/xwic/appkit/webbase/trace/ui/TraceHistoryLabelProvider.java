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

package de.xwic.appkit.webbase.trace.ui;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.jwic.controls.tableviewer.CellLabel;
import de.jwic.controls.tableviewer.ITableLabelProvider;
import de.jwic.controls.tableviewer.RowContext;
import de.jwic.controls.tableviewer.TableColumn;
import de.xwic.appkit.core.dao.DAO;
import de.xwic.appkit.core.trace.ITraceCategory;
import de.xwic.appkit.core.trace.ITraceContext;
import de.xwic.appkit.webbase.trace.HttpTraceFilter;

/**
 * @author lippisch
 */
public class TraceHistoryLabelProvider implements ITableLabelProvider {

	private DateFormat dfDayTime = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
	
	
	/* (non-Javadoc)
	 * @see de.jwic.ecolib.tableviewer.ITableLabelProvider#getCellLabel(java.lang.Object, de.jwic.ecolib.tableviewer.TableColumn, de.jwic.ecolib.tableviewer.RowContext)
	 */
	@Override
	public CellLabel getCellLabel(Object row, TableColumn column, RowContext rowContext) {
		
		ITraceContext tx = (ITraceContext)row;
		CellLabel cell = new CellLabel();
		
		if ("startTime".equals(column.getUserObject())) {
			cell.text = dfDayTime.format(new Date(tx.getStartTime()));
			
		} else if ("duration".equals(column.getUserObject())) {
			cell.text = Long.toString(tx.getDuration()) + "ms";
			
		} else if ("user".equals(column.getUserObject())) {
			cell.text = tx.getAttribute("username");
			
		} else if ("method".equals(column.getUserObject())) {
			cell.text = tx.getAttribute(HttpTraceFilter.ATTR_METHOD);
			
		} else if ("remote-user".equals(column.getUserObject())) {
			cell.text = tx.getAttribute(HttpTraceFilter.ATTR_REMOTE_USER);

		} else if ("ip".equals(column.getUserObject())) {
			cell.text = tx.getAttribute(HttpTraceFilter.ATTR_REMOTE_ADDR);

		} else if ("uri".equals(column.getUserObject())) {
			cell.text = tx.getAttribute(HttpTraceFilter.ATTR_REQUEST_URI);

		} else if ("jwic-control".equals(column.getUserObject())) {
			cell.text = tx.getAttribute(HttpTraceFilter.ATTR_JWIC_CONTROL);
		
		} else if ("jwic-action".equals(column.getUserObject())) {
			cell.text = tx.getAttribute(HttpTraceFilter.ATTR_JWIC_ACTION);

		} else if (HttpTraceFilter.ATTR_SITE_MODULE.equals(column.getUserObject())) {
			cell.text = tx.getAttribute(HttpTraceFilter.ATTR_SITE_MODULE);

		} else if (HttpTraceFilter.ATTR_SITE_SUBMODULE.equals(column.getUserObject())) {
			cell.text = tx.getAttribute(HttpTraceFilter.ATTR_SITE_SUBMODULE);

		} else if (HttpTraceFilter.ATTR_USER_PATH.equals(column.getUserObject())) {
			cell.text = tx.getAttribute(HttpTraceFilter.ATTR_USER_PATH);

		} else if ("info".equals(column.getUserObject())) {
			cell.text = tx.getInfo();
		
		} else if ("name".equals(column.getUserObject())) {
			cell.text = tx.getName();

		} else if ("dao-time".equals(column.getUserObject())) {
			ITraceCategory traceCategory = tx.getTraceCategory(DAO.TRACE_CAT);
			if (traceCategory != null) {
				cell.text = Long.toString(traceCategory.getTotalDuration());
			}

		} else if ("dao-count".equals(column.getUserObject())) {
			ITraceCategory traceCategory = tx.getTraceCategory(DAO.TRACE_CAT);
			if (traceCategory != null) {
				cell.text = Integer.toString(traceCategory.getCount());
			}

		}
		
		if (cell.text == null) {
			cell.text = "";
		}
		
		return cell;
	}

}
