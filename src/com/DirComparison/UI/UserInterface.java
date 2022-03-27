/*
 * Author: Jack Jarvis
 * Creation date: 17th April 2017
 */

package com.DirComparison.UI;

import java.io.File;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import com.DirComparison.Comparer.DirectoryComparer;
import com.DirComparison.FileTools.BasicFunctions;
import com.DirComparison.Modeler.SystemModeler;
import com.DirComparison.XML.ConfigFileReader;

public class UserInterface {
	
	private static Scanner scanner;
	private static String[] passedArgs;
	
	/*
	 * Main entry point into application
	 */
	public static void main(String[] args){
		//Remember to change the output stream so the user will see output and you wont be writing to a file.
		System.out.println(""
				+ "*********************************************\n"
				+ "********* Directory Comparison Tool *********\n"
				+ "*************** Version 1.0 *****************\n"
				+ "*************** Jack Jarvis *****************\n"
				+ "*********************************************\n");
		if(args.length>0)passedArgs=args;
		mainMenu();
	}
	
	private static void mainMenu(){
		if(passedArgs != null){
			switch(passedArgs[0]){
				case "-m" :{
					model();
					break;
				}
				
				case "-c" :{
					if(passedArgs.length == 3){
						compareUsingArgs(passedArgs[1], passedArgs[2]);
					}else{
						System.out.println("-c usage: -c dd-MM-yyyy dd-MM-yyyy");
					}
					break;
				}
				
				default:{
					System.out.println("Invalid input.");
				}
			}
		}else{
			System.out.println();
			System.out.println("For help enter: -h");
			
			scanner = new Scanner(System.in);
			String input = scanner.nextLine();
			
			switch(input){
				case "-h":{
					help();
					break;
				}
				
				case "-m" :{
					model();
					break;
				}
				
				case "-c" :{
					compare();
					break;
				}
				
				case "-exit":{
					exit();
				}
				
				default:{
					System.out.println("Invalid input.");
					break;
				}
			}
			mainMenu();
		}
	}
	
	private static void help(){
		System.out.println("Model the default directory specified in the config file: -m");
		System.out.println("Compare two models of a specific directory to see what has changed: -c");
		System.out.println("To exit: -exit");
	}
	
	private static void exit(){
		//BasicFunctions.closeOutputStream();
		scanner.close();
		System.exit(0);
	}
	
	private static void model(){
		try{
			if(!ConfigFileReader.configFileExists())throw new Exception("Cannot locate the configuration file "
					+ "in the directory this tool is running from");
			
			String defaultDirectoryToModel = ConfigFileReader.getDefaultDirectoryToModel();
			String defaultModelFile = ConfigFileReader.getDefaultModelFile();
			String actualModelFile = defaultModelFile.replace("#DATE#", getTodaysDateString());
			if(actualModelFile.equals(defaultModelFile))throw new Exception("Unable to find #DATE# in: "+defaultModelFile);
			
			System.out.println(
					"Modelling: "+defaultDirectoryToModel+"\n"+
					"Sending output to: "+actualModelFile);
			
			BasicFunctions.setOutputStreamToFile(actualModelFile);
			SystemModeler.modelFileSystem(new File(defaultDirectoryToModel), ConfigFileReader.getFilePathExceptions());
			BasicFunctions.setOutputStreamToConsole();
			System.out.println("Complete");
		}catch(Exception e){
			BasicFunctions.setOutputStreamToConsole();
			System.out.println("The following error occured:");
			e.printStackTrace();
		}
	}
	
