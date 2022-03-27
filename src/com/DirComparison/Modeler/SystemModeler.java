/*
 * Author: Jack Jarvis
 * Creation date: 17th April 2017
 */

package com.DirComparison.Modeler;

import java.io.File;
import java.util.List;

import com.DirComparison.FileTools.*;

public class SystemModeler{
	
	/*
	 * When given a file, its file path and sub files if directory are sent to System.out
	 */
	public static void modelFileSystem(File file, List<String> filePathExceptions) throws Exception{
		if((filePathExceptions != null) && (filePathExceptions.size() >0))for(String filePathException : filePathExceptions)if(file.getAbsolutePath().equals(filePathException))return;
		if(!file.exists()) throw new NullFileException("The file you are modelling on is null");
		System.out.println(file.getAbsolutePath());
		if(file.isDirectory()){
			File[] files = file.listFiles();
			if((files != null)&&(files.length > 0))
				for(File childFile : files)
						modelFileSystem(childFile, filePathExceptions);
		}
	}
}
