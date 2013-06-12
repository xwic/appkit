/**
 * 
 */
package de.xwic.appkit.webbase.actions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.jwic.base.ImageRef;
import de.xwic.appkit.core.ApplicationData;
import de.xwic.appkit.core.dao.DAO;
import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.registry.ExtensionRegistry;
import de.xwic.appkit.core.registry.IExtension;
import de.xwic.appkit.webbase.toolkit.app.Site;

/**
 * Utility class to help getting Actions through the Extensions system
 * 
 * @author Adrian Ionescu
 */
public class EntityActionsHelper {
	
	private final static Log log = LogFactory.getLog(EntityActionsHelper.class);
	
	public final static String EXTENSION_POINT_ACTIONS = "entityActions";
	public final static String ATTRIBUTE_SCOPE = "scope";
	public final static String ATTRIBUTE_TITLE = "title";
	public final static String ATTRIBUTE_ICON_ENABLED = "iconEnabled";
	public final static String ATTRIBUTE_ICON_DISABLED = "iconDisabled";
	
	/**
	 * @param site
	 * @param entityProvider
	 * @param view
	 * @param entityClass
	 * @return
	 */
	public static Map<String, List<IEntityAction>> getEntityActionsInGroups(Site site, IEntityProvider entityProvider, String view, Class<? extends IEntity> entityClass) {
		Map<String, List<IEntityAction>> actionsInGroups = new LinkedHashMap<String, List<IEntityAction>>();

		DAO dao = DAOSystem.findDAOforEntity(entityClass);
		List<IExtension> extensions = ExtensionRegistry.getInstance().getExtensions(EXTENSION_POINT_ACTIONS);

		Collections.sort(extensions, new Comparator<IExtension>() {

			@Override
			public int compare(IExtension e0, IExtension e1) {
				// AI 05-Jun-2013: no longer sort by groups alphabetically - keep only the index sorting and 
				// make sure you define the actions in extensions.xml exactly in the order you want them
				
//				String g0 = e0.getAttribute("group");
//				if (g0 == null) {
//					g0 = "";
//				}
//				
//				String g1 = e1.getAttribute("group");
//				if (g1 == null) {
//					g1 = "";
//				}
//				
//				if (!g0.equals(g1)) {
//					return g0.compareTo(g1);
//				}
				
				String strIndex0 = e0.getAttribute("index");
				int i0 = strIndex0 != null ? Integer.parseInt(strIndex0) : 0;

				String strIndex1 = e1.getAttribute("index");
				int i1 = strIndex1 != null ? Integer.parseInt(strIndex1) : 0;
				
				return i0 < i1 ? -1 : i0 == i1 ? 0 : 1;
			}
		});
		
		for (IExtension extension : extensions) {
			String applyToView = extension.getAttribute("applyToView");
			String applyToEntity = extension.getAttribute("applyToEntity");
			
			boolean applies = (applyToView != null && !applyToView.isEmpty() && ("*".equals(applyToView) || applyToView.contains(view))) ||
								(applyToEntity != null && !applyToEntity.isEmpty() && ("*".equals(applyToEntity) || applyToEntity.contains(entityClass.getName())));
			
			if (applies) {
				IEntityAction action = createEntityAction(site, extension.getId(), dao, entityProvider);
				if (action != null) {
					String inDropDown = extension.getAttribute("inDropDown");					
					action.setInDropDown(inDropDown != null && "true".equalsIgnoreCase(inDropDown));
					
					String group = extension.getAttribute("group");
					if (group == null) {
						group = "";
					}
					
					List<IEntityAction> actions = null;
					if (actionsInGroups.containsKey(group)) {
						actions = actionsInGroups.get(group);
					} else {
						actions = new ArrayList<IEntityAction>();
						actionsInGroups.put(group, actions);					
					}
					
					actions.add(action);
				}
			}
		}
		
		return actionsInGroups;
	}
	
	/**
	 * @param site
	 * @param actionId
	 * @param entityDao
	 * @param entityProvider
	 * @return
	 */
	private static IEntityAction createEntityAction(Site site, String actionId, DAO entityDao, IEntityProvider entityProvider) {
		IEntityAction action = null;
		
		IExtension actionExtension = ExtensionRegistry.getInstance().getExtension(EXTENSION_POINT_ACTIONS, actionId);

		if (actionExtension == null) {
			throw new RuntimeException("Could not find an action extension with id " + actionId);
		}

		// we might have a scope attribute specified for an action
		String scope = actionExtension.getAttribute(ATTRIBUTE_SCOPE);
		if (scope != null && !scope.isEmpty()) {
			if (!DAOSystem.getSecurityManager().hasRight(scope, ApplicationData.SECURITY_ACTION_ACCESS)) {
				log.info(String.format("Skipped action '%s' because the user doesn't have access to scope '%s'", actionId, scope));
				return null;
			}
		}
		
		try {
			action = (IEntityAction) actionExtension.createExtensionObject();
		} catch (Exception e) {
			log.error("Could not create extension object for " + actionExtension.getId(), e);
			throw new RuntimeException("Could not create extension object for " + actionExtension.getId());
		}

		if (action == null) {
			throw new RuntimeException("The EntityAction is null for " + actionExtension.getId());
		}
		
		action.setId(actionExtension.getId());
		
		if (entityDao != null) {
			action.setEntityDao(entityDao);
		}
		if (entityProvider != null) {
			action.setEntityProvider(entityProvider);
		}
		if (site != null) {
			action.setSite(site);
		}
		
		// set the action's fields from the extension's attributes
		String strTitle = actionExtension.getAttribute(ATTRIBUTE_TITLE);
		if (strTitle != null && !strTitle.trim().isEmpty()) {
			action.setTitle(strTitle);
		}

		String strIconEnabled = actionExtension.getAttribute(ATTRIBUTE_ICON_ENABLED);
		if (strIconEnabled != null && !strIconEnabled.trim().isEmpty()) {
			action.setIconEnabled(new ImageRef(strIconEnabled));
		}

		String strIconDisabled = actionExtension.getAttribute(ATTRIBUTE_ICON_DISABLED);
		if (strIconDisabled != null && !strIconDisabled.trim().isEmpty()) {
			action.setIconDisabled(new ImageRef(strIconDisabled));
		}

		action.updateState(entityProvider != null ? entityProvider.getEntity() : null);
		
		return action;
	}
}