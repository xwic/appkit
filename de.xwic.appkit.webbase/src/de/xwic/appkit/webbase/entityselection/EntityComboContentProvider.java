package de.xwic.appkit.webbase.entityselection;

import java.util.Iterator;
import java.util.List;

import de.jwic.controls.combo.FilteredRange;
import de.jwic.data.IContentProvider;
import de.jwic.data.Range;
import de.xwic.appkit.core.dao.DAO;
import de.xwic.appkit.core.dao.EntityQuery;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.dao.Limit;
import de.xwic.appkit.core.model.queries.PropertyQuery;

/**
 * 
 * <a href="mailto:vzhovtiuk@gmail.com">Vitaliy Zhovtyuk</a>
 */
public class EntityComboContentProvider<E extends IEntity> implements IContentProvider<E> {

	protected List<String> queryProperties;
	protected DAO dao;
	
	protected IEntitySelectionContributor contributor;
	
	public EntityComboContentProvider(IEntitySelectionContributor contributor) {
		this.contributor = contributor;
		
		this.queryProperties = contributor.getSelectionModel().getQueryProperties();
		this.dao = contributor.getListModel().getDAO();
	}

	@Override
	public Iterator<E> getChildren(E object) {
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Iterator<E> getContentIterator(Range range) {
		String filter = null;
		Limit limit = new Limit(range.getStart(), range.getMax());
		if (range instanceof FilteredRange) {
			FilteredRange fRange = (FilteredRange) range;
			filter = fRange.getFilter();
		}
		
		PropertyQuery query = new PropertyQuery();
		if (filter != null && queryProperties != null) {
			
			// normalize to %FILTER%
			if (!filter.startsWith("%")) {
				filter = "%" + filter;
			}
			
			if (!filter.endsWith("%")) {
				filter = filter + "%";
			}
			
			if (!queryProperties.isEmpty()) {
				query.addLike(queryProperties.get(0), filter);
				for(int i = 1; i < queryProperties.size(); i++) {
					query.addOrLike(queryProperties.get(i), filter);
				}
			}
		}
		
		// keep the sorting of the original query of the list model
		EntityQuery originalQuery = contributor.getListModel().getOriginalQuery();

		PropertyQuery finalQuery = new PropertyQuery();
		
		EntityQuery currentQuery = contributor.getListModel().getQuery();
		if (currentQuery instanceof PropertyQuery) {
			// add the existing query as an AND, if it's not empty !
			PropertyQuery pq = (PropertyQuery) currentQuery;
			if (!pq.getElements().isEmpty()) {
				finalQuery.addSubQuery(pq);
			}
		}
		
		if (!query.getElements().isEmpty()) {
			// add the custom created query now
			finalQuery.addSubQuery(query);
		}
		
		finalQuery.setSortField(originalQuery.getSortField());
		finalQuery.setSortDirection(originalQuery.getSortDirection());
		
		List<E> entities = dao.getEntities(limit, finalQuery);
		return entities.iterator();
		
	}

	/* (non-Javadoc)
	 * @see de.jwic.base.IContentProvider#getObjectFromKey(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public E getObjectFromKey(String uniqueKey) {
		if (uniqueKey == null || uniqueKey.isEmpty()) {
			return null;
		}
		return (E) dao.getEntity(Long.parseLong(uniqueKey));
	}

	/* (non-Javadoc)
	 * @see de.jwic.base.IContentProvider#getTotalSize()
	 */
	@Override
	public int getTotalSize() {
		return 0;
	}

	/* (non-Javadoc)
	 * @see de.jwic.base.IContentProvider#getUniqueKey(java.lang.Object)
	 */
	@Override
	public String getUniqueKey(IEntity entity) {
		return Long.toString(entity.getId());
	}

	/* (non-Javadoc)
	 * @see de.jwic.base.IContentProvider#hasChildren(java.lang.Object)
	 */
	@Override
	public boolean hasChildren(IEntity object) {
		return false;
	}
	

}
