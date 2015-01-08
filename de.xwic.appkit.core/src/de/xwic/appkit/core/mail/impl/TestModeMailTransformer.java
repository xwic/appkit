package de.xwic.appkit.core.mail.impl;

import java.util.ArrayList;
import java.util.List;

import de.xwic.appkit.core.util.ConfigurationUtil;

/**
 * @author Andrei Pat
 * 
 */
public class TestModeMailTransformer {

	/**
	 * @param toAddresses
	 * @param ccAddresses
	 * @param subj
	 * @return
	 */
	public static TransformOutcome transform(List<String> toAddresses, List<String> ccAddresses, List<String> bccAddresses, String subj,
			boolean ignoreTestMode) {
		boolean systemTestMode = ConfigurationUtil.isTestMode();

		List<String> toAddr = new ArrayList<String>();
		List<String> ccAddr = new ArrayList<String>();
		List<String> bccAddr = new ArrayList<String>();

		String subject = new String(subj);

		String ccSuffix = ccAddresses != null && !ccAddresses.isEmpty() ? "; CC: (" + ccAddresses + ")" : "";
		String bccSuffix = (null != bccAddresses && !bccAddresses.isEmpty() ? " BCC: (" + bccAddresses + ")" : "");

		List<String> testEmailReceiver = ConfigurationUtil.getTestEmailReceiver();

		final String initialAddresses = toAddresses + ccSuffix + bccSuffix;

		if (systemTestMode && !ignoreTestMode) {
			toAddr.addAll(testEmailReceiver);
			subject = "TestMode: " + subject + "; Original Receivers: " + initialAddresses;
		} else {
			toAddr.addAll(toAddresses);
			if (ccAddresses != null) {
				ccAddr.addAll(ccAddresses);
			}
			if (bccAddresses != null) {
				bccAddr.addAll(bccAddresses);
			}
		}

		TransformOutcome outcome = new TransformOutcome();
		outcome.newToAddresses = toAddr;
		outcome.newCCAddresses = ccAddr;
		outcome.newSubject = subject;
		outcome.newBccAddress = bccAddr;

		return outcome;

	}

	/**
	 * @author Aron Cotrau
	 */
	public static class TransformOutcome {

		private List<String> newBccAddress;
		private List<String> newToAddresses;
		private List<String> newCCAddresses;
		private String newSubject;

		/**
		 * @return the newBccAddress
		 */
		public List<String> getNewBccAddress() {
			return newBccAddress;
		}

		/**
		 * @return the newToAddresses
		 */
		public List<String> getNewToAddresses() {
			return newToAddresses;
		}

		/**
		 * @return the newCCAddresses
		 */
		public List<String> getNewCCAddresses() {
			return newCCAddresses;
		}

		/**
		 * @return the newSubject
		 */
		public String getNewSubject() {
			return newSubject;
		}
	}
}
