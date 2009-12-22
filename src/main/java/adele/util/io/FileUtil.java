/* 
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
/*-------------------------------------------------\
| Classe      : FileUtil                           |
| Type        : Librairie I/O                      |
| Herite      :                                    |
| Description : Classe regroupant differentes      |
| methodes pour la manipulation et l'extraction    |
| d'informations sur les fichiers.                 |
| Version     : 1.0                                |
|--------------------------------------------------|
| Teste sous  : JDK 1.4                            |
|--------------------------------------------------|
| Auteur      : Remy Sanlaville                    |
| Date        : 18/10/2002                         |
|--------------------------------------------------|
| Copyright   : LSR-Adele                          |
\-------------------------------------------------*/
/*------------------------------\
| Definition du nom du package  |
\------------------------------*/
package adele.util.io;

/*------------------------------\
| Liste des packages a importer |
\------------------------------*/
//*** Packages du JDK
//** Packages I/O
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URL;

//** Packages util
//import java.util.StringTokenizer;

/*----------------------------------\
| Declaration de la classe FileUtil |
\----------------------------------*/
/**
 *   Own Library for the manipulation of the file
 *
 * @author    Remy Sanlaville
 * @since     23 august 2002
 */
public class FileUtil
{
  // Constante(s) globale(s)

  // Variable(s) globale(s)

  /*-------------------------------------------\
  |      Constructeur(s) de la classe          |
  \-------------------------------------------*/
  /*-------------------------------------------\
  |           Interface de la classe           |
  \-------------------------------------------*/
  /**
   * Getter: Gets the extension in lower case of the given file f.
   * For example:
   * FileUtil.getExtension(new File("classes\control\application.class"))
   * returns "class"
   *
   * @param  f  the file to get the extension
   * @return    The extension value (in lower case) of the given file.
   * Return null if the file has no extension or the parameter is not correct.
   */
  public static String getExtension(File f)
  {
    if (f != null)
      return getExtension(f.getName());
    return null;
  }


  /**
   * Getter: Gets the extension in lower case of the given string fileName.
   * For example:
   * FileUtil.getExtension("classes\control\application.class")
   * returns "class"
   *
   * @param  fileName  the string to get the extension
   * @return           The extension value (in lower case) of the given string.
   * Return null if the string has no extension or the parameter is null.
   */
  public static String getExtension(String fileName)
  {
    if (fileName != null)
      {
        String name          = fileName;

        // !!! Warning !!! Ne pas enlever
        // Certaines applications utilisent cette fonction avec des repertoires.
        // Il faut donc gerer le fait qu'on peut nous passer un repertoire
        // qui contenient un '.'
        // Exemples : MyAppli/Version1.0/classes/
        //            ./Version1.0/classes/
        int    lastSeparator = name.lastIndexOf(File.separatorChar);
        if (lastSeparator > 0 && lastSeparator < name.length() - 1)
          name = name.substring(lastSeparator + 1);
        // !!! Warning !!! Ne pas enlever

        int    lastSuffix    = name.lastIndexOf('.');
        if (lastSuffix > 0 && lastSuffix < name.length() - 1)
          return name.substring(lastSuffix + 1).toLowerCase();
      }
    return null;
  }


  /**
   * Getter: Gets the short name of the given fileName.
   * For example:
   * FileUtil.getShortName("P:\Appli1\classes\control\application.class")
   * returns "application"
   *
   * @param  fileName  the original name
   * @return           The short name value of the given name.
   * Return null if the parameter fileName is null.
   * Return itself it fileName has no extension
   */
  public static String getShortName(String fileName)
  {
    if (fileName != null)
      {
        // Recuperer le nom du fichier (exemple "application.class")
        fileName = getName(fileName);

        // Recuperer le nom court du fichier (exemple "application")
        int lastIndexExtension = fileName.lastIndexOf(".");
        if (lastIndexExtension == -1)
          return fileName;
        else
          return fileName.substring(0, lastIndexExtension);
      }
    return null;
  }


