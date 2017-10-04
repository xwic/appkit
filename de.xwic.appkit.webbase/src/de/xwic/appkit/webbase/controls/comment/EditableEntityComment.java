/**
 * 
 */
package de.xwic.appkit.webbase.controls.comment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.jwic.base.ControlContainer;
import de.jwic.base.JavaScriptSupport;
import de.jwic.controls.Button;
import de.jwic.controls.InputBox;
import de.jwic.events.SelectionEvent;
import de.jwic.events.SelectionListener;
import de.xwic.appkit.core.dao.DAOSystem;
import de.xwic.appkit.core.model.daos.IEntityCommentDAO;
import de.xwic.appkit.core.model.entities.IEntityComment;
import de.xwic.appkit.core.model.entities.IMitarbeiter;

/**
 * @author Razvan Pat
 *
 */
@SuppressWarnings("serial")
@JavaScriptSupport
public class EditableEntityComment extends ControlContainer {

	private IEntityComment comment;
	private IMitarbeiter employee;
	
	private boolean editMode;
	private boolean allowEdit;
	
	private Button btnEdit;
	private Button btnDelete;
	private Button btnSave;
	private Button btnCancel;
	
	private InputBox ibComment;
	
	EntityCommentList entityCommentList;
	private DateFormat dfDateTimeWithTimeZone;
	
	/**
	 * @param container
	 * @param comment
	 * @param employee
	 * @param allowEdit 
	 */
	public EditableEntityComment(EntityCommentList container, IEntityComment comment, IMitarbeiter employee, boolean allowEdit) {
		super(container, null);
		this.entityCommentList = container;
		this.comment = comment;
		this.employee = employee;
		this.editMode = false;
		this.allowEdit = allowEdit;
		
		dfDateTimeWithTimeZone = new SimpleDateFormat("dd-MMM-yyyy hh:mm aa z");
		dfDateTimeWithTimeZone.setTimeZone(getSessionContext().getTimeZone());
		
		if(isAllowEdit()) {
			btnEdit = new Button(this, "btnEdit");
			btnEdit.setTitle("Edit");
			btnEdit.addSelectionListener(new SelectionListener() {
				@Override
				public void objectSelected(SelectionEvent arg0) {
					onEdit();
				}
			});
	
			btnDelete = new Button(this, "btnDelete");
			btnDelete.setTitle("Delete");
			btnDelete.setConfirmMsg("Are you sure you want to delete the comment? This operation cannot be undone.");
			btnDelete.addSelectionListener(new SelectionListener() {
				@Override
				public void objectSelected(SelectionEvent arg0) {
					onDelete();
				}
			});
	
			btnSave = new Button(this, "btnSave");
			btnSave.setTitle("Save");
			btnSave.addSelectionListener(new SelectionListener() {
				@Override
				public void objectSelected(SelectionEvent arg0) {
					onSave();
				}
			});
	
			btnCancel = new Button(this, "btnCancel");
			btnCancel.setTitle("Cancel");
			btnCancel.addSelectionListener(new SelectionListener() {
				@Override
				public void objectSelected(SelectionEvent arg0) {
					onCancel();
				}
			});
			
			ibComment = new InputBox(this, "ibComment");
			ibComment.setMultiLine(true);
			ibComment.setFillWidth(true);
			
		}
	}
	
	/**
	 * 
	 */
	public void onEdit() {
		ibComment.setText(comment.getComment());
		setEditMode(true);
		btnEdit.setEnabled(false);
		btnDelete.setEnabled(false);
	}
	
	/**
	 * 
	 */
	public void onDelete() {
		IEntityCommentDAO dao = DAOSystem.getDAO(IEntityCommentDAO.class);
		dao.softDelete(comment);
		entityCommentList.removeCommentControl(this);
		entityCommentList.requireRedraw();
	}

	/**
	 * 
	 */
	public void onSave() {
		comment.setComment(ibComment.getText());
		IEntityCommentDAO dao = DAOSystem.getDAO(IEntityCommentDAO.class);
		dao.update(comment);
		
		setEditMode(false);
		btnEdit.setEnabled(true);
		btnDelete.setEnabled(true);
	}
	
	/**
	 * 
	 */
	public void onCancel() {
		setEditMode(false);
		btnEdit.setEnabled(true);
		btnDelete.setEnabled(true);
	}
	
	/**
	 * @return the employeeFlags
	 */
	public IMitarbeiter getEmployeeFlags() {
		return employee;
	}
	
	/**
	 * @return the vtlHelper
	 */
	public String dateTime(Date date) {
		return dfDateTimeWithTimeZone.format(date);
	}

	/**
	 * @return the comment
	 */
	public IEntityComment getComment() {
		return comment;
	}
	
	/**
	 * @return
	 */
	public String getCommentText() {
		String comm = comment.getComment();
		if (comm != null) {
			comm = comm.replaceAll("<br\\/>(\r\n|\n)", "<br/>");
			if (!comment.getCreatedFrom().equals("SYSTEM")) {
				// don't do this for comments created by the system, they are already HTML
				comm = comm.replace("\n", "<br/>");
			}
		}
		
		return comm;
	}

	/**
	 * @return the editMode
	 */
	public boolean isEditMode() {
		return editMode;
	}

	/**
	 * @param editMode the editMode to set
	 */
	public void setEditMode(boolean editMode) {
		this.editMode = editMode;
		this.requireRedraw();
	}

	/**
	 * @return the allowEdit
	 */
	public boolean isAllowEdit() {
		if (employee != null) {
			return allowEdit
					|| employee.getLogonName().equals(DAOSystem.getSecurityManager().getCurrentUser().getLogonName());
		}
		return allowEdit;
	}
}
