/*
 * Author: Jack Jarvis
 * Creation date: 17th April 2017
 */

package com.DirComparison.XML;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/*
 * Referenced library:
 * Xerces Parser
 * http://xerces.apache.org/
 * 
 * All credit to apache.org
 */
import org.apache.xerces.parsers.SAXParser;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;

public class DCTSAXParser extends DefaultHandler{
	
	private static String searchResult;
	private static List<String> searchResultList;
	private static String configItem_;
	private static Boolean foundItem;
	private static Boolean lookingForMultiple;
	
	public static String parse(String configItem, String configFileLocation) throws IOException, SAXException{
		configItem_ = configItem;
		foundItem = false;
		lookingForMultiple=false;
		DCTSAXParser handler = new DCTSAXParser();
		SAXParser parser = new SAXParser();
		parser.setContentHandler(handler);
		parser.setErrorHandler(handler);
		parser.parse(configFileLocation);
		return searchResult;
	}
	
	public static List<String> parseForList(String configItem, String configFileLocation) throws IOException, SAXException{
		configItem_ = configItem;
		foundItem = false;
		lookingForMultiple=true;
		searchResultList = new LinkedList<String>();
		DCTSAXParser handler = new DCTSAXParser();
		SAXParser parser = new SAXParser();
		parser.setContentHandler(handler);
		parser.setErrorHandler(handler);
		parser.parse(configFileLocation);
		return searchResultList;
	}
	
	public void startElement(String uri, String localName, String rawName, Attributes attributes){
		if(rawName.equals(configItem_)) foundItem = true;
	}
	
	public void endElement(String uri, String localName, String rawName){
		if(rawName.equals(configItem_)) foundItem = false;
	}
	
	public void characters(char characters[], int start, int length){
		String chData = (new String(characters, start, length)).trim();
		if(lookingForMultiple){
			if(chData.indexOf("\n")<0 && chData.length() >0 && foundItem)searchResultList.add(chData);
		}else{
			if(chData.indexOf("\n")<0 && chData.length() >0 && foundItem)searchResult = chData;
		}
	}
}