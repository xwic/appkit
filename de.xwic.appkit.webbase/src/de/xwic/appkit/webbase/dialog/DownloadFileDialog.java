/*******************************************************************************
 * Copyright 2015 xWic group (http://www.xwic.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 		http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 *  
 *******************************************************************************/
/**
 * 
 */
package de.xwic.appkit.webbase.dialog;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import de.jwic.base.ControlContainer;
import de.jwic.base.IResourceControl;
import de.xwic.appkit.webbase.toolkit.app.Site;
import de.xwic.appkit.webbase.utils.MimeTypeUtil;


/**
 * @author Claudiu Mateias
 * @author bogdan
 */
public class DownloadFileDialog extends AbstractDialogWindow implements IResourceControl {

	private final byte[] generatedContent;
	private final String fileName;
	
	/**
	 * @param site
	 * @param generatedContent
	 * @param fileName
	 */
	public DownloadFileDialog(Site site, byte[] generatedContent, String fileName) {
		super(site);
		
		this.generatedContent = generatedContent;
		this.fileName = fileName;
		
		setHeight(120);
	}

	/* (non-Javadoc)
	 * @see de.xwic.appkit.webbase.dialog.AbstractDialogWindow#createContent(de.xwic.appkit.webbase.dialog.DialogContent)
	 */
	@Override
	protected void createContent(DialogContent content) {
		ControlContainer container = new ControlContainer(content, "container");
		
		container.setTemplateName(getClass().getName() + "_container");
		
		btOk.setVisible(false);
		btCancel.setVisible(true);
	}
	
	/**
	 * @return
	 */
	public String getDownloadURL() {
		return getSessionContext().getCallBackURL() + "&_resreq=1&controlId=" + getControlID();
	}

	/*
	 * (non-Javadoc)
	 * @see de.jwic.base.IResourceControl#attachResource(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public void attachResource(HttpServletRequest req, HttpServletResponse res) throws IOException {
		res.setContentType(MimeTypeUtil.getMimeTypeForFileName(fileName));
		res.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

		if (generatedContent == null) {
			return;
		}
		
		InputStream in = new ByteArrayInputStream(generatedContent);
		
		res.setContentLength(generatedContent.length);
		
		OutputStream out = res.getOutputStream();

		try {
			byte[] buf = new byte[1024];
			int length = 0;

			while ((in != null) && ((length = in.read(buf)) != -1)) {
				out.write(buf, 0, length);
			}
			
		} catch (Exception e) {
			log.error("Error sending data to client (" + fileName + ")", e);
		} finally {
			in.close();
			out.flush();
			out.close();
			
		}
	}
}