  /**
   * Getter: Gets the name of the given full name.
   * For example:
   * FileUtil.getName("P:\Appli1\classes\control\application.class")
   * returns "application.class"
   *
   * @param  fullName  the full name to get the name
   * @return           the name of the given full name.
   */
  public static String getName(String fullName)
  {
    if (fullName != null)
      {
        int lastIndexExtension = fullName.lastIndexOf(File.separator);
        if (lastIndexExtension == -1)
          return fullName;
        else
          return fullName.substring(lastIndexExtension + 1);
      }
    return null;
  }


  /**
   * Getter: Gets the path of the given full name.
   * For example:
   * FileUtil.getPath("P:\Appli1\classes\control\application.class")
   * returns "P:\Appli1\classes\control"
   *
   * @param  fullName  the full name to get the path
   * @return           the path of the given full name.
   */
  public static String getPath(String fullName)
  {
    if (fullName != null)
      {
        int lastIndexExtension = fullName.lastIndexOf(File.separator);
        if (lastIndexExtension == -1)
          return fullName;
        else
          return fullName.substring(0, lastIndexExtension);
      }
    return null;
  }


  /**
   * Getter: Gets the full path of the given path, and file names
   * For example:
   * FileUtil.getFullPath("P:\\Appli1\\classes\\control\\interface", "IApplication.class")
   * returns "P:\\Appli1\\classes\\control\\interface\\IApplication.class"
   *
   * @param  pathName  the path name to get the full path
   * @param  fileName  the file name to get the full path
   * @return           the full path of the given parameters.
   */
  public static String getFullPath(String pathName, String fileName)
  {
    String fullPath = null;

    if (pathName != null)
      {
        // Test if pathName ends with "/" or "\"
        if (pathName.endsWith("/") || pathName.endsWith("\\"))
          pathName = pathName.substring(0, pathName.length() - 1);
      }

    if (fileName != null)
      {
        // Test if fileName starts with a "/" or a "\"
        if (fileName.startsWith("/") || fileName.startsWith("\\"))
          fileName = fileName.substring(1, fileName.length());
      }

    // construct the full path
    fullPath = pathName + File.separator + fileName;

    return fullPath;
  }


  /**
   * Getter: Gets the full path of the given path, package and file names
   * For example:
   * FileUtil.getFullPath("P:\\Appli1\\classes", "control.interface" , "IApplication.class")
   * returns "P:\\Appli1\\classes\\control\\interface\\IApplication.class"
   *
   * @param  pathName     the path name to get the full path
   * @param  packageName  the package name to get the full path
   * @param  fileName     the file name to get the full path
   * @return              the full path of the given parameters.
   */
  public static String getFullPath(String pathName, String packageName, String fileName)
  {
    String fullPath = null;

    if (pathName != null)
      {
        // Test if pathName ends with "/" or "\"
        if (pathName.endsWith("/") || pathName.endsWith("\\"))
          pathName = pathName.substring(0, pathName.length() - 1);
      }

    if (packageName != null)
      {
        // Test if packageName ends with a "."
        if (packageName.endsWith("."))
          packageName = packageName.substring(0, packageName.length() - 1);

        // Replace character '.' by File.separatorChar
        packageName = packageName.replace('.', File.separatorChar);
      }

    if (fileName != null)
      {
        // Test if fileName starts with a "/" or a "\"
        if (fileName.startsWith("/") || fileName.startsWith("\\"))
          fileName = fileName.substring(1, fileName.length());
      }

    // construct the full path
    fullPath = pathName + File.separator + packageName + File.separator + fileName;

    return fullPath;
  }


  /**
   *  Util: Check if the given directory is empty
   *
   * @param  directory  the directory to check
   * @return            true if the given directory is empty, false otherwise
   */
  public static boolean isEmpty(File directory)
  {
    boolean res = false;

    // Tester si c'est un repertoire
    if (directory.isDirectory())
      res = directory.listFiles().length == 0;

    return res;
  }


