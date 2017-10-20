/*
 * Copyright (c) NetApp Inc. - All Rights Reserved
 * 
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 * 
 * com.netapp.pulse.start.ui.home.news.NewsEditor 
 */
package de.xwic.appkit.webbase.home.news;

import de.jwic.base.ControlContainer;
import de.jwic.base.IControlContainer;
import de.jwic.controls.Button;
import de.jwic.controls.CheckBox;
import de.jwic.controls.DatePicker;
import de.jwic.controls.InputBox;
import de.jwic.controls.ckeditor.CKEditor;
import de.jwic.controls.ckeditor.ToolBarBand;
import de.jwic.controls.ckeditor.ToolBarButton;
import de.jwic.events.SelectionEvent;
import de.jwic.events.SelectionListener;
import de.xwic.appkit.core.AppkitModelConfig;
import de.xwic.appkit.core.model.entities.INews;
import de.xwic.appkit.webbase.toolkit.util.ImageLibrary;
import de.xwic.appkit.webbase.utils.NotificationUtil;

/**
 * @author lippisch
 */
public class NewsEditor extends ControlContainer {

	private InputBox inpTitle;
	private InputBox inpAuthor;
	private CKEditor inpBody;
	private CheckBox chkVisible;
	private DatePicker dpPublishDate;
	private DatePicker dpVisibleUntil;

	private INewsEditorHandler neHandler;
	private INews news;
	

	/**
	 * @param container
	 * @param name
	 * @param news
	 * @param neHandler
	 */
	public NewsEditor(IControlContainer container, String name, INews news, INewsEditorHandler neHandler) {
		super(container, name);
		this.news = news;
		
		this.neHandler = neHandler;
		
		inpTitle = new InputBox(this, "inpTitle");
		inpTitle.setMaxLength(150);
		inpTitle.setText(news.getTitle() != null ? news.getTitle() : null);
		
		inpAuthor = new InputBox(this, "inpAuthor");
		inpAuthor.setMaxLength(150);
		inpAuthor.setText(news.getAuthor() != null ? news.getAuthor() : null);
		
		inpBody = new CKEditor(this,  "inpBody");
		inpBody.setHeight(400);
		inpBody.setText(news.getBody() != null ? news.getBody() : "");
		inpBody.setCustomToolBar(
				new ToolBarBand(ToolBarBand.Default_Clipboard),
				new ToolBarBand(ToolBarBand.Default_Paragraph),
				new ToolBarBand(ToolBarBand.Default_Links),
				new ToolBarBand(ToolBarButton.Table),
				new ToolBarBand(true, ToolBarBand.Default_Styles),
				new ToolBarBand(ToolBarBand.Default_Colors),
				new ToolBarBand(ToolBarBand.Default_BasicStyles),
				new ToolBarBand(ToolBarButton.Source)
			);
		
		chkVisible = new CheckBox(this, "visible");
		chkVisible.setLabel("Visible");
		chkVisible.setChecked(news.isVisible());
		
		dpPublishDate = new DatePicker(this, "publishDate");
		dpPublishDate.setWidth(120);
		dpPublishDate.setDate(news.getPublishDate());
		
		dpVisibleUntil = new DatePicker(this, "visibleUntil");
		dpVisibleUntil.setWidth(120);
		dpVisibleUntil.setDate(news.getVisibleUntil());

		Button btSave = new Button(this, "btSave");
		btSave.setIconEnabled(ImageLibrary.ICON_SAVECLOSE_ACTIVE);
		btSave.setTitle("Save");
		btSave.addSelectionListener(new SelectionListener() {
			@Override
			public void objectSelected(SelectionEvent event) {
				onSave();
			}
		});
		
		Button btAbort = new Button(this, "btAbort");
		btAbort.setIconEnabled(ImageLibrary.ICON_CLOSED);
		btAbort.setTitle("Abort");
		btAbort.addSelectionListener(new SelectionListener() {
			@Override
			public void objectSelected(SelectionEvent event) {
				onAbort();
			}
		});
	}
	
	/**
	 * 
	 */
	protected void onAbort() {
		neHandler.editorDone(false);
	}

	/**
	 * 
	 */
	protected void onSave() {
		
		if (inpTitle.getText().trim().isEmpty()) {
			NotificationUtil.showError("You must specify a title", getSessionContext());
			inpTitle.setFlagAsError(true);
			return;
		} else {
			inpTitle.setFlagAsError(false);
		}
		
		if (dpVisibleUntil.getDate() != null && dpPublishDate.getDate() != null && dpVisibleUntil.getDate().before(dpPublishDate.getDate())) {
			NotificationUtil.showError("End date cannot be before start date", getSessionContext());
			return;
		}
		
		// update the news
		news.setTitle(inpTitle.getText().trim());
		news.setAuthor(inpAuthor.getText().trim());
		news.setBody(inpBody.getText());
		news.setVisible(chkVisible.isChecked());
		news.setPublishDate(dpPublishDate.getDate());
		news.setVisibleUntil(dpVisibleUntil.getDate());
		
		AppkitModelConfig.getNewsDAO().update(news);
		
		neHandler.editorDone(true);
	}

	/**
	 * @param width
	 * @param height
	 */
	public void setDimension(int width, int height) {
		int fieldWidth = width - 150;
		
		inpTitle.setWidth(fieldWidth);
		inpAuthor.setWidth(fieldWidth);
		inpBody.setWidth(fieldWidth);
		inpBody.setHeight(height - 240);
	}
}
