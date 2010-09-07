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
 *
 * Copyright (C) 2006-2010 Adele Team/LIG/Grenoble University, France
 */
package adele.util.io;

/*------------------------------\
| Liste des packages a importer |
\------------------------------*/
//*** Packages de la librairie
//** Packages util
//** Packages I/O
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;

/*-----------------------------------\
|  Declaration de la classe JarUtil  |
\-----------------------------------*/
/**
 *  It is an utility class for the manipulation of jar files
 *
 * @author    Remy Sanlaville
 * @since     23 janvier 2003
 */
public class JarUtil
{
  // Constante(s) globale(s)

  // Variable(s) globale(s)

  /*-----------------------------------------\
  |       Constructeur(s) de la classe       |
  \-----------------------------------------*/
  /*---------------------------------------\
  |         Interface de la classe         |
  \---------------------------------------*/
  /**
   *  Util: gets information about the given jar file name
   *
   * @param  jarFileName    file name of the jar file to get information
   * @exception  Exception  an exception occurs
   */
  public static void getInfo(String jarFileName) throws Exception
  {
    // Create a Jar file.
    JarFile     jarFile         = new JarFile(jarFileName);

    System.out.println("\n---------------------------------------------------------");
    System.out.println("Information about the jar file: " + jarFileName);
    System.out.println("---------------------------------------------------------");

    // Get all of the Jar entries.
    Enumeration jarEntries      = jarFile.entries();

    // Print out information.
    JarEntry    currentJarEntry = null;
    while (jarEntries.hasMoreElements())
      {
        currentJarEntry = (JarEntry) jarEntries.nextElement();

        System.out.println();
        if (currentJarEntry.isDirectory())
          System.out.println("Directory = " + currentJarEntry.getName());
        else
          System.out.println("File = " + currentJarEntry.getName());
        System.out.println("\tcomment = " + currentJarEntry.getComment());
        System.out.println("\ttime = " + new Date(currentJarEntry.getTime()));
        System.out.println("\tsize = " + currentJarEntry.getSize());
        System.out.println("\tcompress size = " + currentJarEntry.getCompressedSize());
        System.out.println("\tcrc = " + currentJarEntry.getCrc());
      }

    // Fermeture du fichier jar
    jarFile.close();
  }


  /**
   *  Util: gets the name of the entries from the given jar file name
   *
   * @param  jarFileName    the jar file name
   * @return                a tabular of the entries name or null if the jar
   * file is empty.
   * @exception  Exception  an exception occurs
   */
  public static String[] getEntriesName(String jarFileName) throws Exception
  {
    // Create a Jar file.
    JarFile  jarFile        = new JarFile(jarFileName);

    String[] tabEntriesName = null;
    int      nbEntries      = jarFile.size();

    if (nbEntries > 0)
      {
        // Get all of the Jar entries.
        Enumeration jarEntries      = jarFile.entries();

        tabEntriesName = new String[nbEntries];
        JarEntry    currentJarEntry = null;
        int         i               = 0;

        while (jarEntries.hasMoreElements())
          {
            currentJarEntry = (JarEntry) jarEntries.nextElement();

            // Ajouter le nom de la nouvelle entree
            tabEntriesName[i] = currentJarEntry.getName();

            // Incrementer l'indice
            i++;
          }
      }

    // Fermeture du fichier jar
    jarFile.close();

    return tabEntriesName;
  }


  /**
   *  Util : test if the given file is an entry of the given jar file
   *
   * @param  jarFileName      the jar file name
   * @param  fileName         the file name to test
   * @return                  true if the given file is an entry of the given
   * jar file, false otherwise
   * @exception  IOException  Description of Exception
   */
  public static boolean isEntry(String fileName, String jarFileName) throws IOException
  {
    boolean res     = false;
    JarFile jarFile = null;

    try
      {
        // Create a Jar file.
        File     fileJar         = new File(jarFileName);

        jarFile = new JarFile(fileJar.getCanonicalPath());

        // Il y a des problemes avec les separateurs de repertoire.
        // Je ne comprends pas pourquoi. Donc pour assurer, on va tester
        // les deux types de separateurs de fichier.
        // A changer si une solution est trouvee !!!
        String   fileNameWindows = fileName.replace('/', '\\');
        String   fileNameUnix    = fileName.replace('\\', '/');

        // Get the entry jar for the specific file
        JarEntry jarEntry        = jarFile.getJarEntry(fileNameWindows);

        if (jarEntry != null)
          res = true;
        else
          {
            jarEntry = jarFile.getJarEntry(fileNameUnix);

            if (jarEntry != null)
              res = true;
          }
      }
    finally
      {
        // Fermeture du fichier jar
        if (jarFile != null)
          jarFile.close();
      }

    return res;
  }


