/*******************************************************************************
 * Copyright 2015 xWic group (http://www.xwic.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 		http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 *  
 *******************************************************************************/
package de.xwic.appkit.core.dao;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.xwic.appkit.core.cluster.INode;

/**
 * Validation result object for checking Entities. <p>
 * 
 * This class is used to keep errors or warnings in form <br>
 * of a resource keys like "entity.validate.args.wrong" in a Map. <br>
 * The properties/attributes of Entities are used to be key of the map entries.
 * 
 * @author Ronny Pfretzschner
 *
 */
public class ValidationResult {

	/** The field must contain data */
	public final static String FIELD_REQUIRED = "entity.validate.error.field.required";
	/** The field data exceeds the maximum number of characters */
	public final static String FIELD_TOO_LONG = "entity.validate.error.field.too.long";
	/** default error key */
	public final static String ENTITY_ARGUMENTS_ERROR_RESOURCE_KEY = "entity.validate.args.error.default";
	/** default warning key */
	public final static String ENTITY_ARGUMENTS_WARNING_RESOURCE_KEY = "entity.validate.args.warning.default";
	/** warning if a required property can not be read due to access restrictions */
	public static final String FIELD_REQUIRED_NOT_ACCESSABLE = "entity.validate.args.warning.noaccess";
	
	private Map<String, String> valErrorMap = new HashMap<String, String>();
	private Map<String, String> valWarningMap = new HashMap<String, String>();
	private Map<String, List<Object>> valErrorInfoMap = new HashMap<String, List<Object>>();
	
	private String entityType = null;
	
	/**
	 * Default ctor to keep compatible
	 */
	public ValidationResult() {
		
	}
	
	
	public ValidationResult(String entityType) {
		this.entityType = entityType;
	}
	
	
	
	
	/**
	 * Returns false, if no errors had been added through validation. <p>
	 * 
	 * @return true, if errors were added
	 */
	public boolean hasErrors() {
		return !valErrorMap.isEmpty();
	}
	
	/**
	 * Returns false, if no warnings had been added through validation. <p>
	 * 
	 * @return true, if warnings were added
	 */
	public boolean hasWarnings() {
		return !valWarningMap.isEmpty();
	}
	
	/**
	 * Adds a warning key like "un.validate.args.betreuer" to 
	 * the given property. <p> 
	 * 
	 * The default warning keys are provided as global variables in this class. <br>
	 * Example: ENTITY_ARGUMENTS_WARNING_RESOURCE_KEY for a text like "Arguments not set yet!". <br>
	 * The real text can be resolved through the <code>getResourceString(..)</code>
	 * methode of the DALPlugin class.
	 * 
	 * @param property the attribute, which has warnings 
	 * @param warningKey the resource string key
	 */
	public void addWarning(String property, String warningKey) {
		valWarningMap.put(property, warningKey);
	}
	
	/**
	 * Adds an error key like "entity.validate.args.wrong" to 
	 * the given property. <p> 
	 * 
	 * The error keys are provided as global variables in this class. <br>
	 * Example: ENTITY_ARGUMENTS_ERROR_RESOURCE_KEY for a text like "Arguments not allowed!". <br>
	 * The real text can be resolved through the <code>getResourceString(..)</code>
	 * methode of the DALPlugin class.
	 * 
	 * @param property the attribute, which has errors 
	 * @param errorKey the resource string key
	 * @param infoProps the additional properties info to be added to the error message.
	 */
	public void addError(String property, String errorKey, Object... infoProps) {
		valErrorMap.put(property, errorKey);
		if(!valErrorInfoMap.containsKey(property)){
			valErrorInfoMap.put(property, new LinkedList<Object>());
		}
		
		if(infoProps != null){
			for (Object infoProp : infoProps) {
				valErrorInfoMap.get(property).add(infoProp);
			}
		}
	}
	
	/**
	 * Returns the Map with all registered errors of
	 * a specific Entity. <p>
	 * 
	 * @return a new instance of the Map with all errors registered 
	 */
	public Map<String, String> getErrorMap() {
		return new HashMap<String, String>(valErrorMap);
	}
	
	/**
	 * Returns the Map with additional error informations for error properties
	 * @return
	 */
	public Map<String, List<Object>> getValErrorInfoMap() {
		return valErrorInfoMap;
	}
	
	/**
	 * Returns the Map with all registered warnings of
	 * a specific Entity. <p>
	 * 
	 * @return a new instance of the Map with all warnings registered 
	 */
	public Map<String, String> getWarningMap() {
		return new HashMap<String, String>(valWarningMap);
	}
	
	/**
	 * Adds a complete already created error map to this result. <p>
	 * 
	 * Could be used to collect them for one result.
	 * 
	 * @param allErrors
	 */
	public void addErrors(Map<String, String> allErrors) {
		valErrorMap.putAll(allErrors);
	}
	
	
	/**
	 * Adds a complete already created warning map to this result. <p>
	 * 
	 * Could be used to collect them for one result.
	 * 
	 * @param allWarnings
	 */
	public void addWarnings(Map<String, String> allWarnings) {
		valWarningMap.putAll(allWarnings);
	}


	/**
	 * @return the entityType
	 */
	public String getEntityType() {
		return entityType;
	}
	
}
