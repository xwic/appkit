/**
 * 
 */
package de.xwic.appkit.core.mail;

import java.util.List;
import java.util.Map;

import de.xwic.appkit.core.config.ConfigurationException;

/**
 * Simple Mail Manager.
 * 
 * @author Ronny Pfretzschner
 * 
 */
public interface IMailManager {

	/**
	 * Send an email.
	 * 
	 * @param content
	 *            , mail content
	 * @param subject
	 * @param toAddresses
	 *            , recipients
	 * @param toAddressesCC
	 *            , recipients on CC
	 * @param fromAddress
	 *            , the one who sends the mail (mail address of actual user)
	 */
	@Deprecated()
	public void sendEmail(String content, String subject, String[] toAddresses, String[] toAddressesCC, String fromAddress);

	/**
	 * Sends the email.
	 * 
	 * @param email
	 */
	public void sendEmail(IMail email) throws Exception;

	/**
	 * Sends the email.
	 * 
	 * @param email
	 * @param ignoreTestMode
	 *            - if true it sends to real receivers
	 */
	public void sendEmail(IMail email, boolean ignoreTestMode) throws Exception;

	/**
	 * Starts serverside registered mail agents (thread) for automatic email sending.
	 * 
	 */
	public void startEmailAgents();

	/**
	 * Stops the mail agents.
	 */
	public void stopEmailAgents();

	/**
	 * Registers a custom made mail agent to get started running on the server.
	 * 
	 * @param mailAgent
	 */
	public void registerMailAgent(IMailAgent mailAgent);

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
	public void fromTemplate(String subject, String resourceTemplateKey, Map<String, Object> context, String from, String... to)
			throws ConfigurationException;
	
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
	public void fromTemplate(String subject, String resourceTemplateKey, Map<String, Object> context, String from, List<String> to)
			throws ConfigurationException;

	/**
	 * 
	 * 
	 * 
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
	 * 
	 */
	public void fromTemplate(String subject, String resourceTemplateKey, Map<String, Object> context, String from, String[] cc,
			String... to) throws ConfigurationException;



}
