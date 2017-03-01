/**
 * 
 */
package de.xwic.appkit.dev.engine;

/**
 * @author lippisch
 *
 */
public class EngineUtil {

	/**
	 * Translates a java style name to a DB style name. For example:
	 * <li>name = NAME</li>
	 * <li>masterAndSlave = MASTER_AND_SLAVE</li>
	 * <li>do3Times = DO3_TIMES</li>
	 * <li>with_special_chars = WITH_SPECIAL_CHARS</li>
	 * @param javaName
	 * @return
	 */
	public static String toDBName(String javaName) {
		
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < javaName.length(); i++) {
			char c = javaName.charAt(i);
			if (c >= 'a' && c <= 'z') {
				sb.append(String.valueOf(c).toUpperCase());
			} else if (c >= 'A' && c <= 'Z') {
				if (i > 0) {
					sb.append("_");
				}
				sb.append(c);
			} else if (c >= '0' && c <= '9') {
				sb.append(c);
			} else if (c == '_') {
				sb.append("_");
			}
		}
		return sb.toString();
		
	}
	
}
