
package de.xwic.appkit.dev.cli;

import org.apache.log4j.PropertyConfigurator;


/**
 * Launcher for the AppKit Dev Tool
 * 
 * @author lippisch
 */
public class Launch {

	/**
	 * @param cmdLineArgs
	 */
	public static void main(String[] args) {

		PropertyConfigurator.configure("log4j.properties");
		
		try {
			CliController jcpCli = new CliController();
			jcpCli.printHeader();
			jcpCli.run(args);
			
		} catch (Exception e) {
			System.err.println("An error occured during the execution of AKD:");
			e.printStackTrace();
			System.err.println();
			
		}
		
	}

}
