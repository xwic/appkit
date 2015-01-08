package de.xwic.appkit.core.mail;

import java.util.List;

import de.xwic.appkit.core.util.CollectionUtil;

public class MailBuilder {

	private final String from;
	private final List<String> to;

	private String content;
	private List<String> cc;
	private List<String> bcc;
	private String subject;
	private List<IAttachment> attachments;
	private Prority priority;

	/**
	 * @param to
	 * @param from
	 */
	public MailBuilder(String from, List<String> to) {
		super();
		this.to = to;
		this.from = from;
		this.priority = Prority.UNSPECIFIED;

	}

	public MailBuilder(String from, String... to) {
		this(from, CollectionUtil.convertToList(to));
	}

	/**
	 * @param content
	 *            the content to set
	 */
	public MailBuilder setContent(String content) {
		this.content = content;
		return this;
	}

	/**
	 * @param cc
	 *            the cc to set
	 */
	public MailBuilder setCc(List<String> cc) {
		this.cc = cc;
		return this;
	}

	public MailBuilder setCc(String... cc) {
		return this.setCc(CollectionUtil.convertToList(cc));
	}

	/**
	 * @param bcc
	 *            the bcc to set
	 */
	public MailBuilder setBcc(List<String> bcc) {
		this.bcc = bcc;
		return this;
	}

	public MailBuilder setBcc(String... bcc) {
		return this.setBcc(CollectionUtil.convertToList(bcc));
	}

	public MailBuilder setPriority(Prority priority) {
		this.priority = priority != null ? priority : Prority.UNSPECIFIED;
		return this;
	}

	public MailBuilder setAttachments(List<IAttachment> attachments) {
		this.attachments = attachments;
		return this;
	}

	public MailBuilder setAttachments(IAttachment... attachments) {
		return this.setAttachments(CollectionUtil.convertToList(attachments));
	}

	/**
	 * @param subject
	 *            the subject to set
	 */
	public MailBuilder setSubject(String subject) {
		this.subject = subject;
		return this;
	}

	public IMail build() {
		IMail mail = new EmptyMail();
		mail.setSenderAddress(this.from);
		mail.setCcAddresses(this.cc);
		mail.setToAddresses(this.to);
		mail.setSubject(this.subject);
		mail.setContent(this.content);
		mail.setAttachments(this.attachments);
		mail.setPriority(this.priority.priority);
		mail.setBccAddresses(this.bcc);
		return mail;
	}

	public static enum Prority {
		HIGH(IMail.HIGH_PRIO), NORMAL(IMail.NORMAL_PRIO), LOW(IMail.LOW_PRIO), UNSPECIFIED(null);

		String priority;

		private Prority(String p) {
			this.priority = p;
		}
	}

}