	private static void compare(){
		try{
			if(!ConfigFileReader.configFileExists())throw new Exception("Cannot locate the configuration file "
					+ "in the directory this tool is running from");
			
			String defaultModelFile = ConfigFileReader.getDefaultModelFile();
			
			if(ConfigFileReader.getDefaultCompareWithToday() == true){
				File directoryModelTakenToday = new File(defaultModelFile.replace("#DATE#", getTodaysDateString()));
				if(directoryModelTakenToday.equals(defaultModelFile))throw new Exception("Unable to find #DATE# in: "+defaultModelFile);
				
				System.out.println("Checking a directory model of today exists...");
				if(directoryModelTakenToday.isFile()){
					System.out.println("Enter date of the older directory model (dd-MM-yyyy)");
					String previousDate = scanner.nextLine();
					
					File directoryModelToCompareAgainst = new File(defaultModelFile.replace("#DATE#", previousDate));
					if(directoryModelToCompareAgainst.equals(defaultModelFile))throw new Exception("Unable to find #DATE# in: "+defaultModelFile);
					
					if(directoryModelToCompareAgainst.isFile()){
						System.out.println("Commencing comparison");

						String resultsDirectory = ConfigFileReader.getDefaultResultsDirectory();
						String resultsDirectory1 = resultsDirectory.replace("#DATE_START#", previousDate);
						if(resultsDirectory1.equals(resultsDirectory))throw new Exception("Unable to find #DATE_START# in: "+resultsDirectory);
						String resultsDirectory2 = resultsDirectory1.replace("#DATE_END#", getTodaysDateString());
						if(resultsDirectory2.equals(resultsDirectory1))throw new Exception("Unable to find #DATE_END# in: "+resultsDirectory1);
						
						DirectoryComparer.compareDirectories(directoryModelToCompareAgainst, directoryModelTakenToday, Paths.get(resultsDirectory2));
						System.out.println("Comparison complete");
					}else{
						System.out.println("Specified directory model does not exist");
					}
				}else{
					System.out.println("Directory model of today does not exist");
				}
			}else{
				System.out.println("Enter date of the older directory model (dd-MM-yyyy)");
				String firstDate = scanner.nextLine();
				File firstDirectoryModel = new File(defaultModelFile.replace("#DATE#", firstDate));
				if(firstDirectoryModel.equals(defaultModelFile))throw new Exception("Unable to find #DATE# in: "+defaultModelFile);
				if(firstDirectoryModel.exists()){
					System.out.println("Enter date of the newer directory model (dd-MM-yyyy)");
					String secondDate = scanner.nextLine();
					File secondDirectoryModel = new File(defaultModelFile.replace("#DATE#", secondDate));
					if(secondDirectoryModel.equals(defaultModelFile))throw new Exception("Unable to find #DATE# in: "+defaultModelFile);
					if(secondDirectoryModel.exists()){
						System.out.println("Commencing comparison");
						
						String resultsDirectory = ConfigFileReader.getDefaultResultsDirectory();
						String resultsDirectory1 = resultsDirectory.replace("#DATE_START#", firstDate);
						if(resultsDirectory1.equals(resultsDirectory))throw new Exception("Unable to find #DATE_START# in: "+resultsDirectory);
						String resultsDirectory2 = resultsDirectory1.replace("#DATE_END#", secondDate);
						if(resultsDirectory2.equals(resultsDirectory1))throw new Exception("Unable to find #DATE_END# in: "+resultsDirectory1);
						
						DirectoryComparer.compareDirectories(firstDirectoryModel, secondDirectoryModel, Paths.get(resultsDirectory2));
						System.out.println("Comparison complete");
					}else{
						System.out.println("Second directory model does not exist");
					}
				}else{
					System.out.println("First directory model does not exist");
				}
			}
			
			BasicFunctions.setOutputStreamToConsole();
		}catch(Exception e){
			BasicFunctions.setOutputStreamToConsole();
			System.out.println("The following error occured:");
			e.printStackTrace();
		}
	}
	
	private static void compareUsingArgs(String date1, String date2){
		try{
			String defaultModelFile = ConfigFileReader.getDefaultModelFile();
			File firstDirectoryModel = new File(defaultModelFile.replace("#DATE#", date1));
			if(firstDirectoryModel.equals(defaultModelFile))throw new Exception("Unable to find #DATE# in: "+defaultModelFile);
			if(firstDirectoryModel.exists()){
				File secondDirectoryModel = new File(defaultModelFile.replace("#DATE#", date2));
				if(secondDirectoryModel.equals(defaultModelFile))throw new Exception("Unable to find #DATE# in: "+defaultModelFile);
				if(secondDirectoryModel.exists()){
					System.out.println("Commencing comparison");
					
					String resultsDirectory = ConfigFileReader.getDefaultResultsDirectory();
					String resultsDirectory1 = resultsDirectory.replace("#DATE_START#", date1);
					if(resultsDirectory1.equals(resultsDirectory))throw new Exception("Unable to find #DATE_START# in: "+resultsDirectory);
					String resultsDirectory2 = resultsDirectory1.replace("#DATE_END#", date2);
					if(resultsDirectory2.equals(resultsDirectory1))throw new Exception("Unable to find #DATE_END# in: "+resultsDirectory1);
					
					DirectoryComparer.compareDirectories(firstDirectoryModel, secondDirectoryModel, Paths.get(resultsDirectory2));
					System.out.println("Comparison complete");
				}else{
					System.out.println("Second directory model does not exist");
				}
			}else{
				System.out.println("First directory model does not exist");
			}
		}catch(Exception e){
			BasicFunctions.setOutputStreamToConsole();
			System.out.println("The following error occured:");
			e.printStackTrace();
		}
	}
	
	private static String getTodaysDateString(){
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		Date date = new Date();
		
		return dateFormat.format(date);
	}
}
