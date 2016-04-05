/*******************************************************************************
 * Copyright 2015 xWic group (http://www.xwic.de)
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License").
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package de.xwic.appkit.core.dao;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Validation result object for checking Entities. <p>
 *
 * This class is used to keep errors or warnings in form <br>
 * of a resource keys like "entity.validate.args.wrong" in a Map. <br>
 * The properties/attributes of Entities are used to be key of the map entries.
 *
 * @author Vitaliy Zhovtyuk
 *
 */
public class ValidationCallContext implements IValidationResult {
    private final static ValidationCallContext INSTANCE = new ValidationCallContext();

    private static final ThreadLocal<ValidationResult> THREAD_LOCAL = new ThreadLocal<ValidationResult>() {
        @Override
        protected ValidationResult initialValue() {
            return new ValidationResult();
        }
    };

    public synchronized static ValidationCallContext getInstance() {
        return INSTANCE;
    }



    @Override
    public boolean hasErrors() {
        return THREAD_LOCAL.get().hasErrors();
    }

    @Override
    public boolean hasWarnings() {
        return THREAD_LOCAL.get().hasWarnings();
    }

    @Override
    public void addWarning(String property, String warningKey) {
        THREAD_LOCAL.get().addWarning(property, warningKey);
    }

    @Override
    public void addError(String property, String errorKey, Object... infoProps) {
        THREAD_LOCAL.get().addError(property, errorKey, infoProps);
    }

    @Override
    public Map<String, String> getErrorMap() {
        return THREAD_LOCAL.get().getErrorMap();
    }

    @Override
    public Map<String, List<Object>> getValErrorInfoMap() {
        return THREAD_LOCAL.get().getValErrorInfoMap();
    }

    @Override
    public Map<String, String> getWarningMap() {
        return THREAD_LOCAL.get().getWarningMap();
    }

    @Override
    public void addErrors(Map<String, String> allErrors) {
        THREAD_LOCAL.get().addErrors(allErrors);
    }

    @Override
    public void addWarnings(Map<String, String> allWarnings) {
        THREAD_LOCAL.get().addWarnings(allWarnings);
    }

    public static void popuplateValidtionFromContext(ValidationResult validationResult) {
        validationResult.addErrors(ValidationCallContext.getInstance().getErrorMap());
        validationResult.addWarnings(ValidationCallContext.getInstance().getWarningMap());
        ValidationCallContext.THREAD_LOCAL.set(new ValidationResult());
    }

}
