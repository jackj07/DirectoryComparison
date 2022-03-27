/*
 * Author: Jack Jarvis
 * Creation date: 17th April 2017
 */

package com.DirComparison.FileTools;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

public class BasicFunctions{
	//So all System.out outputs go to specified file
	private static PrintStream out;
	
	public static void setOutputStreamToFile(String outputFileLocation) throws FileNotFoundException{
		Path outputFileLocationParent = Paths.get(outputFileLocation).getParent();
		File outputFileParentLocation = new File(outputFileLocationParent.toString());
		if(!outputFileParentLocation.exists())outputFileParentLocation.mkdirs();
		out = new PrintStream(new FileOutputStream(outputFileLocation));
		System.setOut(out);
	}
	
	/*
	public static void closeOutputStream(){
		if(System.out != null){
			System.out.flush();
			System.out.close();
		}
	}
	*/
	
	//So all System.out outputs go to command line
	public static void setOutputStreamToConsole(){
		if(out != null){
			out.flush();
			out.close();
		}
		System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
	}
	
	public static String getFileNameExtension(String fileName){
		String extension = "";

		int i = fileName.lastIndexOf('.');
		if (i > 0) {
		    extension = fileName.substring(i+1);
		}
		
		return extension;
	}
	
	public static void copyFile(File from, File to) throws IOException{
		InputStream inStream = null;
    	OutputStream outStream = null;
    
	    inStream = new FileInputStream(from);
	    outStream = new FileOutputStream(to); // for override file content
	 
	    byte[] buffer = new byte[1024];
 
	    int length;
	    while ((length = inStream.read(buffer)) > 0){
	    	outStream.write(buffer, 0, length);
	    }
 
	    if (inStream != null)inStream.close();
	    if (outStream != null)outStream.close();
	}
	
	//Java uses pass by value, so you cannot modify systemInformation here, you will need to copy the temp file contents to the original manually
	public static String extractFirstLineOfFileSystem(File systemInformation, File tempFile) throws FileNotFoundException, IOException{
		BufferedReader reader = new BufferedReader(new FileReader(systemInformation));
		BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
		
		String firstLine = reader.readLine();
		
		String nextLine;
		while((nextLine = reader.readLine()) != null){
			writer.write(nextLine + System.getProperty("line.separator"));
		}
		
		writer.close();
		reader.close();
		
		return firstLine;
	}
	
	//function to help in copying file contents to original (after above function)
	public static void replaceFile(File original, File contentsToReplace) throws IOException{
		copyFile(contentsToReplace, original);
		contentsToReplace.delete();
	}
	
	public static Boolean fileHasContent(File file) throws IOException{
		BufferedReader reader = new BufferedReader(new FileReader(file));
		Boolean to_return = (reader.readLine()!=null);
		reader.close();
		return to_return;
	}
	
	public static Boolean fileInfoExistsInFileSystemInformation(String fileInformation, File systemInformation)throws FileNotFoundException, IOException{
		BufferedReader reader = new BufferedReader(new FileReader(systemInformation));
		
		String lineInSystemInformation;
		while((lineInSystemInformation = reader.readLine()) != null){
			if(lineInSystemInformation.trim().equals(fileInformation)){
				reader.close();
				return true;
			}
		}
		
		reader.close();
		return false;
	}
}
