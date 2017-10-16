/*
 * Copyright (c) 2009 Network Appliance, Inc.
 * All rights reserved.
 */

package de.xwic.appkit.webbase.entityselection.people;


import de.jwic.base.IControlContainer;
import de.jwic.data.DataLabel;
import de.jwic.data.IBaseLabelProvider;
import de.xwic.appkit.core.model.entities.IMitarbeiter;
import de.xwic.appkit.webbase.entityselection.EntityComboSelector;
import de.xwic.appkit.webbase.entityselection.IEntitySelectionContributor;

/**
 *
 * @author Aron Cotrau
 */
public class EmployeeComboSelector extends EntityComboSelector<IMitarbeiter> {

	public EmployeeComboSelector(IControlContainer container, String name, IEntitySelectionContributor contributor, boolean lifeSearch) {
		super(container, name, contributor, lifeSearch);
		setTemplateName(EntityComboSelector.class.getName());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.netapp.pulse.basegui.entityselection.EntityComboSelector#createControls()
	 */
	@Override
	protected void createControls() {
		super.createControls();

		// install custom base label provider for higher performance...
		combo.setBaseLabelProvider(new IBaseLabelProvider<IMitarbeiter>() {

			@Override
			public DataLabel getBaseLabel(IMitarbeiter ma) {
				
				if (ma != null) {
					return new DataLabel(ma.getNachname() + ", " + ma.getVorname());
				} else {
					return new DataLabel("Invalid EmployeeFlags#" + ma.getId());
				}
			}
		});

		combo.getComboBehavior().setLabelProviderJSClass("Sandbox.EmployeeListRenderer");
		combo.getComboBehavior().setTransferFullObject(true); // we use a custom provider
		combo.setObjectSerializer(new EmployeeJsonSerializer());
		combo.setContentProvider(new EmployeeComboContentProvider(contributor));
	}


}
