/**
 * 
 */
package de.xwic.appkit.core.mail.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.InternetHeaders;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.xwic.appkit.core.config.ConfigurationException;
import de.xwic.appkit.core.config.ConfigurationManager;
import de.xwic.appkit.core.config.Domain;
import de.xwic.appkit.core.config.Resource;
import de.xwic.appkit.core.config.Setup;
import de.xwic.appkit.core.mail.IAttachment;
import de.xwic.appkit.core.mail.IMail;
import de.xwic.appkit.core.mail.IMailAgent;
import de.xwic.appkit.core.mail.IMailManager;
import de.xwic.appkit.core.mail.ITemplateEngine;
import de.xwic.appkit.core.mail.MailFactory;

/**
 * Implementation of mail sending via javax.mail.
 * 
 * @author Ronny Pfretzschner
 * 
 */
public class JavaMailManager implements IMailManager {

	private Properties props = null;

	private Log log = LogFactory.getLog(JavaMailManager.class);

	private Map<IMailAgent, Thread> agents = new HashMap<IMailAgent, Thread>();

	/**
	 * Creates the manager with the given config path.
	 * 
	 * @param configPropertiesPath
	 */
	public JavaMailManager(Properties properties) {
		if (properties == null || properties.isEmpty()) {
			throw new IllegalArgumentException("Mailproperties are not set!");
		}
		props = properties;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.netapp.mail.IMailManager#sendEmail(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void sendEmail(String content, String subject, String[] toAddresses, String[] toAddressesCC, String fromAddress) {

		try {
			Session session = Session.getDefaultInstance(props);
			MimeMessage msg = new MimeMessage(session);

			msg.setSubject(subject, "UTF-8");
			msg.setContent(content, "text/html; charset=\"UTF-8\"");

			Address from = new InternetAddress(fromAddress, fromAddress);
			msg.setFrom(from);

			Address[] addressesMain = new Address[toAddresses.length];

			for (int i = 0; i < toAddresses.length; i++) {
				addressesMain[i] = new InternetAddress(toAddresses[i]);
			}

			Address[] addressescc = new Address[toAddressesCC.length];

			for (int i = 0; i < toAddressesCC.length; i++) {
				addressescc[i] = new InternetAddress(toAddressesCC[i]);
			}

			msg.setRecipients(Message.RecipientType.TO, addressesMain);
			msg.setRecipients(Message.RecipientType.CC, addressescc);

			sendTheEmail(msg);

		} catch (Exception e) {
			//RPF: TODO a better error handling is necessary
			//maybe with validationresult and error messages
			log.error("Error sending mail", e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.netapp.mail.IMailManager#startEmailAgents()
	 */
	@Override
	public void startEmailAgents() {

		for (Iterator<IMailAgent> iterator = agents.keySet().iterator(); iterator.hasNext();) {
			IMailAgent agentRunnable = iterator.next();
			Thread agent = agents.get(agentRunnable);

			//is already running, stop old agent
			if (agentRunnable != null && agent != null) {
				log.info("Agent already running, shut down agent and restarting it now...");
				log.info("Stopping Email agent now...");
				agentRunnable.exitAgent();
				agent.interrupt();
			}

			agent.setDaemon(true);
			agent.setName(agentRunnable.getAgentId());
			agent.start();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.netapp.mail.IMailManager#stopEmailAgents()
	 */
	@Override
	public void stopEmailAgents() {
		for (Iterator<IMailAgent> iterator = agents.keySet().iterator(); iterator.hasNext();) {
			IMailAgent agentRunnable = iterator.next();
			Thread agent = agents.get(agentRunnable);

			if (agentRunnable != null && agent != null) {
				log.info("Stopping Email agent now...");
				agentRunnable.exitAgent();
				agent.interrupt();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.netapp.mail.IMailManager#registerMailAgent(com.netapp.mail.IMailAgent)
	 */
	@Override
	public void registerMailAgent(IMailAgent mailAgent) {
		agents.put(mailAgent, new Thread(mailAgent));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.pol.netapp.spc.model.util.mail.IMailManager#sendEmail(de.pol.netapp.spc.model.util.mail.IMail)
	 */
	@Override
	public void sendEmail(IMail mail) throws Exception {
		sendEmail(mail, false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.netapp.pulse.model.mail.IMailManager#sendEmail(com.netapp.pulse.model.mail.IMail, boolean)
	 */
	@Override
	public void sendEmail(IMail mail, boolean ignoreTestMode) throws Exception {
		// ignoreTestMode flag used in AreaMailManager

		Session session = Session.getDefaultInstance(props);
		MimeMessage msg = new MimeMessage(session);

		msg.setSubject(mail.getSubject(), "UTF-8");

		Address from = new InternetAddress(mail.getSenderAddress(), mail.getSenderAddress());
		msg.setFrom(from);

		if (null != mail.getPriority() && !mail.getPriority().equals(IMail.NORMAL_PRIO)) {
			msg.setHeader(IMail.PRIORITY, mail.getPriority());
		}

		msg.setRecipients(Message.RecipientType.TO, convertToAddresses(mail.getToAddresses()));

		final List<String> ccAddresses = mail.getCcAddresses();
		if (ccAddresses != null) {
			msg.setRecipients(Message.RecipientType.CC, convertToAddresses(ccAddresses));
		}
		final List<String> bccAddresses = mail.getBccAddresses();
		if (bccAddresses != null) {
			msg.setRecipients(Message.RecipientType.BCC, convertToAddresses(bccAddresses));
		}

		List<IAttachment> attachments = mail.getAttachments();
		if (attachments == null || attachments.isEmpty()) {
			msg.setContent(mail.getContent(), "text/html; charset=\"UTF-8\"");
		} else {
			MimeMultipart multipart = new MimeMultipart();
			MimeBodyPart part = new MimeBodyPart(new InternetHeaders(), mail.getContent().getBytes());
			part.addHeader(IMail.CONTENT_TYPE, "text/html; charset=\"UTF-8\"");
			multipart.addBodyPart(part);

			for (IAttachment att : attachments) {

				if (true) { // wired bug with 
					// try with temp file.
					File tempFile = File.createTempFile("javamailattm", ".tmp");
					FileOutputStream fos = null;
					try {
						fos = new FileOutputStream(tempFile);
						fos.write(att.getData()); // write it
					} finally {
						try {
							fos.close();
						} catch (IOException e) {
							log.error(e.getMessage(), e);
						}
					}

					DataSource dsAttm = new FileDataSource(tempFile);

					part = new MimeBodyPart();
					part.setDataHandler(new DataHandler(dsAttm));
					part.setFileName(att.getFileName());
					multipart.addBodyPart(part);
					tempFile.deleteOnExit();
				}

				// this is dead code below, commented it out
				//				} else {
				//				
				//					byte[] encodedData = encodeBase64(att.getData());
				//					part = new MimeBodyPart(new InternetHeaders(), encodedData);
				//					part.addHeader(IMail.MIME_VERSION, "1.0");
				//					part.addHeader(IMail.CONTENT_TYPE, att.getContentType() + "; name=\"" + att.getFileName() +"\"");
				//					part.addHeader(IMail.CONTENT_ENCODING, "base64");
				//					multipart.addBodyPart(part);
				//				}
			}
			msg.setContent(multipart);
		}

		sendTheEmail(msg);
	}

	protected byte[] encodeBase64(byte[] data) throws Exception {
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		OutputStream out = MimeUtility.encode(bout, "base64");

		out.write(data);
		return bout.toByteArray();
	}

	/**
	 * @param addressList
	 * @return an address array
	 * @throws AddressException
	 */
	private Address[] convertToAddresses(List<String> addressList) throws AddressException {
		Address[] ar = new Address[addressList.size()];

		int i = 0;
		for (String addr : addressList) {
			ar[i++] = new InternetAddress(addr);
		}
		return ar;
	}

	/**
	 * Send an email to the specified address by parsing the VLT template specified. The key to the template needs to be specified like
	 * this: "domainkey:resourcekey". If the domainkey is not specified, the "resources" domain is used by default.
	 * 
	 * @param from
	 * @param to
	 * @param subject
	 * @param body
	 * @throws ConfigurationException
	 */
	@Override
	public void fromTemplate(String subject, String resourceTemplateKey, Map<String, Object> context, String from, String... to)
			throws ConfigurationException {
		fromTemplate(subject, resourceTemplateKey, context, from, new String[0], to);
	}
	
	/**
	 * Send an email to the specified address by parsing the VLT template specified. The key to the template needs to be specified like
	 * this: "domainkey:resourcekey". If the domainkey is not specified, the "resources" domain is used by default.
	 * 
	 * @param from
	 * @param to
	 * @param subject
	 * @param body
	 * @throws ConfigurationException
	 */
	@Override
	public void fromTemplate(String subject, String resourceTemplateKey, Map<String, Object> context, String from, List<String> to)
			throws ConfigurationException {
		fromTemplate(subject, resourceTemplateKey, context, from, new String[0], to.toArray(new String[to.size()]));
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
	@Override
	public void fromTemplate(String subject, String resourceTemplateKey, Map<String, Object> context, String from, String[] cc,
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

		sendEmail(body, subject, to, cc, from);

	}

	/**
	 * @param msg
	 * @throws MessagingException
	 */
	private void sendTheEmail(MimeMessage msg) throws MessagingException {
		String strSendNoEmails = ConfigurationManager.getSetup().getProperty("send-no-emails", "false");
		Boolean sendNoEmails = Boolean.parseBoolean(strSendNoEmails);
		if (sendNoEmails) {
			StringBuilder sbRecipients = new StringBuilder();
			sbRecipients.append("' TO: ");
			boolean first = true;
			if (msg.getRecipients(Message.RecipientType.TO) != null) {
				for (Address add : msg.getRecipients(Message.RecipientType.TO)) {
					if (!first) {
						sbRecipients.append(", ");
					}
					sbRecipients.append(add);
					first = false;
				}
			}

			if (msg.getRecipients(Message.RecipientType.CC) != null) {
				sbRecipients.append("; CC: ");
				first = true;
				for (Address add : msg.getRecipients(Message.RecipientType.CC)) {
					if (!first) {
						sbRecipients.append(", ");
					}
					sbRecipients.append(add);
					first = false;
				}
			}

			log.info("Email '" + msg.getSubject() + sbRecipients.toString() + " was not sent because email sending is disabled!");
			return;
		}

		Transport.send(msg);
	}

}
