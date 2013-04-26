/**
 * 
 */
package de.xwic.appkit.webbase.toolkit.comment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import de.jwic.base.ControlContainer;
import de.jwic.base.IControlContainer;
import de.jwic.controls.Button;
import de.jwic.controls.ErrorWarning;
import de.jwic.events.SelectionEvent;
import de.jwic.events.SelectionListener;
import de.jwic.util.SerObservable;
import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.model.daos.IEntityCommentDAO;
import de.xwic.appkit.core.model.entities.IEntityComment;
import de.xwic.appkit.webbase.toolkit.app.ExtendedApplication;
import de.xwic.appkit.webbase.toolkit.app.Site;
import de.xwic.appkit.webbase.toolkit.editor.EditorModel;
import de.xwic.appkit.webbase.toolkit.editor.EditorModelEvent;
import de.xwic.appkit.webbase.toolkit.editor.IEditorModelListener;
import de.xwic.appkit.webbase.toolkit.util.EntityInformationActionBarControl;
import de.xwic.appkit.webbase.toolkit.util.ImageLibrary;

/**
 * @author Ronny Pfretzschner
 *
 */
public class CommentEditorControl extends ControlContainer implements IEditorModelListener {

    private EditorModel model;
    private Map<IEntityComment, CommentHelperView> commentViewHelperList;
    private IEntityCommentDAO commentDAO;

    private EntityInformationActionBarControl actionBar = null;
    private Button btNew = null;
	
    private ErrorWarning errorControl = null;
    
	public CommentEditorControl(IControlContainer container, String name, EditorModel model) {
		super(container, name);
        this.model = model;
        commentDAO = (IEntityCommentDAO) DAOSystem.getDAO(IEntityCommentDAO.class);
        commentViewHelperList = new LinkedHashMap<IEntityComment, CommentHelperView>();
        model.addModelListener(this);
        model.getCommentsByEntity(model.getEntity(false));
        createControls();
        loadFields();
	}

	
	private void createControls() {
		actionBar = new EntityInformationActionBarControl(this, "actionbar");
		errorControl = new ErrorWarning(this, "errorControl");
		errorControl.setAutoClose(true);
		errorControl.setShowStackTrace(false);
		
		
		btNew = new Button(actionBar);
		btNew.setTitle("New Comment");
		btNew.addSelectionListener(new SelectionListener() {
			public void objectSelected(SelectionEvent event) {
				performNew();
			}
		});
		btNew.setIconEnabled(ImageLibrary.ICON_NEW_ACTIVE);
		btNew.setIconDisabled(ImageLibrary.ICON_NEW_INACTIVE);

	}


	

	public void performNew() {
        IEntityComment comment = (IEntityComment)commentDAO.createEntity();
        comment.setEntityId(model.getEntity(false).getId());
        comment.setEntityType(model.getEntity(false).type().getName());
        CommentEditorModel commentEditorModel = new CommentEditorModel(comment, model, false);
        commentEditorModel.addModelListener(this);
        Site site = ExtendedApplication.getInstance(this).getSite();
        CommentEditorDialog editor = new CommentEditorDialog(site, commentEditorModel);        
        editor.open();
		
	}


	public void update(SerObservable o, Object arg) {
		// TODO Auto-generated method stub
		
	}

	
	public List<CommentHelperView> getCommentViews() {
		List<CommentHelperView> erg = new ArrayList<CommentHelperView>(commentViewHelperList.values());
		Collections.reverse(erg);
		return erg;
	}
	
	public void loadFields() {
		Map<IEntityComment, CommentHelperView> commentViewHelperList2 = new HashMap<IEntityComment, CommentHelperView>(commentViewHelperList);
		for (IEntityComment comment : model.getComments()) {
            // check, if comment is not a singlecomment
            if(comment.getEntityId() != 0 && comment.getEntityType() != null) {
                if(commentViewHelperList2.containsKey(comment)) {
                    CommentHelperView view = commentViewHelperList2.get(comment);
                    view.refresh();
                }else {
                    commentViewHelperList.put(comment, new CommentHelperView(this, null, model, comment, false, false));
                }
            }
			
		}
		//check buttonstate
		btNew.setEnabled(model.getEntity(false) != null && model.getEntity(false).getId() > 0);
	}


	@Override
	public void destroy() {
		if (model != null) {
			model.removeModelListener(this);
		}
		super.destroy();
	}
	
	public void modelContentChanged(EditorModelEvent event) {
		if (event.getSource() instanceof CommentEditorModel) {
			if (EditorModelEvent.CLOSE_REQUEST == event.getEventType()) {
				loadFields();
				requireRedraw();
			}
		}
		else if (event.getSource() instanceof EditorModel) {
			if (EditorModelEvent.AFTER_SAVE == event.getEventType()) {
	            // 'real' redraw is required because of some ugly hashcode implementation problems...
				for (Iterator iterator = commentViewHelperList.values().iterator(); iterator.hasNext();) {
					CommentHelperView view = (CommentHelperView) iterator.next();
					this.model.removeModelListener(view);
					view.destroy();
					
				}
				
	            commentViewHelperList.clear();

	            loadFields();
	            requireRedraw();
			}
		}
	}


	/**
	 * @return the actionBar
	 */
	public EntityInformationActionBarControl getActionBar() {
		return actionBar;
	}


	/**
	 * @return the errorControl
	 */
	public ErrorWarning getErrorControl() {
		return errorControl;
	}
}
