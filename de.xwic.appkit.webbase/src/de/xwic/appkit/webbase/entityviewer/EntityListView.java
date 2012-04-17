/**
 * 
 */
package de.xwic.appkit.webbase.entityviewer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Map.Entry;

import de.jwic.base.ControlContainer;
import de.jwic.base.Event;
import de.jwic.base.IControlContainer;
import de.jwic.base.ImageRef;
import de.jwic.base.Page;
import de.jwic.controls.Button;
import de.jwic.ecolib.controls.ErrorWarningControl;
import de.jwic.ecolib.tableviewer.export.ExcelExportControl;
import de.jwic.ecolib.toolbar.Toolbar;
import de.jwic.ecolib.toolbar.ToolbarGroup;
import de.jwic.events.ElementSelectedEvent;
import de.jwic.events.ElementSelectedListener;
import de.jwic.events.SelectionEvent;
import de.jwic.events.SelectionListener;
import de.xwic.appkit.core.ApplicationData;
import de.xwic.appkit.core.config.Bundle;
import de.xwic.appkit.core.config.ConfigurationException;
import de.xwic.appkit.core.config.ConfigurationManager;
import de.xwic.appkit.core.config.Setup;
import de.xwic.appkit.core.config.list.ListSetup;
import de.xwic.appkit.core.config.model.EntityDescriptor;
import de.xwic.appkit.core.dao.DAO;
import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.dao.DataAccessException;
import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.dao.ISecurityManager;
import de.xwic.appkit.webbase.actions.EntityActionDelete;
import de.xwic.appkit.webbase.actions.EntityActionEdit;
import de.xwic.appkit.webbase.actions.EntityActionNew;
import de.xwic.appkit.webbase.actions.EntityActionsHelper;
import de.xwic.appkit.webbase.actions.IEntityAction;
import de.xwic.appkit.webbase.actions.IEntityProvider;
import de.xwic.appkit.webbase.dialog.DialogEvent;
import de.xwic.appkit.webbase.dialog.DialogWindowAdapter;
import de.xwic.appkit.webbase.entityviewer.config.IUserConfigurationWindowListener;
import de.xwic.appkit.webbase.entityviewer.config.UserConfigurationWindow;
import de.xwic.appkit.webbase.entityviewer.quickfilter.AbstractQuickFilterPanel;
import de.xwic.appkit.webbase.table.EntityTable;
import de.xwic.appkit.webbase.table.EntityTableAdapter;
import de.xwic.appkit.webbase.table.EntityTableEvent;
import de.xwic.appkit.webbase.toolkit.app.ExtendedApplication;
import de.xwic.appkit.webbase.toolkit.app.Site;
import de.xwic.appkit.webbase.toolkit.components.DeleteFailedException;
import de.xwic.appkit.webbase.toolkit.util.ImageLibrary;

/**
 * A class grouping several controls offering complete functionality for viewing
 * a list of entities
 * 
 * @author Adrian Ionescu
 */
public class EntityListView extends ControlContainer implements IEntityProvider {

	protected EntityListViewConfiguration configuration;
	
	private AbstractQuickFilterPanel quickFilterPanel;
	
	protected EntityTable entityTable;
	protected Toolbar toolbar;

	protected int widthDecrease = 50;
	protected int heightDecrease = 230;
	
	private UserConfigurationWindow dialog;
	private Button btUserConfig;
	
	protected List<IEntityAction> standardActions;
	protected List<IEntityAction> extensionActions;

	protected ErrorWarningControl errorWarning;

	private DAO dao;
	
	/**
	 * @param container
	 * @param configuration
	 * @throws ConfigurationException
	 */
	public EntityListView(IControlContainer container, EntityListViewConfiguration configuration) throws ConfigurationException {
		this (container, null, configuration);
	}
	
