package de.xwic.appkit.core.config;

/**
 * Represents a logical group of functionality that is presented to the user
 * as an application on its own. Apps run on the same platform, so they can
 * share modules, functions, etc. with each other, but still are accessible 
 * in a different manner for the user, like with their own URL.
 * 
 * Certain components may be sensitive to under which App a session is currently
 * running, to behave specific to the current App.
 *   
 * @author lippisch
 */
public class App {

	private String id;
	private String title;
	
	/**
	 * @param id
	 * @param title
	 */
	public App(String id, String title) {
		super();
		this.id = id;
		this.title = title;
	}
	
	/**
	 * Unique id of the App. Should be globally unique, like a package.
	 * <p>Examples:
	 * <ul><li>de.xwic.MyApp
	 * <li>com.domain.WeatherChannel
	 * </ul>
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * Unique id of the App. Should be globally unique, like a package.
	 * <p>Examples:
	 * <ul><li>de.xwic.MyApp
	 * <li>com.domain.WeatherChannel
	 * </ul>
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * Title of the application, which may be presented to the user.
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * Title of the application, which may be presented to the user.
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	
}