  /**
   *  Util: Create the given file and the potential directory of the file if
   * they do not exists.
   *
   * @param  newFile          the file to create
   * @return                  true if the file is successfully created; false otherwise
   * @exception  IOException  Description of Exception
   */
  public static boolean createNewFile(File newFile) throws IOException
  {
    boolean res = false;

    if (!newFile.exists())
      {
        newFile.getParentFile().mkdirs();
        res = newFile.createNewFile();
      }

    return res;
  }


  /**
   *  Util: Create the given file and the potential directory of the file if
   * they do not exists.
   *
   * @param  newFileName      the file name to create
   * @return                  true if the file is successfully created; false otherwise
   * @exception  IOException  Description of Exception
   */
  public static boolean createNewFile(String newFileName) throws IOException
  {
    return createNewFile(new File(newFileName));
  }


  /**
     * Util: Deletes recursively the given directory.
     * 
     * @param delDir
     *            The directory to delelte
     * @return true if the directory is successfully deleted; false otherwise
     */
    public static boolean deleteDir(File delDir) {
        if (!delDir.exists())
            return true;

        File[] allDelFiles = delDir.listFiles();

        if (allDelFiles != null)
            for (int iF = 0; iF < allDelFiles.length; iF++) {
                if (allDelFiles[iF].isDirectory())
                    deleteDir(allDelFiles[iF]);
                else
                    allDelFiles[iF].delete();
            }

        return delDir.delete();
    }


  /**
   *  Util : Move the given file in the given dest directory
   *
   * @param  nameFileToMove  full name of the file to move
   * @param  destDir         dest directory
   * @return                 true if the file is successfully moved, false otherwise
   */
  public static boolean moveToDir(String nameFileToMove, String destDir)
  {
    // Instancier le fichier a deplacer
    File fileToMove = new File(nameFileToMove);

    // Tenter de deplacer le fichier
    return fileToMove.renameTo(new File(destDir, fileToMove.getName()));
  }

  /**
   *  Util: copy recursively the given source directory to the given destination
   * directory
   *
   * @param  srcDir           the source directory to copy
   * @param  destDir          the destination directory.
   * @exception  IOException  Description of Exception
   */

  public static void copy(File src, File dest, boolean deleteBefore)
            throws IOException 
    {
        if (deleteBefore && dest.exists())
            deleteDir(dest);
        if (src.isDirectory()) {
            File[] allCopyFiles = src.listFiles();
            if (!dest.exists())
                dest.mkdirs();
            for (int iF = 0; iF < allCopyFiles.length; iF++) {
                if (allCopyFiles[iF].isDirectory())
                    copy(allCopyFiles[iF], new File(dest.getCanonicalPath(),
                            allCopyFiles[iF].getName()),false);
                else
                    copyToDir(allCopyFiles[iF].getCanonicalPath(), dest
                            .getCanonicalPath());
            }
        } else {
            copyToDir(src.getCanonicalPath(),dest.getParentFile().getCanonicalPath());
        }
    }

  /**
   *  Util: copy recursively the given source directory to the given destination
   * directory
   *
   * @param  srcDir           the source directory to copy
   * @param  destDir          the destination directory.
   * @exception  IOException  Description of Exception
   * @deprecated (use {@link #copy(File, File, boolean)})
   */
  public static void copyDir(File srcDir, File destDir) throws IOException
  {
    File[] allCopyFiles = srcDir.listFiles();
    if (!destDir.exists())
        destDir.mkdirs();
    for (int iF = 0; iF < allCopyFiles.length; iF++)
      {
        if (allCopyFiles[iF].isDirectory())
          copyDir(allCopyFiles[iF], new File(destDir.getCanonicalPath(), allCopyFiles[iF].getName()));
        else
          copyToDir(allCopyFiles[iF].getCanonicalPath(), destDir.getCanonicalPath());
      }
  }


