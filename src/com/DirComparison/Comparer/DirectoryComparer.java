/*
 * Author: Jack Jarvis
 * Creation date: 17th April 2017
 */

package com.DirComparison.Comparer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.nio.file.Path;

import com.DirComparison.FileTools.*;

public class DirectoryComparer {
	
	/*
	 * Takes two files of recorded directory structures and compares them
	 */
	public static void compareDirectories(File oldSystemInformationLocation, File newSystemInformationLocation, Path resultsDirectory) throws NullFileException, NotADirectoryException, Exception{
		//Get System information files as paths
		File oldSystemInformation = new File(oldSystemInformationLocation.toString());
		File newSystemInformation = new File(newSystemInformationLocation.toString());
		
		//make the results directory if it doesn't exist
		File resultsDirectoryAsFile = new File(resultsDirectory.toString());
		if(!resultsDirectoryAsFile.exists())resultsDirectoryAsFile.mkdirs();
		
		//throw corresponding exceptions if they do not exist
		if(!(oldSystemInformation.exists())) throw new NullFileException("The file specified at: "+oldSystemInformationLocation+" does not exist");
		if(!(newSystemInformation.exists())) throw new NullFileException("The file specified at: "+newSystemInformationLocation+" does not exist");
		if(!BasicFunctions.getFileNameExtension(resultsDirectory.toString()).equals(""))throw new NotADirectoryException("The results directory is not a directory");
		
		//make a copy of the files which you can manipulate without changing file information records
		File oldSystemInformation_COPY = new File(oldSystemInformationLocation.getParent().toString()+"\\OLDSYSINFOCOPY."+BasicFunctions.getFileNameExtension(oldSystemInformationLocation.toString()));
		BasicFunctions.copyFile(oldSystemInformation, oldSystemInformation_COPY);
		File newSystemInformation_COPY = new File(newSystemInformationLocation.getParent().toString()+"\\NEWSYSINFOCOPY."+BasicFunctions.getFileNameExtension(newSystemInformationLocation.toString()));
		BasicFunctions.copyFile(newSystemInformation, newSystemInformation_COPY);
		
		BasicFunctions.setOutputStreamToFile(resultsDirectory.toString()+"\\MovedOrDeletedFiles."+BasicFunctions.getFileNameExtension(oldSystemInformationLocation.toString()));
		BufferedReader reader_old = new BufferedReader(new FileReader(oldSystemInformation_COPY));
		BufferedWriter writer_mvdel = new BufferedWriter(new OutputStreamWriter(System.out));
		String line_old;
		while((line_old = reader_old.readLine())!=null){
			if (!BasicFunctions.fileInfoExistsInFileSystemInformation(line_old, newSystemInformation)){
				writer_mvdel.write(line_old);
				writer_mvdel.write(System.getProperty( "line.separator" ));
			}
		}
		reader_old.close();
		writer_mvdel.close();
		
		BasicFunctions.setOutputStreamToFile(resultsDirectory.toString()+"\\MovedOrNewFiles."+BasicFunctions.getFileNameExtension(oldSystemInformationLocation.toString()));
		BufferedReader reader_new = new BufferedReader(new FileReader(newSystemInformation_COPY));
		BufferedWriter writer_mvnew = new BufferedWriter(new OutputStreamWriter(System.out));
		String line_new;
		while((line_new = reader_new.readLine())!=null){
			if (!BasicFunctions.fileInfoExistsInFileSystemInformation(line_new, oldSystemInformation)){
				writer_mvnew.write(line_new);
				writer_mvnew.write(System.getProperty( "line.separator" ));
			}
		}
		reader_new.close();
		writer_mvnew.close();
		
		newSystemInformation_COPY.delete();
		oldSystemInformation_COPY.delete();
		BasicFunctions.setOutputStreamToConsole();
	}
}
