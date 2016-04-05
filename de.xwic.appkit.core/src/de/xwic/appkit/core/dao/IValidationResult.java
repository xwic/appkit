package de.xwic.appkit.core.dao;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:vzhovtiuk@gmail.com">Vitaliy Zhovtyuk</a>
 */
public interface IValidationResult {
	boolean hasErrors();

	boolean hasWarnings();

	void addWarning(String property, String warningKey);

	void addError(String property, String errorKey, Object... infoProps);

	Map<String, String> getErrorMap();

	Map<String, List<Object>> getValErrorInfoMap();

	Map<String, String> getWarningMap();

	void addErrors(Map<String, String> allErrors);

	void addWarnings(Map<String, String> allWarnings);
}
