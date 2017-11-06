/**
 * 
 */
package de.xwic.appkit.dev.engine;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Contains information about the application being modified/generated, such
 * as the root directory, project folders etc.
 *  
 * @author lippisch
 */
public class AppContext {

	private final Log log = LogFactory.getLog(getClass());
	
	private File repositoryRoot = null;
	private File productConfigFolder = null;
	
	private String sourceFolderName = "src";
	
	private int filesCreated = 0; 
	
	/**
	 * Instantiate from configuration properties, usually loaded from 
	 * a file.
	 * @param prop
	 * @throws IOException 
	 */
	public AppContext(Properties prop) throws IOException {
		
		repositoryRoot = new File(prop.getProperty("root", "."));
		productConfigFolder = new File(repositoryRoot, prop.getProperty("config.folder", "./config"));
		sourceFolderName = prop.getProperty("src.foldername", sourceFolderName);
		
		System.out.println("Root:        " + repositoryRoot.getCanonicalPath());
		System.out.println("Config:      " + productConfigFolder.getCanonicalPath());
		
		if (!repositoryRoot.exists()) {
			log.warn("The repository root directory specified does not exist!");
		}
		
	}

	/**
	 * @return the repositoryRoot
	 */
	public File getRepositoryRoot() {
		return repositoryRoot;
	}

	/**
	 * @return the productConfigFolder
	 */
	public File getProductConfigFolder() {
		return productConfigFolder;
	}

	/**
	 * @return the sourceFolderName
	 */
	public String getSourceFolderName() {
		return sourceFolderName;
	}

	/**
	 * A file was created.
	 */
	public void countFileCreated() {
		filesCreated++;
	}
	
	/**
	 * @return the filesCreated
	 */
	public int getFilesCreated() {
		return filesCreated;
	}
	
}
