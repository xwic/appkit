/*
 * Copyright (c) 2009 Network Appliance, Inc.
 * All rights reserved.
 */

package de.xwic.appkit.core.mail.impl;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.xwic.appkit.core.config.ConfigurationException;
import de.xwic.appkit.core.config.ConfigurationManager;
import de.xwic.appkit.core.config.Domain;
import de.xwic.appkit.core.config.Resource;
import de.xwic.appkit.core.config.Setup;
import de.xwic.appkit.core.mail.EmptyMail;
import de.xwic.appkit.core.mail.IAttachment;
import de.xwic.appkit.core.mail.IMail;
import de.xwic.appkit.core.mail.IMailManager;
import de.xwic.appkit.core.mail.ITemplateEngine;
import de.xwic.appkit.core.mail.MailFactory;
import de.xwic.appkit.core.util.ConfigurationUtil;

/**
 * Simple helper class to send emails easily. Checks if the system (in general) is in 'testmode'. If so, all emails are redirected.
 * 
 * @author Lippisch
 */
public class SendMail {

	private static final Log log = LogFactory.getLog(SendMail.class);

	/**
	 * @param area
	 * @param subject
	 * @param body
	 * @param from
	 * @param cc
	 * @param to
	 */
	public static void textHtml(String subject, String body, String from, String[] cc, String... to) {
		if (to.length == 0) {
			throw new IllegalArgumentException("You must specify at least one TO address.");
		}

		IMailManager mailManager = MailFactory.getJavaMailManager();
		mailManager.sendEmail(body, subject, to, cc != null ? cc : new String[0], from);
	}

	/**
	 * Send an email to the specified address by parsing the VLT template specified. The key to the template needs to be specified like
	 * this: "domainkey:resourcekey". If the domainkey is not specified, the "resources" domain is used by default.
	 * 
	 * @param subject
	 * @param resourceTemplateKey
	 * @param context
	 * @param from
	 * @param cc
	 * @param to
	 * @throws ConfigurationException
	 */
	public static void fromTemplate(String subject, String resourceTemplateKey, Map<String, Object> context, String from, String[] cc,
			String... to) throws ConfigurationException {
		if (to.length == 0) {
			throw new IllegalArgumentException("You must specify at least one TO address.");
		}

		String domainKey = "resources";
		String[] tmp = resourceTemplateKey.split(":");
		if (tmp.length > 1) {
			domainKey = tmp[0];
			resourceTemplateKey = tmp[1];
		}

		Setup setup = ConfigurationManager.getSetup();

		Domain domain = setup.getDomain(domainKey);
		Resource resource = domain.getResource(resourceTemplateKey);

		ITemplateEngine tpEngine = MailFactory.getTemplateEngine();
		String body = tpEngine.generateContentFromTemplate(resource.getLocation(), context);

		textHtml(subject, body, from, cc, to);

	}

	/**
	 * @param subject
	 * @param templateName
	 * @param contextObjects
	 * @param toAddresses
	 * @param ccAddresses
	 * @param attachments
	 * @param area
	 */
	public static void sendMessage(String subject, String templateName, Map<String, Object> contextObjects, List<String> toAddresses,
			List<String> ccAddresses, List<IAttachment> attachments) {

		sendMessage(subject, templateName, contextObjects, ConfigurationUtil.getSystemEmailAddress(), toAddresses, ccAddresses, attachments);
	}

