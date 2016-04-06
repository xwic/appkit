package de.xwic.appkit.webbase.editors;

import de.xwic.appkit.core.dao.ValidationResult;
import de.xwic.appkit.core.model.IEntityModel;

/**
 * @author <a href="mailto:vzhovtiuk@gmail.com">Vitaliy Zhovtyuk</a>
 */
public interface IEditorValidator {
    void validate(IEntityModel model, ValidationResult validationResult);
}
