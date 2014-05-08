/*
 * Copyright 2005-2007 jWic group (http://www.jwic.de)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * de.jwic.ecolib.tableviewer.export.ExcelExportControl
 * Created on Apr 3, 2007
 * $Id: ExcelExportControl.java,v 1.13 2011/09/08 11:25:16 adrianionescu12 Exp $
 */
package de.xwic.appkit.webbase.controls.export;

import de.jwic.async.IProcessListener;
import de.jwic.async.ProcessEvent;
import de.jwic.base.IControlContainer;
import de.jwic.controls.Button;
import de.jwic.controls.tableviewer.TableViewer;
import de.jwic.events.SelectionEvent;
import de.jwic.events.SelectionListener;
import de.xwic.appkit.webbase.async.AsyncProcessDialog;
import de.xwic.appkit.webbase.dialog.DownloadFileDialog;
import de.xwic.appkit.webbase.toolkit.app.ExtendedApplication;

/**
 * This control defines the ExcelExport button.
 * 
 * @author Aron Cotrau
 */
public class ExcelExportControl extends Button {
	private static final long serialVersionUID = 1L;
	private boolean showDownload = false;
	private boolean plain = false;

	private TableViewer tableViewer = null;

	/**
	 * @param container
	 * @param tableViewer
	 * @throws IllegalArgumentException
	 *             if tableViewer is <code>null</code>
	 */
	public ExcelExportControl(IControlContainer container, TableViewer tableViewer) {
		this(container, null, tableViewer);
	}

	/**
	 * @param container
	 * @param name
	 * @param tableViewer
	 * @throws IllegalArgumentException
	 *             if tableViewer is <code>null</code>
	 */
	public ExcelExportControl(IControlContainer container, String name, TableViewer tableViewer) {
		super(container, name);
		if (null == tableViewer) {
			throw new IllegalArgumentException("The TableViewer object is not allowed to be null !");
		}
		this.setTemplateName(Button.class.getName());
		this.tableViewer = tableViewer;
		this.addSelectionListener(new SelectionListener() {
			
			@Override
			public void objectSelected(SelectionEvent arg0) {
				onClick();
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.jwic.controls.SelectableControl#click()
	 */
	public void click() {
		super.click();
		setShowDownload(true);
		requireRedraw();
	}


	/**
	 * @return Returns the showDownload.
	 */
	public boolean isShowDownload() {
		return showDownload;
	}

	/**
	 * @param showDownload
	 *            The showDownload to set.
	 */
	public void setShowDownload(boolean showDownload) {
		this.showDownload = showDownload;
	}

	/**
	 * @return if the button is plain or action
	 */
	public boolean isPlain() {
		return plain;
	}

	/**
	 * set this attribute to <code>true</code> when this button is supposed to
	 * be a regular button instead of an action type.
	 * 
	 * @param isPlain
	 */
	public void setPlain(boolean isPlain) {
		if (this.plain != isPlain) {
			requireRedraw();
		}

		this.plain = isPlain;
	}
	
	private void onClick() {
		final ExcelExportAsyncProcess process = getExportAsyncProcess();
		
		final AsyncProcessDialog apd = new AsyncProcessDialog(ExtendedApplication.getInstance(this).getSite(), process);
		apd.setTitle("Generating Excel");
		apd.start();
		
		process.addProcessListener(new IProcessListener() {
			@Override
			public void processFinished(ProcessEvent event) {
				
				Object result = process.getResult();
				if (result != null && result instanceof byte[]) {
					byte[] generatedContent = (byte[]) result;
					onFinish(apd, generatedContent);
				}else if(process.isCancelled()){
					onCancelled();
					return;
				}else if (result instanceof Throwable){
					onError(apd, (Throwable) result);
				} else {
					String type = "null";
					if (result != null){
						type = result.getClass().getName();
					}
					log.error("Unkown result type: " + type, new IllegalStateException());
					apd.setVisible(false);
					throw new RuntimeException("Unknown result type");
				}				
				requireRedraw();
			}
		});
	}
	
	protected void onCancelled(){
		log.info("Export was cancelled");
	}
	
	protected void onFinish(AsyncProcessDialog window, byte[] result){
		DownloadFileDialog dialog = new DownloadFileDialog(ExtendedApplication.getInstance(ExcelExportControl.this).getSite(), result, "export.xlsx");
		dialog.setTitle("Download Excel");
		dialog.show();
	}
	
	protected void onError(AsyncProcessDialog window, Throwable result){
		log.error("Error occured while generating the report ", result);
		window.setVisible(false);
		throw new RuntimeException("Error occured while generating the report");
	}
	
	/**
	 * 
	 * @return
	 */
	protected ExcelExportAsyncProcess getExportAsyncProcess(){
		return new ExcelExportAsyncProcess(tableViewer.getModel(), tableViewer.getTableLabelProvider());
	}
	
}
