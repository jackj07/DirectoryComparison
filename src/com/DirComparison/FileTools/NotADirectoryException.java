/*
 * Author: Jack Jarvis
 * Creation date: 17th April 2017
 */

package com.DirComparison.FileTools;

@SuppressWarnings("serial")
public class NotADirectoryException extends Exception{
	public NotADirectoryException(){
		super();
	}
	
	public NotADirectoryException(String errorMessage){
		super(errorMessage);
	}
}