	/**
	 * @param container
	 * @param name
	 * @param configuration
	 * @throws ConfigurationException
	 */
	public EntityListView(IControlContainer container, String name, EntityListViewConfiguration configuration) throws ConfigurationException {
		super(container, name);
		
		setTemplateName(EntityListView.class.getName());
		
		this.configuration = configuration;
		this.dao = DAOSystem.findDAOforEntity(configuration.getEntityClass());
		
		standardActions = new ArrayList<IEntityAction>();
		extensionActions = new ArrayList<IEntityAction>();
		
		createControls();
		
		addStandardActions();
		loadActionsFromExtensions();
		
		addElementSelectedListener(new ElementSelectedListener() {
			@Override
			public void elementSelected(ElementSelectedEvent event) {
				
				IEntity entity = getEntity();
				
				for (IEntityAction action : standardActions) {
					action.updateState(entity);
				}
				
				for (IEntityAction action : extensionActions) {
					action.updateState(entity);
				}
			}
		});
		
		init();
		
		updateTableViewerSize();
	}

	/**
	 * @throws ConfigurationException
	 */
	protected void createControls() throws ConfigurationException {
		// making sure some properties are set
		if(configuration.getListId() == null) {
			configuration.setListId(Setup.ID_DEFAULT);
		}
		
		if (configuration.getViewId() == null) {
			configuration.setViewId(configuration.getListId());
		}
		// add the entity class to the viewId, just to be sure
		configuration.setViewId(configuration.getEntityClass().getName() + "-" + configuration.getViewId());
		
		toolbar = new Toolbar(this, "toolbar");
		
		errorWarning = new ErrorWarningControl(this, "errorWarning");
        errorWarning.setAutoClose(true);
        errorWarning.setShowStackTrace(false);
		
		entityTable = new EntityTable(this, "entityTable", configuration);
		entityTable.getModel().addEntityTableListener(new EntityTableAdapter() {
			@Override
			public void userConfigurationChanged(EntityTableEvent event) {
				btUserConfig.setTitle("Profile: " + entityTable.getModel().getCurrentUserConfigurationName());
				dialog.close();
				dialog = null;
			}
		});

		if(configuration.getQuickFilterPanelCreator() != null) {
			quickFilterPanel = configuration.getQuickFilterPanelCreator().createQuickFilterPanel(this, entityTable.getModel());
		}
		
		ToolbarGroup grpRight = toolbar.addRightGroup();

		if (showClearFilters()) {
			ClearFiltersButton clearFilters = new ClearFiltersButton(grpRight, null, entityTable.getModel());
			clearFilters.setCssClass("j-button-h j-btn-small");
		}
		
		if (showColumnConfig()) {
			Button btColumns = grpRight.addButton();
			btColumns.setIconEnabled(ImageLibrary.ICON_TABLE);
			btColumns.setTitle("");
			btColumns.setTooltip("Manage visible columns");
			btColumns.addSelectionListener(new SelectionListener() {
				@Override
				public void objectSelected(SelectionEvent event) {
					entityTable.openColumnSelector();
				}
			});

			btUserConfig = grpRight.addButton();
			btUserConfig.setIconEnabled(ImageLibrary.ICON_CONFIG);
			btUserConfig.setTitle("Profile: " + entityTable.getModel().getCurrentUserConfigurationName());
			btUserConfig.setTooltip("Manage profiles");
			btUserConfig.addSelectionListener(new SelectionListener() {
				@Override
				public void objectSelected(SelectionEvent event) {
					openUserConfigWindow();
				}
			});
		}

		if (showExcelExport()) {
			ExcelExportControl excelExport = new ExcelExportControl(grpRight, "excel", entityTable.getTableViewer());
			excelExport.setCssClass("j-button-h j-btn-small");
			excelExport.setTitle("");
			excelExport.setTooltip("Export table to Excel");
			ImageRef imgDef = ImageLibrary.ICON_EXCEL;
			excelExport.setIconEnabled(imgDef);
		}
	}
	
