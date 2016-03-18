import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.webbase.actions.AbstractEntityAction;
import de.xwic.appkit.webbase.actions.ICustomEntityActionCreator;
import de.xwic.appkit.webbase.actions.IEntityAction;
import de.xwic.appkit.webbase.dialog.CenteredWindow;
import de.xwic.appkit.webbase.toolkit.app.Site;
import de.xwic.appkit.webbase.toolkit.util.ImageLibrary;

/**
 * @author <a href="mailto:vzhovtiuk@gmail.com">Vitaliy Zhovtyuk</a>
 */
public class CompanyUrlActionCreator implements ICustomEntityActionCreator {
    @Override
    public IEntityAction createAction(Site site, String entityType, String id) {
        IEntityAction entityAction = new AbstractEntityAction() {
            @Override
            public void run() {
                CenteredWindow centeredWindow = new CenteredWindow(site);
                centeredWindow.show();
            }
        };
        entityAction.setTitle("Show company URL");
        entityAction.setSite(site);
        entityAction.setIconEnabled(ImageLibrary.ICON_EDIT_ACTIVE);
        entityAction.setIconDisabled(ImageLibrary.ICON_EDIT_INACTIVE);
        entityAction.setId(id);
        entityAction.setEntityDao(DAOSystem.findDAOforEntity(entityType));
        return entityAction;
    }
}