  /**
   *  Util : unjar a Jar archive file to a given directory
   *
   * @param  jarFileName                the jar file name
   * @param  destDir                    dest directory
   * @exception  FileNotFoundException  Description of Exception
   * @exception  IOException            Description of Exception
   */
  public static void unjar(String jarFileName, String destDir) throws FileNotFoundException, IOException
  {
    // Verifier que le repertoire destinataire existe
    // sinon le creer
    File             dirDest = new File(destDir);
    if (!dirDest.exists())
      {
        // Creation du repertoire d'archive
        dirDest.mkdirs();
      }

    JarFile          jarFile = null;
    InputStream      input   = null;
    FileOutputStream output  = null;

    try
      {
        // Create a Jar file.
        jarFile = new JarFile(jarFileName);

        // Get all of the Jar entries.
        Enumeration jarEntries      = jarFile.entries();

        JarEntry    currentJarEntry = null;
        File        destJarFile     = null;

        while (jarEntries.hasMoreElements())
          {
            currentJarEntry = (JarEntry) jarEntries.nextElement();

            // Test if the current entry is a directory
            if (currentJarEntry.isDirectory())
              {
                // Create the new directory
                destJarFile = new File(FileUtil.getFullPath(destDir, currentJarEntry.getName()));
                destJarFile.mkdirs();
              }
            else
              {
                // Create the new file
                input = jarFile.getInputStream(currentJarEntry);

                destJarFile = new File(destDir, currentJarEntry.getName());
                FileUtil.createNewFile(destJarFile);
                output = new FileOutputStream(destJarFile);

                FileUtil.copy(input, output);
              }
          }
      }
    finally
      {
        if (input != null)
          input.close();
        if (output != null)
          {
            output.flush();
            output.close();
          }

        // Fermeture du fichier jar
        if (jarFile != null)
          jarFile.close();
      }
  }


  /**
   *  Util : unjar a specific file in a jar archive file to a given directory
   *
   * @param  jarFileName                the jar file name
   * @param  destDir                    dest directory
   * @param  fileName                   the file to unjar
   * @exception  FileNotFoundException  Description of Exception
   * @exception  IOException            Description of Exception
   */
  public static void unjar(String fileName, String jarFileName, String destDir) throws FileNotFoundException, IOException
  {
    // Verifier que le repertoire destinataire existe
    // sinon le creer
    File             dirDest = new File(destDir);
    if (!dirDest.exists())
      {
        // Creation du repertoire d'archive
        dirDest.mkdirs();
      }

    JarFile          jarFile = null;
    InputStream      input   = null;
    FileOutputStream output  = null;

    try
      {
        // Create a Jar file.
        jarFile = new JarFile(jarFileName);

        // Il y a des problemes avec les separateurs de repertoire.
        // Je ne comprends pas pourquoi. Donc pour assurer, on va tester
        // les deux types de separateurs de fichier.
        // A changer si une solution est trouvee !!!
        String   fileNameWindows = fileName.replace('/', '\\');
        String   fileNameUnix    = fileName.replace('\\', '/');

        // Get the entry jar for the specific file
        JarEntry jarEntry        = jarFile.getJarEntry(fileNameWindows);

        if (jarEntry == null)
          {
            jarEntry = jarFile.getJarEntry(fileNameUnix);
            if (jarEntry == null)
              throw new FileNotFoundException("The jar entry " + fileName + " does not exist in the jar file " + jarFileName);
          }

        // Test if the jar entry is a directory
        if (jarEntry.isDirectory())
          {
            // Create the new directory
            File destJarDir = new File(FileUtil.getFullPath(destDir, jarEntry.getName()));
            destJarDir.mkdirs();
          }
        else
          {
            input = jarFile.getInputStream(jarEntry);

            File destFile = new File(FileUtil.getFullPath(destDir, jarEntry.getName()));
            FileUtil.createNewFile(destFile);
            output = new FileOutputStream(destFile);

            FileUtil.copy(input, output);
          }
      }
    finally
      {
        if (input != null)
          input.close();
        if (output != null)
          {
            output.flush();
            output.close();
          }

        // Fermeture du fichier jar
        if (jarFile != null)
          jarFile.close();
      }
  }


