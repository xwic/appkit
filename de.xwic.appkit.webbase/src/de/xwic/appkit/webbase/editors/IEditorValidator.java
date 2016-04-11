package de.xwic.appkit.webbase.editors;

import de.xwic.appkit.core.dao.ValidationResult;
import de.xwic.appkit.core.model.IEntityModel;

/**
 * Custom entity validator used to extend validation as extension.
 *
 * @author <a href="mailto:vzhovtiuk@gmail.com">Vitaliy Zhovtyuk</a>
 */
public interface IEditorValidator {
    /**
     * Validate model and provide result.
     * @param model entity model
     * @param validationResult validation result
     */
    void validate(IEntityModel model, ValidationResult validationResult);
}
