package de.xwic.appkit.webbase.editors;

import de.xwic.appkit.core.dao.ValidationResult;
import de.xwic.appkit.core.model.IEntityModel;
import de.xwic.appkit.webbase.editors.events.ValidationEvent;

/**
 * Custom entity validator used to extend validation as extension.
 *
 * @author <a href="mailto:vzhovtiuk@gmail.com">Vitaliy Zhovtyuk</a>
 */
public interface IEditorValidatorListener {
    /**
     * Validate model and provide result.
     * @param model entity model
     * @param validationResult validation result
     */
    void modelValidated(ValidationEvent validationEvent);
}
