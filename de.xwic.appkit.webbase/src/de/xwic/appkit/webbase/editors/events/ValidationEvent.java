package de.xwic.appkit.webbase.editors.events;

import de.jwic.base.Event;
import de.xwic.appkit.core.dao.ValidationResult;

/**
 * Validation event class for model validation.
 *
 * @author <a href="mailto:vzhovtiuk@gmail.com">Vitaliy Zhovtyuk</a>
 */
public class ValidationEvent extends Event {
    /**
     * Validation result.
     */
    private final ValidationResult validationResult;
    /**
     * Validate on model save.
     */
    private final boolean onSave;

    public ValidationEvent(Object eventSource, ValidationResult validationResult, boolean onSave) {
        super(eventSource);
        this.validationResult = validationResult;
        this.onSave = onSave;
    }

    public ValidationResult getValidationResult() {
        return validationResult;
    }

    public boolean isOnSave() {
        return onSave;
    }
}
