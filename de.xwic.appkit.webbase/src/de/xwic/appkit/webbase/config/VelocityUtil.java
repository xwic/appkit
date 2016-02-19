/**
 * 
 */
package de.xwic.appkit.webbase.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * @author lippisch
 *
 */
public class VelocityUtil {

	/**
	 * Sort a list of strings without changing the original list.
	 * @param list
	 * @return
	 */
	public Collection<String> sorted(Collection<String>  list) {
		
		ArrayList<String> newList = new ArrayList<String>();
		newList.addAll(list);
		Collections.sort(newList);
		
		return newList;
		
	}
	
}