  /**
   *  Util : create a Jar archive file for the given files and/or directories.
   *
   * @param  entryFileNames             A vector of String objects for the
   * files and/or directories name to add in the Jar file
   * @param  jarFileName                the jar file name to create
   * @exception  FileNotFoundException  Description of Exception
   * @exception  IOException            Description of Exception
   */
  public static void jar(Vector entryFileNames, String jarFileName) throws FileNotFoundException, IOException
  {
    jar(entryFileNames, jarFileName, new File(""));
  }


  /**
   *  Util : create a Jar archive file for the given files and/or directories.
   *
   * @param  entryFileNames             A vector of String objects for the
   * files and/or directories name to add in the Jar file
   * @param  jarFileName                the jar file name to create
   * @param  baseDir                    the base directory for the entries to jar
   * @exception  FileNotFoundException  Description of Exception
   * @exception  IOException            Description of Exception
   */
  public static void jar(Vector<String> entryFileNames, String jarFileName, File baseDir) throws FileNotFoundException, IOException
  {
    // Creation de stream de sortie pour le fichier jar
    JarOutputStream outJar = new JarOutputStream(new FileOutputStream(jarFileName));

    try
      {
        // Creation de stream de sortie pour le fichier jar
        outJar = new JarOutputStream(new FileOutputStream(jarFileName));

        String[] tabEntryFileNames = new String[entryFileNames.size()];
        entryFileNames.toArray(tabEntryFileNames);

        jar(tabEntryFileNames, outJar, baseDir);
      }
    finally
      {
        // Fermeture du fichier jar
        outJar.close();
      }
  }


  /**
   *  Util : create a Jar archive file for the given files and/or directories.
   *
   * @param  entryFileNames             A collection of String objects for the
   * files and/or directories name to add in the Jar file
   * @param  jarFileName                the jar file name to create
   * @exception  FileNotFoundException  Description of Exception
   * @exception  IOException            Description of Exception
   */
  public static void jar(Collection entryFileNames, String jarFileName) throws FileNotFoundException, IOException
  {
    jar(entryFileNames, jarFileName, new File(""));
  }


  /**
   *  Util : create a Jar archive file for the given files and/or directories.
   *
   * @param  entryFileNames             A collection of String objects for the
   * files and/or directories name to add in the Jar file
   * @param  jarFileName                the jar file name to create
   * @param  baseDir                    the base directory for the entries to jar
   * @exception  FileNotFoundException  Description of Exception
   * @exception  IOException            Description of Exception
   */
  public static void jar(Collection<String> entryFileNames, String jarFileName, File baseDir) throws FileNotFoundException, IOException
  {
    // Creation de stream de sortie pour le fichier jar
    JarOutputStream outJar = new JarOutputStream(new FileOutputStream(jarFileName));

    try
      {
        // Creation de stream de sortie pour le fichier jar
        outJar = new JarOutputStream(new FileOutputStream(jarFileName));

        String[] tabEntryFileNames = new String[entryFileNames.size()];
        entryFileNames.toArray(tabEntryFileNames);

        jar(tabEntryFileNames, outJar, baseDir);
      }
    finally
      {
        // Fermeture du fichier jar
        outJar.close();
      }
  }


  /**
   *  Util : create a Jar archive file for the given file or directory.
   *
   * @param  entryFileName              file or directory name to add in the Jar
   * file
   * @param  jarFileName                the jar file name to create
   * @exception  FileNotFoundException  Description of Exception
   * @exception  IOException            Description of Exception
   */
  public static void jar(String entryFileName, String jarFileName) throws FileNotFoundException, IOException
  {
    String[] entryFileNames = new String[1];
    entryFileNames[0] = entryFileName;

    jar(entryFileNames, jarFileName);
  }


