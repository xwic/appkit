/*
 * Copyright (c) NetApp Inc. - All Rights Reserved
 * 
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 * Proprietary and confidential.
 * 
 * com.netapp.jcp.CliController 
 */

package de.xwic.appkit.dev.cli;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import de.xwic.appkit.dev.engine.BuilderEngine;

/**
 * Starts the command line version of AppKit Dev Tools. 
 * 
 * @author lippisch
 */
public class CliController {

	public final static String CMD_HELP = "help";
	public final static String CMD_BUILD = "build";
	
	
	private final String[] COMMANDS = {
			CMD_HELP,
			CMD_BUILD
	};
	private final String[] COMMAND_HELP = {
			"Display all commands and options.",
			"Generate java and config files based upon the model definition"
	};
	
	private BuilderEngine engine;
	
	/**
	 * Constructor.
	 */
	public CliController() {
		engine = new BuilderEngine();
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
					
					// launch the engine
					engine.start();
					
					if (CMD_BUILD.equals(command)) {
						prepareAndRunBuild(cmd);
					} else {
						System.err.println("Unknown command: " + command);
					}
					
				} catch (ParseException pe) {
					System.out.println("Error parsing arguments: " + pe);
				} finally {
					engine.shutdown();
				}
			}
		}
		
	}

	/**
	 * @param cmd
	 */
	private void prepareAndRunBuild(CommandLine cmd) {
		
		String[] args = cmd.getArgs();
		
		if (args.length < 2) {
			System.err.println("Missing the location. Use: jcp scan location [options]");
			System.err.println("                      Example:  jcp scan smb://server/share/folder -report");
			return;
		}
		
		String location = args[1];
		
	}

	/**
	 * Define the command line options available.
	 * @return
	 */
	public Options buildOptions(String command) {
		
		Options opt = new Options();
		
		if (command.equals(CMD_BUILD)) {
			opt.addOption("r", "report", false, "Display a summary report after the scan.");
			opt.addOption("f", "filter", true, "Only count/find files matching the given filter");
			opt.addOption("s", "save", true, "Save the file list in the specified file");
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
		
		p("AppKit Dev Tool - Version " + engine.getVersion());
		p("");
		
	}


	/**
	 * 
	 */
	private void printBasicHelp() {
		p("Usage: jcp command <options>");
		p("");
		p("  Enter jcp help for more details.");
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
			p("Use jcp help <command> to show command specific options.");
		
		} else {
			String helpCommand = args[1];
			
			Options options = buildOptions(helpCommand);
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp( "jcp " + helpCommand, options );
		}
	}

}
