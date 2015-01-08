/**
 * 
 */
package de.xwic.appkit.core.mail;

import java.util.List;

/**
 * @author Oleksiy Samokhvalov
 * 
 */
public interface IMail {

	public static final String MIME_VERSION = "MIME-Version";
	public static final String CONTENT_TYPE = "Content-Type";
	public static final String CONTENT_ENCODING = "Content-Transfer-Encoding";
	public static final String PRIORITY = "X-Priority";

	public static final String HIGH_PRIO = "1";
	public static final String NORMAL_PRIO = "3";
	public static final String LOW_PRIO = "5";

	/**
	 * @return the sender email.
	 */
	String getSenderAddress();

	/**
	 * @return the subject
	 */
	String getSubject();

	/**
	 * @return a list of the TO addresses.
	 */
	List<String> getToAddresses();

	/**
	 * @return a lis tof CC addresses.
	 */
	List<String> getCcAddresses();

	/**
	 * @return the email content.
	 */
	String getContent();

	/**
	 * @return a list of attachments.
	 */
	List<IAttachment> getAttachments();

	/**
	 * @param attachments
	 *            the attachments to set
	 */
	public void setAttachments(List<IAttachment> attachments);

	/**
	 * @param ccAddresses
	 *            the ccAddresses to set
	 */
	public void setCcAddresses(List<String> ccAddresses);

	/**
	 * @param content
	 *            the content to set
	 */
	public void setContent(String content);

	/**
	 * @param senderAddress
	 *            the senderAddress to set
	 */
	public void setSenderAddress(String senderAddress);

	/**
	 * @param subject
	 *            the subject to set
	 */
	public void setSubject(String subject);

	/**
	 * @param toAddresses
	 *            the toAddresses to set
	 */
	public void setToAddresses(List<String> toAddresses);

	/**
	 * @return the priority
	 */
	public String getPriority();

	/**
	 * @param priority
	 *            the priority to set
	 */
	public void setPriority(String priority);

	public abstract List<String> getBccAddresses();

	public abstract void setBccAddresses(List<String> bcc);
}