  /**
   *  Util : create a Jar archive file for the given files and/or directories.
   *
   * @param  entryFileNames             An enumeration of the files and/or directories
   * name to add in the Jar file
   * @param  jarFileName                the jar file name to create
   * @exception  FileNotFoundException  Description of Exception
   * @exception  IOException            Description of Exception
   */
  public static void jar(String[] entryFileNames, String jarFileName) throws FileNotFoundException, IOException
  {
    jar(entryFileNames, jarFileName, new File(""));
  }


  /**
   *  Util : create a Jar archive file for the given file or directory.
   *
   * @param  entryFileName              file or directory name to add in the Jar
   * file
   * @param  jarFileName                the jar file name to create
   * @param  baseDir                    the base directory for the entries to jar
   * @exception  FileNotFoundException  Description of Exception
   * @exception  IOException            Description of Exception
   */
  public static void jar(String entryFileName, String jarFileName, File baseDir) throws FileNotFoundException, IOException
  {
    String[] entryFileNames = new String[1];
    entryFileNames[0] = entryFileName;

    jar(entryFileNames, jarFileName, baseDir);
  }


  /**
   *  Util : create a Jar archive file for the given files and/or directories.
   *
   * @param  entryFileNames             An enumeration of the files and/or directories
   * name to add in the Jar file
   * @param  jarFileName                the jar file name to create
   * @param  baseDir                    the base directory for the entries to jar
   * @exception  FileNotFoundException  Description of Exception
   * @exception  IOException            Description of Exception
   */
  public static void jar(String[] entryFileNames, String jarFileName, File baseDir) throws FileNotFoundException, IOException
  {

    JarOutputStream outJar = null;

    try
      {
        // Creation de stream de sortie pour le fichier jar
        outJar = new JarOutputStream(new FileOutputStream(jarFileName));

        jar(entryFileNames, outJar, baseDir);
      }
    finally
      {
        // Fermeture du fichier jar
        if (outJar != null)
          outJar.close();
      }
  }


  /**
   *  Util : create a Jar archive file for the given files and/or directories.
   *
   * @param  entryFileNames             An enumeration of the files and/or directories
   * name to add in the Jar file
   * @param  jarFileName                the jar file name to create
   * @param  baseDir                    the base directory for the entryJar
   * @exception  FileNotFoundException  Description of Exception
   * @exception  IOException            Description of Exception
   */
  public static void jar(String[] entryFileNames, JarOutputStream jarFileName, File baseDir) throws FileNotFoundException, IOException
  {
	  jar(entryFileNames,null, jarFileName,baseDir);
  }
  
  /**
   *  Util : create a Jar archive file for the given files and/or directories.
   *
   * @param  entryFileNames             An enumeration of the files and/or directories
   * name to add in the Jar file
   * @param  jarFileName                the jar file name to create
   * @param  baseDir                    the base directory for the entryJar
   * @exception  FileNotFoundException  Description of Exception
   * @exception  IOException            Description of Exception
   */
  public static void jar(String[] entryFileNames, File[] baseDirs, JarOutputStream jarFileName, File baseDir) throws FileNotFoundException, IOException
  {
    String currentFileName = null;
    File   currentFile     = null;

    // Ajout des fichiers et/ou repertoires dans le fichier jar
    for (int i = 0; i < entryFileNames.length; i++)
      {
        // Recuperer le nom du fichier courant
        currentFileName = entryFileNames[i];

        // Lecture des informations du fichier courant
        currentFile = new File(currentFileName);

        File currentBaseDir = baseDirs != null && baseDirs[i] != null? baseDirs[i]  : baseDir;
        addEntryJar(currentFile, jarFileName, currentBaseDir);
      }
  }


  /**
   *  Util : update a Jar archive file for the given files and/or directories.
   *
   * @param  entryFileNames             A collection of String objects for the
   * files and/or directories name to update in the Jar file
   * @param  jarFileName                the jar file name to update
   * @exception  FileNotFoundException  Description of Exception
   * @exception  IOException            Description of Exception
   */
  public static void updateJar(Collection entryFileNames, String jarFileName) throws FileNotFoundException, IOException
  {
    updateJar(entryFileNames, jarFileName, new File(""));
  }


