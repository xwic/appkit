/*
 * Copyright (c) NetApp Inc. - All Rights Reserved
 * 
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 * 
 * com.netapp.pulse.cap.ui.editor.EmployeeComboContentProvider 
 */
package de.xwic.appkit.webbase.entityselection.people;

import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.jwic.controls.combo.FilteredRange;
import de.jwic.data.Range;
import de.xwic.appkit.core.dao.EntityQuery;
import de.xwic.appkit.core.dao.Limit;
import de.xwic.appkit.core.model.entities.IMitarbeiter;
import de.xwic.appkit.core.model.queries.PropertyQuery;
import de.xwic.appkit.webbase.entityselection.EntityComboContentProvider;
import de.xwic.appkit.webbase.entityselection.IEntitySelectionContributor;

/**
 * @author nicu
 *
 */
public class EmployeeComboContentProvider extends EntityComboContentProvider<IMitarbeiter> {

	private static String DELIMS = ", %";
	private static String FIRST_NAME_PATTERN = "(%?\\s*fn:\\s*)(.+[^%])(%?)";
	private static String LAST_NAME_PATTERN = "(%?\\s*ln:\\s*)(.+[^%])(%?)";
	private static String USER_NAME_PATTERN = "(%?\\s*un:\\s*)(.+[^%])(%?)";

	public EmployeeComboContentProvider(IEntitySelectionContributor contributor) {
		super(contributor);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Iterator<IMitarbeiter> getContentIterator(Range range) {

		String filter = null;
		Limit limit = new Limit(range.getStart(), range.getMax());
		if (range instanceof FilteredRange) {
			FilteredRange fRange = (FilteredRange) range;
			filter = fRange.getFilter();
		}

		PropertyQuery query = createFilterQuery(filter);

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

		List<IMitarbeiter> entities = dao.getEntities(limit, finalQuery);
		return entities.iterator();
	}

	/**
	 * @return
	 */
	private PropertyQuery createFilterQuery(String filter) {

		PropertyQuery query = new PropertyQuery();

		if (filter != null) {

			Pattern firstNamePattern = Pattern.compile(FIRST_NAME_PATTERN);
			Pattern lastNamePattern = Pattern.compile(LAST_NAME_PATTERN);
			Pattern userNamePattern = Pattern.compile(USER_NAME_PATTERN);
			Matcher firstNameMatcher = firstNamePattern.matcher(filter);
			Matcher lastNameMatcher = lastNamePattern.matcher(filter);
			Matcher userNameMatcher = userNamePattern.matcher(filter);

			if (firstNameMatcher.find()) {

				String firstName = firstNameMatcher.group(2);

				firstName = "%" + firstName + "%";

				query.addLike("vorname", firstName);

			} else if (lastNameMatcher.find()) {

				String lastName = lastNameMatcher.group(2);

				lastName = "%" + lastName + "%";

				query.addLike("nachname", lastName);

			} else if (userNameMatcher.find()) {

				String userName = userNameMatcher.group(2);

				userName = "%" + userName + "%";

				query.addLike("logonName", userName);

			} else {
				StringTokenizer st = new StringTokenizer(filter, DELIMS);

				while (st.hasMoreTokens()) {

					PropertyQuery subQuery = new PropertyQuery();
					String token = st.nextToken();

					token = "%" + token + "%";

					if (!queryProperties.isEmpty()) {
						subQuery.addLike(queryProperties.get(0), token);
						for (int i = 1; i < queryProperties.size(); i++) {
							subQuery.addOrLike(queryProperties.get(i), token);
						}
					}
					query.addSubQuery(subQuery);
				}
			}
		}

		return query;
	}
}
