package de.xwic.appkit.core.model.daos.impl;

import de.xwic.appkit.core.dao.AbstractDAO;
import de.xwic.appkit.core.model.daos.INewsDAO;
import de.xwic.appkit.core.model.entities.INews;
import de.xwic.appkit.core.model.entities.impl.News;

/**
 * DAO Implementation for the News entity.
 * @author lippisch
 */
public class NewsDAO extends AbstractDAO<INews, News> implements INewsDAO {

	/**
	 *
	 */
	public NewsDAO() {
		super(INews.class, News.class);
	}

}
