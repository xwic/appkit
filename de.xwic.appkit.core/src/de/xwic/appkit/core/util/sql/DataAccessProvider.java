/*
 * Copyright (c) NetApp Inc. - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 *
 * com.netapp.pulse.model.util.dataaccess.DataAccessProvider
 */
package de.xwic.appkit.core.util.sql;

import java.util.List;

/**
 * @author Alexandru Bledea
 * @since Jun 25, 2015
 */
public interface DataAccessProvider {

	/**
	 * @param hql
	 * @param params
	 * @return
	 */
	List<Integer> getIntsHql(String hql, Object... params);

	/**
	 * @param hql
	 * @param objects
	 * @return
	 */
	List<Long> getLongsHql(String hql, Object... params);

}
