/**
 * 
 */
package de.xwic.appkit.webbase.editors.controls;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import de.jwic.base.ControlContainer;
import de.jwic.base.IControlContainer;
import de.jwic.controls.CheckBox;
import de.jwic.controls.layout.TableLayoutContainer;
import de.xwic.appkit.core.ApplicationData;
import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.dao.ISecurityManager;
import de.xwic.appkit.core.model.queries.PropertyQuery;
import de.xwic.appkit.core.security.IRole;
import de.xwic.appkit.core.security.IUser;
import de.xwic.appkit.core.security.daos.IRoleDAO;
import de.xwic.appkit.core.security.daos.IUserDAO;
import de.xwic.appkit.core.util.Equals;

/**
 * Displays roles of a user and allows editing them.
 * 
 * @author lippisch
 */
public class UserRolesEditorControl extends ControlContainer {

	private String logonName = null;
	private boolean enabled = true;

	private int columns = 1;
	private TableLayoutContainer table;

	private Map<Long, CheckBox> controlMap = new HashMap<>();
	private Set<Long> editableRoles = new HashSet<>();
	private boolean canEdit;
	
	/**
	 * @param container
	 * @param name
	 */
	public UserRolesEditorControl(IControlContainer container, String name) {
		super(container, name);

		table = new TableLayoutContainer(this);
		
		loadRoles();
		
	}

	/**
	 * Load the roles the user is associated with and the roles the current user can edit.
	 */
	private void loadRoles() {
		
		ISecurityManager securityManager = DAOSystem.getSecurityManager();
		boolean allRoles = securityManager.hasAccess(ApplicationData.CAN_MANAGE_ALL_ROLES);
		canEdit = securityManager.hasAccess(ApplicationData.CAN_MANAGE_USER_ROLES);
		
		Set<IRole> assignableRoles = getAssignableRoles();
		
		// load all roles
		IRoleDAO roleDAO = DAOSystem.getDAO(IRoleDAO.class);

		PropertyQuery pqRoles = new PropertyQuery();
		pqRoles.setSortField("name");

		List<IRole> roles = roleDAO.getEntities(null, pqRoles);
		editableRoles.clear();
		for (IRole role : roles) {
			
			CheckBox chk = new CheckBox(table, Long.toString(role.getId()));
			chk.setLabel(role.getName());
			chk.setChangeNotification(false);
			controlMap.put(role.getId(), chk);
			
			if (canEdit && (allRoles || assignableRoles.contains(role))) {
				editableRoles.add(role.getId());
			} else {
				chk.setEnabled(false);
			}
			
		}
		
	}
	
	/**
	 * Composes the list of roles that the current user can assign to another user.
	 * @return
	 */
	private Set<IRole> getAssignableRoles() {

		ISecurityManager securityManager = DAOSystem.getSecurityManager();
		IUser currentUser = securityManager.getCurrentUser();
		Set<IRole> userRoles = currentUser.getRoles();
		
		Set<IRole> assignableRoles = new HashSet<>();
		for (IRole role : userRoles) {
			if (role.getRestrictGrantToPeers() == null || role.getRestrictGrantToPeers() == false) {
				assignableRoles.add(role);
			}
			assignableRoles.addAll(role.getAssignableRoles());
		}
		return assignableRoles;
	}

	/**
	 * 
	 */
	private void loadUserRoles() {
		
		IUserDAO userDAO = DAOSystem.getDAO(IUserDAO.class);
		IUser editUser = null;
		
		if (logonName != null && !logonName.isEmpty()) {
			editUser = userDAO.getUserByLogonName(logonName);
		}
		
		// clear all selections.
		for (CheckBox cb : controlMap.values()) {
			cb.setChecked(false);
		}
		if (editUser != null) {
			for (IRole role : editUser.getRoles()) {
				CheckBox cb = controlMap.get(role.getId());
				if (cb != null) {
					cb.setChecked(true);
				}
			}
		}
		
		
	}


	/**
	 * @return the logonName
	 */
	public String getLogonName() {
		return logonName;
	}

	/**
	 * @param logonName the logonName to set
	 */
	public void setLogonName(String logonName) {
		if (!Equals.equal(this.logonName, logonName)) {
			this.logonName = logonName;
			loadUserRoles();
		}
	}


	/**
	 * @return the enabled
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * @param enabled the enabled to set
	 */
	public void setEnabled(boolean enabled) {
		if (this.enabled != enabled) {
			this.enabled = enabled;
			for (Entry<Long, CheckBox> entry : controlMap.entrySet()) {
				Long id = entry.getKey();
				CheckBox cb = entry.getValue();
				cb.setEnabled(enabled && editableRoles.contains(id));
			}
		}
	}

	/**
	 * @return the columns
	 */
	public int getColumns() {
		return columns;
	}

	/**
	 * @param columns the columns to set
	 */
	public void setColumns(int columns) {
		this.columns = columns;
		table.setColumnCount(columns);
	}

	/**
	 * Update the user being edited with the roles.
	 */
	public void saveRoles() {
		
		if (!canEdit) {
			log.debug("UserRoles are not modified because the current user has no rights");
			return;
		}
		
		IUserDAO userDAO = DAOSystem.getDAO(IUserDAO.class);
		ISecurityManager securityManager = DAOSystem.getSecurityManager();
		boolean allRoles = securityManager.hasAccess(ApplicationData.CAN_MANAGE_ALL_ROLES); 
		
		IUser editUser = null;
		
		if (logonName != null && !logonName.isEmpty()) {
			editUser = userDAO.getUserByLogonName(logonName);
		}
		
		if (editUser != null) {
		
			// load all roles
			IRoleDAO roleDAO = DAOSystem.getDAO(IRoleDAO.class);
	
			PropertyQuery pqRoles = new PropertyQuery();
			pqRoles.setSortField("name");
	
			Set<IRole> editRoles = editUser.getRoles();
			
			List<IRole> roles = roleDAO.getEntities(null, pqRoles);
			boolean modified = false;
			for (IRole role : roles) {
				
				CheckBox chk = controlMap.get(role.getId());
				if (chk != null && (allRoles || editableRoles.contains(role.getId()))) {
					// only look at roles the user has access to
					if (chk.isChecked() && !editRoles.contains(role)) {
						editRoles.add(role);
						modified = true;
					} else if (!chk.isChecked() && editRoles.contains(role)) {
						editRoles.remove(role);
						modified = true;
					}
				}
				
			}
			
			if (modified) {
				userDAO.update(editUser);
				log.info(String.format("User Roles of '%s' modified by user '%s'", editUser.getLogonName(), securityManager.getCurrentUser().getLogonName()));
			}
			
		}

		
	}
	
}
