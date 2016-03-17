package de.xwic.appkit.webbase.entityselection;


/**
 * Adapter convenience implementation for {@link IEntitySelectionContributor}
 * <a href="mailto:vzhovtiuk@gmail.com">Vitaliy Zhovtyuk</a>
 */
public abstract class EntitySelectionAdapter implements IEntitySelectionContributor {

	/* (non-Javadoc)
	 * @see com.netapp.pulse.basegui.entityselection.IEntitySelectionContributor#getListSetupId()
	 */
	@Override
	public String getListSetupId() {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.netapp.pulse.basegui.entityselection.IEntitySelectionContributor#getPageSubTitle()
	 */
	@Override
	public String getPageSubTitle() {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.netapp.pulse.basegui.entityselection.IEntitySelectionContributor#getPageTitle()
	 */
	@Override
	public String getPageTitle() {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.netapp.pulse.basegui.entityselection.IEntitySelectionContributor#getViewTitle()
	 */
	@Override
	public String getViewTitle() {
		return null;
	}

}
