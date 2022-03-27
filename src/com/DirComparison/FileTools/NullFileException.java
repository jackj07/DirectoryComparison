/*
 * Author: Jack Jarvis
 * Creation date: 17th April 2017
 */

package com.DirComparison.FileTools;

@SuppressWarnings("serial")
public class NullFileException extends Exception{

	public NullFileException(){
		super();
	}
	
	public NullFileException(String errorMessage){
		super(errorMessage);
	}
}