  /**
   *  Util : update a Jar archive file for the given files and/or directories.
   *
   * @param  entryFileNames             A collection of String objects for the
   * files and/or directories name to update in the Jar file
   * @param  jarFileName                the jar file name to update
   * @param  baseDir                    the base directory for the entries to
   * update
   * @exception  FileNotFoundException  Description of Exception
   * @exception  IOException            Description of Exception
   */
  public static void updateJar(Collection entryFileNames, String jarFileName, File baseDir) throws FileNotFoundException, IOException
  {
    // La mise a jour se fait en trois temps.
    //  1. Decompresser le fichier jar dans un repertoire temporaire
    //  2. Recopier les fichiers et/ou repertoires a mettre a jour
    //  3. Recreer le fichier jar et supprimer le repertoire temporaire
    // Important: il faut tout de meme gerer le cas des repertoires


    //*** Etape 1 : Decompresser le fichier jar dans un repertoire temporaire
    //** Creation du repertoire temporaire
    TmpUtil  tmpDir            = new TmpUtil();
    String   pathTmpDir        = tmpDir.getPath();
    unjar(jarFileName, pathTmpDir);

    // Etape 2 : Recopier les fichiers et/ou repertoires a mettre a jour
    Iterator itEntryFilesNames = entryFileNames.iterator();
    String   currentFile       = null;
    File     srcDir            = null;
    File     destDir           = null;
    String   endDestDir        = null;
    String   baseDirName       = null;
    while (itEntryFilesNames.hasNext())
      {
        // Recuperer le fichier ou repertoire courant
        currentFile = (String) itEntryFilesNames.next();

        // Gestion du repertoire source
        srcDir = new File(currentFile);
        if (srcDir.isDirectory())
          {
            // Cas d'un repertoire

            // Gestion du repertoire destination
            baseDirName = baseDir.getCanonicalPath();
            if (currentFile.startsWith(baseDirName))
              {
                endDestDir = currentFile.substring(baseDirName.length() + 1);
                destDir = new File(tmpDir.getFile(), endDestDir);
              }
            else
              destDir = tmpDir.getFile();

            FileUtil.copyDir(srcDir, destDir);
          }
        else
          {
            // Cas d'un fichier

            // Gestion du repertoire destination
            baseDirName = baseDir.getCanonicalPath();
            if (currentFile.startsWith(baseDirName))
              {
                endDestDir = FileUtil.getPath(currentFile.substring(baseDirName.length() + 1));
                destDir = new File(tmpDir.getFile(), endDestDir);
              }
            else
              destDir = tmpDir.getFile();

            // Recopier le fichier dans le repertoire temporaire destinataire
            FileUtil.copyToDir(currentFile, destDir.getCanonicalPath());
          }
      }

    // Etape 3 : Recreer le fichier jar
    jar(pathTmpDir, jarFileName, tmpDir.getFile());
    tmpDir.delete();
  }


  /*-------------------------------------\
  |         Methode(s) Privee(s)         |
  \-------------------------------------*/
  /**
   *  Util: Writes an entry in the given jar file. If the entry is a directory
   * it adds the entries recursively. Entries are named relative to the given
   * base directory.
   *
   * @param  outJar           the output stream file jar
   * @param  entryJar         The file or directory to jar
   * @param  baseDir          the base directory for the entryJar
   * @exception  IOException  an IOExecption occurs during the jar process
   */
  private static void addEntryJar(File entryJar, JarOutputStream outJar, File baseDir) throws IOException
  {
    if (entryJar.isDirectory())
      {
        // Gestion des repertoires
        File files[] = entryJar.listFiles();
        for (int i = 0; i < files.length; i++)
          {
            addEntryJar(files[i], outJar, baseDir);
          }
      }
    else
      {
        FileInputStream file = null;
        try
          {
            // Gestion des fichiers

            String   entryJarName = entryJar.getCanonicalPath();
            String   baseDirName  = baseDir.getCanonicalPath();
            if (entryJarName.startsWith(baseDirName))
              {
                entryJarName = entryJarName.substring(baseDirName.length() + 1);
              }

            // Imposer que les separateurs de fichiers soient '/', norme des
            // fichiers jars, sinon on ne peut pas retrouver les fichiers
            // classes par le classpath...
            entryJarName = entryJarName.replace('\\', '/');

            //	Ajouter la nouvelle entree dans le fichier jar
            file = new FileInputStream(entryJar);

            JarEntry jarEntry     = new JarEntry(entryJarName);
            outJar.putNextEntry(jarEntry);

            // Copier le fichier a compresser dans le fichier jar
            FileUtil.copy(file, outJar);
          }
        finally
          {
            if (file != null)
              file.close();
            if (outJar != null)
              outJar.closeEntry();
          }
      }
  }


