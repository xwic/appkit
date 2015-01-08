package de.xwic.appkit.core.mail;

public class TemplateNotFoundException extends Exception {

	private static final long serialVersionUID = -6987469094283734446L;

	public TemplateNotFoundException() {
		// TODO Auto-generated constructor stub
	}

	public TemplateNotFoundException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public TemplateNotFoundException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public TemplateNotFoundException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public static String buildMessage(String templateName) {
		return String.format("No mail templete defined for %s! Please define one in Mail Template with the Template \"%s\"",
				templateName, templateName);
	}
}
