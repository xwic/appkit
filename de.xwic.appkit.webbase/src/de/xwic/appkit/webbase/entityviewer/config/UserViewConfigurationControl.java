/**
 * 
 */
package de.xwic.appkit.webbase.entityviewer.config;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import de.jwic.base.ControlContainer;
import de.jwic.base.Event;
import de.jwic.base.IControlContainer;
import de.jwic.controls.InputBoxControl;
import de.jwic.controls.LabelControl;
import de.jwic.controls.RadioButton;
import de.xwic.appkit.core.dao.DAO;
import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.model.daos.IMitarbeiterDAO;
import de.xwic.appkit.core.model.daos.IUserViewConfigurationDAO;
import de.xwic.appkit.core.model.entities.IUserViewConfiguration;
import de.xwic.appkit.webbase.table.EntityTableModel;

/**
 * @author Adrian Ionescu
 */
public class UserViewConfigurationControl extends ControlContainer {

	private final static int EVENT_TYPE_DELETE = 0;
	private final static int EVENT_TYPE_APPLY = 1;
	private final static int EVENT_TYPE_COPY_PUBLIC = 2;

	private LabelControl lblName;
	private LabelControl lblDescription;
	private LabelControl lblDate;
	private InputBoxControl ibName;
	private InputBoxControl ibDescription;
	private RadioButton rbtnYes;
	private RadioButton rbtnNo;

	private IUserViewConfiguration userConfig;
	private EntityTableModel tableModel;

	private List<IUserViewConfigurationControlListener> listeners;

	private boolean editMode = false;
	private String editError = "";
	private boolean sharedView;
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

	/**
	 * @param container
	 * @param userConfig
	 * @param tableModel
	 * @param editMode
	 * @param sharedView
	 */
	public UserViewConfigurationControl(IControlContainer container, IUserViewConfiguration userConfig, EntityTableModel tableModel, boolean editMode, boolean sharedView) {
		super(container);

		this.userConfig = userConfig;
		this.tableModel = tableModel;
		this.editMode = editMode;

		listeners = new ArrayList<IUserViewConfigurationControlListener>();

		this.sharedView = sharedView;
		
		createControls();
	}

	/**
	 * 
	 */
	private void createControls() {
		lblName = new LabelControl(this, "lblName");

		lblDescription = new LabelControl(this, "lblDescription");

		lblDate = new LabelControl(this, "lblDate");
		
		ibName = new InputBoxControl(this, "ibName");
		ibName.setWidth(360);

		ibDescription = new InputBoxControl(this, "ibDescription");
		ibDescription.setEmptyInfoText("Enter a description of this list profile");
		ibDescription.setMultiLine(true);
		ibDescription.setWidth(360);
		ibDescription.setHeight(70);

		rbtnYes = new RadioButton(this, "rbtnYes");
		rbtnYes.setCssClass("radioButton");
		rbtnYes.setTitle("Yes");

		rbtnNo = new RadioButton(this, "rbtnNo", rbtnYes);
		rbtnNo.setCssClass("radioButton");
		rbtnNo.setTitle("No");

		updateFieldsValues();
	}

	/**
	 * 
	 */
	private void updateFieldsValues() {
		lblName.setText(userConfig.getName());
		if (isCurrentConfig()) {
			lblName.setText(lblName.getText() + "*");
		}
		lblDescription.setText(userConfig.getDescription() != null ? userConfig.getDescription() : "");
		lblDate.setText(sdf.format(isSharedView() ? userConfig.getLastModifiedAt() : userConfig.getCreatedAt()));
		
		ibName.setText(userConfig.getName());
		ibDescription.setText(userConfig.getDescription() != null ? userConfig.getDescription() : "");
		if (userConfig.isPublic()) {
			rbtnYes.setSelected(true);
		} else {
			rbtnNo.setSelected(true);
		}
	}

	/**
	 * 
	 */
	public void actionApply() {		
		fireEvent(EVENT_TYPE_APPLY);
	}

	/**
	 * 
	 */
	public void actionCopyPublic() {		
		fireEvent(EVENT_TYPE_COPY_PUBLIC);
	}
	
	/**
	 * 
	 */
	public void actionDelete() {
		fireEvent(EVENT_TYPE_DELETE);
	}
	
	/**
	 * 
	 */
	public void actionEdit() {
		editMode = true;
		editError = "";
		requireRedraw();
	}

	/**
	 * 
	 */
	public void actionUpdate() {
		if (ibName.getText().isEmpty()) {
			editError = "You must enter a name";
			requireRedraw();
			return;
		} else {
			editError = "";
		}

		if (tableModel.nameAlreadyExists(ibName.getText(), userConfig.getId())) {
			editError = "A configuration with this name already exists";
			requireRedraw();
			return;
		}

		userConfig.setName(ibName.getText());
		userConfig.setDescription(ibDescription.getText());
		userConfig.setPublic(rbtnYes.isSelected());

		DAO dao = DAOSystem.getDAO(IUserViewConfigurationDAO.class);
		dao.update(userConfig);

		updateFieldsValues();
		editMode = false;
		requireRedraw();
	}

	/**
	 * 
	 */
	public void actionCancelUpdate() {
		// if it's a new config we need to fire the delete event to have the
		// control removed, since the entity will not be saved
		if (userConfig.getId() < 1) {
			fireEvent(EVENT_TYPE_DELETE);
			return;
		}

		ibName.setText(userConfig.getName());
		ibDescription.setText(userConfig.getDescription());
		if (userConfig.isPublic()) {
			rbtnYes.setSelected(true);
		} else {
			rbtnNo.setSelected(true);
		}

		editMode = false;
		requireRedraw();
	}

	/**
	 * @return
	 */
	public boolean isCurrentConfig() {
		return tableModel.getCurrentUserConfigurationId() == userConfig.getId();
	}

	/**
	 * @param listener
	 */
	public void addListener(IUserViewConfigurationControlListener listener) {
		if (!listeners.contains(listener)) {
			listeners.add(listener);
		}
	}

	/**
	 * @param listener
	 */
	public void removeListener(IUserViewConfigurationControlListener listener) {
		if (listeners.contains(listener)) {
			listeners.remove(listener);
		}
	}

	/**
	 * 
	 */
	private void fireEvent(int eventType) {
		for (IUserViewConfigurationControlListener listener : listeners) {
			if (eventType == EVENT_TYPE_APPLY) {
				listener.onConfigApplied(new Event(this));
			} else if (eventType == EVENT_TYPE_COPY_PUBLIC) {
				listener.onPublicConfigCopied(new Event(this));
			} else if (eventType == EVENT_TYPE_DELETE) {
				listener.onConfigDeleted(new Event(this));
			}
		}
	}

	/**
	 * @return
	 */
	public IUserViewConfiguration getUserViewConfiguration() {
		return userConfig;
	}
	
	/**
	 * Used in the VTL
	 * @return
	 */
	public boolean isEditMode() {
		return editMode;
	}

	/**
	 * Used in the VTL
	 * @return
	 */
	public String getEditError() {
		return editError;
	}
	
	/**
	 * Used in the VTL
	 * @return
	 */
	public boolean isSharedView() {
		return sharedView; 
	}
	
	/**
	 * Used in the VTL
	 * @return
	 */
	public String getOwnerName() {
		return DAOSystem.getDAO(IMitarbeiterDAO.class).buildTitle(userConfig.getOwner()); 
	}
}