	/**
	 * Add the New, Edit, Delete actions
	 */
	protected void addStandardActions() {
		ToolbarGroup tg = toolbar.addGroup();

		Site site = ExtendedApplication.getInstance(this).getSite();

		String className = entityTable.getModel().getEntityClass().getName();
		Set<String> actions = entityTable.getModel().getListSetup().getActions();
		
		ISecurityManager secMan = DAOSystem.getSecurityManager();
		
		boolean showAction = actions.contains(ListSetup.ACTION_CREATE) && secMan.hasRight(className, ApplicationData.SECURITY_ACTION_CREATE);
		if (showAction) {
			IEntityAction actionNew = new EntityActionNew(site, dao, this);
			standardActions.add(actionNew);
			tg.addAction(actionNew);
		}
		
		showAction = actions.contains(ListSetup.ACTION_EDIT) && secMan.hasRight(className, ApplicationData.SECURITY_ACTION_UPDATE);
		if (showAction) {
			final EntityActionEdit actionEdit = new EntityActionEdit(site, dao, this);
			standardActions.add(actionEdit);
			tg.addAction(actionEdit);
			
			entityTable.addElementSelectedListener(new ElementSelectedListener() {
				@Override
				public void elementSelected(ElementSelectedEvent event) {
					if(event.isDblClick()){
						actionEdit.run();
					}
				}
			});
		}

		showAction = actions.contains(ListSetup.ACTION_DELETE) && secMan.hasRight(className, ApplicationData.SECURITY_ACTION_DELETE);
		if (showAction) {
			IEntityAction actionDelete = new EntityActionDelete(site, dao, this){
				@Override
				public void run() {
					try {
						super.run();
						showInfo("Item was successfully deleted");
						
						entityTable.getTableViewer().getModel().clearSelection();
						entityTable.getTableViewer().requireRedraw();
					} catch (Exception ex) {
						showError(ex);
					}
				}
			};
			standardActions.add(actionDelete);
			Button btnDelete = tg.addAction(actionDelete);
			btnDelete.setConfirmMsg("Are you sure you want to delete this item?");
		}
	}

	/**
	 * 
	 */
	protected void loadActionsFromExtensions() {
		ToolbarGroup tg = toolbar.addGroup();
		Site site = ExtendedApplication.getInstance(this).getSite();
		
		Map<String, List<IEntityAction>> actionsInGroups = EntityActionsHelper.getEntityActionsInGroups(site, this, getClass().getName(), configuration.getEntityClass());
		for (Entry<String, List<IEntityAction>> entry : actionsInGroups.entrySet()) {
			List<IEntityAction> actions = entry.getValue();
			
			if (actions.size() > 0) {
				tg.addSpacer();
				
				for (IEntityAction action : actions) {
					tg.addAction(action);
					extensionActions.add(action);
				}
			}
		}
	}

	/**
	 * 
	 */
	protected void init() {
	}

	/**
	 * @return
	 */
	protected boolean showExcelExport() {
		return true;
	}
	
	/**
	 * 
	 */
	protected boolean showColumnConfig() {
		return true;
	}
	
	/**
	 * 
	 */
	protected boolean showClearFilters() {
		return true;
	}
	
	/**
	 * 
	 */
	private void openUserConfigWindow() {
		// to prevent multiple dialog openings
		if (dialog != null) {
			return;
		}
		
		dialog = new UserConfigurationWindow(ExtendedApplication.getInstance(this).getSite(), entityTable.getModel());
		dialog.addDuplicateProfileListener(new IUserConfigurationWindowListener() {
			@Override
			public void beforeDuplicateProfile(Event event) {
				entityTable.storeCurrentConfig();
			}
		});
		dialog.addDialogWindowListener(new DialogWindowAdapter() {
			@Override
			public void onDialogAborted(DialogEvent event) {				
				dialog = null;
			}
		});		
		dialog.show();
	}
	
	/**
	 * @return
	 */
	public Toolbar getToolbar() {
		return toolbar;
	}

