package de.xwic.appkit.core.file.uc;

import de.xwic.appkit.core.dao.IEntity;
import de.xwic.appkit.core.model.entities.IAnhang;

public interface IAttachmentWrapper {

	public abstract boolean isDeleted();

	public abstract int getFileId();

	public abstract String getFileName();

	public void setFileName(String fileName);
	
	public void setContentLength(long length);
	
	public abstract long getContentLength();

	public abstract int getEntityId();

	public abstract String getEntityType();

	public abstract void setEntity(IEntity entity);

	public abstract String getKey();

	public abstract void setKey(String key);

	public abstract String getTmpFileName();

	public abstract void setTmpFileName(String tmpFileName);

	public abstract String getContentType();

	public abstract void setContentType(String contentType);

	public void setFileId(int fileId);
	
	public void setDeleted(boolean deleted);

	/**
	 * @return the anhang
	 */
	public IAnhang getAnhang();

	/**
	 * @param anhang the anhang to set
	 */
	public void setAnhang(IAnhang anhang);
}