  /*-------------------------------------\
  |                 Main                 |
  \-------------------------------------*/
  /**
   *  Main of the jar utility class
   *
   * @param  args  parameters need to execute the commands
   */
  public static void main(String args[])
  {
    int nbArgs = args.length;

    // Gestion des arguments
    if (nbArgs > 1)
      {
        if (args[0].equals("-i"))
          {
            // Gets the information of the jar file
            try
              {
                getInfo(args[1]);
                System.exit(0);
              }
            catch (Exception e)
              {
                e.printStackTrace();
                System.exit(-1);
              }
          }

        if (args[0].equals("-x"))
          {
            // Extract the jar file
            switch (nbArgs)
              {
                  case 2:
                    try
                      {
                        unjar(args[1], ".");
                      }
                    catch (Exception e)
                      {
                        e.printStackTrace();
                        System.exit(-1);
                      }
                    break;
                  case 3:
                    try
                      {
                        unjar(args[1], args[2]);
                      }
                    catch (Exception e)
                      {
                        e.printStackTrace();
                        System.exit(-1);
                      }
                    break;
                  default:
                    try
                      {
                        for (int i = 3; i < nbArgs; i++)
                          unjar(args[i], args[1], args[2]);
                      }
                    catch (Exception e)
                      {
                        e.printStackTrace();
                        System.exit(-1);
                      }
              }
            System.exit(0);
          }
        if (args[0].equals("-j"))
          {
            // Extract the jar file
            switch (nbArgs)
              {
                  case 2:
                    // Missing argument
                    System.out.println("[Error] Missing base directory");
                    System.out.println("Help JarUtil");
                    System.out.println("\tjava adele.util.io.JarUtil -i jarFileName");
                    System.out.println("\tjava adele.util.io.JarUtil -j jarFileName baseDir FileDirToJar*");
                    System.out.println("\tjava adele.util.io.JarUtil -x jarFileName [DestDir] [FileToExtract]*");
                    System.exit(-1);
                    break;
                  case 3:
                    // Missing argument
                    System.out.println("[Error] Missing file(s) or directory(ies) to jar");
                    System.out.println("Help JarUtil");
                    System.out.println("\tjava adele.util.io.JarUtil -i jarFileName");
                    System.out.println("\tjava adele.util.io.JarUtil -z jarFileName baseDir FileDirToJar*");
                    System.out.println("\tjava adele.util.io.JarUtil -x jarFileName [DestDir] [FileToExtract]*");
                    System.exit(-1);
                    break;
                  default:
                    try
                      {
                        Collection vFiles = new HashSet();
                        for (int i = 3; i < nbArgs; i++)
                          vFiles.add(args[i]);
                        jar(vFiles, args[1], new File(args[2]));
                      }
                    catch (Exception e)
                      {
                        e.printStackTrace();
                        System.exit(-1);
                      }
              }
            System.exit(0);
          }

        // else Bad option
        System.out.println("[Error] option " + args[0] + " unknown");
        System.out.println("Help JarUtil");
        System.out.println("\tjava adele.util.io.JarUtil -i jarFileName");
        System.out.println("\tjava adele.util.io.JarUtil -z jarFileName baseDir FileDirToJar*");
        System.out.println("\tjava adele.util.io.JarUtil -x jarFileName [DestDir] [FileToExtract]*");
        System.exit(-1);
      }
    else
      {
        System.out.println("[Error] Bad command line");
        System.out.println("Help JarUtil");
        System.out.println("\tjava adele.util.io.JarUtil -i jarFileName");
        System.out.println("\tjava adele.util.io.JarUtil -z jarFileName baseDir FileDirToJar*");
        System.out.println("\tjava adele.util.io.JarUtil -x jarFileName [DestDir] [FileToExtract]*");
        System.exit(-1);
      }
  }
}

