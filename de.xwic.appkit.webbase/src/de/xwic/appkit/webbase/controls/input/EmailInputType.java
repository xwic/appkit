/*
 * Copyright 2005-2007 jWic group (http://www.jwic.de)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * de.jwic.controls.input.EmailInputType
 * Created on 03.09.2012
 * $Id:$
 */
package de.xwic.appkit.webbase.controls.input;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A type just to provide as type parameter for the TypedInoutBoxControl. Also, the field should not know what
 * a valid string value is, the type knows what a valid string representation looks like.
 *
 * Might hold several email addresses, separated by ','.
 * Checks for the separate email addresses having <one or more characters>@<one or more characters>.<one or more characters>
 *
 * @author Martin Weinand
 */
public class EmailInputType {
	
	//private static final String emailRegexString = "\\S+@\\S+\\.\\S+(,\\S+@\\S+\\.\\S+)*";//how can one formalize repeat, comma separated?
	private static final String EMAIL_REGEX_STRING = "\\S+@\\S+\\.\\S+";
	public static final String EMAIL_TOKEN_SEPARATORS = "[,;]";
	
	private static final Matcher matcher = Pattern.compile(EMAIL_REGEX_STRING).matcher("");
	
	private String emailString;

	public EmailInputType(){
		
	}
	
	/**
	 * 
	 * @param emailString may hold several email addresses separated by ',' or ';'; whitespaces are trimmed
	 */
	public EmailInputType(String emailString) {
		super();
		this.emailString = normalize(emailString);
		
	}

	public String getString(){
		return emailString;
	}
	
	public boolean isValid(){
		
		if(emailString == null || emailString.isEmpty()){
			return true;
		}
		
		String[] emailTokens = emailString.split(EMAIL_TOKEN_SEPARATORS);
		for (int i = 0; i < emailTokens.length; i++) {
			String token = emailTokens[i];
			if(!matcher.reset(token).matches()){
				return false;
			}
		}
		
		return true;
	}
	
	/*
	 * removes white spaces
	 */
	private String normalize(String emailString2) {
		if(emailString2 == null){
			return emailString2;
		}
		String replaceAll = emailString2.replaceAll(";", ",");
		replaceAll = emailString2.replaceAll("\\s*,\\s*", ",");
		return replaceAll;
	}
}
