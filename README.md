# DirectoryComparison
Java tool to track changes in local directories over time. 

Outputs a full directory listing first, which can then be used as an input to compare with a previous listing - showing moved / deleted files and moved / new files.

# Requirements:
- Java JRE (Tested with version 7)

# Config File:
< defaultDirectoryToModel > The directory you want to model

< defaultModelFile > The name and location of the file that will be written to when you model your file system.

< defaultResultsDirectory > When you compare two directory models, this will be the name of the directory that contains the results

< defaultCompareWithToday > If this is set to y, the tool will automatically run a comparison with a model taken from today, if it is set to n you will have to input the name of the model you want to compare with

< filePathExceptions > List all of the sub directories under <defaultDirectoryToModel> that you do not want to be included in the output

You MUST edit the config.xml file before running the tool for the first time. The one available contains example values.

# Inputs:
- -h toggles help
- -m creates a model of the specified directory and sends the output to the specified file
- -c commences a comparison between two directory models*
- -exit closes the application

# References:
- Xerces parser, downloadable from: http://xerces.apache.org/

# Author:
Jack Jarvis
# Creation Date:
17th April 2017