  /**
   *  Util : Copy the given file in the given dest directory
   *
   * @param  nameFileToCopy             full name of the file to copy
   * @param  destDir                    dest directory
   * @exception  FileNotFoundException  Potential FileNotFoundException
   * @exception  IOException            Potential IOException
   * @deprecated use {@link #copy(File, File, boolean)}
   */
  public static void copyToDir(String nameFileToCopy, String destDir) throws FileNotFoundException, IOException
  {
    // Verifier que le repertoire destinataire existe
    // sinon le creer
    File             dirDest = new File(destDir);
    if (!dirDest.exists())
      {
        // Creation du repertoire d'archive
        dirDest.mkdirs();
      }

    FileInputStream  input   = null;
    FileOutputStream output   = null;
    try
      {
        // Instantier le fichier source
        File fileToCopy = new File(nameFileToCopy);
        input = new FileInputStream(fileToCopy);

        File fileOutput = new File(destDir, fileToCopy.getName());
        if (fileOutput.exists())
            fileOutput.delete();
        // Instantier le fichier destinataire
        output = new FileOutputStream(fileOutput);

        // Copie du fichier
        copy(input, output);
      }
    finally
      {
        // Fermeture des buffers
        if (input != null)
        	input.close();
        if (output != null) {
            output.flush();
            output.close();
        }
        
      }
  }


  /**
   * Util: copy the given source file to the given destination file.
   *
   * @param  sourceFile                 the source file
   * @param  destFile                   the destination file
   * @exception  FileNotFoundException  Description of Exception
   * @exception  IOException            Description of Exception
   * @deprecated use {@link #copy(File, File, boolean)}
   */
  public static void copy(File sourceFile, File destFile) throws FileNotFoundException, IOException
  {
    FileInputStream  fisSrcFile  = null;
    FileOutputStream fosDestFile = null;

    // Create the destination file
    destFile.createNewFile();

    try
      {
        // Initialisation
        fisSrcFile = new java.io.FileInputStream(sourceFile);
        fosDestFile = new java.io.FileOutputStream(destFile);
        copy(fisSrcFile, fosDestFile);
      }
    finally
      {
       if (fisSrcFile != null)
    	   fisSrcFile.close();
       if (fosDestFile != null)
       	 fosDestFile.flush();
       if (fosDestFile != null)
       	 fosDestFile.close();
      }
  }
  
  /**
   * Util: copy the given source file to the given destination file.
   *
   * @param  sourceFile                 the source file
   * @param  destFile                   the destination file
   * @exception  FileNotFoundException  Description of Exception
   * @exception  IOException            Description of Exception
   */
  public static void copy(URL sourceFile, File destFile) throws FileNotFoundException, IOException
  {
	  BufferedInputStream  input  = null;
	  FileOutputStream output = null;

    // Create the destination file
    destFile.createNewFile();

    try
      {
        // Initialisation
    	input = new BufferedInputStream(sourceFile.openStream());
        output = new java.io.FileOutputStream(destFile);

        byte buffer[]       = new byte[1024];
        int  lastReadSize = 0;

        while ((lastReadSize = input.read(buffer)) != -1)
          {
            output.write(buffer, 0, lastReadSize);
          }

        output.flush();
      }
    finally
      {
    	if (input != null) {
			try {
				input.close();
			} catch (Throwable e) {
			}			
		}
    	if (output != null) {
    		try {
    			output.flush();
        		output.close();
			} catch (Throwable e) {
			}
    	}
      }
  }


  /**
   * Util: copy the given input stream to the given output stream.
   *
   * @param  input            the input stream
   * @param  output           the output stream
   * @exception  IOException  Description of Exception
   *
   */
  public static void copy(InputStream input, OutputStream output) throws IOException
  {

    byte buffer[]       = new byte[1024];
    int  lastReadSize = 0;

    while ((lastReadSize = input.read(buffer)) != -1)
      {
        output.write(buffer, 0, lastReadSize);
      }

    output.flush();
  }

/**
 * set the content of a file
 * @param file a file
 * @param value the content must be put into this file
 * @throws IOException
 */
	public static void setFile(File file, String value) throws IOException {
		createNewFile(file);
		PrintWriter stream = new PrintWriter(file);	
		stream.write(value);
		stream.close();
		
	}
}

