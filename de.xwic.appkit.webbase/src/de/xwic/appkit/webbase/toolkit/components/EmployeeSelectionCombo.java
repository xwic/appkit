/**
 *
 */
package de.xwic.appkit.webbase.toolkit.components;

import java.util.List;

import de.jwic.base.IControlContainer;
import de.xwic.appkit.core.dao.DAO;
import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.model.daos.IMitarbeiterDAO;
import de.xwic.appkit.core.model.entities.IMitarbeiter;
import de.xwic.appkit.core.model.queries.PropertyQuery;

/**
 * Selection Control for employees.
 *
 * @author Ronny Pfretzschner
 *
 */
public class EmployeeSelectionCombo extends AbstractEntityComboControl<IMitarbeiter> {

	private IMitarbeiterDAO emplDao = null;
	private String emptySelectionText = "- All -";

	/**
	 * Creates a combo box control for the employees.
	 *
	 * @param container
	 * @param name
	 */
	public EmployeeSelectionCombo(IControlContainer container, String name, boolean allowEmptySelection) {
		super(container, name);
		this.allowEmptySelection = allowEmptySelection;

		emplDao = (IMitarbeiterDAO) DAOSystem.getDAO(IMitarbeiterDAO.class);
		setupEntries();
		if (allowEmptySelection) {
			setSelectedKey("0");
		}
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.toolkit.components.AbstractEntityComboControl#setupEntries()
	 */
	@Override
	protected void setupEntries(){

		PropertyQuery query = new PropertyQuery();
		query.setSortDirection(PropertyQuery.SORT_DIRECTION_UP);
		query.setSortField("nachname");

		List entryList = emplDao.getEntities(null, query);

		if (null != entryList){
			//add empty selection
			if (allowEmptySelection){
				addElement(emptySelectionText, "0");
			}

			String defSel = null;

			for (int i = 0; i < entryList.size(); i++) {
				IMitarbeiter employee = (IMitarbeiter) entryList.get(i);
				String title = employee.getNachname() + ", " + employee.getVorname();

				String key = String.valueOf(employee.getId());

				if (i == 0) {
					defSel = key;
				}

				addElement(title, key);
			}

			if (defSel != null && !allowEmptySelection) {
				setSelectedKey(defSel);
			}
		}
	}

	/*
	 *
	 */
	@Override
	public DAO getEntityDao() {
		return emplDao;
	}

	/**
	 * @return the emptySelectionText
	 */
	public String getEmptySelectionText() {
		return emptySelectionText;
	}

	/**
	 * @param emptySelectionText the emptySelectionText to set
	 */
	public void setEmptySelectionText(String emptySelectionText) {
		this.emptySelectionText = emptySelectionText;
		setupEntries();
	}

}
