/**
 * 
 */
package de.xwic.appkit.core.model.entities.impl;

import java.util.Date;

import de.xwic.appkit.core.dao.Entity;
import de.xwic.appkit.core.model.entities.INews;

/**
 * @author lippisch
 */
public class News extends Entity implements INews {

	private String author;
	private String title;
	private String body;
	private Date publishDate;
	private Date visibleUntil;
	private boolean visible = false;
	private String appId;
	
	/* (non-Javadoc)
	 * @see com.netapp.pulse.start.model.entities.impl.INews#getAuthor()
	 */
	@Override
	public String getAuthor() {
		return author;
	}
	/* (non-Javadoc)
	 * @see com.netapp.pulse.start.model.entities.impl.INews#setAuthor(de.xwic.appkit.core.model.entities.IMitarbeiter)
	 */
	@Override
	public void setAuthor(String author) {
		this.author = author;
	}
	/* (non-Javadoc)
	 * @see com.netapp.pulse.start.model.entities.impl.INews#getTitle()
	 */
	@Override
	public String getTitle() {
		return title;
	}
	/* (non-Javadoc)
	 * @see com.netapp.pulse.start.model.entities.impl.INews#setTitle(java.lang.String)
	 */
	@Override
	public void setTitle(String title) {
		this.title = title;
	}
	/* (non-Javadoc)
	 * @see com.netapp.pulse.start.model.entities.impl.INews#getBody()
	 */
	@Override
	public String getBody() {
		return body;
	}
	/* (non-Javadoc)
	 * @see com.netapp.pulse.start.model.entities.impl.INews#setBody(java.lang.String)
	 */
	@Override
	public void setBody(String body) {
		this.body = body;
	}
	/* (non-Javadoc)
	 * @see com.netapp.pulse.start.model.entities.impl.INews#getPublishDate()
	 */
	@Override
	public Date getPublishDate() {
		return publishDate;
	}
	/* (non-Javadoc)
	 * @see com.netapp.pulse.start.model.entities.impl.INews#setPublishDate(java.util.Date)
	 */
	@Override
	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}
	/* (non-Javadoc)
	 * @see com.netapp.pulse.start.model.entities.impl.INews#getVisibleUntil()
	 */
	@Override
	public Date getVisibleUntil() {
		return visibleUntil;
	}
	/* (non-Javadoc)
	 * @see com.netapp.pulse.start.model.entities.impl.INews#setVisibleUntil(java.util.Date)
	 */
	@Override
	public void setVisibleUntil(Date visibleUntil) {
		this.visibleUntil = visibleUntil;
	}
	/* (non-Javadoc)
	 * @see com.netapp.pulse.start.model.entities.impl.INews#isVisible()
	 */
	@Override
	public boolean isVisible() {
		return visible;
	}
	/* (non-Javadoc)
	 * @see com.netapp.pulse.start.model.entities.impl.INews#setVisible(boolean)
	 */
	@Override
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	
	/* (non-Javadoc)
	 * @see com.netapp.pulse.start.model.entities.INews#getAppId()
	 */
	@Override
	public String getAppId() {
		return appId;
	}
	
	/* (non-Javadoc)
	 * @see com.netapp.pulse.start.model.entities.INews#setAppId(java.lang.String)
	 */
	@Override
	public void setAppId(String appId) {
		this.appId = appId;
	}

}
