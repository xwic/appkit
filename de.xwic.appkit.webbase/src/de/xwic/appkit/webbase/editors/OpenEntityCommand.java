/*
 * (c) Copyright 2005, 2006 by pol GmbH 
 * 
 * de.xwic.appkit.webbase.editor.OpenEntityCommand
 * Created on Jun 12, 2007 by Aron Cotrau
 *
 */
package de.xwic.appkit.webbase.editors;


/**
 * @author Aron Cotrau
 */
public class OpenEntityCommand {
//
//	/**
//	 * Opens the Editor of the given entity.
//	 * <p>
//	 * The perspective is changed to the belonging registered perspective of the
//	 * given Entity. If the entity is null, an error message is displayed and
//	 * the action is aborted. <br>
//	 * An additional EditorListener can be registered to be informed about saved
//	 * events as long as the instance of the editor lives. This is used
//	 * especially for related views, which needs extra work to be done like
//	 * creating a new relation.
//	 * 
//	 * @param An
//	 *            entity for opening an editor
//	 * @param EditorListener
//	 *            for events from the opened Editor
//	 * @param site
//	 *            the site
//	 */
//	public static GenericEntityEditor openEntityEditor(IEntity entity, EditorListener listener, Site site) {
//
//		String editorID = GenericEntityEditor.ID_EDITOR;
//		String perspectiveID = null;
//		String entityInterfaceName = null;
//
//		Registry extensionRegistry = Platform.getRegistry();
//		String pluginID = WebToolsPlugin.ID_PLUGIN;
//		String extensionPointID = WebToolsPlugin.EP_EDITOR_PERSPECTIVE;
//		Collection extensionPoint = extensionRegistry.getExtensionConfigurations(pluginID, extensionPointID);
//		if (null != extensionPoint) {
//			if (extensionPoint != null) {
//				for (Iterator it = extensionPoint.iterator(); it.hasNext();) {
//					try {
//						IEntityEditorPerspective obj = (IEntityEditorPerspective) ((ExtensionConfiguration) it.next())
//								.createExtension();
//						entityInterfaceName = obj.getEntityType();
//						boolean found = false;
//						Class clazz = entity.getClass();
//						while (!found && clazz != null) {
//							Class[] interfaces = clazz.getInterfaces();
//							// get interfaces from entity and see, if one entity
//							// attribute of
//							// extension fits
//							for (int j = 0; j < interfaces.length; j++) {
//								if (interfaces[j].getName().equals(entityInterfaceName)) {
//									found = true;
//									break;
//								}
//							}
//							if (!found) {
//								clazz = clazz.getSuperclass();
//							}
//						}
//
//						if (found) {
//							perspectiveID = obj.getPerspectiveId();
//							break;
//						}
//
//					} catch (Exception e) {
//						JWicErrorDialog.openError("Error occured opening editor", e, site);
//					}
//				}
//			}
//		}
//
//		GenericEntityEditor editor = null;
//
//		// registered perspective for editor found -> open it and open editor!
//		if (perspectiveID != null && entityInterfaceName != null) {
//			try {
//				EditorConfiguration conf = ConfigurationManager.getUserProfile().getEditorConfiguration(entityInterfaceName);
//				GenericEditorInput inp = new GenericEditorInput(entity, conf);
//				site.activatePerspective(perspectiveID);
//				editor = (GenericEntityEditor) ((PartHandle) site.openEditor(editorID, inp)).getPart();
//				// new editor, register listener, if available
//				if (listener != null) {
//					editor.getContext().addEditorListener(listener);
//				}
//			} catch (Exception e) {
//				JWicErrorDialog.openError("Error occured opening editor", e, site);
//			}
//		} else {
//			JWicErrorDialog.openError("Error occured opening editor", site);
//		}
//		return editor;
//	}
//
//	/**
//	 * Opens the Editor of the given entity.
//	 * <p>
//	 * The perspective is changed to the belonging registered perspective of the
//	 * given Entity. If the entity is null, an error message is displayed and
//	 * the action is aborted.
//	 * 
//	 * @param An
//	 *            entity for opening an editor
//	 * @param site
//	 *            the site
//	 */
//	public static GenericEntityEditor openEntityEditor(IEntity entity, Site site) {
//		return openEntityEditor(entity, null, site);
//	}
}