	/**
	 * @param subject
	 * @param templateName
	 * @param contextObjects
	 * @param senderAddress
	 * @param toAddresses
	 * @param ccAddresses
	 * @param attachments
	 * @param area
	 */
	public static void sendMessage(String subject, String templateName, Map<String, Object> contextObjects, String senderAddress,
			List<String> toAddresses, List<String> ccAddresses, List<IAttachment> attachments) {
		try {
			IMailManager mailManager = MailFactory.getJavaMailManager();
			ITemplateEngine engine = MailFactory.getTemplateEngine();

			IMail mail = new EmptyMail();
			mail.setSenderAddress(senderAddress);

			Setup setup = ConfigurationManager.getSetup();
			Resource res = setup.getDomain("resources").getResource(templateName);

			mail.setContent(engine.generateContentFromTemplate(res.getLocation(), contextObjects));
			mail.setSubject(subject);
			mail.setToAddresses(toAddresses);
			if (ccAddresses != null && !ccAddresses.isEmpty()) {
				mail.setCcAddresses(ccAddresses);
			}

			if (attachments != null) {
				mail.setAttachments(attachments);
			}

			mailManager.sendEmail(mail);
		} catch (Exception ex) {
			//			isn't this important information?
			StringBuilder sb = new StringBuilder("Failed to send Email!");
			sb.append("\nfrom: ").append(senderAddress);
			sb.append("\nto: ").append(toAddresses);
			sb.append("\ncc: ").append(ccAddresses);
			sb.append("\nsubject: ").append(subject);
			sb.append("\ntemplate: ").append(templateName);
			//			sb.append("\ncontext: ").append(contextObjects);
			sb.append("\nattachments: ").append(attachments);
			log.error(sb.toString(), ex);
		}
	}

	public void sendMessage(IMail mail) {
		try {
			IMailManager mailManager = MailFactory.getJavaMailManager();
			mailManager.sendEmail(mail);
		} catch (Exception e) {
			StringBuilder sb = new StringBuilder("Failed to send Email!");
			sb.append("\nfrom: ").append(mail.getSubject());
			sb.append("\nto: ").append(mail.getToAddresses());
			sb.append("\ncc: ").append(mail.getCcAddresses());
			sb.append("\nbcc: ").append(mail.getBccAddresses());
			sb.append("\nsubject: ").append(mail.getSubject());
			sb.append("\nattachments: ").append(mail.getAttachments());
			log.error(sb.toString(), e);
		}
	}

	/**
	 * This is sending an email to the Emergency DL with the details. Use this to notify admins in case of an urgent system failure. This
	 * method will catch all exceptions and log them to file in case the sending fails to avoid follow-up errors.
	 * 
	 * @param msg
	 * @param details
	 * @param error
	 */
	public static void sendEmergencyInfo(String msg, String details, Exception error) {
		List<String> receivers = ConfigurationUtil.getEmergencyEmailReceiver();
		sendEmergencyInfo(msg, details, error, receivers);
	}

	/**
	 * This is sending an email to the receivers with the details. Use this to notify a list of receivers in case of an urgent system
	 * failure. This method will catch all exceptions and log them to file in case the sending fails to avoid follow-up errors. <br>
	 * <br>
	 * The receivers string is split using {@link com.netapp.pulse.model.util.StringUtil#parseEmails(String)}.
	 * 
	 * @param msg
	 * @param details
	 * @param ex
	 * @param receivers
	 */
	public static void sendEmergencyInfo(String msg, String details, Exception ex, List<String> receivers) {
		try {
			Setup setup = ConfigurationManager.getSetup();
			String sysName = setup.getProperty("server.name", "<unnamed>");
			StringBuilder body = new StringBuilder();
			body.append("<html><body>");
			body.append("<p style=\"font-family: Arial, Sans-Serif; font-size: 14pt; color: #ff0000;\">System Emergency Notification</p>");
			body.append("<p style=\"font-family: Arial, Sans-Serif; font-size: 10pt;\">");
			body.append("System name: <b>" + sysName + "</b><br/>");
			body.append("Host URL: " + setup.getProperty("my.baseurl", "-unknown-") + "<br/><br/>");
			body.append("Error message: <b>" + msg + "</b><br/><br/>");
			body.append("Details: " + details + "<br/><br/>");
			if (ex != null) {
				body.append("Exception: " + ex.toString()).append("<br/><pre>");
				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				ex.printStackTrace(pw);
				pw.flush();
				body.append(sw.getBuffer().toString());
				body.append("</pre>");
			}

			body.append("</p></body></html>");

			textHtml("[" + sysName + "]: Emergency Notification: " + msg, body.toString(), ConfigurationUtil.getSystemEmailAddress(),
					new String[0], receivers.toArray(new String[receivers.size()]));
		} catch (Exception e) {
			log.error("Critical - can not send emergency info: " + e, e);
		}
	}

}
