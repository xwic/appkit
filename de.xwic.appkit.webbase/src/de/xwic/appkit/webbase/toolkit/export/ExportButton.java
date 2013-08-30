/**
 * 
 */
package de.xwic.appkit.webbase.toolkit.export;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.jwic.base.IControlContainer;
import de.jwic.base.IResourceControl;
import de.jwic.base.JavaScriptSupport;
import de.jwic.controls.Button;

/**
 * @author Oleksiy Samokhvalov
 *
 */
@SuppressWarnings("serial")
@JavaScriptSupport(jsTemplate="de.jwic.controls.Button")
public class ExportButton  extends Button implements IResourceControl {

	private IExportContentProvider contentProvider;
	
	private boolean showDownload = false;
	private boolean plain = false;
	
	
	/**
	 * @param container
	 * @param name
	 */
	public ExportButton(IControlContainer container, String name, IExportContentProvider contentProvider) {
		super(container, name);
		setTemplateName(getClass().getName());
		this.contentProvider = contentProvider;
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
	 * Returns the URL that calls the attachResource method.
	 * 
	 * @return
	 */
	public String getDownloadURL() {
		return getSessionContext().getCallBackURL() + "&"
			+ URL_RESOURCE_PARAM + "=1&"
			+ URL_CONTROLID_PARAM + "=" + getControlID();
	}
	
	
	/* (non-Javadoc)
	 * @see de.jwic.base.IResourceControl#attachResource(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void attachResource(HttpServletRequest req, HttpServletResponse res) throws IOException {
		
		res.setContentType(contentProvider.getContentType());
		res.setHeader("Content-Disposition", "attachment; filename=" + contentProvider.getFileName());
		contentProvider.writeContent(res.getOutputStream());
		res.flushBuffer();
		res.getOutputStream().close();
	}

	/**
	 * @return the showDownload
	 */
	public boolean isShowDownload() {
		return showDownload;
	}

	/**
	 * @param showDownload the showDownload to set
	 */
	public void setShowDownload(boolean showDownload) {
		this.showDownload = showDownload;
	}

	/**
	 * @return the plain
	 */
	public boolean isPlain() {
		return plain;
	}

	/**
	 * @param plain the plain to set
	 */
	public void setPlain(boolean plain) {
		this.plain = plain;
	}

	/**
	 * @return the contentProvider
	 */
	public IExportContentProvider getContentProvider() {
		return contentProvider;
	}

	/**
	 * @param contentProvider the contentProvider to set
	 */
	public void setContentProvider(IExportContentProvider contentProvider) {
		this.contentProvider = contentProvider;
	}
	
}