	/**
	 * @return the widthDecrease
	 */
	public int getWidthDecrease() {
		return widthDecrease;
	}

	/**
	 * @param widthDecrease the widthDecrease to set
	 */
	public void setWidthDecrease(int widthDecrease) {
		this.widthDecrease = widthDecrease;
		
		updateTableViewerSize();
	}

	/**
	 * @return the heightDecrease
	 */
	public int getHeightDecrease() {
		return heightDecrease;
	}

	/**
	 * @param heightDecrease the heightDecrease to set
	 */
	public void setHeightDecrease(int heightDecrease) {
		this.heightDecrease = heightDecrease;
		
		updateTableViewerSize();
	}
	
	/**
	 * 
	 */
	private void updateTableViewerSize() {
		if (entityTable != null) {
			Page page = (Page) getSessionContext().getTopControl();
			if (null != page) {
				int pageHeight = page.getPageSize().height;
				int pageWidth = page.getPageSize().width;

				int height = pageHeight - getHeightDecrease();
				if (quickFilterPanel != null) {
					height -= quickFilterPanel.getPrefferedHeight();
				}
				if (height < 200) {
					height = 200;
				}
				int width = pageWidth - getWidthDecrease();
				if (width < 100) {
					width = 100;
				}
				entityTable.setWidth(width);
				entityTable.setHeight(height);
			} else {
				entityTable.setWidth(600);
				entityTable.setHeight(300);
			}
		}
	}

	/**
	 * 
	 */
	public EntityTable getEntityTable() {
		return entityTable;
	}
	
	/**
	 * Used in the VTL
	 * @return
	 */
	public String getQuickFilterPanelName() {
		return quickFilterPanel!= null ? quickFilterPanel.getName() : "";
	}
	
	 /**
     * Show the ErrorWarningControl as error with red background
     * 
     * @param message
     */
    public void showError(String message) {
        errorWarning.showError(message);
        errorWarning.setRequireRedraw(true); // control does not trigger this 
    }

    /**
     * Show the ErrorWarningControl with an exception.
     * 
     * @param e
     */
    public void showError(Exception e) {
        String langID = getSessionContext().getLocale().getLanguage();
        if (e instanceof DataAccessException) {
            DataAccessException dae = (DataAccessException)e;
            errorWarning.showError(createDataAccessExceptionContent(dae, langID));
        } else if (e instanceof DeleteFailedException) {
        	errorWarning.showError(e);
        } else {
            log.error("ShowError: ", e);
            errorWarning.showError(e);
        }
        errorWarning.setRequireRedraw(true); // control does not trigger this 
    }
    
