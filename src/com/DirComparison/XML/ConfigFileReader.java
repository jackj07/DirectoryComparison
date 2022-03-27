/*
 * Author: Jack Jarvis
 * Creation date: 17th April 2017
 */

package com.DirComparison.XML;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.xml.sax.SAXException;

public class ConfigFileReader {
	private static String configFileLocation = "config.xml";
	
	public static Boolean configFileExists(){
		File configFile = new File(configFileLocation);
		return configFile.isFile();
	}
	
	public static String getDefaultDirectoryToModel() throws IOException, SAXException{
		return DCTSAXParser.parse("defaultDirectoryToModel", configFileLocation);
	}
	
	public static String getDefaultResultsDirectory() throws IOException, SAXException{
		return DCTSAXParser.parse("defaultResultsDirectory", configFileLocation);
	}
	
	public static String getDefaultModelFile() throws IOException, SAXException{
		return DCTSAXParser.parse("defaultModelFile", configFileLocation);
	}
	
	public static Boolean getDefaultCompareWithToday() throws IOException, SAXException{
		String to_return = DCTSAXParser.parse("defaultCompareWithToday", configFileLocation);
		return to_return.equals("y");
	}
	
	public static List<String> getFilePathExceptions() throws IOException, SAXException{
		return DCTSAXParser.parseForList("filePathException", configFileLocation);
	}
}
