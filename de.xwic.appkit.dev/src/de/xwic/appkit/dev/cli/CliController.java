/*
 * Copyright (c) NetApp Inc. - All Rights Reserved
 * 
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 * 
 * com.netapp.jcp.CliController 
 */

package de.xwic.appkit.dev.cli;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.xwic.appkit.dev.engine.AppContext;
import de.xwic.appkit.dev.engine.BuilderEngine;
import de.xwic.appkit.dev.engine.ConfigurationException;
import de.xwic.appkit.dev.engine.model.DataModel;
import de.xwic.appkit.dev.engine.model.DataModelFactory;

/**
 * Starts the command line version of AppKit Dev Tools. 
 * 
 * @author lippisch
 */
public class CliController {

	public final static String CMD_HELP = "help";
	public final static String CMD_BUILD = "build";

	private final Log log = LogFactory.getLog(getClass());
	
	private final String[] COMMANDS = {
			CMD_HELP,
			CMD_BUILD
	};
	private final String[] COMMAND_HELP = {
			"Display all commands and options.",
			"Generate java and config files based upon the model definition"
	};
	
	/**
	 * Constructor.
	 */
	public CliController() {

	}
	

	/**
	 * @param args
	 * @param cmd
	 */
	public void run(String[] args) {
		
		
		if (args.length == 0) {
			printBasicHelp();
		} else {
			String command = args[0];
			if (CMD_HELP.equals(command)) {
				printHelp(args);
			} else {
				try {
					CommandLineParser parser = new DefaultParser();
					CommandLine cmd = parser.parse(buildOptions(command), args);
					
					// check engine attributes
					
					if (CMD_BUILD.equals(command)) {
						prepareAndRunBuild(cmd);
					} else {
						System.err.println("Unknown command: " + command);
					}
					
				} catch (ParseException pe) {
					System.out.println("Error parsing arguments: " + pe);
				} catch (ConfigurationException ce) {
					System.err.println("There seems to be a problem with the configuration:");
					ce.printStackTrace();
				}
			}
		}
		
	}

	/**
	 * @param cmd
	 * @throws ConfigurationException 
	 */
	private void prepareAndRunBuild(CommandLine cmd) throws ConfigurationException {
		
		String[] args = cmd.getArgs();
		
		if (args.length < 2) {
			System.err.println("Missing the model definition file. Use: akd build filename");
			System.err.println("Example: akd build admin.model.xml");
			return;
		}
		
		
		AppContext context = initContext();
		
		File modelFile = new File(args[1]);
		if (!modelFile.exists()) {
			System.err.println("The specified model file '" + args[1] + "' does not exist.");
			return;
		}

		// load model
		DataModel model = DataModelFactory.createModel(modelFile);
		
		if (model.getDomainId() == null) {
			log.error("domain not specified in model file.");
			return;
		}

		if (model.getProjectName() == null) {
			log.error("ProjectName not specified in model file.");
			return;
		}
		
		if (model.getPackageName() == null) {
			log.error("Package not specified in model file.");
			return;
		}

		BuilderEngine engine = new BuilderEngine(context);
		engine.start();
		
		try {
			engine.generateFiles(model, 
					cmd.hasOption("all") || cmd.hasOption("dao"),
					cmd.hasOption("all") || cmd.hasOption("listsetup"),
					cmd.hasOption("all") || cmd.hasOption("editors"));
			
			if (cmd.hasOption("all") || cmd.hasOption("bundle")) {
				engine.updateBundles(model, cmd.hasOption("ob"));
			}
			
		} catch (Exception e) {
			log.error(e);
		} finally {
			engine.shutdown();
		}
		
		log.info("Build completed - " + context.getFilesCreated() + " files created.");
		
		
	}

	/**
	 * Initialize the context.
	 * @return
	 * @throws ConfigurationException
	 */
	private AppContext initContext() throws ConfigurationException {

		// load configuration file
		File configFile = new File("akdev.properties");
		if (!configFile.exists()) {
			throw new ConfigurationException("The configuration file akdev.properties is not found.");
		}
		
		try {
			Properties prop = new Properties();
			FileInputStream fin = new FileInputStream(configFile);
			try {
				prop.load(fin);
			} finally {
				fin.close();
			}
			AppContext ctx = new AppContext(prop);
			return ctx;
		} catch (IOException ie) {
			throw new ConfigurationException("Error reading configuration", ie);
		}
		
	}
	
	/**
	 * Define the command line options available.
	 * @return
	 */
	public Options buildOptions(String command) {
		
		Options opt = new Options();
		
		if (command.equals(CMD_BUILD)) {
			opt.addOption("d", "dao", false, "Generate DAO files including entity and hbm mapping.");
			opt.addOption("l", "listsetup", false, "Generate listsetup");
			opt.addOption("e", "editors", false, "Generate editors");
			opt.addOption("a", "all", false, "Generate all files");
			opt.addOption("b", "bundle", false, "Generate bundle file - by default it keeps old values");
			opt.addOption("ob", "overridebundle", false, "Override all bundle values");
		}
		
		return opt;
	}

	/**
	 * Print a string to the console.
	 * @param s
	 */
	private void p(String s) {
		System.out.println(s);
	}
	
	/**
	 * Print a formatted string.
	 * @param indent
	 * @param s
	 * @param newLine
	 * @param fillToSpaces
	 */
	private void p(int indent, String s, boolean newLine, int fillToSpaces) {
		if (indent != 0 || fillToSpaces == 0) {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < indent; i++) {
				sb.append(' ');
			}
			sb.append(s);
			if (sb.length() < fillToSpaces) {
				for (int i = sb.length(); i < fillToSpaces; i++) {
					sb.append(' ');
				}
			}
			s = sb.toString();
		}
		if (newLine) {
			System.out.println(s);
		} else {
			System.out.print(s);
		}
	}
	
	/**
	 * Print standard copyright headers to System.out.
	 */
	public void printHeader() {
		
		p("AppKit Dev Tool");
		p("===============");
		p("");
		
	}


	/**
	 * 
	 */
	private void printBasicHelp() {
		p("Usage: akd command <options>");
		p("");
		p("  Enter akd help for more details.");
	}

	/**
	 * @param args 
	 * 
	 */
	private void printHelp(String[] args) {
		
		if (args.length < 2) {
			for (int i = 0 ; i < COMMANDS.length; i++) {
				String cmd = COMMANDS[i];
				String help = COMMAND_HELP[i];
				p(2, cmd, false, 20);
				p(help);
			}
			
			p("");
			p("Use akd help <command> to show command specific options.");
		
		} else {
			String helpCommand = args[1];
			
			Options options = buildOptions(helpCommand);
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp( "akd " + helpCommand, options );
		}
	}

}
