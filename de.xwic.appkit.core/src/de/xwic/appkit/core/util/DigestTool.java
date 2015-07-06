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
/*
 * de.jwic.wap.util.DigestTool
 * Created on 01.10.2004
 * $Id: DigestTool.java,v 1.1 2008/08/20 17:51:19 flippisch Exp $
 */
package de.xwic.appkit.core.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Encodes a String with the java.security.MessageDigest class and returns the
 * result as a hex string.
 * @author Florian Lippisch
 * @version $Revision: 1.1 $
 */
public class DigestTool {

	public final static String DEFAULT_METHOD = "MD5";
	
	/**
	 * Encodes the string in with default method (MD5) and returns it as 
	 * a hex string.
	 * @param s
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public static String encodeString(String s) {
		try {
			return encodeString(s, DEFAULT_METHOD);
		} catch (NoSuchAlgorithmException e) {
			// since we use a standard format, this exception should never happen.
			throw new IllegalStateException("MessageDigest does not support default method " + DEFAULT_METHOD);
		}
	}
	
	/**
	 * Encodes the string in with specified method and returns it as 
	 * a hex string.  
	 * @see java.security.MessageDigest
	 * @param s
	 * @param method
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public static String encodeString(String s, String method) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance(method);
		
		md.update(s.getBytes());
		
		byte[] dig = md.digest();
		return convert(dig);
	}
	
	/**
	 * Convert a byte array to an hex string.
	 * @param bytes
	 * @return
	 */
    public static String convert(byte bytes[])
    {
        StringBuffer sb = new StringBuffer(bytes.length * 2);
        for(int i = 0; i < bytes.length; i++)
        {
            sb.append(convertDigit(bytes[i] >> 4));
            sb.append(convertDigit(bytes[i] & 0xf));
        }

        return sb.toString();
    }
    
    /**
     * convert an int value to the corresponding hex character (0-9,A-F)
     * @param value
     * @return
     */
	private static char convertDigit(int value)
    {
        value &= 0xf;
        if(value >= 10)
            return (char)((value - 10) + 97);
        else
            return (char)(value + 48);
    }

	
}