    /**
     * Tries to format the content of the given DataAccessException.
     * 
     * @param dae
     * @param langID
     * @return a string for the error control.
     */
    private String createDataAccessExceptionContent(DataAccessException dae, String langID) {
        StringBuilder builder = new StringBuilder();
        Bundle coreBundle = null;
        try {
            coreBundle = ConfigurationManager.getSetup().getDomain("core").getBundle(langID);
        }
        catch (Exception ex) {
            builder.append(ex.getMessage());
            builder.append("<br>");
        }

        if (coreBundle == null) {
            builder.append("No Common Bundle found.");
            builder.append("<br>");
            builder.append("Common Error Strings cannot be resolved, showing keys only.");
            builder.append("<br>");
        }

        if (dae.getMessage() == "softdelete.hasref") {
            List<?> errorContent = (List<?>)dae.getInfo();

            //dirtydirtydirty... if the customer likes it, replace strings
            //if not, delete the code below :(
            if (coreBundle != null) {
                builder.append(coreBundle.getString("softdel.hasref"));
            }
            else {
                builder.append(dae.getMessage());
            }

            builder.append("<br><br><table class=\"datalist\" cellpadding=\"2\" cellspacing=\"0\"><tr><th>Entity ID</th><th>Entity Type</th><th>Description</th><th>Used Property</th></tr>");

            int maxCounter = 5;
            int i = 0;
            boolean hasmore = false;
            
            for (Object obj : errorContent) {
            	String stringEntry = (String) obj;
                if (i == maxCounter) {
                    builder.append("</table><br>");
                    builder.append("&nbsp;&nbsp;+ ").append(errorContent.size() - maxCounter).append(" More... ");
                    hasmore = true;
                    break;
                }
                builder.append("<tr>");
                StringTokenizer stk = new StringTokenizer(stringEntry, ";");
                String type = stk.nextToken();
                String propName = stk.nextToken();
                String id = stk.nextToken();
                String propTitle = null;
                String entityTitle = null;
                try {
                    EntityDescriptor ed = ConfigurationManager.getSetup().getEntityDescriptor(type);
                    Bundle bundle = ed.getDomain().getBundle(langID);
                    propTitle = bundle.getString(type + "." + propName);
                    entityTitle = bundle.getString(type);

                    if (propTitle.startsWith("!")) {
                        propTitle = propName;
                    }
                    if (entityTitle.startsWith("!")) {
                        entityTitle = type.substring(type.lastIndexOf(".") + 1);
                    }
                }
                catch (ConfigurationException ce) {
                    builder.append(ce.getMessage());
                    propTitle = propName;
                    entityTitle = type.substring(type.lastIndexOf(".") + 1);
                }

                builder.append("<td>");
                builder.append(id);
                builder.append("</td>");
                builder.append("<td>");
                builder.append(type);
                builder.append("</td>");
                builder.append("<td>");
                builder.append(entityTitle);
                builder.append("</td>");
                builder.append("<td>");
                builder.append(propTitle);
                builder.append("</td>");
                builder.append("</tr>");

                i++;
            }
            if (!hasmore) {
                builder.append("</table><br>");
            }
            log.error("ShowError: " + builder, dae);

        }
        else {
            builder.append(dae.getMessage());
        }

        return builder.toString();
    }
    
    /**
     * Show a warning message.
     * 
     * @param message
     * @param cssClass
     */
    public void showWarning(String message) {
        getSessionContext().notifyMessage(message, "xwic-notify-warning");
    }
	
    /**
     * Show a warning message.
     * 
     * @param message
     * @param cssClass
     */
    public void showInfo(String message) {
        getSessionContext().notifyMessage(message, "xwic-notify-info");
    }

	/* (non-Javadoc)
	 * @see de.jwic.base.ControlContainer#destroy()
	 */
	@Override
	public void destroy() {
		if (dialog != null) {
			dialog.close();
		}
		
		super.destroy();
	}
	
	/**
	 * @param listener
	 */
	public void addElementSelectedListener(ElementSelectedListener listener) {
		entityTable.getTableViewer().getModel().addElementSelectedListener(listener);
	}	
	
	// ****************** FROM IEntityProvider *********************************

	
	/* (non-Javadoc)
	 * @see com.netapp.pulse.basegui.extensions.actions.IEntityProvider#getEntityKey()
	 */
	@Override
	public String getEntityKey() {
		return entityTable.getTableViewer().getModel().getFirstSelectedKey();
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.actions.IEntityProvider#getEntity()
	 */
	@Override
	public IEntity getEntity() {
		if (!hasEntity()) {
			return null;
		}
		
		try {
			String selection = getEntityKey();
			
			if (selection.trim().length() > 0) {
				DAO dao = DAOSystem.findDAOforEntity(configuration.getEntityClass());

				int id = Integer.parseInt(selection);
				return dao.getEntity(id);
			}
			
			return null;
		} catch (Exception e) {
			return null;
		}
	}
	
	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.actions.IEntityProvider#getBaseEntity()
	 */
	@Override
	public IEntity getBaseEntity() {
		return configuration.getBaseEntity();
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.xwic.appkit.webbase.actions.IEntityProvider#hasEntity()
	 */
	public boolean hasEntity() {
		return !entityTable.getTableViewer().getModel().getSelection().isEmpty();
	}
}
